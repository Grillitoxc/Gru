package tingeso.mueblesstgo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.entities.ExtraHoursEntity;
import tingeso.mueblesstgo.entities.JustifierEntity;
import tingeso.mueblesstgo.repositories.ClockRepository;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.repositories.ExtraHoursRepository;
import tingeso.mueblesstgo.repositories.JustifierRepository;

@Service
public class ClockService {
    @Autowired private JustifierRepository justifierRepository;
    @Autowired private ClockRepository clockRepository;
    @Autowired private ExtraHoursRepository extraHoursRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EmployeeService employeeService;

    public void setJustifier(String dateImput, String employeeName) {
        JustifierEntity justifier = new JustifierEntity();
        EmployeeEntity employee = employeeService.getEmployeeByName(employeeName);
        if (verifyDate(dateImput)) {
            if ((clockRepository.findByDateAndEmployee(dateImput, employee) == null) ||
                    (clockRepository.findByDateAndEmployee(dateImput, employee).getDiscount() >= 15)) {
                justifier.setDate(dateImput);
                justifier.setName(employeeName);
                justifierRepository.save(justifier);
            }
        }
    }

    public void setExtraHours(String employeeName, String extraHours) {
        if (employeeRepository.findByName(employeeName) != null && verifyExtraHours(extraHours) &&
                extraHoursRepository.findByName(employeeName) == null) {
            int extraHoursInt = Integer.parseInt(extraHours);
            if (extraHoursInt > 0 && extraHoursInt <= 30) {
                ExtraHoursEntity extraHoursEntity = new ExtraHoursEntity();
                extraHoursEntity.setName(employeeName);
                extraHoursEntity.setHours(extraHoursInt);
                extraHoursRepository.save(extraHoursEntity);
            }
        }
    }

    public boolean verifyExtraHours(String extraHours) {
        if (extraHours.length() > 0 && extraHours.length() < 3) {
            char[] extraHoursArray = extraHours.toCharArray();
            for (char c : extraHoursArray) {
                if (!Character.isDigit(c))
                    return false;
            }
            return true;
        }
        return false;
    }


    public boolean verifyDate(String dateImput){
        // Verify Length of date
        if (dateImput.length() != 10)
            return false;

        // Verify Slashes
        char firstSlash = dateImput.charAt(4);
        char secondSlash = dateImput.charAt(7);
        if (firstSlash != '/' || secondSlash != '/')
            return false;

        // Verify valid numbers
        char[] year = dateImput.substring(0,4).toCharArray();
        char[] month = dateImput.substring(5,7).toCharArray();
        char[] day = dateImput.substring(8,10).toCharArray();
        for (char c : year) {
            if (!Character.isDigit(c))
                return false;
        }
        for (char c : month) {
            if (!Character.isDigit(c))
                return false;
        }
        for (char c : day) {
            if (!Character.isDigit(c))
                return false;
        }
        
        // Verify valid date
        int yearInt = Integer.parseInt(dateImput.substring(0,4));
        int monthInt = Integer.parseInt(dateImput.substring(5,7));
        int dayInt = Integer.parseInt(dateImput.substring(8,10));
        if (monthInt < 1 || monthInt > 12 || dayInt < 1 || dayInt > 31 || yearInt != 2022)
            return false;

        return true;
    }

    public int calculateDiscount(String hourImput){
        int hour = Integer.parseInt(hourImput.substring(0,2));
        int minutes = Integer.parseInt(hourImput.substring(3,5));
        if (hour == 8 && minutes > 10 && minutes <= 25)
            return 1;
        else if (hour == 8 && minutes > 25 && minutes <= 45)
            return 3;
        else if ((hour == 8 && minutes > 45 && minutes <= 59) || (hour == 9 && minutes < 10))
            return 6;
        else if (hour > 9)
            return -1;
        else
            return 0;
    }
}
