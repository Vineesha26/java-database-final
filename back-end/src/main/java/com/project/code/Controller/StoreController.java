package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired private StoreRepository storeRepository;
    @Autowired private OrderService orderService;

    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> resp = new HashMap<>();
        try {
            Store saved = storeRepository.save(store);
            resp.put("message", "Store created with id " + saved.getId());
            return resp;
        } catch (DataIntegrityViolationException e) {
            resp.put("message", "Data integrity error: " + e.getMostSpecificCause().getMessage());
            return resp;
        } catch (Exception e) {
            resp.put("message", "Error: " + e.getMessage());
            return resp;
        }
    }

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable("storeId") Long storeId) {
        return storeRepository.findById(storeId).isPresent();
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO request) {
        Map<String, String> resp = new HashMap<>();
        try {
            orderService.saveOrder(request);
            resp.put("message", "Order placed successfully");
            return resp;
        } catch (Exception e) {
            resp.put("Error", e.getMessage());
            return resp;
        }
    }
}
