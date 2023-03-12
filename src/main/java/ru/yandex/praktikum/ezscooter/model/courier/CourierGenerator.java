package ru.yandex.praktikum.ezscooter.model.courier;

import com.github.javafaker.Faker;

import java.util.Locale;

public class CourierGenerator {

    public static Courier getRandom() {
        Faker faker = Faker.instance(new Locale("en-GB"));
        String login = faker.name().username();
        String password = faker.internet().password();
        String firstName = faker.name().firstName();
        return new Courier(login, password, firstName);
    }

}