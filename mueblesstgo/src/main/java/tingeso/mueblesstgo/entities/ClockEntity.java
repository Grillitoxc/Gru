package tingeso.mueblesstgo.entities;

import lombok.*;
import javax.persistence.*;
import tingeso.mueblesstgo.entities.EmployeeEntity;

@Entity
@Table(name = "clock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String date;

    @Getter
    @Setter
    @Column(nullable = false)
    private String checkInTime;

    @Getter
    @Setter
    @Column(nullable = false)
    private int discount;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_employee")
    private EmployeeEntity employee;
}