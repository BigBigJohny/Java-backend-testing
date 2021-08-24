package ru.geekbrains.lesson3;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AccountTests extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", equalTo(username))
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
    }

    @Test
    void getAccountImagesTest() {
        given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .contentType("application/json")
                .when()
                .get("https://api.imgur.com/3/account/me/images")
                .prettyPeek();
    }

    @Test
    void getAccountBlockStatusTest() {
        Response response = given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
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
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
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
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
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
