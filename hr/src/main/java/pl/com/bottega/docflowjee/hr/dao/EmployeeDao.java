package pl.com.bottega.docflowjee.hr.dao;

import pl.com.bottega.docflowjee.hr.model.Employee;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class EmployeeDao {

    @Inject
    private EntityManager entityManager;

    public Employee save(Employee employee) {
        return entityManager.merge(employee);
    }

    public Employee find(Long id) {
        return (Employee) entityManager.createNamedQuery("Employee.findById")
            .setParameter("id", id)
            .getSingleResult();
    }
}
