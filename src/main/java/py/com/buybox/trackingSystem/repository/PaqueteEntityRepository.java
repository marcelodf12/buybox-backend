package py.com.buybox.trackingSystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;
import py.com.buybox.trackingSystem.entities.PaqueteEntity;

import java.util.Calendar;
import java.util.List;

public interface PaqueteEntityRepository extends JpaRepository<PaqueteEntity, Integer>, JpaSpecificationExecutor<PaqueteEntity> {

    @Query("SELECT P FROM PaqueteEntity P\n" +
            "JOIN P.cliente bc\n" +
            "JOIN P.sucursalDestino s\n" +
            "WHERE\n" +
            "    UPPER(P.codigoExterno) like UPPER(CONCAT('%', ?1, '%')) AND\n" +
            "    UPPER(P.codigoInterno) like UPPER(CONCAT('%', ?2, '%')) AND\n" +
            "    UPPER(P.numeroTracking) like UPPER(CONCAT('%', ?8, '%')) AND\n" +
            "    UPPER(CONCAT(bc.nombre, ' ' , bc.apellido)) like UPPER(CONCAT('%', ?9, '%')) AND\n" +
            "    UPPER(P.vuelo) like UPPER(CONCAT('%', ?3, '%')) AND\n" +
            "    UPPER(bc.casilla) like UPPER(CONCAT('%', ?4, '%')) AND\n" +
            "    P.sucursalActual.id = coalesce(?5, P.sucursalActual.id) AND\n" +
            "    P.ingreso >= ?6 AND P.ingreso <= ?7\n")
    Page<PaqueteEntity> findPaquete(String codigoExterno,
                                    String codigoInterno,
                                    String vuelo,
                                    String casilla,
                                    Integer idSucursal,
                                    Calendar desde,
                                    Calendar hasta,
                                    String numeroTracking,
                                    String cliente,
                                    PageRequest pageRequest
                                    );
}
