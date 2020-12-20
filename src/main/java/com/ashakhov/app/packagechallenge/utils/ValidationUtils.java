package com.ashakhov.app.packagechallenge.utils;

import com.ashakhov.app.packagechallenge.exception.PackageFormatException;
import java.util.function.Function;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationUtils<T> {

    public <T> boolean isValid(@NonNull T t, @NonNull Function<T, Boolean> condition, @NonNull String message)
            throws PackageFormatException {
        if (condition.apply(t)) {
            throw new PackageFormatException(message);
        } else {
            return true;
        }
    }
}
