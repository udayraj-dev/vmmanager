package me.udayraj.vmmanager.model;

public record SshLogin(
        String ipAddress,
        int portNumber,
        String username,
        String password) {}
