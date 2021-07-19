package boutique.models;

public class ProductResponse {
    private Integer id;
    private String name;
    private String description;
    private String picture;
    private Double discount;
    private Double price;
    private CategoryResponse category;

    public ProductResponse(Integer id, String name, String description, String picture,
                           Double discount, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.discount = discount;
        this.price = price;
    }

    public ProductResponse(Integer id, String name, String description, String picture,
                           Double discount, Double price, CategoryResponse category) {
        this(id, name, description, picture, discount, price);

        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }
}
