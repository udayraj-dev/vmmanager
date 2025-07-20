package com.rajmentors.vmmanager.model;

public record InstanceRecord(
    String osName,
    String hostName,
    String cpuCores,
    String rootPartitionSize,
    String totalRamSize,
    String macAddress
) {}