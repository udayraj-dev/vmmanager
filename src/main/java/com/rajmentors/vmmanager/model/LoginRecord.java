package com.rajmentors.vmmanager.model;

public record LoginRecord(
        String ipAddress,
        int portNumber,
        String username,
        String password) {}
