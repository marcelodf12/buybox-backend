package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;

public interface PaqueteEntityRepository extends JpaRepository<PaqueteEntity, Integer>, JpaSpecificationExecutor<PaqueteEntity> {

}