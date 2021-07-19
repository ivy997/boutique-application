package boutique.models;

import java.util.List;

public class CategoryResponse {
    private Integer id;
    private String name;
    private List<ProductResponse> product;

    public CategoryResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryResponse(Integer id, String name, List<ProductResponse> product) {
        this(id, name);

        this.product = product;
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
}
