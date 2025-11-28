package com.example.laundry.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * An order item refers to a {@link Clothes} instance and a quantity.
 * Used for ironing cost computation.
 */
public class OrderItem {
    private Clothes clothes;
    private int quantity = 1;

    public OrderItem() {}

    public OrderItem(Clothes clothes, int quantity) {
        this.clothes = clothes;
        this.quantity = quantity;
    }

    public Clothes getClothes() { return clothes; }
    public void setClothes(Clothes clothes) { this.clothes = clothes; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Total ironing cost for this OrderItem = per-item ironCost * quantity.
     */
    public BigDecimal totalIronCost() {
        if (clothes == null || clothes.getIronCost() == null) return BigDecimal.ZERO;
        return clothes.getIronCost().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        OrderItem that = (OrderItem) o;
        return quantity == that.quantity && Objects.equals(clothes, that.clothes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clothes, quantity);
    }
}