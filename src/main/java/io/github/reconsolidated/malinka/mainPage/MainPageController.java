package io.github.reconsolidated.malinka.mainPage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {
    @GetMapping("/")
    public String mainPage(@RequestParam(required = false) String category) {
        if (category != null) {
            category.toLowerCase();
        }

        return "index";
    }
}
