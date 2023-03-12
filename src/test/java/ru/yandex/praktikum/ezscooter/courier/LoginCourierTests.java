package ru.yandex.praktikum.ezscooter.courier;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.praktikum.ezscooter.client.CourierClient;
import ru.yandex.praktikum.ezscooter.model.courier.Courier;
import ru.yandex.praktikum.ezscooter.model.courier.CourierCredentials;
import ru.yandex.praktikum.ezscooter.model.courier.CourierGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTests {

    private CourierClient courierClient;
    private int courierId;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    public void courierCanBeLoginWithValidDataOk200() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier);

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.delete(courierId);

    }

    @Test
    public void courierCannotBeLoginWithInvalidDataNotFound404() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    public void courierCannotBeLoginWithoutLoginBadRequest400() {
        Courier courier = CourierGenerator.getRandom();
        courier.setLogin("");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));

    }

    @Test
    public void courierCannotBeLoginWithoutPasswordBadRequest400() {
        Courier courier = CourierGenerator.getRandom();
        courier.setPassword("");

        courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", is("Недостаточно данных для входа"));
    }

}