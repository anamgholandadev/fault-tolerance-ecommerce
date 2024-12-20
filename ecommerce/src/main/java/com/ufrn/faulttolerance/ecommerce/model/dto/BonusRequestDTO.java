package com.ufrn.faulttolerance.ecommerce.model.dto;

public class BonusRequestDTO {

    private String user;
    private int bonus;

    public BonusRequestDTO(String user, int bonus) {
        this.user = user;
        this.bonus = bonus;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
