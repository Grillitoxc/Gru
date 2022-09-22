package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.ExtraHoursEntity;

@Repository
public interface ExtraHoursRepository extends JpaRepository<ExtraHoursEntity, Long> {
    ExtraHoursEntity findByName(String name);
}
