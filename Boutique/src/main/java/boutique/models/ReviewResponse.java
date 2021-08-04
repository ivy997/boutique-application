package boutique.models;

public class ReviewResponse {
    private Integer id;
    private Integer score;
    private String comment;
    private UserResponse user;
    private ProductResponse product;

    public ReviewResponse(Integer id, Integer score, String comment, UserResponse user, ProductResponse product) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }
}
