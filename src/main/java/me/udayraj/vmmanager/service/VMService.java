package me.udayraj.vmmanager.service;

import java.io.IOException;

import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.repository.VmInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.JSchException;
import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.util.SshUtil;

@Service
public class VMService {

    private static final Logger logger = LoggerFactory.getLogger(VMService.class);

    @Value("${ssh.sessionTimeout:15000}")
    private int sessionTimeout;

    @Value("${ssh.channelTimeout:15000}")
    private int channelTimeout;

    @Autowired
    private VmInfoRepository vmInfoRepository;

    public VMInformation getInformation(SshLogin sshLogin) {
        SshUtil sshInfo = new SshUtil(sshLogin, sessionTimeout, channelTimeout);
        VMInformation information = null;
        try {
            information = sshInfo.getInstanceInformation();
        } catch (JSchException | IOException e) {
            logger.error("Error getting VM information for host {}:{}", sshLogin.ipAddress(),
                    sshLogin.portNumber(), e);
        }
        return information;
    }

    public void saveVmInfo(VMInformation vmInformation) {
        VmInfo vmInfo = new VmInfo(
                vmInformation.ipAddress(),
                vmInformation.portNumber(),
                vmInformation.username(),
                vmInformation.password(),
                vmInformation.osName(),
                vmInformation.hostName(),
                vmInformation.cpuCores(),
                vmInformation.rootPartitionSize(),
                vmInformation.totalRamSize(),
                vmInformation.macAddress()
        );
        vmInfoRepository.save(vmInfo);
    }
}
