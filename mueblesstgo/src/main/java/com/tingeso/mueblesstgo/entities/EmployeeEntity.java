package com.tingeso.mueblesstgo.entities;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private String rut;

    @Getter
    @Setter
    @Column(nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(nullable = false)
    private char category;

    @OneToMany(mappedBy = "employee")
    private List<ClockEntity> clocks;
}
