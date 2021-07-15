package boutique.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    private Integer id;
    private User user;
    private Set<Product> products;

    public Order() {}

    public Order(User user, Set<Product> products) {
        this.user = user;
        this.products = products;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany
    @JoinTable(name = "orders_products")
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
