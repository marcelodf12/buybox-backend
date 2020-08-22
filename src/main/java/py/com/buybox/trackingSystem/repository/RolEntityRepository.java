package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import py.com.buybox.trackingSystem.entities.RolEntity;

import java.util.List;

public interface RolEntityRepository extends JpaRepository<RolEntity, Integer>, JpaSpecificationExecutor<RolEntity> {

    @Query(
            "SELECT r FROM RolEntity r WHERE r.rol IN ?1"
    )
    List<RolEntity> findRolIn(List<String> roles);

}
