package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.Query;
import tingeso.mueblesstgo.entities.ClockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.EmployeeEntity;

import java.util.ArrayList;

@Repository
public interface ClockRepository extends JpaRepository<ClockEntity, Long> {
    ClockEntity findByDateAndEmployee(String date, EmployeeEntity employee);

    @Query(value = "FROM ClockEntity c WHERE c.employee = ?1")
    ArrayList<ClockEntity> findAllByEmployeeContaining(EmployeeEntity employee);

    @Query(value = "SELECT c.discount FROM ClockEntity c WHERE c.employee = ?1")
    ArrayList<Integer> findDiscountByEmployee(EmployeeEntity employee);

}
