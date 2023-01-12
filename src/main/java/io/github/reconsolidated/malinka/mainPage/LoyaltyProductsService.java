package io.github.reconsolidated.malinka.mainPage;

import io.github.reconsolidated.malinka.model.LoyaltyProduct;
import io.github.reconsolidated.malinka.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LoyaltyProductsService {

    private final LoyaltyProductsRepository productsRepository;

    public LoyaltyProductsService(LoyaltyProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public List<LoyaltyProduct> getLoyaltyProducts() {
        return productsRepository.getByCategory("all");
    }

    public LoyaltyProduct getByName(String productName) {
        return productsRepository.getByName(productName);
    }

    public LoyaltyProduct getRandomProduct() {
        return productsRepository.getRandomLoyaltyProduct();
    }

    public List<LoyaltyProduct> getUniqueRandomForMainPage() {
        Set<LoyaltyProduct> uniqueProducts = new HashSet<LoyaltyProduct>();
        while (uniqueProducts.size() < Constants.RANDOM_PRODUCTS_FOR_MAIN_PAGE) {
            uniqueProducts.add(productsRepository.getRandomLoyaltyProduct());
        }

        return uniqueProducts.stream().toList();
    }
}
