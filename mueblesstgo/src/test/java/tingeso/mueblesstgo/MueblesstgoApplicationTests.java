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
        assertThat(result).isEqualTo(true);
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
        assertThat(result).isEqualTo(true);
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
        assertThat(result).isEqualTo(true);
        assertThat(clockService.verifyExtraHours("0")).isEqualTo(false);
        assertThat(clockService.verifyExtraHours("a")).isEqualTo(false);
        assertThat(clockService.verifyExtraHours("1.5")).isEqualTo(false);
        assertThat(clockService.verifyExtraHours("13")).isEqualTo(false);
    }

    @Test
    void testVerifyDate() {
        // when
        boolean result = clockService.verifyDate(date);
        // then
        assertThat(result).isEqualTo(true);
        assertThat(clockService.verifyDate("2021-01/01")).isEqualTo(false);
        assertThat(clockService.verifyDate("2021-01-01-01")).isEqualTo(false);
        assertThat(clockService.verifyDate("2022/09/18")).isEqualTo(true);
    }

    @Test
    void testCalculateDiscount() {
        // when
        int result1 = clockService.calculateDiscount(hourImput1);
        int result2 = clockService.calculateDiscount(hourImput2);
        int result3 = clockService.calculateDiscount(hourImput3);
        int result4 = clockService.calculateDiscount(hourImput4);
        // then
        assertThat(result1).isEqualTo(0);
        assertThat(result2).isEqualTo(6);
        assertThat(result3).isEqualTo(-1);
        assertThat(result4).isEqualTo(3);
    }
}

