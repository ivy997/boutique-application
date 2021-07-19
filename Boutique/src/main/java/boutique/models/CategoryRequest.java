package boutique.models;

import com.sun.istack.NotNull;

public class CategoryRequest {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
