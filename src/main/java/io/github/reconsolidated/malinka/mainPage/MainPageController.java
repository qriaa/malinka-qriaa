package io.github.reconsolidated.malinka.mainPage;

import io.github.reconsolidated.malinka.basket.BasketLoyaltyProduct;
import io.github.reconsolidated.malinka.basket.BasketProduct;
import io.github.reconsolidated.malinka.basket.BasketService;
import io.github.reconsolidated.malinka.model.LoyaltyProduct;
import io.github.reconsolidated.malinka.model.User;
import io.github.reconsolidated.malinka.orders.Order;
import io.github.reconsolidated.malinka.orders.OrderService;
import io.github.reconsolidated.malinka.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class MainPageController {
    private final ProductsService productsService;
    private final LoyaltyProductsService loyaltyProductsService;
    private final BasketService basketService;
    private final OrderService orderService;

    private final UserService userService;

    @GetMapping("/")
    public String mainPage(@RequestParam(required = false) String category,
                           @RequestParam(required = false) String promotionCategory,
                           Model model) {

        if (category == null) {
            category = "all";
        }
        if (promotionCategory == null) {
            promotionCategory = "all";
        }

        List<Product> products = productsService.getByCategory(category);
        List<Product> promotions = productsService.getPromotionsByCategory(category);
        List<Product> randomProducts = productsService.getUniqueRandomForMainPage();
        List<LoyaltyProduct> loyaltyProducts = loyaltyProductsService.getLoyaltyProducts();
        User user = userService.getUserByUsername("jkowal");

        model.addAttribute("category", category);
        model.addAttribute("promotionCategory", promotionCategory);
        model.addAttribute("products", products);
        model.addAttribute("promotions", promotions);
        model.addAttribute("loyaltyProducts", loyaltyProducts);
        model.addAttribute("randomProducts", randomProducts);
        model.addAttribute("basketSize", basketService.getNumOfProducts());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        model.addAttribute("userInfo", String.format("%s %s", user.getName(), user.getSurname()));
        model.addAttribute("userPoints", user.getLoyaltyPoints());
        return "index";
    }

    @GetMapping("/reclamation_success")
    public String reclamationSuccess(Model model) {
        model.addAttribute("basket", basketService.getProductsInBasket());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "reclamation_success";
    }

    @GetMapping("/reclamation")
    public String reclamationsPage(@RequestParam("orderId") long orderId, Model model) {
        Order order = orderService.getOrders().stream().filter((o) -> o.getId() == orderId).findFirst().orElseThrow();
        model.addAttribute("order", order);
        model.addAttribute("basketProducts", basketService.getProductsInBasket());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "reclamation";
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

    @PostMapping("/add_loyalty_to_basket")
    public String addLoyaltyToBasket(@RequestParam(name="category") String category,
                              @RequestParam(name="productName") String productName,
                              @RequestParam(name="quantity") int quantity,
                              @RequestParam(name="points") int points,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (category == null) {
            category = "all";
        }

        LoyaltyProduct product = loyaltyProductsService.getByName(productName);

        int usedPoints = basketService.getLoyaltyTotal();

        if(product.getPoints() * quantity + usedPoints > points) {
            model.addAttribute("product", String.format("%s %d pkt", product.getName(), product.getPoints()));
            model.addAttribute("userPoints", String.format("%d pkt", points));
            return "basket_error_points";
        }

        basketService.addLoyaltyProduct(product, quantity);
        redirectAttributes.addAttribute("category", category);
        return "redirect:/";
    }

    @GetMapping("/basket")
    public String basketPage(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        List<BasketLoyaltyProduct> basketLoyaltyProducts = basketService.getLoyaltyProductsInBasket();

        if(basketProducts.isEmpty() && basketLoyaltyProducts.isEmpty()) {
            return "basket_empty";
        }
        User user = userService.getUserByUsername("jkowal");

        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketLoyaltyProducts", basketLoyaltyProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        model.addAttribute("loyaltyTotal", basketService.getLoyaltyTotal() + " pkt.");
        model.addAttribute("basketSize", basketService.getNumOfProducts());
        model.addAttribute("userInfo", String.format("%s %s", user.getName(), user.getSurname()));
        model.addAttribute("userPoints", user.getLoyaltyPoints());
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

    @PostMapping("/update_basket_loyalty_amount")
    public String updateBasketLoyaltyAmount(@RequestParam(name="productName") String productName,
                                     @RequestParam(name="quantity") int quantity,
                                     @RequestParam(name="points") int points,
                                     @RequestParam(name="added", required = false) Integer added,
                                     RedirectAttributes redirectAttributes,
                                     Model model) {

        if (added == null) added = 0;
        List<BasketLoyaltyProduct> productsInBasket = basketService.getLoyaltyProductsInBasket();

        for (BasketLoyaltyProduct bp : productsInBasket) {
            if (bp.getLoyaltyProduct().getName().equals(productName)) {
                if (quantity + added <= 0) {
                    basketService.removeLoyaltyProduct(bp.getLoyaltyProduct());
                } else {
                    bp.setQuantity(quantity + added);
                    int totalPoints = basketService.getLoyaltyTotal();

                    if (totalPoints > points) {
                        bp.setQuantity(quantity);
                        model.addAttribute("product", String.format("%s %d pkt", bp.getLoyaltyProduct().getName(), bp.getLoyaltyProduct().getPoints()));
                        model.addAttribute("userPoints", String.format("%d pkt", points));
                        return "basket_error_points";
                    }
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

    @GetMapping("/history")
    public String transactionHistory(Model model) {
        model.addAttribute("orders", orderService.getOrders());
        model.addAttribute("basketProducts", basketService.getProductsInBasket());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "transaction_history";
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
        orderService.getCurrentOrder().setAddress(address);
        orderService.getCurrentOrder().setCity(city);
        orderService.getCurrentOrder().setStreet(street);
        orderService.getCurrentOrder().setLocalNumber(localNum);
        orderService.getCurrentOrder().setSelectParcelLocker(selectParcelLocker);
        orderService.getCurrentOrder().setPickupTime(pickupTime);

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
        orderService.getCurrentOrder().setPaymentMethod(paymentMethod);
        orderService.getCurrentOrder().setCost(String.format("%.2f",basketService.getTotal()) + " zł");
        orderService.getCurrentOrder().setAmount(basketService.getProductsInBasket().size());
        orderService.getCurrentOrder().getProducts().addAll(basketService.getProductsInBasket());
        if (paymentMethod.equals("BLIK")) {
            orderService.getCurrentOrder().setPaymentSuccessful(false);
            orderService.saveOrder();
            basketService.clearBasket();
            return "redirect:/fail";
        } else {
            orderService.getCurrentOrder().setPaymentSuccessful(true);
            orderService.saveOrder();
            basketService.clearBasket();
            return "redirect:/success";
        }
    }

    @GetMapping("/success")
    public String success(Model model) {
        List<BasketProduct> basketProducts = basketService.getProductsInBasket();
        model.addAttribute("basketProducts", basketProducts);
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        model.addAttribute("order", orderService.getCurrentOrder());
        return "payment_success";
    }

    @GetMapping("/fail")
    public String fail(Model model) {
        model.addAttribute("basketProducts", basketService.getProductsInBasket());
        model.addAttribute("basketTotal", String.format("%.2f", basketService.getTotal()) + " zł");
        return "no_payment";
    }
}
