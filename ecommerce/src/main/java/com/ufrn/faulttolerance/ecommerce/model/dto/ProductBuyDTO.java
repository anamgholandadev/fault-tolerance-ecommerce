package com.ufrn.faulttolerance.ecommerce.model.dto;

public class ProductBuyDTO {
    private String productId;
    private String userId;
    private boolean ft;

    public ProductBuyDTO(String productId, String userId, boolean ft) {
        this.productId = productId;
        this.userId = userId;
        this.ft = ft;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isFt() {
        return ft;
    }

    public void setFt(boolean ft) {
        this.ft = ft;
    }
}
