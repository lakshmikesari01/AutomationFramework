package com.automationexercise.testdata;

import com.github.javafaker.Faker;

import java.util.UUID;

/**
 * TestDataGenerator
 * -----------------
 * Generates realistic, randomised test data using JavaFaker.
 * Each call to createUniqueUser() produces a distinct email,
 * preventing "email already registered" collisions between test runs.
 */
public class TestDataGenerator {

    private static final Faker faker = new Faker();

    private TestDataGenerator() {}

    /** Returns a fully populated unique user map. */
    public static UserData createUniqueUser() {
        String uid = UUID.randomUUID().toString().substring(0, 8);
        return new UserData(
                faker.name().fullName(),
                "qa_auto_" + uid + "@testmail.example.com",
                "SecurePass@123",
                faker.name().firstName(),
                faker.name().lastName(),
                faker.company().name(),
                faker.address().streetAddress(),
                "United States",
                faker.address().state(),
                faker.address().city(),
                faker.address().zipCode().replaceAll("[^0-9]", "0").substring(0, 5),
                faker.numerify("##########")
        );
    }

    // ── Inner record ─────────────────────────────────────────────────────────

    public record UserData(
            String name,
            String email,
            String password,
            String firstName,
            String lastName,
            String company,
            String address,
            String country,
            String state,
            String city,
            String zipcode,
            String mobile
    ) {}
}
