package io.github.reconsolidated.malinka.mainPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private String name = "default";
    private String fileName = "default";
    private String category = "default";
    private String priceTag = "default";
    private double price = 0.01;

    private boolean isOnSale = false;

    public Product(String name, String fileName, String category, String priceTag, double price, boolean isOnSale) {
        this.name = name;
        this.fileName = fileName;
        this.category = category;
        this.priceTag = priceTag;
        this.price = price;
        this.isOnSale = isOnSale;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;

        return Objects.equals(this.id, other.id);
    }
}
