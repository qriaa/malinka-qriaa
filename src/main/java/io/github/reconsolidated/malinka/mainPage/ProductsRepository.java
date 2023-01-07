package io.github.reconsolidated.malinka.mainPage;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductsRepository {
    private final List<Product> productList;

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

    public ProductsRepository() {
        productList = new ArrayList<>();
        // fill productList with 20 products with real names and categories, filenames should end wih .jpg
        productList.add(new Product("Mleko", "/images/mleko.jpg","dairy", "1,99 zł", 1.99));
        productList.add(new Product("Jajka", "/images/mleko.jpg","dairy", "2,99 zł", 2.99));
        productList.add(new Product("Ser", "/images/mleko.jpg","dairy", "3,99 zł", 3.99));
        productList.add(new Product("Masło", "/images/mleko.jpg","dairy", "4,99 zł", 4.99));
        productList.add(new Product("Śmietana", "/images/mleko.jpg","dairy", "5,99 zł", 5.99));
        productList.add(new Product("Kefir", "/images/mleko.jpg","dairy", "6,99 zł", 6.99));
        productList.add(new Product("Jogurt", "/images/mleko.jpg","dairy", "7,99 zł", 7.99));
        productList.add(new Product("Mąka", "/images/mleko.jpg","other", "8,99 zł", 8.99));
        productList.add(new Product("Ryż", "/images/mleko.jpg","other", "9,99 zł", 9.99));
        productList.add(new Product("Płatki", "/images/mleko.jpg","other", "10,99 zł", 10.99));
        productList.add(new Product("Kasza", "/images/mleko.jpg","other", "11,99 zł", 11.99));
        productList.add(new Product("Makaron", "/images/mleko.jpg","other", "12,99 zł", 12.99));
        productList.add(new Product("Kurczak", "/images/mleko.jpg","meat", "13,99 zł", 13.99));
        productList.add(new Product("Wołowina", "/images/mleko.jpg","meat", "14,99 zł", 14.99));
        productList.add(new Product("Wędliny", "/images/mleko.jpg","meat", "15,99 zł", 15.99));
        productList.add(new Product("Kiełbasa", "/images/mleko.jpg","meat", "16,99 zł", 16.99));
        productList.add(new Product("Krewetki", "/images/mleko.jpg","meat", "17,99 zł", 17.99));
        productList.add(new Product("Kurczak", "/images/mleko.jpg","meat", "18,99 zł", 18.99));
        productList.add(new Product("Wołowina", "/images/mleko.jpg","meat", "19,99 zł", 19.99));
        productList.add(new Product("Wędliny", "/images/mleko.jpg","meat", "20,99 zł", 20.99));
        productList.add(new Product("Kiełbasa", "/images/mleko.jpg","meat", "21,99 zł", 21.99));
        productList.add(new Product("Krewetki", "/images/mleko.jpg","meat", "22,99 zł", 22.99));
        // dodaj 7 produktow ktore beda owocami
        productList.add(new Product("Jabłka 2kg", "/images/mleko.jpg","fruit", "23,99 zł", 23.99));
        productList.add(new Product("Gruszki 1kg", "/images/mleko.jpg","fruit", "24,99 zł", 24.99));
        productList.add(new Product("Pomarańcze 5kg", "/images/mleko.jpg","fruit", "25,99 zł", 25.99));
        productList.add(new Product("Banan 3szt.", "/images/mleko.jpg","fruit", "26,99 zł", 26.99));
        productList.add(new Product("Truskawki 20szt.", "/images/mleko.jpg","fruit", "27,99 zł", 27.99));
        productList.add(new Product("Maliny 1kg", "/images/mleko.jpg","fruit", "28,99 zł", 28.99));
        productList.add(new Product("Cytryny 2kg", "/images/mleko.jpg","fruit", "29,99 zł", 29.99));
        // dodaj 7 produktow ktore beda warzywami
        productList.add(new Product("Marchew 1kg", "/images/mleko.jpg","vegetable", "14,99 zł", 14.99));
        productList.add(new Product("Pietruszka 1kg", "/images/mleko.jpg","vegetable", "15,99 zł", 15.99));
        productList.add(new Product("Cebula 1kg", "/images/mleko.jpg","vegetable", "16,99 zł", 16.99));
        productList.add(new Product("Papryka 1kg", "/images/mleko.jpg","vegetable", "6,49 zł", 6.49));
        productList.add(new Product("Pomidor 1kg", "/images/mleko.jpg","vegetable", "7,49 zł", 7.49));
        productList.add(new Product("Ziemniaki 1kg", "/images/mleko.jpg","vegetable", "8,49 zł", 8.49));
        productList.add(new Product("Kalafior 1kg", "/images/mleko.jpg","vegetable", "9,49 zł", 9.49));

        productList.add(new Product("Kalafior 1kg", "/images/mleko.jpg","sweet", "9,49 zł", 9.49));
        productList.add(new Product("Kalafior 1kg", "/images/mleko.jpg","sweet", "9,49 zł", 9.49));

    }
}
