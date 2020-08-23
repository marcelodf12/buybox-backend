package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import py.com.buybox.trackingSystem.entities.PermisoEntity;
import py.com.buybox.trackingSystem.entities.RolEntity;

import java.util.List;

public interface PermisoEntityRepository extends JpaRepository<PermisoEntity, Integer>, JpaSpecificationExecutor<PermisoEntity> {

    @Query(
            "SELECT p.permiso FROM PermisoEntity p " +
            "INNER JOIN p.rolList r " +
            "INNER JOIN r.usuarioList u " +
            "WHERE u.idUsuario IN :idUsuario"
    )
    List<String> findByUsuario(Integer idUsuario);
}
