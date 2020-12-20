package com.ashakhov.app.packagechallenge.dto;

import com.ashakhov.app.packagechallenge.model.Item;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class PackageRequest {
    @NonNull
    private final double limit;
    @NonNull
    private final List<Item> items;
}
