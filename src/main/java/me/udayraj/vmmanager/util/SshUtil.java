package me.udayraj.vmmanager.util;

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

import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.model.SshLogin;

public class SshUtil {
    private static final Logger logger = LoggerFactory.getLogger(SshUtil.class);
    private final SshLogin sshSshLogin;
    private final int sessionTimeout;
    private final int channelTimeout;
    private Session session;

    public SshUtil(SshLogin sshLogin, int sessionTimeout, int channelTimeout) {
        this.sshSshLogin = sshLogin;
        this.sessionTimeout = sessionTimeout;
        this.channelTimeout = channelTimeout;
    }

    public void connect() throws JSchException {
        if (session != null && session.isConnected()) {
            logger.info("SSH Session is already connected.");
            return;
        }
        JSch jsch = new JSch();
        session = jsch.getSession(sshSshLogin.username(), sshSshLogin.ipAddress(),
                sshSshLogin.portNumber());
        session.setPassword(sshSshLogin.password());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        logger.info("Connecting to SSH server at {}:{}...", sshSshLogin.ipAddress(),
                sshSshLogin.portNumber());
        session.connect(sessionTimeout);
        logger.info("SSH Session established.");
    }

    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();
            logger.info("SSH Session disconnected.");
        }
        session = null;
    }

    public List<String> executeCommand(String command) throws JSchException, IOException {
        if (session == null || !session.isConnected()) {
            throw new IllegalStateException("SSH session is not connected. Please call connect() first.");
        }
        ChannelExec channel = null;
        List<String> outputLines = new ArrayList<>();
        try {
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
                    Thread.currentThread().interrupt();
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
                logger.error("Command '{}' failed with error:\n{}", command, errorOutput);
                throw new IOException("Remote command failed: " + command + ", Exit status: " + exitStatus + ", Error: "
                        + errorOutput);
            }
        } finally {
            if (channel != null) {
                channel.disconnect();
                logger.info("SSH Channel disconnected.");
            }
        }
        return outputLines;
    }

    public VMInformation getInstanceInformation() throws JSchException, IOException {
        String compoundCommand = String.join(" && ",
                "echo 'OS_NAME:' && cat /etc/os-release | grep '^NAME='",
                "echo -n 'HOSTNAME:' && hostname -f",
                "echo -n 'CPU_CORES:' && nproc",
                "echo -n 'ROOT_PARTITION:' && df --si / | awk 'NR==2 {print $2}'",
                "echo -n 'TOTAL_RAM:' && free -h --si | grep Mem: | awk '{print $2}'",
                "echo -n 'MAC_ADDRESS:' && ip link show | grep 'link/ether' | awk '{print $2}' | head -1");

        connect();
        List<String> output = executeCommand(compoundCommand);
        disconnect();
        String osName = "", hostName = "", cpuCores = "", rootPartitionSize = "", totalRamSize = "", macAddress = "";

        for (String line : output) {
            if (line.startsWith("NAME=")) {
                osName = line.substring(line.indexOf("=") + 1).replace("\"", "");
            } else if (line.startsWith("HOSTNAME:")) {
                hostName = line.replace("HOSTNAME:", "").trim().toLowerCase();
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

        return VMInformation.createWithSshDetails (sshSshLogin,osName, hostName, cpuCores, rootPartitionSize, totalRamSize, macAddress);
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
