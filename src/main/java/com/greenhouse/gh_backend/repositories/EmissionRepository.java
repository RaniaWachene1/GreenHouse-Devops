package com.greenhouse.gh_backend.repositories;

import com.greenhouse.gh_backend.entities.Emission;
import com.greenhouse.gh_backend.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmissionRepository  extends JpaRepository<Emission, Long> {
    List<Emission> findByLocation(Location location);
    @Query("SELECT SUM(e.co2Emissions) FROM Emission e WHERE e.location.user.idUser = :userId AND e.scope = com.greenhouse.gh_backend.entities.EmissionScope.SCOPE_1")
    Double findTotalCo2EmissionsByUserIdAndScope1(@Param("userId") long userId);

    @Query("SELECT SUM(e.co2Emissions) FROM Emission e WHERE e.location.user.idUser = :userId AND e.scope = com.greenhouse.gh_backend.entities.EmissionScope.SCOPE_2")
    Double findTotalCo2EmissionsByUserIdAndScope2(@Param("userId") long userId);

    @Query("SELECT SUM(e.co2Emissions) FROM Emission e WHERE e.equipment.user.idUser = :userId AND e.scope = com.greenhouse.gh_backend.entities.EmissionScope.SCOPE_3 ")
    Double findTotalCo2EmissionsByUserIdAndScope3(@Param("userId") long userId);

}
