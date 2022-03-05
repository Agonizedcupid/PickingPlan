package com.aariyan.pickingplan.Model;

public class PostModel {
    private int pickingId;
    private String itemName,quantity;

    public PostModel(){}

    public PostModel(int pickingId, String itemName, String quantity) {
        this.pickingId = pickingId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public int getPickingId() {
        return pickingId;
    }

    public void setPickingId(int pickingId) {
        this.pickingId = pickingId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
