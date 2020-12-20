package com.ashakhov.app.packagechallenge;

import com.ashakhov.app.packagechallenge.dto.PackageRequest;
import com.ashakhov.app.packagechallenge.model.Package;
import com.ashakhov.app.packagechallenge.service.PackageFinder;
import com.ashakhov.app.packagechallenge.utils.PackageFinderUtils;
import java.util.List;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Slf4j
@SpringBootApplication
public class PackageChallengeApplication {
    @NonNull
    private final PackageFinder packageFinder;

    public PackageChallengeApplication(@NonNull @Qualifier("dynamic") PackageFinder packageFinder) {
        this.packageFinder = packageFinder;
    }

    public static void main(String[] args) {
        SpringApplication.run(PackageChallengeApplication.class, args);
    }

    @Bean
    public CommandLineRunner init() {
        return args -> {
            final List<PackageRequest> requests;
            if (args.length == 0) {
                requests = PackageFinderUtils.readFromFile("sampleInput.txt");
            } else {
                requests = PackageFinderUtils.readFromFile(args[0]);
            }
            requests.forEach(r -> {
                final Package aPackage = packageFinder.find(r);
                PackageFinderUtils.print(aPackage.getItems());
            });
        };
    }
}
