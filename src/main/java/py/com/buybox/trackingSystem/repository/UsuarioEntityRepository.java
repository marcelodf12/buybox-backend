package py.com.buybox.trackingSystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import py.com.buybox.trackingSystem.entities.ClienteEntity;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;
import py.com.buybox.trackingSystem.entities.UsuarioEntity;

import java.time.LocalDate;

public interface UsuarioEntityRepository extends JpaRepository<UsuarioEntity, Integer>, JpaSpecificationExecutor<UsuarioEntity> {

    UsuarioEntity findByLinkDeRecuperacion(String linkDeRecuperacion);

    UsuarioEntity findByCorreo(String correo);

    @Query(" SELECT DISTINCT U FROM UsuarioEntity U\n" +
            " INNER JOIN U.rolList R\n" +
            " WHERE\n" +
            "    R.rol != 'CLIENTE' AND U.activo >= 0\n")
    Page<UsuarioEntity> findAllEmployees(PageRequest pageRequest);
}
