package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.DeliveryEntity;

public interface DeliveryEntityRepository extends JpaRepository<DeliveryEntity, Integer>, JpaSpecificationExecutor<DeliveryEntity> {

}