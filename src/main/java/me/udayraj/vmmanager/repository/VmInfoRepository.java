package me.udayraj.vmmanager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import me.udayraj.vmmanager.model.VmInfo;
@Repository
public interface VmInfoRepository extends JpaRepository<VmInfo, UUID>, JpaSpecificationExecutor<VmInfo> {}
