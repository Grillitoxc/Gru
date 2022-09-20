package tingeso.mueblesstgo;


import tingeso.mueblesstgo.entities.EmployeeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tingeso.mueblesstgo.entities.ExtraHoursEntity;
import tingeso.mueblesstgo.entities.JustifierEntity;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.repositories.ExtraHoursRepository;
import tingeso.mueblesstgo.repositories.JustifierRepository;
import tingeso.mueblesstgo.services.ClockService;
import tingeso.mueblesstgo.services.EmployeeService;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

@SpringBootTest
class EmployeeeServiceTest {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    String name = "Test";
    String rut = "12345678-9";
    char category = 'A';
    String dateOfAdmission = "2021-01-01";
    int yearsOfService = 1;

    @Test
    void testGetAllEmployees() {
        // given
        EmployeeEntity employeeTemp = new EmployeeEntity();
        employeeTemp.setName(name);
        employeeTemp.setRut(rut);
        employeeTemp.setCategory(category);
        employeeTemp.setDateOfAdmission(dateOfAdmission);
        employeeTemp.setYearsOfService(yearsOfService);
        employeeRepository.save(employeeTemp);
        // when
        List<EmployeeEntity> employees = employeeService.getAllEmployees();
        // then
        assertThat(employees.size()).isGreaterThan(0);
        // clean
        employeeRepository.delete(employeeTemp);
    }

    @Test
    void testGetEmployeeByName() {
        // given
        EmployeeEntity employeeTemp = new EmployeeEntity();
        employeeTemp.setName(name);
        employeeTemp.setRut(rut);
        employeeTemp.setCategory(category);
        employeeTemp.setDateOfAdmission(dateOfAdmission);
        employeeTemp.setYearsOfService(yearsOfService);
        employeeRepository.save(employeeTemp);
        // when
        EmployeeEntity employee = employeeService.getEmployeeByName("Test");
        // then
        assertThat(employee.getName()).isEqualTo("Test");
        // clean
        employeeRepository.delete(employee);
        assertThat(employeeRepository.findByName("Test")).isNull();
    }
}

@SpringBootTest
class ClockServiceTest {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ClockService clockService;
    @Autowired
    JustifierRepository justifierRepository;
    @Autowired
    ExtraHoursRepository extraHoursRepository;

    String name = "Test";
    String rut = "12345678-9";
    char category = 'A';
    String dateOfAdmission = "2021/01/01";
    String date = "2022/08/01";
    int yearsOfService = 1;
    String extraHours = "10";
    String hourImput1 = "08:00";
    String hourImput2 = "09:00";
    String hourImput3 = "10:00";
    String hourImput4 = "08:26";
    char[] dateDigts = (date.substring(0,4) + date.substring(5,7) + date.substring(8,10)).toCharArray();

    @Test
    void testSetJustifier() {
        // given
        EmployeeEntity employeeTemp = new EmployeeEntity();
        employeeTemp.setName(name);
        employeeTemp.setRut(rut);
        employeeTemp.setCategory(category);
        employeeTemp.setDateOfAdmission(dateOfAdmission);
        employeeTemp.setYearsOfService(yearsOfService);
        employeeRepository.save(employeeTemp);
        // when
        boolean result = clockService.setJustifier(date, name);
        // then
        assertThat(result).isTrue();
        assertThat(justifierRepository.findByDateAndName(date, name)).isNotNull();
        // clean
        JustifierEntity justifier = justifierRepository.findByDateAndName(date, name);
        justifierRepository.delete(justifier);
        EmployeeEntity employee = employeeService.getEmployeeByName(name);
        employeeRepository.delete(employee);
    }

    @Test
    void testSetExtraHours() {
        // given
        EmployeeEntity employeeTemp = new EmployeeEntity();
        employeeTemp.setName(name);
        employeeTemp.setRut(rut);
        employeeTemp.setCategory(category);
        employeeTemp.setDateOfAdmission(dateOfAdmission);
        employeeTemp.setYearsOfService(yearsOfService);
        employeeRepository.save(employeeTemp);
        // when
        boolean result = clockService.setExtraHours(name, extraHours);
        // then
        assertThat(result).isTrue();
        assertThat(extraHoursRepository.findByName(name).getHours()).isEqualTo(10);
        // clean
        ExtraHoursEntity extraHoursEntity = extraHoursRepository.findByName(name);
        extraHoursRepository.delete(extraHoursEntity);
        EmployeeEntity employee = employeeService.getEmployeeByName(name);
        employeeRepository.delete(employee);
    }

    @Test
    void testVerifyExtraHours() {
        // when
        boolean result = clockService.verifyExtraHours(extraHours);
        // then
        assertThat(result).isTrue();
        assertThat(clockService.verifyExtraHours("0")).isFalse();
        assertThat(clockService.verifyExtraHours("a")).isFalse();
        assertThat(clockService.verifyExtraHours("1.5")).isFalse();
        assertThat(clockService.verifyExtraHours("13")).isFalse();
    }

    @Test
    void testVerifyDate() {
        // when
        boolean result = clockService.verifyDate(date);
        // then
        assertThat(result).isEqualTo(true);
        assertThat(clockService.verifyDate("2021-01/01")).isFalse();
        assertThat(clockService.verifyDate("2021-01-01-01")).isFalse();
        assertThat(clockService.verifyDate("2022/09/18")).isTrue();
    }

    @Test
    void testCalculateDiscount() {
        // when
        int result1 = clockService.calculateDiscount(hourImput1);
        int result2 = clockService.calculateDiscount(hourImput2);
        int result3 = clockService.calculateDiscount(hourImput3);
        int result4 = clockService.calculateDiscount(hourImput4);
        // then
        assertThat(result1).isZero();
        assertThat(result2).isEqualTo(6);
        assertThat(result3).isEqualTo(-1);
        assertThat(result4).isEqualTo(3);
    }

    @Test
    void testVerifyInts() {
        // given
        String date2 = "2021/01/01";
        // when
        boolean result1 = clockService.verifyInts(date);
        boolean result2 = clockService.verifyInts(date2);
        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    void testVerifyDigits() {
        // given
        char[] array = {'A', '2', '3', '4', 'C', '6', '7', '8', '9', '0'};
        // when
        boolean result1 = clockService.verifyDigits(dateDigts);
        boolean result2 = clockService.verifyDigits(array);
        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}

