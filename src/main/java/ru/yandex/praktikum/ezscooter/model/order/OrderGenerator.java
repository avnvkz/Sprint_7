package ru.yandex.praktikum.ezscooter.model.order;

import com.github.javafaker.Faker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import static java.util.concurrent.TimeUnit.DAYS;

public class OrderGenerator {

    public static Order getRandom() {
        Faker faker = Faker.instance(new Locale("ru"));
        Random random = new Random();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().streetAddress();
        String metroStation = Integer.toString(random.nextInt(221)); //от 1 до 220
        String phone = faker.phoneNumber().cellPhone().toString();
        int rentTime = random.nextInt(8); // от 1 до 7
        String deliveryDate = df.format(faker.date().future(100, DAYS));
        String comment = faker.weather().description();
        Color[] color = {Color.GRAY, Color.BLACK};

        return new Order(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
    }
}
