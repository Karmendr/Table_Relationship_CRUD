package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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
                displayDepartment(config,sessionFactory);

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
                displayDepartment(config,sessionFactory);
            }
            else if(choice == 4)
            {
                Session session = sessionFactory.openSession();
                System.out.println("Enter Department number that department's employees do you want to display");
                Department department = session.get(Department.class,sc.nextInt());
                System.out.println("Department Name : - "+department.getDname());
                System.out.println("Enrolled Employees : ");
                for(Employee item : department.getEmployees())
                {
                    System.out.println(item.getName()+"\t"+item.getAge()+"\t"+item.getSalary());
                }
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
                displayDepartment(config,sessionFactory);
                System.out.println("Enter Department number that you want to remove ");
                Department department = session.get(Department.class,sc.nextInt());
                session.remove(department);
                transaction.commit();
                System.out.println(department.getDname()+" is Removed!.....");
                session.close();
            }
        }
    }
    public static void displayDepartment(Configuration config,SessionFactory sessionFactory)
    {
        Session session = sessionFactory.openSession();
        List<Department> departmentList = session.createQuery("from Department").getResultList();

        for(Department item : departmentList)
        {
            System.out.println(item.getDno()+"\t"+item.getDname());
        }
        session.close();
    }

}
