package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.RastreoEntity;

public interface RastreoEntityRepository extends JpaRepository<RastreoEntity, Integer>, JpaSpecificationExecutor<RastreoEntity> {

}