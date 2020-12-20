package com.ashakhov.app.packagechallenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.exception.PackageFormatException;
import com.ashakhov.app.packagechallenge.model.Package;
import com.ashakhov.app.packagechallenge.service.PackageFinder;
import com.ashakhov.app.packagechallenge.utils.PackageFinderUtils;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PackageChallengeApplicationTests {

    @Autowired
    @Qualifier("bruteforce")
    private PackageFinder bruteforceFinder;

    @Autowired
    @Qualifier("dynamic")
    private PackageFinder dynamicFinder;

    @NonNull
    private static Stream<Arguments> testDataProvider() {
        return Stream.of(
                Arguments.of("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)",
                        "4"), Arguments.of("8 : (1,15.3,€34)", "-"), Arguments.of(
                        "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)",
                        "2,7"), Arguments.of(
                        "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "8,9"));
    }

    @NonNull
    private static Stream<Arguments> invalidTestDataProvider() {
        return Stream.of(
                Arguments.of("102 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)",
                        "format is not valid: package limit is greater than 100"),
                Arguments.of(
                        "75 : (0,15.31,€29) (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78) (10,85.32,€29) (11,85.33,€29) (12,85.34,€29) (13,85.35,€29) (14,85.36,€29) (15,85.37,€29)",
                        "format is not valid: package must not be greater than 15"),
                Arguments.of(
                        "56 : (1,190.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "format is not valid: weight is greater than 100"),
                Arguments.of(
                        "46 : (1,19.72,€13) (2,33.80,€140) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "format is not valid: price is greater than 100"));
    }

    @ParameterizedTest
    @MethodSource("testDataProvider")
    void bruteforceTest(@NonNull String line, @NonNull String expected) {
        final List<PackageRequest> packages = PackageFinderUtils.readFromLine(line);
        packages.forEach(p -> {
            final Package aPackage = bruteforceFinder.find(p);
            assertThat(expected).isEqualTo(PackageFinderUtils.print(aPackage.getItems()));
        });
    }

    @ParameterizedTest
    @MethodSource("testDataProvider")
    void dynamicTest(@NonNull String line, @NonNull String expected) {
        final List<PackageRequest> packages = PackageFinderUtils.readFromLine(line);
        packages.forEach(p -> {
            final Package aPackage = dynamicFinder.find(p);
            assertThat(expected).isEqualTo(PackageFinderUtils.print(aPackage.getItems()));
        });
    }

    @ParameterizedTest
    @MethodSource("invalidTestDataProvider")
    public void constraintsTest(@NonNull String line, @NonNull String message) {
        final Exception exception = assertThrows(PackageFormatException.class, () -> {
            PackageFinderUtils.readFromLine(line);
        });
        assertEquals(message, exception.getMessage());
    }
}
