package com.sgurin.inetback.exeption;

public class PermissionException extends RuntimeException {
    public PermissionException(Long id) {
        super("id not found : " + id);
    }

    public PermissionException(String permissionName) {
        super("do not have enough privileges: " + permissionName);
    }

    public PermissionException() {
        super("do not have enough privileges");
    }
}
