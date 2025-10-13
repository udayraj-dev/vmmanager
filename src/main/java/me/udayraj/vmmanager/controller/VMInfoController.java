package me.udayraj.vmmanager.controller;

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
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;

import me.udayraj.vmmanager.dto.VmFilterCriteria;
import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.service.VMService;

@RestController
@RequestMapping("/vms")
@Tag(name = "VM Management", description = "APIs for managing and querying Virtual Machines")
public class VMInfoController {
    private final VMService vmService;

    public VMInfoController(VMService vmService) {
        this.vmService = vmService;
    }

    @Operation(summary = "Test SSH connection and get VM details", description = "Connects to a VM via SSH using provided credentials and retrieves its hardware and OS information.")
    @PostMapping("/test-connection")
    public ResponseEntity<VMInformation> getInformation(@RequestBody SshLogin sshSshLogin) {
        VMInformation vmInformation = vmService.getInformation(sshSshLogin);
        return ResponseEntity.ok(vmInformation);
    }

    @Operation(summary = "Save a new VM's information", description = "Saves the details of a new VM to the database. The ID is auto-generated.")
    @PostMapping
    public ResponseEntity<VMInformation> save(@Valid @RequestBody VMInformation vmInformation) {
        VmInfo savedVmInfo = vmService.saveVmInfo(vmInformation);
        VMInformation responseDto = convertToDto(savedVmInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @Operation(summary = "Update an existing VM", description = "Updates the details of an existing VM identified by its UUID.")
    @PutMapping("/{id}")
    public ResponseEntity<VMInformation> updateVm(@PathVariable UUID id, @Valid @RequestBody VMInformation vmInformation) {
        return vmService.updateVmInfo(id, vmInformation)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Soft-delete a VM", description = "Marks a VM as DELETED. The record is not physically removed from the database.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVm(@PathVariable UUID id) {
        boolean wasDeleted = vmService.softDeleteVm(id).isPresent();
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // Standard response for successful DELETE
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Find VMs by criteria", description = "Finds and paginates VMs based on a flexible set of filter criteria (e.g., status, ownerName, ipAddress).")
    @GetMapping
    public ResponseEntity<Page<VMInformation>> findVms(@ParameterObject VmFilterCriteria filters, @ParameterObject Pageable pageable) {
        Page<VmInfo> vmPage = vmService.findVmsByCriteria(filters, pageable);
        return ResponseEntity.ok(vmPage.map(this::convertToDto));
    }

    @Operation(summary = "Get a VM by its ID", description = "Retrieves a single VM by its unique identifier (UUID).")
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
