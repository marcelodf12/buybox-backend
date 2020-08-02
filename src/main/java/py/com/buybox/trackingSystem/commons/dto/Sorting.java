package py.com.buybox.trackingSystem.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sorting implements Comparable{
    private String column;
    private Integer direction;
    private Integer order;

    @Override
    public int compareTo(Object o) {
        Sorting s = (Sorting) o;
        return this.order.compareTo(s.order);
    }
}
