package py.com.buybox.trackingSystem.commons.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class SortUtil {

    protected static final Log logger = LogFactory.getLog(new SortUtil().getClass());

    public static List<Order> sortingList(String s, String query){
        logger.debug(s);
        String[] columns = s.split(",");
        List<Order> orders = new ArrayList<Order>();
        for(String column:columns){
            Order order = null;
            String nombreDeColumna = queries.get(query).get(column.substring(1));
            if(nombreDeColumna!=null){
                if(column.startsWith("*")){
                    order = new Order(Sort.Direction.ASC, nombreDeColumna);
                }
                if(column.startsWith("-")){
                    order = new Order(Sort.Direction.DESC, nombreDeColumna);
                }
                orders.add(order);
            }
        }
        if(orders.isEmpty()){
            orders.add(new Order(Sort.Direction.ASC, queries.get(query).get("default")));
        }
        logger.debug(orders);
        return orders;
    }

    static final Map<String, Map<String, String>> queries = Map.ofEntries(
            entry("paquetes",
                    Map.ofEntries(
                            entry("default","bc.casilla"),
                            entry("casilla","bc.casilla"),
                            entry("cliente",""),
                            entry("trackPaquete","P.codigoExterno"),
                            entry("trackProveedor","P.codigoInterno"),
                            entry("ingreso","P.ingreso"),
                            entry("destino","s.nombre")
                    )
            )
    );
}
