package com.ashakhov.app.packagechallenge.service;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.model.Item;
import com.ashakhov.app.packagechallenge.model.Package;
import java.util.LinkedList;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service("bruteforce")
public class BruteforceFinderService implements PackageFinder {

    @NonNull
    @Override
    public Package find(@NonNull PackageRequest request) {
        final Package aPackage = new Package(request.getLimit());
        calculate(request.getItems(), aPackage);
        return aPackage;
    }

    private void calculate(@NonNull List<Item> items, @NonNull Package aPackage) {
        if (!CollectionUtils.isEmpty(items)) {
            checkPackage(items, aPackage);
        }

        items.forEach(item -> calculate(excludeCurrent(items, item), aPackage));
    }

    private void checkPackage(@NonNull List<Item> items, @NonNull Package aPackage) {
        final Pair pair = count(items);

        if (pair.weight > aPackage.getLimit()) {
            return;
        }

        if (aPackage.getItems().isEmpty() || pair.price > aPackage.getPrice()) {
            aPackage.setItems(items);
            aPackage.setPrice(pair.price);
            aPackage.setWeight(pair.weight);
        }
    }

    @NonNull
    private List<Item> excludeCurrent(@NonNull List<Item> items, @NonNull Item currentItem) {
        final List<Item> newItems = new LinkedList<>(items);
        newItems.remove(currentItem);
        return newItems;
    }

    @NonNull
    private Pair count(@NonNull List<Item> items) {
        final Pair pair = new Pair();
        items.forEach(item -> {
            pair.price += item.getPrice();
            pair.weight += item.getWeight();
        });
        return pair;
    }

    private static final class Pair {
        private double price;
        private double weight;
    }
}
