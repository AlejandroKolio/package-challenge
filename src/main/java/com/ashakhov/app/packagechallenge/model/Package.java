package com.ashakhov.app.packagechallenge.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Package {
    @NonNull
    private final double limit;
    @NonNull
    private List<Item> items;
    @NonNull
    private double weight;
    @NonNull
    private double price;

    public Package(@NonNull Double limit) {
        this.limit = limit;
        items = new CopyOnWriteArrayList<>();
    }

    public void add(Item item) {
        items.add(item);
        weight += item.getWeight();
    }
}
