package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.DepartamentoEntity;

public interface DepartamentoEntityRepository extends JpaRepository<DepartamentoEntity, Integer>, JpaSpecificationExecutor<DepartamentoEntity> {

}