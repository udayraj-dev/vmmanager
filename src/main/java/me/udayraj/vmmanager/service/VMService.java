package me.udayraj.vmmanager.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;

import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.dto.VmFilterCriteria;
import me.udayraj.vmmanager.exception.VmConnectionException;
import me.udayraj.vmmanager.exception.VmPersistenceException;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.model.VmStatus;
import me.udayraj.vmmanager.repository.VmInfoRepository;
import me.udayraj.vmmanager.repository.VmInfoSpecifications;
import me.udayraj.vmmanager.util.SshUtil;

@Service
public class VMService {
    private final int sessionTimeout;
    private final int channelTimeout;
    private final VmInfoRepository vmInfoRepository;
    private final MessageService messageService;

    public VMService(VmInfoRepository vmInfoRepository, MessageService messageService, @Value("${ssh.sessionTimeout:15000}") int sessionTimeout, @Value("${ssh.channelTimeout:15000}") int channelTimeout) {
        this.vmInfoRepository = vmInfoRepository;
        this.messageService = messageService;
        this.sessionTimeout = sessionTimeout;
        this.channelTimeout = channelTimeout;
    }

    public VMInformation getInformation(SshLogin sshLogin) {
        SshUtil sshInfo = new SshUtil(sshLogin, sessionTimeout, channelTimeout);
        try {
            return sshInfo.getInstanceInformation();
        } catch (JSchException | IOException e) {
            String message = messageService.getMessage("error.vm.connection", sshLogin.ipAddress(), sshLogin.portNumber());
            throw new VmConnectionException(message, e);
        }
    }

    public VmInfo saveVmInfo(VMInformation vmInformation) {
        if (vmInformation == null) {
            throw new IllegalArgumentException(messageService.getMessage("error.validation.vm.null"));
        }
        try {
            VmInfo vmInfo = new VmInfo(vmInformation);
            return vmInfoRepository.save(vmInfo);
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.save", vmInformation.hostName());
            throw new VmPersistenceException(message, e);
        }
    }

    public Optional<VmInfo> updateVmInfo(UUID id, VMInformation vmInformation) {
        try {
            Optional<VmInfo> existingVmOptional = vmInfoRepository.findById(id);
            if (existingVmOptional.isEmpty()) {
                return Optional.empty(); // VM not found
            }

            VmInfo existingVm = existingVmOptional.get();
            existingVm.updateFromDto(vmInformation);
            return Optional.of(vmInfoRepository.save(existingVm));
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.update", id);
            throw new VmPersistenceException(message, e);
        }
    }

    public Optional<VmInfo> softDeleteVm(UUID id) {
        try {
            Optional<VmInfo> existingVmOptional = vmInfoRepository.findById(id);
            if (existingVmOptional.isEmpty()) {
                return Optional.empty(); // VM not found
            }

            VmInfo existingVm = existingVmOptional.get();
            existingVm.setStatus(VmStatus.DELETED);
            return Optional.of(vmInfoRepository.save(existingVm));
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.delete", id);
            throw new VmPersistenceException(message, e);
        }
    }

    public Optional<VmInfo> getVmById(UUID id) {
        try {
            return vmInfoRepository.findById(id);
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.retrieve.id", id);
            throw new VmPersistenceException(message, e);
        }
    }

    public Page<VmInfo> findVmsByCriteria(VmFilterCriteria filters, Pageable pageable) {
        Specification<VmInfo> spec = Specification.allOf();

        if (filters != null) {
            spec = addSpec(spec, filters.status(), VmInfoSpecifications::withStatus);
            spec = addSpec(spec, filters.isLinux(), VmInfoSpecifications::isLinux);
            spec = addSpec(spec, filters.ownerName(), VmInfoSpecifications::hasOwnerName);
            spec = addSpec(spec, filters.category(), VmInfoSpecifications::hasCategory);
            spec = addSpec(spec, filters.purpose(), VmInfoSpecifications::hasPurpose);
            spec = addSpec(spec, filters.hostName(), VmInfoSpecifications::hasHostName);
            spec = addSpec(spec, filters.cpuCores(), VmInfoSpecifications::hasCpuCores);
            spec = addSpec(spec, filters.totalRamSize(), VmInfoSpecifications::hasTotalRamSize);
            spec = addSpec(spec, filters.ipAddress(), VmInfoSpecifications::hasIpAddress);
        }

        try {
            return vmInfoRepository.findAll(spec, pageable);
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.retrieve.criteria");
            throw new VmPersistenceException(message, e);
        }
    }

    private <T> Specification<VmInfo> addSpec(Specification<VmInfo> spec, T value, java.util.function.Function<T, Specification<VmInfo>> specFunction) {
        if (value instanceof String stringValue && stringValue.isBlank()) {
            return spec;
        }
        return value != null ? spec.and(specFunction.apply(value)) : spec;
    }
}
