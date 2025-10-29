package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired private ProductRepository productRepository;
    @Autowired private ServiceClass serviceClass;
    @Autowired private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> resp = new HashMap<>();
        try {
            if (!serviceClass.validateProduct(product)) {
                resp.put("message", "Product already exists");
                return resp;
            }
            productRepository.save(product);
            resp.put("message", "Product saved successfully");
            return resp;
        } catch (DataIntegrityViolationException e) {
            resp.put("message", "Data integrity error: " + e.getMostSpecificCause().getMessage());
            return resp;
        } catch (Exception e) {
            resp.put("message", "Error: " + e.getMessage());
            return resp;
        }
    }

    @GetMapping("/product/{id}")
    public Map<String, Object> getProductbyId(@PathVariable("id") Long id) {
        Map<String, Object> resp = new HashMap<>();
        // Avoid Optional compile error by using the built-in Optional finder
        Product p = productRepository.findById(id).orElse(null);
        resp.put("products", p);
        return resp;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> resp = new HashMap<>();
        try {
            productRepository.save(product);
            resp.put("message", "Product updated successfully");
            return resp;
        } catch (Exception e) {
            resp.put("message", "Error: " + e.getMessage());
            return resp;
        }
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable("name") String name,
                                                       @PathVariable("category") String category) {
        Map<String, Object> resp = new HashMap<>();
        boolean nameNull = "null".equalsIgnoreCase(name);
        boolean catNull  = "null".equalsIgnoreCase(category);

        if (nameNull && !catNull) {
            resp.put("products", productRepository.findByCategory(category));
        } else if (!nameNull && catNull) {
            resp.put("products", productRepository.findProductBySubName(name));
        } else if (!nameNull && !catNull) {
            resp.put("products", productRepository.findProductBySubNameAndCategory(name, category));
        } else {
            resp.put("products", List.of());
        }
        return resp;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> resp = new HashMap<>();
        resp.put("products", productRepository.findAll());
        return resp;
    }

    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable("category") String category,
                                                              @PathVariable("storeid") Long storeId) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("product", productRepository.findProductByCategory(category, storeId));
        return resp;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable("id") Long id) {
        Map<String, String> resp = new HashMap<>();
        if (!serviceClass.validateProductId(id)) {
            resp.put("message", "Product not present in database");
            return resp;
        }
        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);
        resp.put("message", "Product deleted successfully");
        return resp;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable("name") String name) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("products", productRepository.findProductBySubName(name));
        return resp;
    }
}
