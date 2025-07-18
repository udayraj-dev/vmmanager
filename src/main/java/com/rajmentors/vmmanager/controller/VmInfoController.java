package com.rajmentors.vmmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajmentors.vmmanager.model.SshLoginRecord;
import com.rajmentors.vmmanager.model.VmInfo;
import com.rajmentors.vmmanager.service.VMInfoService;

@RestController
@RequestMapping("/vm")
public class VmInfoController {
    @Autowired
    VMInfoService vmInfoService;

    @PostMapping("/info")
    public VmInfo getVMInfo(@RequestBody SshLoginRecord sshLoginRecord) {
        return vmInfoService.getVMInfo(sshLoginRecord);
    }

}
