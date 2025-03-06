package org.example;

import javax.persistence.*;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dno;
    private String dname;

    @OneToMany(cascade = CascadeType.ALL)
    List<Employee> employees;

    public Department() {
    }

    public Department(int dno, String dname, List<Employee> employees) {
        this.dno = dno;
        this.dname = dname;
        this.employees = employees;
    }

    public int getDno() {
        return dno;
    }

    public void setDno(int dno) {
        this.dno = dno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department{" +
                "dno=" + dno +
                ", dname='" + dname + '\'' +
                ", employees=" + employees +
                '}';
    }
}
