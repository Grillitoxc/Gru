package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.dtos.ExtraInformationEmployee;
import tingeso.mueblesstgo.entities.ClockEntity;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.repositories.ClockRepository;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.repositories.ExtraHoursRepository;
import tingeso.mueblesstgo.repositories.JustifierRepository;
import lombok.Generated;

import java.util.ArrayList;

@Service
public class SalaryCalculatorService {
    @Autowired
    private ClockRepository clockRepository;
    @Autowired
    private ExtraHoursRepository extraHoursRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private JustifierRepository justifierRepository;

    public int checkExtraHours(EmployeeEntity employee) {
        char category = employee.getCategory();
        int multiplier = 1;
        if (extraHoursRepository.findByName(employee.getName()) != null) {
            if (category == 'A')
                multiplier = 25000;
            else if (category == 'B')
                multiplier = 20000;
            else if (category == 'C')
                multiplier = 10000;
            return extraHoursRepository.findByName(employee.getName()).getHours() * multiplier;
        }
        return 0;
    }

    private int getAmountExtraHours(EmployeeEntity employee){
        if (extraHoursRepository.findByName(employee.getName()) != null) {
            return extraHoursRepository.findByName(employee.getName()).getHours();
        }
        return 0;
    }

    public int calculateYearsOfServiceBonus(EmployeeEntity employee) {
        int years = employee.getYearsOfService();
        int yearsBonus;
        if (years >= 5 && years < 10)
            yearsBonus = 5;
        else if (years >= 10 && years < 15)
            yearsBonus = 8;
        else if (years >= 15 && years < 20)
            yearsBonus = 11;
        else if (years >= 20 && years < 25)
            yearsBonus = 14;
        else if (years >= 25)
            yearsBonus = 17;
        else
            yearsBonus = 0;
        return yearsBonus;
    }

    public void setFixedSalary(EmployeeEntity employee) {
        char category = employee.getCategory();
        if (category == 'A')
            employee.setFixedSalary(1700000);
        else if (category == 'B')
            employee.setFixedSalary(1200000);
        else if (category == 'C')
            employee.setFixedSalary(800000);
    }

    @Generated
    public int checkAndSetJustifiers(EmployeeEntity employee) {
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
        return discountsByMissing;
    }

    @Generated
    public int getAmountJustifiers(EmployeeEntity employee){
        int amountJustifiers = 0;
        for (int i = 17; i <= 22; i++) {
            String date = "2022/08/" + i;
            if (justifierRepository.findByDateAndName(date, employee.getName()) != null) {
                amountJustifiers += 1;
            }
        }
        return amountJustifiers;
    }

    @Generated
    public int getAmountArrears(EmployeeEntity employee){
        int amountArrears = 0;
        for (int i = 17; i <= 22; i++) {
            String date = "2022/08/" + i;
            if (clockRepository.findByDateAndEmployee(date, employee) == null) {
                amountArrears += 1;
            }
        }
        return amountArrears;
    }

    @Generated
    public int addDiscounts(EmployeeEntity employee) {
        int totalDiscount = 0;
        ArrayList<Integer> discounts = clockRepository.findDiscountByEmployee(employee);
        for (Integer discount : discounts) {
            totalDiscount += discount;
        }
        return totalDiscount;
    }

    @Generated
    public void calculateSalary() {
        ArrayList<EmployeeEntity> employees = employeeRepository.findAll();
        for (EmployeeEntity employee : employees) {
            /* Definición de variables */
            int yearsBonus;
            int extraHoursMoney;
            int discountsByMissing;
            int totalDiscount;
            setFixedSalary(employee);

            double forecastQuote = 0.1;
            double healthQuote = 0.08;
            double baseSalary = employee.getFixedSalary();

            extraHoursMoney = checkExtraHours(employee);
            yearsBonus = calculateYearsOfServiceBonus(employee);
            discountsByMissing = checkAndSetJustifiers(employee);
            totalDiscount = addDiscounts(employee);

            /* Porcentaje de bonificación por años de servicio */
            double yearsBonusPercentage = formatterDecimals((yearsBonus / 100.0));
            double yearBonusMoney = baseSalary * yearsBonusPercentage;
            /* Descuentos por faltas sin justificar y atrasos */
            double totalDiscountsPercentage = formatterDecimals(((totalDiscount + discountsByMissing) / 100.0));
            double totalDiscountsMoney = baseSalary * totalDiscountsPercentage;
            /* Sueldo bruto */
            double grossSalary = baseSalary + yearBonusMoney + extraHoursMoney;
            /* Cotizaciones */
            double forecastQuoteMoney = baseSalary * forecastQuote;
            double healthQuoteMoney = baseSalary * healthQuote;
            double totalDiscountsMoneyWithQuotes = totalDiscountsMoney + forecastQuoteMoney + healthQuoteMoney;
            /* Sueldo neto (final) */
            double netSalary = grossSalary - totalDiscountsMoneyWithQuotes;

            setAllAndSave(yearBonusMoney, totalDiscountsMoney, extraHoursMoney, forecastQuoteMoney, healthQuoteMoney,
                    netSalary, employee);
        }
    }

    @Generated
    public void setAllAndSave(double yearBonusMoney, double totalDiscountsMoney, int extraHoursMoney,
                               double forecastQuoteMoney, double healthQuoteMoney, double netSalary,
                               EmployeeEntity employee) {
        employee.setYearsOfServiceBonus(yearBonusMoney);
        employee.setDiscounts(totalDiscountsMoney);
        employee.setExtraHoursBonus(extraHoursMoney);
        employee.setForecastQuote(forecastQuoteMoney);
        employee.setHealthQuote(healthQuoteMoney);
        employee.setFinalSalary(netSalary);
        employeeRepository.save(employee);
    }

    public Double formatterDecimals(Double numero) {
        return Math.round(numero * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public ArrayList<ExtraInformationEmployee> getExtraInformation(){
        ArrayList<ExtraInformationEmployee> extraInformation = new ArrayList<>();
        ArrayList<EmployeeEntity> employees = employeeRepository.findAll();

        for (EmployeeEntity employee : employees) {
            ExtraInformationEmployee e = new ExtraInformationEmployee();

            e.setName(employee.getName());
            e.setRut(employee.getRut());
            e.setAmountExtraHours(getAmountExtraHours(employee));
            e.setAmountJustifiedDays(getAmountJustifiers(employee));
            e.setAmountArrears(getAmountArrears(employee));
            extraInformation.add(e);
        }

        return extraInformation;
    }
}
