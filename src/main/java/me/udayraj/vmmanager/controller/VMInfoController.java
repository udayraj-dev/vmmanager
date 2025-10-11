package me.udayraj.vmmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.udayraj.vmmanager.dto.VMInformation;
import me.udayraj.vmmanager.model.SshLogin;
import me.udayraj.vmmanager.service.VMService;

@RestController
@RequestMapping("/vm")
public class VMInfoController {
    @Autowired
    VMService vmService;

    @PostMapping("/test-connection")
    public VMInformation getInformation(@RequestBody SshLogin sshSshLogin) {
        VMInformation vmInformation = vmService.getInformation(sshSshLogin);
        if (vmInformation != null) {
            vmService.saveVmInfo(vmInformation);
        }
        return vmInformation;
    }
}
