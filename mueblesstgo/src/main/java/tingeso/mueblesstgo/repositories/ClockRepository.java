package tingeso.mueblesstgo.repositories;

import tingeso.mueblesstgo.entities.ClockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.EmployeeEntity;

@Repository
public interface ClockRepository extends JpaRepository<ClockEntity, Long> {
    ClockEntity findByDateAndEmployee(String date, EmployeeEntity employee);
}
