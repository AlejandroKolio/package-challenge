package com.ashakhov.app.packagechallenge.service;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.model.Item;
import com.ashakhov.app.packagechallenge.model.Package;
import java.util.Comparator;
import java.util.List;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service("dynamic")
public class DynamicFinderService implements PackageFinder {

    @NonNull
    @Override
    public Package find(@NonNull PackageRequest request) {
        final Package aPackage = new Package(request.getLimit());
        final List<Item> items = request.getItems();
        final double mod = mod10(items);
        final int capacity = (int) (request.getLimit() * mod);
        final int[][] optimal = new int[items.size() + 1][capacity + 1];

        items.sort(Comparator.comparing(Item::getWeight));

        for (int i = 0; i <= capacity; i++) {
            optimal[0][i] = 0;
        }

        for (int i = 1; i <= items.size(); i++) {
            for (int j = 0; j <= capacity; j++) {
                if (items.get(i - 1).getWeight() * mod > j) {
                    optimal[i][j] = optimal[i - 1][j];
                } else {
                    optimal[i][j] = Math.max(optimal[i - 1][j],
                            optimal[i - 1][j - (int) (items.get(i - 1).getWeight() * mod)] + items.get(i - 1)
                                    .getPrice());
                }
            }
        }
        int cost = optimal[items.size()][capacity];
        int weight = capacity;

        for (int i = items.size(); i > 0 && cost > 0; i--) {
            if (cost != optimal[i - 1][weight]) {
                aPackage.add(items.get(i - 1));
                weight -= items.get(i - 1).getWeight() * mod;
                cost -= items.get(i - 1).getPrice();
            }
        }
        return aPackage;
    }

    /**
     * @return Find the maximum decimal place of items weights (say max) and returns 10^max.
     */
    private double mod10(@NonNull List<Item> items) {
        return Math.pow(10, items.stream()
                .map(Item::getWeight)
                .mapToInt(w -> String.valueOf(w).split("\\.").length)
                .max()
                .orElse(0));
    }
}
