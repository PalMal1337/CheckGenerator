package entity;

public class Product {
    private Long id;
    private Integer discountNumber;
    private String productName;
    private Double price;
    private Boolean isOpt;

    public Product(Long id, Integer discountNumber, String productName, Double price, Boolean isOpt) {
        this.id = id;
        this.discountNumber = discountNumber;
        this.productName = productName;
        this.price = price;
        this.isOpt = isOpt;
    }

    public Product(){}

    public void setId(Long id) {
        this.id = id;
    }

    public void setDiscountNumber(Integer discountNumber) {
        this.discountNumber = discountNumber;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setOpt(Boolean opt) {
        isOpt = opt;
    }

    public Long getId() {
        return id;
    }

    public Integer getDiscountNumber() {
        return discountNumber;
    }

    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getOpt() {
        return isOpt;
    }

    @Override
    public String toString() {
        return productName +"\t\t$"+ price;
    }
}
