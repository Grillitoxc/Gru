package tingeso.mueblesstgo.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "justifier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JustifierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(unique = true, nullable = false, name = "id")
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, name = "date")
    private String date;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;
}
