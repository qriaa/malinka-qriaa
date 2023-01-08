package io.github.reconsolidated.malinka.mainPage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Product(String name, String fileName, String category, String priceTag, double price) {
        this.name = name;
        this.fileName = fileName;
        this.category = category;
        this.priceTag = priceTag;
        this.price = price;
    }
}
