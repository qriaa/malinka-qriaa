package io.github.reconsolidated.malinka.mainPage;

import io.github.reconsolidated.malinka.basket.BasketProduct;
import io.github.reconsolidated.malinka.basket.BasketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {
    private final ProductsService productsService;
    private final BasketService basketService;

    @GetMapping("/")
    public String mainPage(@RequestParam(required = false) String category, Model model) {
        if (category == null) {
            category = "all";
        }

        List<Product> products = productsService.getByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("products", products);
        model.addAttribute("basket", basketService.getProductsInBasket());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "index";
    }

    @PostMapping("/add_to_basket")
    public String addToBasket(@RequestParam(name="category") String category,
                              @RequestParam(name="productName") String productName,
                              @RequestParam(name="quantity") int quantity,
                              RedirectAttributes redirectAttributes) {
        if (category == null) {
            category = "all";
        }

        Product product = productsService.getByName(productName);
        basketService.addProduct(product, quantity);
        redirectAttributes.addAttribute("category", category);
        return "redirect:/";
    }

    @GetMapping("/basket")
    public String basketPage(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        model.addAttribute("basket", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "basket";
    }
}
