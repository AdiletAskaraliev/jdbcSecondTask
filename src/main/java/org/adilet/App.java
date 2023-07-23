package org.adilet;

import org.adilet.dao.EmployeeDao;
import org.adilet.model.Employee;
import org.adilet.model.Job;
import org.adilet.service.EmployeeService;
import org.adilet.service.JobService;
import org.adilet.service.serviceImpl.EmployeeServiceImpl;
import org.adilet.service.serviceImpl.JobServiceImpl;

import java.util.Scanner;

/**
 * Hello world!
 **/
public class App {
    public static void main( String[] args ) {

        EmployeeService employeeService = new EmployeeServiceImpl();
        JobService jobService = new JobServiceImpl();


        while (true) {
            int san = new Scanner(System.in).nextInt();
            switch (san){
                case 1:
                    jobService.createJobTable();
                    break;
                case 2:
                    jobService.addJob(new Job("Management", "Project", "PM", 2));
                    jobService.addJob(new Job("Mentor", "Java", "Backend developer", 3));
                    jobService.addJob(new Job("Instructor", "Java", "Backend developer", 4));
                    jobService.addJob(new Job("Mentor", "Java", "Backend developer", 1));
                    break;
                case 3:
                    System.out.println(jobService.getJobById(1L));
                    break;
                case 4:
                    employeeService.addEmployee(new Employee("Adi", "Askaraliev", 23, "adi@gmail.com", 1));
                    employeeService.addEmployee(new Employee("Asan", "Asanov", 25, "asan@gmail.com", 2));
                    employeeService.addEmployee(new Employee("Uson", "Usonov", 21, "uson@gmail.com", 3));
                    employeeService.addEmployee(new Employee("Ivan", "Ivanov", 24, "ivan@gmail.com", 4));
                    break;
                case 5:
                    System.out.println(jobService.getJobByEmployeeId(3L));
                    break;
                case 6:
                    jobService.deleteDescriptionColumn();
                    break;
                case 7:
                    System.out.println(jobService.sortByExperience("asc"));
                    break;
                case 8:
                    employeeService.createEmployee();
                    break;
                case 9:
                    System.out.println(employeeService.getEmployeeById(2L));
                    break;
                case 10:
                    System.out.println(employeeService.findByEmail("ivan@gmail.com"));
                    break;
                case 11:
                    employeeService.updateEmployee(2L,
                            new Employee("Luka", "Jovic", 26, "luka@gmail.com", 2));
                    break;
                case 12:
                    System.out.println(employeeService.getAllEmployees());
                    break;
                case 13:
                    System.out.println(employeeService.getEmployeeByPosition("Mentor"));
                    break;
                case 14:
                    employeeService.cleanTable();
                    break;
                case 15:
                    employeeService.dropTable();


            }
        }


    }
}
