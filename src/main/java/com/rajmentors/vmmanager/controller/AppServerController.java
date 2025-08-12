package com.rajmentors.vmmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rajmentors.vmmanager.model.AppStructure;

@RestController
@RequestMapping("/appservers")
public class AppServerController {

    private void createFolders(AppStructure appStructure) {
        
    }

    private void deployJava(AppStructure appStructure) {

    }

    @PostMapping("/deploy")
    public ResponseEntity<String> deployAppServer(@RequestBody AppStructure appStructure) {
        createFolders(appStructure);
        deployJava(appStructure);
        return null;
    }
}
