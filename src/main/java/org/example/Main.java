package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration().configure("config.xml");
        SessionFactory sessionFactory = config.buildSessionFactory();
        Scanner sc = new Scanner(System.in);

        while (true)
        {
            System.out.println("Press 1 for Add new Department ");
            System.out.println("Press 2 for Add new Employees ");
            System.out.println("Press 3 for Display All Department ");
            System.out.println("Press 4 for Display Employees ");
            System.out.println("Press 5 for Display All Employees ");
            System.out.println("Press 6 for Remove Department ");
            System.out.println("Press 7 for Remove Employee ");
            System.out.println("Press 8 for Search Employee by SNO ");
            System.out.println("Press 9 for Search Employee by Name ");
            System.out.println("Press 10 for Search Department by dno ");
            System.out.println("Press 11 for Search Department by Name ");
            System.out.println("Press 12 for Update Department Information ");
            System.out.println("Press 13 for Update Employee Information ");


            System.out.println("Enter your Choice ");
            int choice = sc.nextInt();

            if(choice == 1)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                Department department = new Department();
                System.out.println("Enter new Department Name ");
                department.setDname(sc.next());
                session.save(department);
                transaction.commit();
                System.out.println("New Department Added!.....");
                session.close();
            }
            else if(choice == 2)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                List<Department> departmentList = session.createQuery("from Department").getResultList();
                displayDepartment(departmentList);

                System.out.println("Enter the department number in which you want to add new employee ");
                Department department = session.get(Department.class,sc.nextInt());

                if(department==null)
                {
                    System.out.println("No such Department on that Number ");
                }
                else {
                    int check=0;
                    do {
                        Employee employee = new Employee();
                        System.out.println("Enter Name ");
                        employee.setName(sc.next());
                        System.out.println("Enter Age ");
                        employee.setAge(sc.nextInt());
                        System.out.println("Enter Salary ");
                        employee.setSalary(sc.nextInt());
                        employee.setDepartment(department);
                        department.getEmployees().add(employee);
                        System.out.println("If you want to Add more Employee then Press 1 otherwise Save data to Press 0");
                        check = sc.nextInt();
                    }while (check == 1);
                    session.save(department);
                    transaction.commit();
                    System.out.println("Record Inserted!.....");
                }

                session.close();
            }
            else if(choice == 3)
            {
                Session session = sessionFactory.openSession();
                List<Department> departmentList = session.createQuery("from Department").getResultList();
                displayDepartment(departmentList);
                session.close();
            }
            else if(choice == 4)
            {
                Session session = sessionFactory.openSession();
                List<Department> departmentList = session.createQuery("from Department").getResultList();
                displayDepartment(departmentList);
                System.out.println("Enter Department number that department's employees do you want to display");
                Department department = session.get(Department.class,sc.nextInt());
                System.out.println("Department Name : - "+department.getDname());
                System.out.println("Enrolled Employees : ");
                List<Employee> employeeList = department.getEmployees();
                displayEmployees(employeeList);
                session.close();
            }
            else if(choice == 5)
            {
                Session session = sessionFactory.openSession();
                List<Employee> data = session.createQuery("from Employee").getResultList();

                System.out.println("SNO."+"\t"+"Name"+"\t"+"Age"+"\t"+"Salary"+"\t"+"Department");

                for(Employee item : data)
                {
                    System.out.println(item.getSno()+"\t\t"+item.getName()+"\t"+item.getAge()+"\t"+item.getSalary()+"\t"+item.getDepartment().getDname());
                }

                session.close();
            }
            else if(choice == 6)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                List<Department> departmentList = session.createQuery("from Department").getResultList();
                displayDepartment(departmentList);
                System.out.println("Enter Department number that you want to remove ");
                Department department = session.get(Department.class,sc.nextInt());
                session.remove(department);
                transaction.commit();
                System.out.println(department.getDname()+" is Removed!.....");
                session.close();
            }
            else if(choice == 7)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                System.out.println("Enter Employee SNO that you want to Remove ");
                int temp = sc.nextInt();
                Employee employee = session.get(Employee.class,temp);
                //session.createNativeQuery("delete from department_employee where employees_sno=:sno",Employee.class).setParameter("sno",temp).executeUpdate();
                NativeQuery query = session.createNativeQuery("delete from department_employee where employees_sno=:sno", Employee.class);
                query.setParameter("sno",temp);
                query.executeUpdate();
                session.remove(employee);
                System.out.println(employee.getName()+" is Deleted!....");
                transaction.commit();
                session.close();
            }
            else if(choice == 8)
            {
                Session session = sessionFactory.openSession();
                List<Employee> employeeList = session.createQuery("from Employee").getResultList();
                displayEmployees(employeeList);
                System.out.println("Enter Employee SNO that you want to search ");
                try {
                    Employee employee = session.get(Employee.class,sc.nextInt());
                    System.out.println(employee.getSno()+"\t"+employee.getName()+"\t"+employee.getAge()+"\t"+employee.getSalary());
                }
                catch (NullPointerException ob)
                {
                    System.out.println("No such Employee on that SNO \n");
                }
                session.close();
            }
            else if(choice == 9)
            {
                Session session = sessionFactory.openSession();
                NativeQuery query = session.createNativeQuery("select * from employee where name=:name",Employee.class);
                System.out.println("Enter Employee Name that you want to search ");
                query.setParameter("name",sc.next());
                try {
                    Employee employee = (Employee) query.getSingleResult();
                    System.out.println(employee.getSno()+"\t"+employee.getName()+"\t"+employee.getAge()+"\t"+employee.getSalary());
                }
                catch (NonUniqueResultException ob)
                {
                    List<Employee> employeeList = query.getResultList();
                    displayEmployees(employeeList);
                }
                catch (NoResultException ob)
                {
                    System.out.println("No such Employee ");
                }

                session.close();
            }
            else if(choice == 10)
            {
                Session session = sessionFactory.openSession();
                System.out.println("Enter Department number that you want to Search ");
                Department department = session.get(Department.class,sc.nextInt());
                if(department == null)
                {
                    System.out.println("No such Department on that Number ");
                }
                else
                {
                    System.out.println(department.getDno()+"\t"+department.getDname());
                }

                session.close();
            }
            else if(choice == 11)
            {
                Session session = sessionFactory.openSession();
                System.out.println("Enter Department Name that you want to search ");
                NativeQuery query = session.createNativeQuery("select * from department where dname=:name", Department.class);
                query.setParameter("name",sc.next());
                Department department = (Department) query.getSingleResult();
                if(department == null)
                {
                    System.out.println("No such Department on that Number ");
                }
                else
                {
                    System.out.println(department.getDno()+"\t"+department.getDname());
                }
                session.close();
            }
            else if(choice == 12)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                List<Department> departmentList = session.createQuery("from Department").getResultList();
                displayDepartment(departmentList);
                System.out.println("Enter Department number that you want to Update ");
                int temp = sc.nextInt();
                NativeQuery query = session.createNativeQuery("update department set dname=:name where dno=:dno", Department.class);
                System.out.println("Enter new Department name ");
                query.setParameter("name",sc.next());
                query.setParameter("dno",temp);
                query.executeUpdate();
                transaction.commit();
                System.out.println("Department Updated!.....");
                session.close();
            }

            else if(choice == 13)
            {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                List<Employee> employeeList = session.createQuery("from Employee").getResultList();
                displayEmployees(employeeList);
                System.out.println("Enter Employee SNO that you want to Update ");
                int sno = sc.nextInt();
                NativeQuery query = session.createNativeQuery("update employee set name=:name,age=:age,salary=:salary where sno=:sno", Employee.class);
                System.out.println("Enter new Name ");
                query.setParameter("name",sc.next());
                System.out.println("Enter new Age ");
                query.setParameter("age",sc.nextInt());
                System.out.println("Enter new Salary ");
                query.setParameter("salary",sc.nextInt());
                query.setParameter("sno",sno);
                query.executeUpdate();
                transaction.commit();
                System.out.println("Record Updated!......");
                session.close();
            }
        }
    }

    public static void displayEmployees(List<Employee> employeeList)
    {
        for (Employee item : employeeList)
        {
            System.out.println(item.getSno()+"\t"+item.getName()+"\t"+item.getAge()+"\t"+item.getSalary());
        }
    }

    public static void displayDepartment(List<Department> departmentList)
    {
        for(Department item : departmentList)
        {
            System.out.println(item.getDno()+"\t"+item.getDname());
        }
    }

}
