package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ServiceClass(InventoryRepository inventoryRepository,
                        ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Returns false if an inventory record already exists for the given product+store, otherwise true.
     */
    public boolean validateInventory(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            return false;
        }
        Long productId = inventory.getProduct().getId();
        Long storeId = inventory.getStore().getId();
        if (productId == null || storeId == null) return false;

        Inventory existing = inventoryRepository.findByProductIdandStoreId(productId, storeId);
        return existing == null; // true -> no duplicate; false -> duplicate exists
    }

    /**
     * Returns false if a product with the same name already exists, otherwise true.
     */
    public boolean validateProduct(Product product) {
        if (product == null || product.getName() == null) return false;
        Product existing = productRepository.findByName(product.getName());
        return existing == null;
    }

    /**
     * Returns false if product doesn't exist, otherwise true.
     */
    public boolean validateProductId(Long id) {
        if (id == null) return false;
        return productRepository.findById(id).isPresent();
    }

    /**
     * Fetch existing inventory for given product+store pair.
     */
    public Inventory getInventoryId(Inventory inventory) {
        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            return null;
        }
        Long productId = inventory.getProduct().getId();
        Long storeId = inventory.getStore().getId();
        if (productId == null || storeId == null) return null;

        return inventoryRepository.findByProductIdandStoreId(productId, storeId);
    }
}
