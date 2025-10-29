package com.project.code.Model;

import java.util.List;

public class PlaceOrderRequestDTO {
    private String customerName;
    private String email;
    private String phone;
    private Long storeId;
    private Double totalPrice;
    private List<PurchaseProductDTO> purchaseProduct;

    public PlaceOrderRequestDTO() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public List<PurchaseProductDTO> getPurchaseProduct() { return purchaseProduct; }
    public void setPurchaseProduct(List<PurchaseProductDTO> purchaseProduct) { this.purchaseProduct = purchaseProduct; }
}
