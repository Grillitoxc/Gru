package tingeso.mueblesstgo.repositories;

import tingeso.mueblesstgo.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    EmployeeEntity findByName(String name);
    EmployeeEntity findByRut(String rut);
}