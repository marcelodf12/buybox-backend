package py.com.buybox.trackingSystem.commons.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;

@Data
public class Paginable implements Serializable {

    private Integer currentPage;

    private Boolean isFirst;

    private Boolean isLast;

    private Integer numberOfPages;

    private Long totalResults;

    private Integer perPage;

    public Paginable() {
    }

    public Paginable(Page page){
        this.currentPage = page.getPageable().getPageNumber();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
        this.numberOfPages = page.getTotalPages();
        this.totalResults = page.getTotalElements();
        this.perPage = page.getSize();
    }

}
