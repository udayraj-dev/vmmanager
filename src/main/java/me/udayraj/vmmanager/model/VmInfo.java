package me.udayraj.vmmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import me.udayraj.vmmanager.dto.VMInformation;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class VmInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String category;
    private String purpose;
    private String ipAddress;
    private boolean isLinux;
    private int portNumber;
    private String username;
    private String password;
    private String osName;
    private String hostName;
    private String cpuCores;
    private String rootPartitionSize;
    private String totalRamSize;
    private String macAddress;
    private String ownerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;

    @Enumerated(EnumType.STRING)
    private VmStatus status = VmStatus.ACTIVE;

    public VmInfo() {
    }

    public VmInfo(VMInformation vmInformation) {
        this.ipAddress = vmInformation.ipAddress();
        this.portNumber = vmInformation.portNumber();
        this.username = vmInformation.username();
        this.password = vmInformation.password();
        this.osName = vmInformation.osName();
        this.hostName = vmInformation.hostName();
        this.cpuCores = vmInformation.cpuCores();
        this.rootPartitionSize = vmInformation.rootPartitionSize();
        this.totalRamSize = vmInformation.totalRamSize();
        this.macAddress = vmInformation.macAddress();
        this.category = vmInformation.category();
        this.purpose = vmInformation.purpose();
        this.ownerName = vmInformation.ownerName();
        this.isLinux = vmInformation.isLinux();
        // Allow overriding the default status if one is provided in the DTO
        if (vmInformation.status() != null) {
            this.status = vmInformation.status();
        }
    }

    /**
     * Updates the entity's fields from a DTO. This method is intended for mutating
     * an existing entity with new data from a client request. It specifically updates
     * user-editable fields like credentials and business context.
     *
     * @param vmInformation The DTO containing the new data.
     */
    public void updateFromDto(VMInformation vmInformation) {
        this.username = vmInformation.username();
        this.password = vmInformation.password();
        this.category = vmInformation.category();
        this.purpose = vmInformation.purpose();
        this.ownerName = vmInformation.ownerName();
        this.status = vmInformation.status();
        // Note: The @PreUpdate annotation will automatically update the lastUpdatedDate.
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isLinux() {
        return isLinux;
    }

    public void setLinux(boolean isLinux) {
        this.isLinux = isLinux;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public VmStatus getStatus() {
        return status;
    }

    public void setStatus(VmStatus status) {
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdDate = lastUpdatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = LocalDateTime.now();
    }
}
