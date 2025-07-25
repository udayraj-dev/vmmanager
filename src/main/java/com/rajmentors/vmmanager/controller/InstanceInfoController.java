package com.rajmentors.vmmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajmentors.vmmanager.model.InstanceRecord;
import com.rajmentors.vmmanager.model.LoginRecord;
import com.rajmentors.vmmanager.service.InstanceService;

@RestController
@RequestMapping("/instance")
public class InstanceInfoController {
    @Autowired
    InstanceService instanceService;

    @PostMapping("/info")
    public InstanceRecord getInformation(@RequestBody LoginRecord sshLoginRecord) {
        return instanceService.getInformation(sshLoginRecord);
    }
}
