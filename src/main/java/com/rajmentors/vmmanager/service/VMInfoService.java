package com.rajmentors.vmmanager.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;
import com.rajmentors.vmmanager.model.SshLoginRecord;
import com.rajmentors.vmmanager.model.VmInfo;
import com.rajmentors.vmmanager.util.SshHost;

@Service
public class VMInfoService {

    private static final Logger logger = LoggerFactory.getLogger(VMInfoService.class);

    @Value("${ssh.sessionTimeout:15000}")
    private int sessionTimeout;

    @Value("${ssh.channelTimeout:15000}")
    private int channelTimeout;

    public VmInfo getVMInfo(SshLoginRecord loginRecord) {
        SshHost host = new SshHost(loginRecord, sessionTimeout, channelTimeout);
        VmInfo vmInfo = null;
        try {
            vmInfo = new VmInfo(
                    host.getOsNameAndVersion(),
                    host.getHostname(),
                    host.getCpuCores(),
                    host.getRootPartitionSize(),
                    host.getTotalRamSize(),
                    host.getMacAddress("enp1s0")); // Note: Interface name is hardcoded
        } catch (JSchException | IOException e) {
            logger.error("Error getting VM information for host {}:{}", loginRecord.ipAddress(), loginRecord.portNumber(), e);
            // Consider re-throwing a custom exception to be handled by a global exception handler.
        }
        return vmInfo;
    }
}
