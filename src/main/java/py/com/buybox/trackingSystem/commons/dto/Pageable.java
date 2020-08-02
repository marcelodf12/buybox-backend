package py.com.buybox.trackingSystem.commons.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Pageable implements Serializable {

    private Long currentPage;

    private Boolean isFirst;

    private Boolean isLast;

    private Long numberOfPages;

    private Long totalResults;

    private Long perPage;

    public Pageable() {
    }

}
