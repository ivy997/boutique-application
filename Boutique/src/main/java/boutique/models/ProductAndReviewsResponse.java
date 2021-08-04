package boutique.models;

public class ProductAndReviewsResponse {
    private ProductResponse productResponse;
    private Integer totalPagesCount;

    public ProductAndReviewsResponse(ProductResponse productResponse, Integer totalPagesCount) {
        this.productResponse = productResponse;
        this.totalPagesCount = totalPagesCount;
    }

    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }

    public Integer getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(Integer totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
