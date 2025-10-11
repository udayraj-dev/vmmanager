package me.udayraj.vmmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.udayraj.vmmanager.model.AppSuite;

@RestController
@RequestMapping("/appservers")
public class AppServerController {

    private void createFolders(AppSuite appSuite) {
        
    }

    private void deployJava(AppSuite appSuite) {

    }

    @PostMapping("/deploy")
    public ResponseEntity<String> deployAppServer(@RequestBody AppSuite appSuite) {
        createFolders(appSuite);
        deployJava(appSuite);
        return null;
    }
}
