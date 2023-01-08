package io.github.reconsolidated.malinka.basket;

import io.github.reconsolidated.malinka.mainPage.Product;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class BasketProduct {
    @GeneratedValue
    @Id
    private Long id;
    private String productName;
    @Transient
    private Product product = new Product();
    private int quantity;

    public String getTotal() {
        return String.format("%.2f", product.getPrice() * quantity);
    }

    public BasketProduct(Product product, int quantity) {
        this.product = product;
        this.productName = product.getName();
        this.quantity = quantity;
    }
}
