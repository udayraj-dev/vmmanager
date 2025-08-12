package com.rajmentors.vmmanager.model;

public record AppStructure(
        String javaHome,
        String deploymentFolder,
        String[] serverFolders,
        AppServer[] appServers) {
}

record AppServer(
        String serverHome,
        String javaOpts) {
}

/*
    {
        "javaHome": "javahome",
        "deploymentFolder" : "servers",
        "serverFolders" : [ "app", "ag", "licence", "help", "ssl-stores" ],
        "appServers" : [
            {
                "serverHome" : "app",
                "javaOpts" : ""
            },
            {
                "serverHome" : "ag",
                "javaOpts" : ""
            }
        ],
    }
    */