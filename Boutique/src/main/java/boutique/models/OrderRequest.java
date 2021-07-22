package boutique.models;

import java.util.List;

public class OrderRequest {
    private List<Integer> products;

    public List<Integer> getProducts() {
        return products;
    }

    public void setProducts(List<Integer> products) {
        this.products = products;
    }
}
