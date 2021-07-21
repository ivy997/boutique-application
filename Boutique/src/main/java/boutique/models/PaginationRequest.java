package boutique.models;

public class PaginationRequest {
    private Integer elements = 12;
    private Integer pageIndex = 1;
    private String sortBy = "id";

    public Integer getElements() {
        return elements;
    }

    public void setElements(Integer elements) {
        this.elements = elements;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
