package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.SucursalEntity;

public interface SucursalEntityRepository extends JpaRepository<SucursalEntity, Integer>, JpaSpecificationExecutor<SucursalEntity> {

}