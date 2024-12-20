package com.ufrn.faulttolerance.ecommerce.model.dto;

public class ProductDTO {
    private String id;

    public ProductDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
