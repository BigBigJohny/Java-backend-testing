package ru.geekbrains.tests;

import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.geekbrains.dto.Category;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.CategoryType;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    static int id;
    Faker faker = new Faker();
    Product product, product2;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        product2 = new Product()
                .withId(id)
                .withTitle(faker.funnyName().name())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }

    @Test
    void postProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();

        id = response.body().getId();
        assertThat(response.body().getId(), notNullValue());
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(response.code(), equalTo(201));
    }

    @Test
    void putProductTest() throws IOException {
        Response<Product> response = productService.modifyProduct(product2).execute();

        assertThat(response.body().getId(), equalTo(product2.getId()));
        assertThat(response.body().getTitle(), equalTo(product2.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product2.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product2.getCategoryTitle()));
    }

    @Test
    void getCreateProduct() throws IOException {
        Response<Product> response = productService.getProduct(id).execute();

        assertThat(response.code(), equalTo(200));
    }

    @Test
    void getCategoryByIdTest() throws IOException {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService.getCategory(id).execute();

        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(), equalTo(id));
    }


    @Test
    void deleteProduct() throws IOException {
        Response<ResponseBody> response = productService.deleteProduct(0).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
}