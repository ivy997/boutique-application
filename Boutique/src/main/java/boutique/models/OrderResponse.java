package boutique.models;

import java.util.List;

public class OrderResponse {
    private Integer id;
    private UserResponse user;
    private List<ProductResponse> products;

    public OrderResponse(Integer id, UserResponse user, List<ProductResponse> products) {
        this.id = id;
        this.user = user;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
