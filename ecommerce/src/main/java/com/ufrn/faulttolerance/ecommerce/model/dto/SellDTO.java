package com.ufrn.faulttolerance.ecommerce.model.dto;

public class SellDTO {
    private String id;

    public SellDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}