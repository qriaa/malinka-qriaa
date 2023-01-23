package io.github.reconsolidated.malinka.mainPage;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is a mock for usecase purposes. Do not use it in production.
 */
@Repository
public class ProductsRepository {
    private final List<Product> productList;

    public List<Product> getAll(){
        return new ArrayList<>(productList);
    }
    public List<Product> getByCategory(String category) {
        if (category.equals("all")) {
            return productList;
        }
        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategory().equals(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> getPromotions() {

        List<Product> result = new ArrayList<>();
        for (Product product : productList) {
            if (product.isOnSale()) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> getPromotionByCategory(String category) {
        List<Product> promotions = getPromotions();

        if (category.equals("all")) {
            return promotions;
        }
        List<Product> result = new ArrayList<>();
        for (Product product : promotions) {
            if (product.getCategory().equals(category)) {
                result.add(product);
            }
        }
        return result;
    }

    public ProductsRepository() {
        productList = new ArrayList<>();
        // fill productList with 20 products with real names and categories, filenames should end wih .jpg
        productList.add(new Product("Mleko", "/images/mleko.jpg","dairy", "1,99 zł/szt", 1.99, false));
        productList.add(new Product("Jajka", "/images/jajka.jpg","dairy", "1,99 zł/szt", 1.99, true));
        productList.add(new Product("Ser", "/images/ser.jpg","dairy", "3,99 zł/kg", 3.99, true));
        productList.add(new Product("Masło", "/images/maslo.jpg","dairy", "4,99 zł/szt", 4.99, true));
        productList.add(new Product("Śmietana", "/images/smietana.jpg","dairy", "5,99 zł/szt", 5.99, true));
        productList.add(new Product("Kefir", "/images/kefir.jpg","dairy", "6,99 zł/szt", 6.99, false));
        productList.add(new Product("Jogurt", "/images/jogurt.jpg","dairy", "7,99 zł/szt", 7.99, true));
        productList.add(new Product("Mąka", "/images/maka.jpg","other", "8,99 zł/kg", 8.99, false));
    }

    public Product getByName(String productName) {
        for (Product product : productList) {
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        return null;
    }

    public Product getById(Long id) {
        for (Product product: productList) {
            if(product.getId().equals(id)){
                return product;
            }
        }
        return null;
    }

    public Product getRandomProduct() {
        Random random = new Random();
        int randomIndex = random.nextInt(productList.size());
        return productList.get(randomIndex);
    }
}
