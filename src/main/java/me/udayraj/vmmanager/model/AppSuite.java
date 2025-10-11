package me.udayraj.vmmanager.model;

public record AppSuite(
    String javaHome,
    String deploymentHome,
    Component[] components) {}

record Component (
    String appName,
    boolean jeeContainer,
    String javaOpts) {}