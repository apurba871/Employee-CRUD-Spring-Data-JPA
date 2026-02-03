package com.apurba.spingboot.cruddemo.rest;

import com.apurba.spingboot.cruddemo.dao.EmployeeDAO;
import com.apurba.spingboot.cruddemo.entity.Employee;
import com.apurba.spingboot.cruddemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    private EmployeeService employeeService;
    private JsonMapper jsonMapper;
    @Autowired
    public EmployeeRestController(EmployeeService theEmployeeService, JsonMapper jsonMapper) {
        employeeService = theEmployeeService;
        this.jsonMapper = jsonMapper;
    }

    //expose "/employees" and return a list of employees
    @GetMapping("/employees")
    public List<Employee> findAll() {
        return employeeService.findAll();
    }

    //expose "/employees/{employeeId}" and return a single employee
    @GetMapping("/employees/{employeeId}")
    public Employee findById(@PathVariable int employeeId) {
        Employee theEmployee = employeeService.findById(employeeId);
        if (theEmployee == null)
            throw new RuntimeException("Employee id not found " + employeeId);
        return theEmployee;
    }

    //expose "/employees" to add a new employee
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee employeeDetails) {
        Employee newEmployee = employeeService.save(employeeDetails);
        return newEmployee;
    }

    //expose "/employees" to update an existing employee
    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employeeDetails) {
        //find if there exists an employee with the given id
        Employee dbEmployee = employeeService.findById(employeeDetails.getId());
        if (dbEmployee == null)
            throw new RuntimeException("Employee id not found in DB " + employeeDetails.getId());

        //update the details of the employee
        dbEmployee = employeeService.save(employeeDetails);
        return dbEmployee;
    }

    //expose "/employees/{employeeId}" to allow partial update to existing employee
    @PatchMapping("/employees/{employeeId}")
    public Employee partialUpdateEmployee(@PathVariable int employeeId, @RequestBody Map<String, Object> patchPayload) {
        //find the employee from DB
        Employee dbEmployee = employeeService.findById(employeeId);

        if (dbEmployee == null)
            throw new RuntimeException("Employee id not found in DB " + employeeId);

        //throw exception if patch request payload contains id
        if (patchPayload.containsKey("id")) {
            throw new RuntimeException("'id' not allowed in payload!");
        }

        //apply the partial updates to the employee
        Employee patchedEmployee = jsonMapper.updateValue(dbEmployee, patchPayload);

        Employee updatedEmployee = employeeService.save(patchedEmployee);
        return updatedEmployee;
    }

    //expose "/employees/{employeeId}" to delete an existing employee
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable int employeeId) {
        //find the employee from DB
        Employee dbEmployee = employeeService.findById(employeeId);
        if (dbEmployee == null)
            throw new RuntimeException("Employee id not found in DB " + employeeId);
        //remove the employee from DB
        employeeService.deleteById(employeeId);
        return "Deleted employee with id: " + employeeId;
    }
}
