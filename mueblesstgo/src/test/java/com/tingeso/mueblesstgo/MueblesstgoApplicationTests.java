package com.tingeso.mueblesstgo;

import com.tingeso.mueblesstgo.repositories.EmployeeRepository;
import com.tingeso.mueblesstgo.entities.EmployeeEntity;
import com.tingeso.mueblesstgo.repositories.ClockRepository;
import com.tingeso.mueblesstgo.entities.ClockEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.text.html.parser.Entity;

@SpringBootTest
class EmployeeeRepositoryTest {
	@Autowired private EmployeeRepository employeeRepository;
	@Test
	void testAddNew() {
		EmployeeEntity employee1 = new EmployeeEntity();
		employee1.setRut("21.024.191-4");
		employee1.setName("Grillitoxc");
		employee1.setCategory('A');
		EmployeeEntity savedEmployee = employeeRepository.save(employee1);

		Assertions.assertThat(savedEmployee).isNotNull();
		Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
		System.out.println("Empleado: " + employee1.toString() + " ingresado con exito");
	}

	@Test
	void testDeleteAllEmployees() {
		employeeRepository.deleteAll();
		Assertions.assertThat(employeeRepository.findAll()).isEmpty();
		System.out.println("Todos los empleados han sido eliminados");
	}

	@Test
	void testDeleteEmployeeById() {
		long id = 1;
		if (employeeRepository.findById(id).isPresent()) {
			System.out.println("Empleado eliminado con id: " + employeeRepository.findById(id).get().getId() + ".");
			employeeRepository.deleteById(id);
		} else {
			System.out.println("No existe el empleado con id: " + id);
		}
		Assertions.assertThat(employeeRepository.findById(id)).isEmpty();
	}

	@Test
	void testFindEmployeeByRut() {
		String rut = "21.014.566-4";
		EmployeeEntity employee = employeeRepository.findByRut(rut);
		if (employee != null)
			System.out.println("Empleado encontrado: " + employee.toString());
		else
			System.out.println("No existe el empleado con rut: " + rut);
		Assertions.assertThat(employee).isNotNull();
	}
}

@SpringBootTest
class ClockRepositoryTest {
	@Autowired private ClockRepository clockRepository;
	@Autowired private EmployeeRepository employeeRepository;
	@Test
	void testAddNew() {
		long id = 2;
		if (employeeRepository.findById(id).isPresent()) {
			ClockEntity clock1 = new ClockEntity();
			clock1.setCheck_in_time("08:00:00");
			clock1.setDate("2021-05-01");
			clock1.setEmployee(employeeRepository.findById(id).get());
			ClockEntity savedClock = clockRepository.save(clock1);
			Assertions.assertThat(savedClock).isNotNull();
			Assertions.assertThat(savedClock.getId()).isGreaterThan(0);
			System.out.println("Reloj: " + clock1.getId() + " ingresado con exito");
		} else {
			System.out.println("No existe el empleado con id: " + id);
		}
	}

	@Test
	void testDeleteAllClocks() {
		clockRepository.deleteAll();
		Assertions.assertThat(clockRepository.findAll()).isEmpty();
		System.out.println("Todos los relojes han sido eliminados");
	}

	@Test
	void testDeleteClockById() {
		long id = 2;
		if (clockRepository.findById(id).isPresent()) {
			System.out.println("Reloj eliminado con id: " + clockRepository.findById(id).get().getId() + ".");
			clockRepository.deleteById(id);
		} else
			System.out.println("No existe el reloj con id: " + id);
		Assertions.assertThat(clockRepository.findById(id)).isPresent();
	}
}

class MueblesstgoApplicationTests {
	@Test
	void contextLoads() {
	}
}

