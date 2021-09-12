package ru.geekbrains.utils;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.db.dao.CategoriesMapper;
import ru.geekbrains.db.dao.ProductsMapper;
import ru.geekbrains.db.model.Categories;
import ru.geekbrains.db.model.CategoriesExample;
import ru.geekbrains.db.model.Products;
import ru.geekbrains.db.model.ProductsExample;
import ru.geekbrains.dto.Category;
import ru.geekbrains.enums.CategoryType;

import java.io.IOException;

@UtilityClass
public class DbUtils {
    private static String resource = "mybatisConfig.xml";
    static Faker faker = new Faker();

    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
        return sqlSessionFactory.openSession(true);
    }

    @SneakyThrows
    public static CategoriesMapper getCategoriesMapper() {
        return getSqlSession().getMapper(CategoriesMapper.class);
    }

    @SneakyThrows
    public static ProductsMapper getProductsMapper() {
        return getSqlSession().getMapper(ProductsMapper.class);
    }

    private static void createNewCategory(CategoriesMapper categoriesMapper) {
        Categories newCategory = new Categories();
        newCategory.setTitle(faker.animal().name());

        categoriesMapper.insert(newCategory);
    }

    public static void updateProductBykey(ProductsMapper productsMapper, String title, long key) {
        Products product = new Products();
        product.setTitle(title);
        product.setId(key);
        product.setPrice((int) (Math.random() * 10));
        product.setCategory_id((long) CategoryType.FOOD.getId());
        productsMapper.updateByPrimaryKey(product);
    }

    public static Integer countCategories(CategoriesMapper categoriesMapper) {
        long categoriesCount = categoriesMapper.countByExample(new CategoriesExample());
        return Math.toIntExact(categoriesCount);
    }

    public static Integer countProducts(ProductsMapper productsMapper) {
        long products = productsMapper.countByExample(new ProductsExample());
        return Math.toIntExact(products);
    }

    public static Products selectProduct(ProductsMapper productsMapper, long key) {
        return productsMapper.selectByPrimaryKey(key);
    }

    public static String selectCategories(CategoriesMapper categoriesMapper, int key) {
        return categoriesMapper.selectByPrimaryKey(key).getTitle();
    }
}
