package com.ashakhov.app.packagechallenge.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Item {
    @NonNull
    private final int index;
    @NonNull
    private final double weight;
    @NonNull
    private final int price;
}
