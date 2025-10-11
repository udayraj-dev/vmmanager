package me.udayraj.vmmanager.dto;

import me.udayraj.vmmanager.model.SshLogin;

import java.util.UUID;

public record VMInformation(
        UUID id,
        String ipAddress,
        int portNumber,
        String username,
        String password,
        String osName,
        String hostName,
        String cpuCores,
        String rootPartitionSize,
        String totalRamSize,
        String macAddress
        ) {
    public static VMInformation createWithSshDetails(
            SshLogin login,
            String osName,
            String hostName,
            String cpuCores,
            String rootPartitionSize,
            String totalRamSize,
            String macAddress
    ) {
        return new VMInformation(
                UUID.randomUUID(),
                login.ipAddress(),
                login.portNumber(),
                login.username(),
                login.password(),
                osName,
                hostName,
                cpuCores,
                rootPartitionSize,
                totalRamSize,
                macAddress
        );
    }
}
