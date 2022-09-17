package tingeso.mueblesstgo;

import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import tingeso.mueblesstgo.repositories.ClockRepository;
import tingeso.mueblesstgo.entities.ClockEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class EmployeeeRepositoryTest {
	@Autowired private EmployeeRepository employeeRepository;

	@Test
	void insertAllEmployees() {
		int actualYear = 2022;

		EmployeeEntity employee1 = new EmployeeEntity();
		employee1.setRut("21.024.191-4");
		employee1.setName("Christopher Alejandro Torres Aceituno");
		employee1.setCategory('A');
		employee1.setDateOfAdmission("2005/01/01");
		employee1.setYearsOfService(actualYear-(Integer.parseInt(employee1.getDateOfAdmission().substring(0,4))));
		employeeRepository.save(employee1);
		EmployeeEntity employee2 = new EmployeeEntity();
		employee2.setRut("21.014.566-4");
		employee2.setName("Vanina Antonia Correa Chávez");
		employee2.setCategory('A');
		employee2.setDateOfAdmission("2010/01/01");
		employee2.setYearsOfService(actualYear-(Integer.parseInt(employee2.getDateOfAdmission().substring(0,4))));
		employeeRepository.save(employee2);
		EmployeeEntity employee3 = new EmployeeEntity();
		employee3.setRut("22.223.596-3");
		employee3.setName("Josed Aguilar Lopez Gutiérrez");
		employee3.setCategory('B');
		employee3.setDateOfAdmission("2015/01/01");
		employee3.setYearsOfService(actualYear-(Integer.parseInt(employee3.getDateOfAdmission().substring(0,4))));
		employeeRepository.save(employee3);
		EmployeeEntity employee4 = new EmployeeEntity();
		employee4.setRut("20.123.456-7");
		employee4.setName("Pablo Enrique Román Aserjo");
		employee4.setCategory('C');
		employee4.setDateOfAdmission("2018/01/01");
		employee4.setYearsOfService(actualYear-(Integer.parseInt(employee4.getDateOfAdmission().substring(0,4))));
		employeeRepository.save(employee4);
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
		String rut = "20.123.456-7";
		EmployeeEntity employee = employeeRepository.findByRut(rut);
		if (employee != null)
			System.out.println("Empleado encontrado: " + employee.getName());
		else
			System.out.println("No existe el empleado con rut: " + rut);
		Assertions.assertThat(employee).isNotNull();
	}

	@Test
	void testFindEmployees() {
		Iterable<EmployeeEntity> employees = employeeRepository.findAll();
		for (EmployeeEntity employee : employees) {
			System.out.println("Empleado encontrado: " + employee.getName());
		}
		Assertions.assertThat(employees).isNotNull();
	}
}

@SpringBootTest
class ClockRepositoryTest {
	@Autowired private ClockRepository clockRepository;
	@Autowired private EmployeeRepository employeeRepository;
	@Test
	void testAddNew() {
		long id = 1;
		if (employeeRepository.findById(id).isPresent()) {
			ClockEntity clock1 = new ClockEntity();
			clock1.setCheckInTime("08:00:00");
			clock1.setDate("2021-05-01");
			clock1.setEmployee(employeeRepository.findByRut("20.123.456-7"));
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

	@Test
	void testFindDiscountByEmployee() {
		EmployeeEntity employee = employeeRepository.findByName("Christopher Alejandro Torres Aceituno");
		ArrayList<Integer> discounts = clockRepository.findDiscountByEmployee(employee);
		for (Integer discount : discounts) {
			System.out.println("Descuento: " + discount);
		}
	}

	@Test
	void testFindAllByEmployeeContaining() {
		EmployeeEntity employee = employeeRepository.findByName("Christopher Alejandro Torres Aceituno");
		ArrayList<ClockEntity> clocks = clockRepository.findAllByEmployeeContaining(employee);
		for (ClockEntity clock : clocks) {
			System.out.println("Reloj: " + clock.getId());
		}
	}
}
