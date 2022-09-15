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

import java.text.DecimalFormat;
import java.util.ArrayList;

@Service
public class SalaryCalculatorService {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";

    @Autowired
    private ClockRepository clockRepository;
    @Autowired
    private ExtraHoursRepository extraHoursRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JustifierRepository justifierRepository;

    public void calculateSalary() {
        ArrayList<EmployeeEntity> employees = employeeRepository.findAll();
        for (EmployeeEntity employee : employees) {
            // Sacar categoría base
            int multiplier = 1;
            int extraHoursMoney = 0;
            char category = employee.getCategory();

            // Ingresar salario base
            if (category == 'A')
                employee.setFixedSalary(1700000);
            else if (category == 'B')
                employee.setFixedSalary(1200000);
            else if (category == 'C')
                employee.setFixedSalary(800000);

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

            /* Bonificaciones por años de servicio */
            int years = employee.getYearsOfService();
            int yearsBonus = 0;
            if (years >= 5 && years < 10)
                yearsBonus = 5;
            else if (years >= 10 && years < 15)
                yearsBonus = 8;
            else if (years >= 15 && years < 20)
                yearsBonus = 11;
            else if (years >= 20 && years < 25)
                yearsBonus = 14;
            else if (years >= 25 && years < 30)
                yearsBonus = 17;
            // en yearsBonus se guarda el porcentaje de bonificación por años de servicio

            /* Justificativos */
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

            /* Aplicación de descuentos */
            int totalDiscount = 0;
            ArrayList<Integer> discounts = clockRepository.findDiscountByEmployee(employee);
            for (Integer discount : discounts) {
                totalDiscount += discount;
            }

            /* Cotizaciones */
            double forecastQuote = 0.1;
            double healthQuote = 0.08;

            /* Sueldo base */
            double baseSalary = employee.getFixedSalary();
            /* Porcentaje de bonificación por años de servicio */
            double yearsBonusPercentage = formatearDecimales((yearsBonus / 100.0), 2);
            double yearBonusMoney = baseSalary * yearsBonusPercentage;
            employee.setYearsOfServiceBonus(yearBonusMoney);
            /* Descuentos por faltas sin justificar y atrasos */
            double totalDiscountsPercentage = formatearDecimales(((totalDiscount + discountsByMissing) / 100.0), 2);
            double totalDiscountsMoney = baseSalary * totalDiscountsPercentage;
            employee.setDiscounts(totalDiscountsMoney);
            /* Horas extras */
            employee.setExtraHoursBonus(extraHoursMoney);

            /* Sueldo bruto */
            double grossSalary = baseSalary + yearBonusMoney + extraHoursMoney;

            /* Cotizaciones */
            double forecastQuoteMoney = baseSalary * forecastQuote;
            double healthQuoteMoney = baseSalary * healthQuote;
            employee.setForecastQuote(forecastQuoteMoney);
            employee.setHealthQuote(healthQuoteMoney);
            double totalDiscountsMoneyWithQuotes = totalDiscountsMoney + forecastQuoteMoney + healthQuoteMoney;

            /* Sueldo neto (final) */
            double netSalary = grossSalary - totalDiscountsMoneyWithQuotes;
            employee.setFinalSalary(netSalary);

            employeeRepository.save(employee);
            // Hacer un print de los datos del empleado bonito
            prints(employee);
        }
    }

    private static void prints(EmployeeEntity employee) {
        System.out.println();
        System.out.println(ANSI_RED_BACKGROUND + "Planilla de sueldo de " + employee.getName() + ANSI_RESET);
        System.out.println("Rut: " + employee.getRut());
        System.out.println("Nombre empleado: " + employee.getName());
        System.out.println("Categoría: " + employee.getCategory());
        System.out.println("Años de servicio: " + employee.getYearsOfService());
        System.out.println("Sueldo base: $" + Math.round(employee.getFixedSalary()));
        System.out.println("Monto bonificación por años servicio: $" + Math.round(employee.getYearsOfServiceBonus()));
        System.out.println("Monto bonificación por horas extras: $" + Math.round(employee.getExtraHoursBonus()));
        System.out.println("Monto descuentos: $" + Math.round(employee.getDiscounts()));
        System.out.println("Sueldo bruto: $" + Math.round(employee.getGrossSalary()));
        System.out.println("Monto cotización previsional: $" + Math.round(employee.getForecastQuote()));
        System.out.println("Monto cotización salud: $" + Math.round(employee.getHealthQuote()));
        System.out.println("Sueldo neto (final): $" + Math.round(employee.getFinalSalary()));
    }

    private static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }
}
