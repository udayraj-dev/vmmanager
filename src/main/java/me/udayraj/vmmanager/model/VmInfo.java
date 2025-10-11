package me.udayraj.vmmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
public class VmInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    private String ipAddress;
    private int portNumber;
    private String username;
    private String password;
    private String osName;
    private String hostName;
    private String cpuCores;
    private String rootPartitionSize;
    private String totalRamSize;
    private String macAddress;

    public VmInfo() {
    }

    public VmInfo(String ipAddress, int portNumber, String username, String password, String osName, String hostName, String cpuCores, String rootPartitionSize, String totalRamSize, String macAddress) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.username = username;
        this.password = password;
        this.osName = osName;
        this.hostName = hostName;
        this.cpuCores = cpuCores;
        this.rootPartitionSize = rootPartitionSize;
        this.totalRamSize = totalRamSize;
        this.macAddress = macAddress;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(String cpuCores) {
        this.cpuCores = cpuCores;
    }

    public String getRootPartitionSize() {
        return rootPartitionSize;
    }

    public void setRootPartitionSize(String rootPartitionSize) {
        this.rootPartitionSize = rootPartitionSize;
    }

    public String getTotalRamSize() {
        return totalRamSize;
    }

    public void setTotalRamSize(String totalRamSize) {
        this.totalRamSize = totalRamSize;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
