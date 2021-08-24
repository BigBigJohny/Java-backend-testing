package ru.geekbrains.lesson3;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ImageTest extends BaseTest {

    @Test
    void getImageCount() {
        Response response = given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/images/count", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data"), equalTo(9));
    }

    @Test
    void getImageIds() {
        Response response = given()
                .header("Authorization", "Bearer bb4a8386df82d707c829bf24161e31c637ae5c34")
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/images/ids/0", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data"), contains("45z8ctm", "USYYEAr", "j5hHDXf", "CEZTgv0", "MMHSEj4", "YHrqTel", "h6XTbrj", "JkgfgPF", "YxlIYfr"));
    }
}
