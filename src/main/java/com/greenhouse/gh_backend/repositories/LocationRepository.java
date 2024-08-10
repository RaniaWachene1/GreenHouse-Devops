package com.greenhouse.gh_backend.repositories;

import com.greenhouse.gh_backend.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUserIdUser(Long idUser);
    List<Location> findByUserIdUserAndUsesNaturalGas(Long idUser, boolean usesNaturalGas);
    @Query("SELECT SUM(l.co2Emissions) FROM Location l WHERE l.user.idUser = :userId")
    Double findTotalCo2EmissionsByUserId(@Param("userId") long userId);
}
