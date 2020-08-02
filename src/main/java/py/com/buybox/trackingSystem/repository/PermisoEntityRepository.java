package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.PermisoEntity;

public interface PermisoEntityRepository extends JpaRepository<PermisoEntity, Integer>, JpaSpecificationExecutor<PermisoEntity> {

}