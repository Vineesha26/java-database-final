package com.project.code.Controller;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Model.CombinedRequest;
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
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private ServiceClass serviceClass;

    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> resp = new HashMap<>();

        Product reqProduct = request.getProduct();
        Inventory reqInventory = request.getInventory();

        try {
            // Validate product id (avoid null comparison with primitive)
            if (reqProduct == null || reqProduct.getId() == 0L) {
                resp.put("message", "Invalid product id");
                return resp;
            }
            if (!serviceClass.validateProductId(reqProduct.getId())) {
                resp.put("message", "Product not present in database");
                return resp;
            }

            // Make sure inventory ties to the same product
            reqInventory.setProduct(reqProduct);

            // Find inventory row by product & store
            if (reqInventory.getStore() == null || reqInventory.getStore().getId() == 0L) {
                resp.put("message", "Invalid store id");
                return resp;
            }

            Inventory dbInv = inventoryRepository.findByProductIdandStoreId(
                    reqProduct.getId(), reqInventory.getStore().getId());

            if (dbInv == null) {
                resp.put("message", "No data available");
                return resp;
            }

            // Update product first (if needed)
            productRepository.save(reqProduct);

            // Update inventory fields
            if (reqInventory.getStockLevel() != null) {
                dbInv.setStockLevel(reqInventory.getStockLevel());
            }
            inventoryRepository.save(dbInv);

            resp.put("message", "Successfully updated product");
            return resp;

        } catch (DataIntegrityViolationException e) {
            resp.put("message", "Data integrity error: " + e.getMostSpecificCause().getMessage());
            return resp;
        } catch (Exception e) {
            resp.put("message", "Error: " + e.getMessage());
            return resp;
        }
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> resp = new HashMap<>();
        try {
            boolean okToCreate = serviceClass.validateInventory(inventory);
            if (!okToCreate) {
                resp.put("message", "Data already present");
                return resp;
            }
            inventoryRepository.save(inventory);
            resp.put("message", "Data saved successfully");
            return resp;
        } catch (DataIntegrityViolationException e) {
            resp.put("message", "Data integrity error: " + e.getMostSpecificCause().getMessage());
            return resp;
        } catch (Exception e) {
            resp.put("message", "Error: " + e.getMessage());
            return resp;
        }
    }

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(@PathVariable("storeid") Long storeId) {
        Map<String, Object> resp = new HashMap<>();
        List<Product> products = productRepository.findProductsByStoreId(storeId);
        resp.put("products", products);
        return resp;
    }

    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable("category") String category,
                                              @PathVariable("name") String name,
                                              @PathVariable("storeid") Long storeId) {
        Map<String, Object> resp = new HashMap<>();

        boolean catNull = "null".equalsIgnoreCase(category);
        boolean nameNull = "null".equalsIgnoreCase(name);

        if (catNull && !nameNull) {
            resp.put("product", productRepository.findByNameLike(storeId, name));
        } else if (!catNull && nameNull) {
            resp.put("product", productRepository.findByCategoryAndStoreId(storeId, category));
        } else if (!catNull && !nameNull) {
            resp.put("product", productRepository.findByNameAndCategory(storeId, name, category));
        } else {
            resp.put("product", List.of());
        }
        return resp;
    }

    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(@PathVariable("name") String name,
                                             @PathVariable("storeId") Long storeId) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("product", productRepository.findByNameLike(storeId, name));
        return resp;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable("id") Long id) {
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

    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable("quantity") Integer quantity,
                                    @PathVariable("storeId") Long storeId,
                                    @PathVariable("productId") Long productId) {
        Inventory inv = inventoryRepository.findByProductIdandStoreId(productId, storeId);
        if (inv == null || inv.getStockLevel() == null) return false;
        return inv.getStockLevel() >= quantity;
    }
}
