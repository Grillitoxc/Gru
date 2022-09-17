package tingeso.mueblesstgo.services;

import org.springframework.stereotype.Service;
import tingeso.mueblesstgo.repositories.EmployeeRepository;
import tingeso.mueblesstgo.entities.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public EmployeeEntity getEmployeeByName(String name) {
        return employeeRepository.findByName(name);
    }
}
