package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.PrecioEntity;

public interface PrecioEntityRepository extends JpaRepository<PrecioEntity, Integer>, JpaSpecificationExecutor<PrecioEntity> {

}