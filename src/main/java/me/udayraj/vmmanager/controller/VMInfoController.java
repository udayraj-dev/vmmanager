package me.udayraj.vmmanager.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.service.VMService;

@RestController
@RequestMapping("/vms")
public class VMInfoController {
    private final VMService vmService;

    public VMInfoController(VMService vmService) {
        this.vmService = vmService;
    }

    @PostMapping("/test-connection")
    public ResponseEntity<VMInformation> getInformation(@RequestBody SshLogin sshSshLogin) {
        VMInformation vmInformation = vmService.getInformation(sshSshLogin);
        return ResponseEntity.ok(vmInformation);
    }

    @PostMapping("/")
    public ResponseEntity<VMInformation> save(@Valid @RequestBody VMInformation vmInformation) {
        VmInfo savedVmInfo = vmService.saveVmInfo(vmInformation);
        VMInformation responseDto = convertToDto(savedVmInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VMInformation> updateVm(@PathVariable UUID id, @Valid @RequestBody VMInformation vmInformation) {
        return vmService.updateVmInfo(id, vmInformation)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVm(@PathVariable UUID id) {
        boolean wasDeleted = vmService.softDeleteVm(id).isPresent();
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // Standard response for successful DELETE
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Page<VMInformation>> findVms(@RequestParam(required = false) Map<String, String> allParams, Pageable pageable) {
        Page<VmInfo> vmPage = vmService.findVmsByCriteria(allParams == null ? Map.of() : allParams, pageable);
        return ResponseEntity.ok(vmPage.map(this::convertToDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VMInformation> getVmById(@PathVariable UUID id) {
        return vmService.getVmById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Converts a VmInfo entity to a VMInformation DTO.
     * This helper method ensures passwords are not exposed in API responses.
     *
     * @param vmInfo The entity to convert.
     * @return The resulting DTO.
     */
    private VMInformation convertToDto(VmInfo vmInfo) {
        return new VMInformation(
                vmInfo.getId(),
                vmInfo.getIpAddress(),
                vmInfo.getPortNumber(),
                vmInfo.getUsername(),
                null, // Never return the password
                vmInfo.getOsName(),
                vmInfo.getHostName(),
                vmInfo.getCpuCores(),
                vmInfo.getRootPartitionSize(),
                vmInfo.getTotalRamSize(),
                vmInfo.getMacAddress(),
                vmInfo.getCategory(),
                vmInfo.getPurpose(),
                vmInfo.getOwnerName(),
                vmInfo.isLinux(),
                vmInfo.getStatus(),
                vmInfo.getCreatedDate(),
                vmInfo.getLastUpdatedDate());
    }
}
