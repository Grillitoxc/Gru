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
    @Autowired
    private JustifierRepository justifierRepository;
    @Autowired
    private ClockRepository clockRepository;
    @Autowired
    private ExtraHoursRepository extraHoursRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeService employeeService;

    public boolean setJustifier(String dateImput, String employeeName) {
        JustifierEntity justifier = new JustifierEntity();
        EmployeeEntity employee = employeeService.getEmployeeByName(employeeName);
        if (verifyDate(dateImput) && ((clockRepository.findByDateAndEmployee(dateImput, employee) == null) ||
                (clockRepository.findByDateAndEmployee(dateImput, employee).getDiscount() == 15))) {
            justifier.setDate(dateImput);
            justifier.setName(employeeName);
            justifierRepository.save(justifier);
            return true;
        }
        return false;
    }

    public boolean setExtraHours(String employeeName, String extraHours) {
        if (employeeRepository.findByName(employeeName) != null && verifyExtraHours(extraHours) &&
                extraHoursRepository.findByName(employeeName) == null) {
            int extraHoursInt = Integer.parseInt(extraHours);
            if (extraHoursInt > 0 && extraHoursInt <= 30) {
                ExtraHoursEntity extraHoursEntity = new ExtraHoursEntity();
                extraHoursEntity.setName(employeeName);
                extraHoursEntity.setHours(extraHoursInt);
                extraHoursRepository.save(extraHoursEntity);
                return true;
            }
        }
        return false;
    }

    public boolean verifyExtraHours(String extraHours) {
        if (extraHours.length() > 0 && extraHours.length() < 3) {
            char[] extraHoursArray = extraHours.toCharArray();
            for (char c : extraHoursArray) {
                if (!Character.isDigit(c))
                    return false;
            }
            return Integer.parseInt(extraHours) > 0 && Integer.parseInt(extraHours) <= 12;
        }
        return false;
    }

    public boolean verifyDate(String dateImput) {
        if (dateImput.length() != 10)
            return false;
        char firstSlash = dateImput.charAt(4);
        char secondSlash = dateImput.charAt(7);
        if (firstSlash != '/' || secondSlash != '/')
            return false;
        char[] date = (dateImput.substring(0, 4) + dateImput.substring(5, 7) + dateImput.substring(8, 10)).toCharArray();
        if (!verifyDigits(date))
            return false;
        return verifyInts(dateImput);
    }

    public boolean verifyDigits(char[] date) {
        for (char c : date) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

    public boolean verifyInts(String date) {
        int yearInt = Integer.parseInt(date.substring(0, 4));
        int monthInt = Integer.parseInt(date.substring(5, 7));
        int dayInt = Integer.parseInt(date.substring(8, 10));
        return yearInt >= 2022 && monthInt >= 1 && monthInt <= 12 && dayInt >= 1 && dayInt <= 31;
    }

    public int calculateDiscount(String hourImput) {
        int hour = Integer.parseInt(hourImput.substring(0, 2));
        int minutes = Integer.parseInt(hourImput.substring(3, 5));
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
