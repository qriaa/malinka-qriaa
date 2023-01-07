package io.github.reconsolidated.malinka.mainPage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {
    private final ProductsService productsService;

    @GetMapping("/")
    public String mainPage(@RequestParam(required = false) String category, Model model) {
        if (category == null) {
            category = "all";
        }

        List<Product> products = productsService.getByCategory(category);
        model.addAttribute("category", category);
        model.addAttribute("products", products);

        return "index";
    }
}
