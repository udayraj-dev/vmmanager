package me.udayraj.vmmanager.exception;

public class VmPersistenceException extends RuntimeException {
    public VmPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public VmPersistenceException(String message) {
        super(message);
    }
}
