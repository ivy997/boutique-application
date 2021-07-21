package boutique.models;

import java.util.List;

public class ListElementsResponse {
    private List<?> elements;
    private Integer totalElementsCount;

    public ListElementsResponse(List<?> elements, Integer totalElementsCount) {
        this.elements = elements;
        this.totalElementsCount = totalElementsCount;
    }

    public List<?> getElements() {
        return elements;
    }

    public void setElements(List<?> elements) {
        this.elements = elements;
    }

    public Integer getTotalElementsCount() {
        return totalElementsCount;
    }

    public void setTotalElementsCount(Integer totalElementsCount) {
        this.totalElementsCount = totalElementsCount;
    }
}
