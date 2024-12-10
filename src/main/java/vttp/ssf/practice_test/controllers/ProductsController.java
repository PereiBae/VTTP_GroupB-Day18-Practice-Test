package vttp.ssf.practice_test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vttp.ssf.practice_test.models.Products;
import vttp.ssf.practice_test.services.PracService;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private PracService pracSvc;

    @GetMapping
    public String getProducts(Model model) {
        List<Products> products;
        products = pracSvc.getAllProducts();
        model.addAttribute("Products", products);
        return "products";
    }

    @PostMapping("/{productId}")
    public String getBuyProducts(@PathVariable String productId, Model model) {

        Products product = pracSvc.getProductById(productId);
        if (product.getStock() == product.getBuy()) {
            model.addAttribute("Product", product);
            return "error";
        }
        int bought = product.getBuy();
        bought += 1;

        product.setBuy(bought);
        pracSvc.saveProduct(product);
        model.addAttribute("Product", product);
        return "successfulPurchase";
    }

}
