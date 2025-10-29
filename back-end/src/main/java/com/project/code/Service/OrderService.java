package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired private ProductRepository productRepository;
    @Autowired private InventoryRepository inventoryRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private StoreRepository storeRepository;
    @Autowired private OrderDetailsRepository orderDetailsRepository;
    @Autowired private OrderItemRepository orderItemRepository;

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        // Find or create customer by email
        Customer customer = customerRepository.findByEmail(placeOrderRequest.getEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setName(placeOrderRequest.getCustomerName());
            customer.setEmail(placeOrderRequest.getEmail());
            customer.setPhone(placeOrderRequest.getPhone());
            customer = customerRepository.save(customer);
        }

        // Get store
        Store store = storeRepository.findById(placeOrderRequest.getStoreId())
                .orElseThrow(() -> new RuntimeException("Store not found"));

        // Create order details
        OrderDetails order = new OrderDetails(customer, store,
                placeOrderRequest.getTotalPrice(), LocalDateTime.now());
        order = orderDetailsRepository.save(order);

        // For each purchased product, update inventory and create order item
        if (placeOrderRequest.getPurchaseProduct() != null) {
            for (PurchaseProductDTO pp : placeOrderRequest.getPurchaseProduct()) {
                // Update inventory
                Inventory inv = inventoryRepository.findByProductIdandStoreId(pp.getId(), store.getId());
                if (inv == null) {
                    throw new RuntimeException("Inventory not found for product id " + pp.getId() +
                            " at store id " + store.getId());
                }
                int newStock = (inv.getStockLevel() == null ? 0 : inv.getStockLevel()) - pp.getQuantity();
                if (newStock < 0) {
                    throw new RuntimeException("Insufficient stock for product id " + pp.getId());
                }
                inv.setStockLevel(newStock);
                inventoryRepository.save(inv);

                // Create order item
                Product product = productRepository.findById(pp.getId()).orElse(null);
                if (product == null) {
                    throw new RuntimeException("Product not found: " + pp.getId());
                }

                OrderItem item = new OrderItem(order, product, pp.getQuantity(), pp.getPrice());
                orderItemRepository.save(item);
            }
        }
    }
}
