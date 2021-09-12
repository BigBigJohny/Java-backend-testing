package ru.geekbrains.tests;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Categories;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.dto.Category;
import ru.geekbrains.dto.Product;
import ru.geekbrains.enums.CategoryType;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.DbUtils;
import ru.geekbrains.utils.RetrofitUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProductTests {
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    static int id;
    Faker faker = new Faker();
    Product product, product2;
    int productId;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper = DbUtils.getCategoriesMapper();
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

    @SneakyThrows
    @Test
    void postProductTest() {
        Integer countProductsBefore = DbUtils.countProducts(productsMapper);
        Response<Product> response = productService.createProduct(product).execute();
        Integer countProductsAfter = DbUtils.countProducts(productsMapper);
        assertThat(countProductsAfter, equalTo(countProductsBefore + 1));
        id = response.body().getId();
        assertThat(response.body().getId(), notNullValue());
        assertThat(response.body().getTitle(), equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), equalTo(product.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(response.code(), equalTo(201));
        productId = response.body().getId();
        Products dbProduct = DbUtils.selectProduct(productsMapper, productId);

        assertThat(product.getTitle(), equalTo(dbProduct.getTitle()));
        assertThat(product.getPrice(), equalTo(dbProduct.getPrice()));
    }

    @SneakyThrows
    @Test
    void getUpdateProduct() {
        String name = faker.food().dish();
        DbUtils.updateProductBykey(productsMapper, name, id);
        Response<Product> response = productService.getProduct(id).execute();
        assertThat(response.body().getTitle(), equalTo(name));
    }

    @SneakyThrows
    @Test
    void putProductTest() {
        Response<Product> response = productService.modifyProduct(product2).execute();

        Products dbProduct = DbUtils.selectProduct(productsMapper, product2.getId());
//        assertThat(response.body().getId(), equalTo(product2.getId()));
//        assertThat(response.body().getTitle(), equalTo(product2.getTitle()));
//        assertThat(response.body().getPrice(), equalTo(product2.getPrice()));
//        assertThat(response.body().getCategoryTitle(), equalTo(product2.getCategoryTitle()));
        String categoryTitle = DbUtils.selectCategories(categoriesMapper, dbProduct.getCategory_id().intValue());
        assertThat(response.body().getId(), equalTo(dbProduct.getId().intValue()));
        assertThat(response.body().getTitle(), equalTo(dbProduct.getTitle()));
        assertThat(response.body().getPrice(), equalTo(dbProduct.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(categoryTitle));
    }

    @SneakyThrows
    @Test
    void getCreateProduct() {
        Response<Product> response = productService.getProduct(id).execute();
        Products dbProduct = DbUtils.selectProduct(productsMapper, id);

        String categoryTitle = DbUtils.selectCategories(categoriesMapper, dbProduct.getCategory_id().intValue());
        assertThat(response.body().getId(), equalTo(dbProduct.getId().intValue()));
        assertThat(response.body().getTitle(), equalTo(dbProduct.getTitle()));
        assertThat(response.body().getPrice(), equalTo(dbProduct.getPrice()));
        assertThat(response.body().getCategoryTitle(), equalTo(categoryTitle));

        assertThat(response.code(), equalTo(200));
    }

    @SneakyThrows
    @Test
    void getCategoryByIdTest() {
        Integer id = CategoryType.FOOD.getId();
        Response<Category> response = categoryService.getCategory(id).execute();

        assertThat(response.body().getTitle(), equalTo(CategoryType.FOOD.getTitle()));
        assertThat(response.body().getId(), equalTo(id));

        int countCategories = DbUtils.countCategories(categoriesMapper);
        assertThat(countCategories, equalTo(32));
    }

    @SneakyThrows
    @Test
    void deleteProduct() {
        Response<ResponseBody> response = productService.deleteProduct(0).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
    }
}