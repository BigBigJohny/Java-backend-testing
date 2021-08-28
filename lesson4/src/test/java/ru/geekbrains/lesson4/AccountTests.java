package ru.geekbrains.lesson4;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .spec(positiveResponseSpecification);
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .spec(positiveResponseSpecification)
                .body("data.url", equalTo(username))
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
    }

    @Test
    void getAccountImagesTest() {
        given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .spec(positiveResponseSpecification)
                .when()
                .get("https://api.imgur.com/3/account/me/images")
                .prettyPeek();
    }

    @Test
    void getAccountBlockStatusTest() {
        Response response = given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/account/v1/{username}/block", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.blocked"), equalTo(false));
    }

    @Test
    void getAccountAvailableAvatarsTest() {
        Response response = given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/available_avatars", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.available_avatars_count"), lessThan(150));
    }

    @Test
    void getAccountCommentCountTest() {
        Response response = given()
                .spec(requestSpecificationWithAuth)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/comments/count", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data"), lessThan(5));
    }
}

