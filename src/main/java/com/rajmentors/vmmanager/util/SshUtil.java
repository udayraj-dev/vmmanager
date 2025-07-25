package com.rajmentors.vmmanager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.rajmentors.vmmanager.model.InstanceRecord;
import com.rajmentors.vmmanager.model.LoginRecord;

public class SshUtil {
    private static final Logger logger = LoggerFactory.getLogger(SshUtil.class);
    private LoginRecord sshLoginRecord;
    private final int sessionTimeout;
    private final int channelTimeout;

    public SshUtil(LoginRecord sshLoginRecord, int sessionTimeout, int channelTimeout) {
        this.sshLoginRecord = sshLoginRecord;
        this.sessionTimeout = sessionTimeout;
        this.channelTimeout = channelTimeout;
    }

    private List<String> executeCommand(String command) throws JSchException, IOException {
        Session session = null;
        ChannelExec channel = null;
        List<String> outputLines = new ArrayList<>();
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(sshLoginRecord.username(), sshLoginRecord.ipAddress(),
                    sshLoginRecord.portNumber());
            session.setPassword(sshLoginRecord.password());

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            logger.info("Connecting to SSH server at {}:{}...", sshLoginRecord.ipAddress(),
                    sshLoginRecord.portNumber());
            session.connect(sessionTimeout);
            logger.info("SSH Session established.");

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect(channelTimeout);
            logger.info("Command '{}' execution started.", command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(commandOutput));
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
                logger.debug("Command Output: {}", line);
            }

            while (!channel.isClosed()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Ignore
                }
            }

            int exitStatus = channel.getExitStatus();
            logger.info("Command finished with exit status: {}", exitStatus);
            if (exitStatus != 0) {
                InputStream errorStream = channel.getErrStream();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                StringBuilder errorOutput = new StringBuilder();
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    errorOutput.append(errorLine).append("\n");
                }
                logger.error("Command '{}' failed with error:\n{}", command, errorOutput.toString());
                throw new IOException("Remote command failed: " + command + ", Exit status: " + exitStatus + ", Error: "
                        + errorOutput.toString());
            }
        } finally {
            if (channel != null) {
                channel.disconnect();
                logger.info("SSH Channel disconnected.");
            }
            if (session != null) {
                session.disconnect();
                logger.info("SSH Session disconnected.");
            }
        }
        return outputLines;
    }

    public InstanceRecord getInstanceInformation() throws JSchException, IOException {
        String compoundCommand = String.join(" && ",
                "echo 'OS_NAME:' && cat /etc/os-release | grep '^NAME='",
                "echo -n 'HOSTNAME:' && hostname -f",
                "echo -n 'CPU_CORES:' && nproc",
                "echo -n 'ROOT_PARTITION:' && df --si / | awk 'NR==2 {print $2}'",
                "echo -n 'TOTAL_RAM:' && free -h --si | grep Mem: | awk '{print $2}'",
                "echo -n 'MAC_ADDRESS:' && ip link show | grep 'link/ether' | awk '{print $2}' | head -1");

        List<String> output = executeCommand(compoundCommand);

        String osName = "", hostName = "", cpuCores = "", rootPartitionSize = "", totalRamSize = "", macAddress = "";

        for (String line : output) {
            if (line.startsWith("NAME=")) {
                osName = line.substring(line.indexOf("=") + 1).replace("\"", "");
            } else if (line.startsWith("HOSTNAME:")) {
                hostName = line.replace("HOSTNAME:", "").trim();
            } else if (line.startsWith("CPU_CORES:")) {
                cpuCores = line.replace("CPU_CORES:", "").trim();
            } else if (line.startsWith("ROOT_PARTITION:")) {
                rootPartitionSize = line.replace("ROOT_PARTITION:", "").trim();
            } else if (line.startsWith("TOTAL_RAM:")) {
                totalRamSize = line.replace("TOTAL_RAM:", "").trim();
            } else if (line.startsWith("MAC_ADDRESS:")) {
                macAddress = line.replace("MAC_ADDRESS:", "").trim();
            }
        }

        return new InstanceRecord(osName, hostName, cpuCores, rootPartitionSize, totalRamSize, macAddress);
    }

    public void createFolderStructure(String parentFolder, String folders) throws JSchException, IOException {
        String[] folderList = folders.split(";");
        StringBuilder commandBuilder = new StringBuilder("mkdir -p ");
        for (String folder : folderList) {
            commandBuilder.append(parentFolder).append("/").append(folder).append(" ");
        }

        String command = commandBuilder.toString().trim();
        logger.info("Creating folder structure with command: {}", command);
        executeCommand(command);
    }
}
