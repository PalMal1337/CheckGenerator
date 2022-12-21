package entity;

public class DiscountCard {
    private Long discountNumber;
    private Integer discount;

    public DiscountCard(Long discountNumber, Integer discount) {
        this.discountNumber = discountNumber;
        this.discount = discount;
    }

    public DiscountCard(){}

    public Long getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Long discountNumber) {
        this.discountNumber = discountNumber;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "DiscountCard{" +
                "discountNumber=" + discountNumber +
                ", discount=" + discount +
                '}';
    }
}
