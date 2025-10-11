package me.udayraj.vmmanager.repository;

import me.udayraj.vmmanager.model.VmInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VmInfoRepository extends JpaRepository<VmInfo, UUID> {
}
