package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.RolEntity;

public interface RolEntityRepository extends JpaRepository<RolEntity, Integer>, JpaSpecificationExecutor<RolEntity> {

}