package me.udayraj.vmmanager.dto;

import me.udayraj.vmmanager.model.VmStatus;

/**
 * A DTO to encapsulate the filtering criteria for searching VMs.
 * This provides type-safety and better API documentation for query parameters.
 */
public record VmFilterCriteria(
    VmStatus status,
    Boolean isLinux,
    String ownerName,
    String category,
    String purpose,
    String hostName,
    String cpuCores,
    String totalRamSize,
    String ipAddress
) {}