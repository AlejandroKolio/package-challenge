package com.ashakhov.app.packagechallenge.service;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.model.Package;
import lombok.NonNull;

public interface PackageFinder {

    @NonNull Package find(@NonNull PackageRequest request);
}
