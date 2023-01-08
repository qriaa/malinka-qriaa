package io.github.reconsolidated.malinka.mainPage;

import io.github.reconsolidated.malinka.basket.BasketProduct;
import io.github.reconsolidated.malinka.basket.BasketService;
import io.github.reconsolidated.malinka.orders.OrderService;
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
    private final OrderService orderService;

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
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "basket";
    }

    @PostMapping("/update_basket_amount")
    public String updateBasketAmount(@RequestParam(name="productName") String productName,
                                     @RequestParam(name="quantity") int quantity,
                                     @RequestParam(name="added", required = false) Integer added,
                                     RedirectAttributes redirectAttributes) {
        if (added == null) added = 0;
        for (BasketProduct bp : basketService.getProductsInBasket()) {
            if (bp.getProduct().getName().equals(productName)) {
                if (quantity + added <= 0) {
                    basketService.removeProduct(bp.getProduct());
                } else {
                    bp.setQuantity(quantity + added);
                }
                return "redirect:/basket";
            }
        }
        return "redirect:/basket";
    }

    @GetMapping("/shipment")
    public String shipment(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "shipment_select";
    }

    @PostMapping("/select_shipment")
    public String selectShipment(
                                 @RequestParam(name="address", required = false) String address,
                                 @RequestParam(name="city", required = false) String city,
                                 @RequestParam(name="street", required = false) String street,
                                 @RequestParam(name="local_num", required = false) String localNum,
                                 @RequestParam(name="select-parcel-locker", required = false) String selectParcelLocker,
                                 @RequestParam(name="pickup-time", required = false) String pickupTime,
                                 RedirectAttributes redirectAttributes) {
        orderService.getOrder().setAddress(address);
        orderService.getOrder().setCity(city);
        orderService.getOrder().setStreet(street);
        orderService.getOrder().setLocalNumber(localNum);
        orderService.getOrder().setSelectParcelLocker(selectParcelLocker);
        orderService.getOrder().setPickupTime(pickupTime);

        return "redirect:/payment";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "payment_select";
    }

    @PostMapping("/payment_info")
    public String paymentInfo(@RequestParam(name="payment") String paymentMethod,
                              RedirectAttributes redirectAttributes) {
        orderService.getOrder().setPaymentMethod(paymentMethod);
        if (paymentMethod.equals("BLIK")) {
            orderService.getOrder().setPaymentSuccessful(false);
            return "redirect:/fail";
        } else {
            orderService.getOrder().setPaymentSuccessful(true);
            return "redirect:/success";
        }
    }

    @GetMapping("/success")
    public String success(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        model.addAttribute("order", orderService.getOrder());
        return "payment_success";
    }

    @GetMapping("/fail")
    public String fail(Model model) {
        return "no_payment";
    }
}
