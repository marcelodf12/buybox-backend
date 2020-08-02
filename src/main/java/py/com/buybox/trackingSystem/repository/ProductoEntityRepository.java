package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.ProductoEntity;

public interface ProductoEntityRepository extends JpaRepository<ProductoEntity, Integer>, JpaSpecificationExecutor<ProductoEntity> {

}