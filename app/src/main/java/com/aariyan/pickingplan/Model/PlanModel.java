package com.aariyan.pickingplan.Model;

public class PlanModel {
    private int intAutoPicking;
    private String Storename,Quantity,ItemCode,Description,SalesOrderNo;
    private int OrderId;
    private String mass;
    private int LineNos;
    private String weights,OrderDate,Instruction,Area,Toinvoice;

    public PlanModel(){}

    public PlanModel(int intAutoPicking, String storename, String quantity, String itemCode, String description, String salesOrderNo, int orderId, String mass, int lineNos, String weights, String orderDate, String instruction, String area, String toinvoice) {
        this.intAutoPicking = intAutoPicking;
        Storename = storename;
        Quantity = quantity;
        ItemCode = itemCode;
        Description = description;
        SalesOrderNo = salesOrderNo;
        OrderId = orderId;
        this.mass = mass;
        LineNos = lineNos;
        this.weights = weights;
        OrderDate = orderDate;
        Instruction = instruction;
        Area = area;
        Toinvoice = toinvoice;
    }

    public int getIntAutoPicking() {
        return intAutoPicking;
    }

    public void setIntAutoPicking(int intAutoPicking) {
        this.intAutoPicking = intAutoPicking;
    }

    public String getStorename() {
        return Storename;
    }

    public void setStorename(String storename) {
        Storename = storename;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSalesOrderNo() {
        return SalesOrderNo;
    }

    public void setSalesOrderNo(String salesOrderNo) {
        SalesOrderNo = salesOrderNo;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public int getLineNos() {
        return LineNos;
    }

    public void setLineNos(int lineNos) {
        LineNos = lineNos;
    }

    public String getWeights() {
        return weights;
    }

    public void setWeights(String weights) {
        this.weights = weights;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getInstruction() {
        return Instruction;
    }

    public void setInstruction(String instruction) {
        Instruction = instruction;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getToinvoice() {
        return Toinvoice;
    }

    public void setToinvoice(String toinvoice) {
        Toinvoice = toinvoice;
    }
}
