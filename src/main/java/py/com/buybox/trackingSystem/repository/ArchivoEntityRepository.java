package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.ArchivoEntity;

public interface ArchivoEntityRepository extends JpaRepository<ArchivoEntity, Integer>, JpaSpecificationExecutor<ArchivoEntity> {

}