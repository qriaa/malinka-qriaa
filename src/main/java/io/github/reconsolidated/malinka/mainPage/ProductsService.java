package io.github.reconsolidated.malinka.mainPage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productsRepository;

    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<Product> getByCategory(String category) {
        return productsRepository.getByCategory(category);
    }

    public Product getByName(String productName) {
        return productsRepository.getByName(productName);
    }

    public Product getRandomProduct() {
        return productsRepository.getRandomProduct();
    }
}
