package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.CiudadEntity;

public interface CiudadEntityRepository extends JpaRepository<CiudadEntity, Integer>, JpaSpecificationExecutor<CiudadEntity> {

}