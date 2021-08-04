package boutique.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String picture;
    private Double discount;
    private Double price;
    private Category category;
    private Set<Order> productOrders;
    private Set<Review> reviews;

    public Product() {}

    public Product(String name, String description, String picture, Double price, Category category) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.category = category;

        this.productOrders = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column(name = "discount")
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Column(name = "price", nullable = false)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @ManyToOne
    @JoinColumn(name = "categoryId", nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToMany(mappedBy = "products")
    public Set<Order> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(Set<Order> productOrders) {
        this.productOrders = productOrders;
    }

    @OneToMany(mappedBy = "product")
    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }
}
