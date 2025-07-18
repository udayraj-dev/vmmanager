package com.rajmentors.vmmanager.model;

public record VmInfo(
    String osName,
    String hostName,
    String cpuCores,
    String rootPartitionSize,
    String totalRamSize,
    String macAddress
) {}