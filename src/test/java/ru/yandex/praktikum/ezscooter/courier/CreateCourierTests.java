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

public class CreateCourierTests {

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
    public void courierCanBeCreatedWithValidDataCreated201() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.delete(courierId);
    }

    @Test
    public void courierCanBeCreatedWithoutFirstNameCreated201() {
        Courier courier = CourierGenerator.getRandom();
        courier.setFirstName(null);

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.delete(courierId);
    }

    @Test
    public void courierCannotBeCreatedWithDuplicateLoginError409() {
        Courier courier = CourierGenerator.getRandom();

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CREATED)
                .and()
                .assertThat()
                .body("ok", is(true));

        courierId = courierClient.login(CourierCredentials.from(courier))
                .assertThat()
                .body("id", notNullValue())
                .extract().path("id");

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .assertThat()
                .body("message", is("Этот логин уже используется"));

        courierClient.delete(courierId);
    }

    @Test
    public void courierCannotBeCreatedWithoutLoginBadRequest400() {
        Courier courier = CourierGenerator.getRandom();
        courier.setLogin(null);

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void courierCannotBeCreatedWithoutPasswordBadRequest400() {
        Courier courier = CourierGenerator.getRandom();
        courier.setPassword(null);

        courierClient.create(courier)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

}