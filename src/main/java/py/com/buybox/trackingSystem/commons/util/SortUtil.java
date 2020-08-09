package py.com.buybox.trackingSystem.commons.util;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public class SortUtil {

    public static List<Order> sortingList(String s){
        String[] columns = s.split(",");
        List<Order> orders = new ArrayList<Order>();
        for(String column:columns){
            Order order = null;
            if(column.startsWith("+")){
                order = new Order(Sort.Direction.ASC, column.substring(1));
            }
            if(column.startsWith("-")){
                order = new Order(Sort.Direction.DESC, column.substring(1));
            }
            orders.add(order);
        }
        return orders;
    }
}
