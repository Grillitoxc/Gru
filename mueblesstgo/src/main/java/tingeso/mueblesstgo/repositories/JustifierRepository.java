package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.entities.JustifierEntity;

public interface JustifierRepository extends JpaRepository<JustifierEntity, Long> {
    JustifierEntity findByDateAndName(String date, String name);
}
