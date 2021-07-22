package boutique.models;

import java.util.List;

public class ListElementsResponse {
    private List<?> elements;
    private Integer totalPagesCount;

    public ListElementsResponse(List<?> elements, Integer totalPagesCount) {
        this.elements = elements;
        this.totalPagesCount = totalPagesCount;
    }

    public List<?> getElements() {
        return elements;
    }

    public void setElements(List<?> elements) {
        this.elements = elements;
    }

    public Integer getTotalPagesCount() {
        return totalPagesCount;
    }

    public void setTotalPagesCount(Integer totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }
}
