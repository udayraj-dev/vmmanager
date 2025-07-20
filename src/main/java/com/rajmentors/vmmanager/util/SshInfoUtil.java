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
import com.rajmentors.vmmanager.model.LoginRecord;

public class SshInfoUtil {

    private static final Logger logger = LoggerFactory.getLogger(SshInfoUtil.class);
    private LoginRecord sshLoginRecord;
    private final int sessionTimeout;
    private final int channelTimeout;

    public SshInfoUtil(LoginRecord sshLoginRecord, int sessionTimeout, int channelTimeout) {
        this.sshLoginRecord = sshLoginRecord;
        this.sessionTimeout = sessionTimeout;
        this.channelTimeout = channelTimeout;
    }

    public List<String> executeCommand(String command) throws JSchException, IOException {
        Session session = null;
        ChannelExec channel = null;
        List<String> outputLines = new ArrayList<>();

        try {
            JSch jsch = new JSch();

            // If using private key authentication:
            // jsch.addIdentity("/path/to/your/private_key", "passphrase_if_any");

            session = jsch.getSession(sshLoginRecord.username(), sshLoginRecord.ipAddress(),
                    sshLoginRecord.portNumber());
            String password = sshLoginRecord.password();
            session.setPassword(password);

            // Avoid strict host key checking for development/testing in controlled
            // environments.
            // For production, you should manage known_hosts properly.
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

            // Wait for the channel to close and get the exit status
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
                // Read error stream if command failed
                InputStream errorStream = channel.getErrStream();
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
                String errorLine;
                StringBuilder errorOutput = new StringBuilder();
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

    // You can add more specific methods here, e.g., getOsInfo(), getMacAddress(),
    // etc.
    public String getOsNameAndVersion() throws JSchException, IOException {
        List<String> output = executeCommand("cat /etc/os-release");
        String name = "";
        String version = "";
        for (String line : output) {
            if (line.startsWith("NAME=")) {
                name = line.substring(line.indexOf("=") + 1).replace("\"", "");
            }
            if (line.startsWith("VERSION=")) {
                version = line.substring(line.indexOf("=") + 1).replace("\"", "");
            }
        }
        return "OS: " + name + " Version: " + version;
    }

    // Refined getMacAddress method
    public String getMacAddress(String interfaceName) throws JSchException, IOException {
        // Get the full output of 'ip link show'
        List<String> output = executeCommand("ip link show");

        boolean foundInterface = false;
        for (String line : output) {
            // Look for the line that defines the interface, e.g., "2: enp1s0: <..."
            if (line.trim().startsWith("2: " + interfaceName) || line.trim().startsWith("3: " + interfaceName)) {
                foundInterface = true;
            }

            // If we found the interface and the next line contains "link/ether"
            if (foundInterface && line.contains("link/ether")) {
                // Expected output: " link/ether 52:54:00:12:34:56 brd ff:ff:ff:ff:ff:ff"
                String[] parts = line.trim().split("\\s+"); // Split by one or more spaces
                if (parts.length >= 2 && parts[0].equals("link/ether")) {
                    // The MAC address is the second part
                    return parts[1];
                }
            }
        }
        return "MAC Address not found for " + interfaceName;
    }

    public String getHostname() throws JSchException, IOException {
        // Use 'hostname -f' to get the Fully Qualified Domain Name (FQDN)
        List<String> output = executeCommand("hostname -f");
        return output.isEmpty() ? "Hostname not found" : output.get(0);
    }

    public String getCpuCores() throws JSchException, IOException {
        List<String> output = executeCommand("nproc"); // or 'lscpu | grep \"^CPU(s):\"'
        return output.isEmpty() ? "CPU cores not found" : output.get(0) + " cores";
    }

    public String getRamInfo() throws JSchException, IOException {
        List<String> output = executeCommand("free -h | grep Mem:");
        if (!output.isEmpty()) {
            // Example output: "Mem: 7.7Gi 1.1Gi 5.8Gi 16Mi 807Mi 6.3Gi"
            return output.get(0).trim().replaceAll("\\s+", " "); // Normalize spaces for easier parsing
        }
        return "RAM information not found";
    }

    public List<String> getStorageInfo() throws JSchException, IOException {
        // Lists storage info for the root partition (/)
        return executeCommand("df -h /");
    }

    public String getRootPartitionSize() throws JSchException, IOException {
        // Use 'df --si /' to get size in SI units (powers of 1000)
        List<String> output = executeCommand("df --si /");
        if (output.size() < 2) {
            return "Root partition size not found";
        }
        // The output of 'df --si /' looks like:
        // Filesystem Size Used Avail Use% Mounted on
        // /dev/vda1 63G 5.6G 58G 9% /
        // We want the second line, and the second column.
        String dataLine = output.get(1);
        String[] parts = dataLine.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[1]; // The 'Size' column
        }
        return "Root partition size not found";
    }

    public String getTotalRamSize() throws JSchException, IOException {
        // Use 'free -h --si' to get RAM size in powers of 1000 (GB, MB, etc.)
        List<String> output = executeCommand("free -h --si | grep Mem:");
        if (output.isEmpty()) {
            return "RAM information not found";
        }
        // Example output: "Mem: 16G 4.5G 2.1G"
        // After splitting by whitespace, it becomes ["Mem:", "16G", "4.5G", ...]
        String memLine = output.get(0);
        String[] parts = memLine.trim().split("\\s+");
        if (parts.length > 1) {
            return parts[1]; // The 'total' column
        }
        return "Total RAM size not found";
    }
}