package py.com.buybox.trackingSystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import py.com.buybox.trackingSystem.entities.ClienteEntity;

public interface ClienteEntityRepository extends JpaRepository<ClienteEntity, Integer>, JpaSpecificationExecutor<ClienteEntity> {

    ClienteEntity findByCorreo(String correo);

    ClienteEntity findByCasilla(String casilla);

    @Query(
            "SELECT C FROM ClienteEntity C\n" +
                    "WHERE\n" +
                    "  UPPER(C.casilla) like UPPER(CONCAT('%', ?1, '%'))AND\n" +
                    "  UPPER(CONCAT(C.nombre, ' ' , C.apellido)) like UPPER(CONCAT('%', ?2, '%'))AND\n" +
                    "  UPPER(C.ruc) like UPPER(CONCAT('%', ?3, '%'))AND\n" +
                    "  UPPER(C.correo) like UPPER(CONCAT('%', ?4, '%'))"
    )
    Page<ClienteEntity> findCliente(
            String casilla,
            String cliente,
            String ruc,
            String correo,
            PageRequest pageRequest
    );

}
