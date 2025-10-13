package me.udayraj.vmmanager.service;

import java.io.IOException;
import java.util.Optional;
import java.util.Map;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;

import com.jcraft.jsch.JSchException;

import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.exception.VmConnectionException;
import me.udayraj.vmmanager.exception.VmPersistenceException;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.model.VmStatus;
import me.udayraj.vmmanager.repository.VmInfoSpecifications;
import me.udayraj.vmmanager.repository.VmInfoRepository;
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

    public Page<VmInfo> findVmsByCriteria(Map<String, String> filters, Pageable pageable) {
        Specification<VmInfo> spec = Specification.allOf();

        if (filters.containsKey("status")) {
            spec = spec.and(VmInfoSpecifications.withStatus(VmStatus.valueOf(filters.get("status").toUpperCase())));
        }
        if (filters.containsKey("isLinux")) {
            spec = spec.and(VmInfoSpecifications.isLinux(Boolean.parseBoolean(filters.get("isLinux"))));
        }
        if (filters.containsKey("ownerName")) {
            spec = spec.and(VmInfoSpecifications.hasOwnerName(filters.get("ownerName")));
        }
        if (filters.containsKey("category")) {
            spec = spec.and(VmInfoSpecifications.hasCategory(filters.get("category")));
        }
        if (filters.containsKey("purpose")) {
            spec = spec.and(VmInfoSpecifications.hasPurpose(filters.get("purpose")));
        }
        if (filters.containsKey("hostName")) {
            spec = spec.and(VmInfoSpecifications.hasHostName(filters.get("hostName")));
        }
        if (filters.containsKey("cpuCores")) {
            spec = spec.and(VmInfoSpecifications.hasCpuCores(filters.get("cpuCores")));
        }
        if (filters.containsKey("totalRamSize")) {
            spec = spec.and(VmInfoSpecifications.hasTotalRamSize(filters.get("totalRamSize")));
        }
        if (filters.containsKey("ipAddress")) {
            spec = spec.and(VmInfoSpecifications.hasIpAddress(filters.get("ipAddress")));
        }

        try {
            return vmInfoRepository.findAll(spec, pageable);
        } catch (DataAccessException e) {
            String message = messageService.getMessage("error.persistence.retrieve.criteria");
            throw new VmPersistenceException(message, e);
        }
    }
}
