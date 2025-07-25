package com.rajmentors.vmmanager.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;
import com.rajmentors.vmmanager.model.InstanceRecord;
import com.rajmentors.vmmanager.model.LoginRecord;
import com.rajmentors.vmmanager.util.SshUtil;

@Service
public class InstanceService {

    private static final Logger logger = LoggerFactory.getLogger(InstanceService.class);

    @Value("${ssh.sessionTimeout:15000}")
    private int sessionTimeout;

    @Value("${ssh.channelTimeout:15000}")
    private int channelTimeout;

    public InstanceRecord getInformation(LoginRecord loginRecord) {
        SshUtil sshInfo = new SshUtil(loginRecord, sessionTimeout, channelTimeout);
        InstanceRecord instance = null;
        try {
            instance = sshInfo.getInstanceInformation();
        } catch (JSchException | IOException e) {
            logger.error("Error getting VM information for host {}:{}", loginRecord.ipAddress(),
                    loginRecord.portNumber(), e);
        }
        return instance;
    }
}
