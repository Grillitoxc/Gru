package tingeso.mueblesstgo.services;

import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.ClockEntity;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.repositories.ClockRepository;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.repositories.ExtraHoursRepository;
import tingeso.mueblesstgo.repositories.JustifierRepository;

import java.util.ArrayList;

@Service
public class SalaryCalculatorService {
    @Autowired private ClockRepository clockRepository;
    @Autowired private ExtraHoursRepository extraHoursRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private JustifierRepository justifierRepository;

    public void calculateSalary() {
        ArrayList<EmployeeEntity> employees = employeeRepository.findAll();
        for (EmployeeEntity employee : employees) {
            // Sacar categoría base
            int multiplier = 1;
            int extraHoursMoney = 0;
            char category = employee.getCategory();
            // Revisar si el empleado tiene horas extras
            if (extraHoursRepository.findByName(employee.getName()) != null) {
                if (category == 'A')
                    multiplier = 25000;
                else if (category == 'B')
                    multiplier = 20000;
                else if (category == 'C')
                    multiplier = 10000;
                extraHoursMoney = extraHoursRepository.findByName(employee.getName()).getHours() * multiplier;
            }
            // extraHoursMoney tiene el valor de las horas extras pagadas


            // aqui van las bonificaciones por años de servicio
            /*
            -
            -
            -
             */

            // Sección donde ver los justificativos
            int discountsByMissing = 0;
            for (int i = 17; i <= 22; i++) {
                String date = "2022/08/" + i;
                // Sobreescribir descuentos de atraso con justificativo
                if (justifierRepository.findByDateAndName(date, employee.getName()) != null) {
                    ClockEntity clock = clockRepository.findByDateAndEmployee(date, employee);
                    clock.setDiscount(0);
                    clockRepository.save(clock);
                }
                // Días donde se faltó y no hay justificativo
                if ((justifierRepository.findByDateAndName(date, employee.getName()) == null) &&
                        (clockRepository.findByDateAndEmployee(date, employee) == null)) {
                    discountsByMissing += 15;
                }
            }
            // En discountsByMissing se tiene el valor de los descuentos por faltas sin justificar y además se
            // actualizan los descuentos de los días con justificativo en la base de datos para la siguiente sección.

            // Sección donde se consideran los descuentos de cada uno
            int totalDiscount = 0;
            ArrayList<Integer> discounts = clockRepository.findDiscountByEmployee(employee);
            for (Integer discount : discounts) {
                totalDiscount += discount;
            }
            // Sección donde se debería calcular el sueldo total
            System.out.println("Descuento de " + employee.getName() + " = " + totalDiscount);
        }
    }
}
