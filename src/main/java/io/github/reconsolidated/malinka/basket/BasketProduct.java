package io.github.reconsolidated.malinka.basket;

import io.github.reconsolidated.malinka.mainPage.Product;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasketProduct {
    private Product product = new Product();
    private int quantity;

    public String getTotal() {
        return String.format("%.2f", product.getPrice() * quantity);
    }
}
