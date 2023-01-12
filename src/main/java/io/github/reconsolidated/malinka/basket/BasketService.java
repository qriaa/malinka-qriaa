package io.github.reconsolidated.malinka.basket;

import io.github.reconsolidated.malinka.mainPage.Product;
import io.github.reconsolidated.malinka.model.LoyaltyProduct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasketService {
    private List<BasketProduct> productsInBasket = new ArrayList<>();
    private List<BasketLoyaltyProduct> loyaltyProductsInBasket = new ArrayList<>();

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

    public void addLoyaltyProduct(LoyaltyProduct product, int quantity) {
        for (BasketLoyaltyProduct basketLoyaltyProduct : loyaltyProductsInBasket) {
            if (basketLoyaltyProduct.getLoyaltyProduct().getName().equals(product.getName())) {
                basketLoyaltyProduct.setQuantity(basketLoyaltyProduct.getQuantity() + quantity);
                return;
            }
        }
        loyaltyProductsInBasket.add(new BasketLoyaltyProduct(product, quantity));
    }

    public void removeLoyaltyProduct(LoyaltyProduct product) {
        for (BasketLoyaltyProduct basketLoyaltyProduct: loyaltyProductsInBasket) {
            if (basketLoyaltyProduct.getLoyaltyProduct().getName().equals(product.getName())) {
                loyaltyProductsInBasket.remove(product);
                return;
            }
        }
    }

    public List<BasketLoyaltyProduct> getLoyaltyProductsInBasket() {
        return loyaltyProductsInBasket;
    }

    public void clearBasket() {
        productsInBasket.clear();
        loyaltyProductsInBasket.clear();
    }

    public double getTotal() {
        double total = 0;
        for (BasketProduct basketProduct : productsInBasket) {
            total += basketProduct.getProduct().getPrice() * basketProduct.getQuantity();
        }
        return total;
    }

    public int getNumOfProducts() {
        int total = 0;
        for (BasketProduct basketProduct : productsInBasket) {
            total += basketProduct.getQuantity();
        }
        for (BasketLoyaltyProduct basketLoyaltyProduct: loyaltyProductsInBasket) {
            total += basketLoyaltyProduct.getQuantity();
        }
        return total;
    }
}
