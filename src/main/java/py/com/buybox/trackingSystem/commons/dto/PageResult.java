package py.com.buybox.trackingSystem.commons.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PageResult<R> {

    private ArrayList<R> result;

    private Pageable pageable;

    public PageResult(ArrayList<R> r, Long currentPage, Long perPage, Long count){
        pageable = new Pageable();
        pageable.setCurrentPage(currentPage);
        pageable.setNumberOfPages(count / perPage + ((count % perPage == 0) ? 0 : 1));
        pageable.setIsFirst(currentPage==0);
        pageable.setIsLast((pageable.getNumberOfPages()-1)==currentPage);
        pageable.setPerPage(perPage);
        pageable.setTotalResults(count);
        result = r;
    }

}
