package io.github.reconsolidated.malinka.mainPage;

import io.github.reconsolidated.malinka.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductsService {
    // this is cursed. i love tight deadlines
    private final ProductsRepository productsRepository;
    private final ProductsDbRepo productsDbRepo;

    public ProductsService(ProductsRepository productsRepository, ProductsDbRepo productsDbRepo) {
        this.productsRepository = productsRepository;
        this.productsDbRepo = productsDbRepo;
        this.productsDbRepo.saveAll(productsRepository.getAll());
    }

    public List<Product> getAll(){
        return productsDbRepo.findAll();
    }
    public List<Product> getByCategory(String category) {
        return productsRepository.getByCategory(category);
    }

    public List<Product> getPromotionsByCategory(String category) {
        return productsRepository.getPromotionByCategory(category);
    }

    public Product getByName(String productName) {
        return productsRepository.getByName(productName);
    }

    public Product getById(Long id){
        return productsDbRepo.getReferenceById(id);
    }

    public Product getRandomProduct() {
        return productsRepository.getRandomProduct();
    }

    public List<Product> getUniqueRandomForMainPage() {
       Set<Product> uniqueProducts = new HashSet<Product>();
       while (uniqueProducts.size() < Constants.RANDOM_PRODUCTS_FOR_MAIN_PAGE) {
           uniqueProducts.add(productsRepository.getRandomProduct());
       }

       return uniqueProducts.stream().toList();
    }
}
