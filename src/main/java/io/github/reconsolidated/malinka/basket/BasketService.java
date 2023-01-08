package io.github.reconsolidated.malinka.basket;

import io.github.reconsolidated.malinka.mainPage.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {
    private List<BasketProduct> productsInBasket = new ArrayList<>();

    public void addProduct(Product product, int quantity) {
        for (BasketProduct basketProduct : productsInBasket) {
            if (basketProduct.getProduct().getName().equals(product.getName())) {
                basketProduct.setQuantity(basketProduct.getQuantity() + quantity);
                return;
            }
        }
        productsInBasket.add(new BasketProduct(product, quantity));
    }

    public void removeProduct(Product product) {
        for (BasketProduct basketProduct : productsInBasket) {
            if (basketProduct.getProduct().getName().equals(product.getName())) {
                productsInBasket.remove(basketProduct);
                return;
            }
        }
    }

    public List<BasketProduct> getProductsInBasket() {
        return productsInBasket;
    }

    public void clearBasket() {
        productsInBasket.clear();
    }

    public double getTotal() {
        double total = 0;
        for (BasketProduct basketProduct : productsInBasket) {
            total += basketProduct.getProduct().getPrice() * basketProduct.getQuantity();
        }
        return total;
    }
}
