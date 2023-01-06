package io.github.reconsolidated.malinka.mainPage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private String name;
    private String fileName;
    private String category;
    private String priceTag;
    private double price;
}
