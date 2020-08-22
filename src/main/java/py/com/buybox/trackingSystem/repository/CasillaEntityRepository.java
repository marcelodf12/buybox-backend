package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.CasillaEntity;

public interface CasillaEntityRepository extends JpaRepository<CasillaEntity, Integer>, JpaSpecificationExecutor<CasillaEntity> {
}
