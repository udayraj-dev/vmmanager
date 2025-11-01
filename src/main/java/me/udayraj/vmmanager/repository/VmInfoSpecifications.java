package me.udayraj.vmmanager.repository;

import org.springframework.data.jpa.domain.Specification;

import me.udayraj.vmmanager.model.VmInfo;
import me.udayraj.vmmanager.model.VmStatus;

public final class VmInfoSpecifications {

    private VmInfoSpecifications() {
        // Private constructor to prevent instantiation
    }

    public static Specification<VmInfo> withStatus(VmStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    public static Specification<VmInfo> isLinux(Boolean isLinux) {
        return (root, query, builder) -> builder.equal(root.get("isLinux"), isLinux);
    }

    public static Specification<VmInfo> hasOwnerName(String ownerName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("ownerName")), "%" + ownerName.toLowerCase() + "%");
    }

    public static Specification<VmInfo> hasCategory(String category) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("category")), "%" + category.toLowerCase() + "%");
    }

    public static Specification<VmInfo> hasPurpose(String purpose) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("purpose")), "%" + purpose.toLowerCase() + "%");
    }

    public static Specification<VmInfo> hasHostName(String hostName) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("hostName")), "%" + hostName.toLowerCase() + "%");
    }

    public static Specification<VmInfo> hasCpuCores(String cpuCores) {
        return (root, query, builder) -> builder.equal(root.get("cpuCores"), cpuCores);
    }

    public static Specification<VmInfo> hasTotalRamSize(String totalRamSize) {
        return (root, query, builder) -> builder.equal(root.get("totalRamSize"), totalRamSize);
    }

    public static Specification<VmInfo> hasIpAddress(String ipAddress) {
        return (root, query, builder) -> builder.equal(root.get("ipAddress"), ipAddress);
    }
}