package com.otica.oticaapi.repository.acquisition;

import com.otica.oticaapi.model.people.Provider;
import com.otica.oticaapi.model.acquisition.Acquisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AcquisitionRepository extends JpaRepository<Acquisition, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM acquisition WHERE YEAR(date) = (:year) AND MONTH(date) = (:month)")
    List<Acquisition> searchMonth(int year, int month);
    boolean existsByDateAndProvider(LocalDate date, Provider provider);

}