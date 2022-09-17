package tingeso.mueblesstgo.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "extra_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraHoursEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, name = "name")
    private String name;

    @Getter
    @Setter
    @Column(nullable = false, name = "hours")
    private int hours;
}
