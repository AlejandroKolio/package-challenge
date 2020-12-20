package com.ashakhov.app.packagechallenge.utils;

import static java.util.stream.Collectors.toList;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.exception.PackageFormatException;
import com.ashakhov.app.packagechallenge.model.Item;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PackageFinderUtils {
    @NonNull
    private static final Pattern PATTERN = Pattern.compile("\\((\\d+),([\\d|.]+),€([\\d|.]+)\\)");

    /**
     * Prepare packages from input file.
     * @param path represents path to file.
     * @return {@code List<PackageRequest>}
     */
    @NonNull
    public List<PackageRequest> readFromFile(@NonNull String path) {
        try {
            final Path file = Paths.get(
                    Objects.requireNonNull(PackageFinderUtils.class.getClassLoader().getResource(path)).toURI());
            try (final Stream<String> stream = Files.lines(file)) {
                return stream.filter(Objects::nonNull).filter(line -> !line.isEmpty()).map(line -> {
                    final String[] parts = line.replaceAll(" ", "").split(":");
                    final double limit = Double.parseDouble(parts[0]);
                    return new PackageRequest(limit, mapItems(parts[1]));
                }).collect(toList());
            } catch (IOException ex) {
                logger.error("Error occured while reading the file: ", ex.getCause());
                throw new PackageFormatException("Error occured while reading the file");
            }
        } catch (NullPointerException | URISyntaxException ex) {
            logger.error("Path '{}' is unknown or file might not exists: ", path);
            throw new PackageFormatException("Path is unknown or file might not exists: " + path);
        }
    }

    /**
     * read one line (for testing purposes)
     * @param line represents one line of data
     * @return {@link List<PackageRequest>}
     */
    @NonNull
    public List<PackageRequest> readFromLine(@NonNull String line) {
        final List<PackageRequest> packages = new ArrayList<>();
        final String[] parts = line.replaceAll(" ", "").split(":");
        final double limit = Double.parseDouble(parts[0]);

        final List<Item> validItems = mapItems(parts[1]).stream()
                .filter(item -> ValidationUtils.isValid(item, i -> item.getWeight() >= 100,
                        "weight is greater than 100"))
                .filter(item -> ValidationUtils.isValid(item, i -> item.getPrice() >= 100, "price is greater than 100"))
                .collect(toList());

        packages.add(new PackageRequest(limit, validItems));

        return packages.stream()
                .filter(pack -> ValidationUtils.isValid(pack, i -> pack.getLimit() >= 100,
                        "package limit is greater than 100"))
                .filter(pack -> ValidationUtils.isValid(pack, i -> pack.getItems().size() > 15,
                        "package must not be greater than 15"))
                .collect(toList());
    }

    /**
     * map line to Item
     * @param line represents a separate line to parse.
     * @return {@code List<Item>}
     */
    @NonNull
    private List<Item> mapItems(@NonNull String line) {
        final List<Item> items = new CopyOnWriteArrayList<>();
        final Matcher matcher = PATTERN.matcher(line);
        while (matcher.find()) {
            try {
                final int id = Integer.parseInt(matcher.group(1));
                final double weight = Double.parseDouble(matcher.group(2));
                final int price = Integer.parseInt(matcher.group(3));
                items.add(new Item(id, weight, price));
            } catch (NumberFormatException e) {
                throw new PackageFormatException(line);
            }
        }
        return items;
    }

    /**
     * Prinout a formated result.
     * @param items represents list of items.
     * @return {@code String}
     */
    @NonNull
    public String print(@NonNull List<Item> items) {
        if (items.isEmpty()) {
            logger.info("-");
            return "-";
        } else {
            final String line = items.stream().map(p -> p.getIndex() + "").sorted().collect(Collectors.joining(","));
            logger.info(line);
            return line;
        }
    }
}
