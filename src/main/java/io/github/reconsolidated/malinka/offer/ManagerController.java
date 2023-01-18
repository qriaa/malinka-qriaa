package io.github.reconsolidated.malinka.offer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {
    @GetMapping("/manager")
    public String managerScreen(Model model) {
        return "manager";
    }
}
