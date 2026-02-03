package com.apurba.spingboot.cruddemo.dao;

import com.apurba.spingboot.cruddemo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
