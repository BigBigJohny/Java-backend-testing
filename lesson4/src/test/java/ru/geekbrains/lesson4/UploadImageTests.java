package ru.geekbrains.lesson4;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Base64;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import ru.geekbrains.dto.PostImageResponse;

import static io.restassured.RestAssured.given;
import static ru.geekbrains.dto.Endpoints.UPLOAD_IMAGE;

public class UploadImageTests extends BaseTest {
    private final String PATH_TO_IMAGE = "src/main/resources/test_cat.jpg";
    static String encodedFile;
    String uploadedImageDeleteId;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification multiPartSpecWithFile;
    static RequestSpecification requestSpecificationWithAuthAndMultipartImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        multiPartSpecWithFile = new MultiPartSpecBuilder(new File(PATH_TO_IMAGE))
                .controlName("image")
                .build();

        requestSpecificationWithAuthAndMultipartImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(multiPartSpecWithFile)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();
    }

    @Test
    void uploadFileTest() {
        uploadedImageDeleteId = given(requestSpecificationWithAuthWithBase64, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

//    @AfterEach
//    void tearDown() {
//        given()
//                .headers("Authorization", token)
//                .when()
//                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", username, uploadedImageDeleteId)
//                .prettyPeek()
//                .then()
//                .statusCode(200);
//    }

    @Test
    void uploadFileImageTest() {
        uploadedImageDeleteId = given(requestSpecificationWithAuthAndMultipartImage)
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @Test
    void uploadWithMultiPart() {
        uploadedImageDeleteId = given(requestSpecificationWithAuthAndMultipartImage)
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

    }


    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}

