package tingeso.mueblesstgo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso.mueblesstgo.entities.JustifierEntity;

@Repository
public interface JustifierRepository extends JpaRepository<JustifierEntity, Long> {
    JustifierEntity findByDateAndName(String date, String name);
}
