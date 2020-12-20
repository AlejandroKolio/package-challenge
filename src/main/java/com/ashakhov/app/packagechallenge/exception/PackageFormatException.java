package com.ashakhov.app.packagechallenge.exception;

import lombok.NonNull;

public class PackageFormatException extends RuntimeException {
    private static final long serialVersionUID = -3345312010173077688L;

    public PackageFormatException() {
        super("format is not valid");
    }

    public PackageFormatException(@NonNull String message) {
        super(String.format("format is not valid: %s", message));
    }
}
