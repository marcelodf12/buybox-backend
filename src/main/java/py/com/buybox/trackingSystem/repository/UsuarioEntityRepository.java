package py.com.buybox.trackingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;

public interface UsuarioEntityRepository extends JpaRepository<UsuarioEntity, Integer>, JpaSpecificationExecutor<UsuarioEntity> {


}
