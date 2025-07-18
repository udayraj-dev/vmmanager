package com.rajmentors.vmmanager.model;

public record SshLoginRecord(
        String ipAddress,
        int portNumber,
        String username,
        String password) {}
