package boutique.models;

public class FilterRequest extends PaginationRequest{
    private Integer categoryId;
    private String productKeyword;

    public FilterRequest(Integer elements, Integer pageIndex) {
        super(elements, pageIndex);
    }

    public FilterRequest(Integer elements, Integer pageIndex, Integer categoryId) {
        super(elements, pageIndex);

        this.categoryId = categoryId;
    }

    public FilterRequest(Integer elements, Integer pageIndex, String productKeyword) {
        super(elements, pageIndex);

        this.productKeyword = productKeyword;
    }

    public FilterRequest(Integer elements, Integer pageIndex, Integer categoryId, String productKeyword) {
        this(elements, pageIndex, categoryId);

        this.productKeyword = productKeyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductKeyword() {
        return productKeyword;
    }

    public void setProductKeyword(String productKeyword) {
        this.productKeyword = productKeyword;
    }
}
