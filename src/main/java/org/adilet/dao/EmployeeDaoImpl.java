package org.adilet.dao;

import org.adilet.config.Config;
import org.adilet.model.Employee;
import org.adilet.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public void createEmployee() {
        String sql = "create table if not exists employees(" +
                "id serial primary key," +
                "first_name varchar," +
                "last_name varchar," +
                "age int," +
                "email varchar, " +
                "job_id int references jobs(id) )";

        try(Connection connection = Config.getConnection();
            Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("Table Employees is created!!!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = "insert into employees(" +
                "first_name, last_name, age, email, job_id)" +
                "values (?, ?, ?, ?,?)";

        try(Connection connection = Config.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setInt(5, employee.getJobId());
            preparedStatement.executeUpdate();

            System.out.println("new " + employee.getFirstName()+ " " + employee.getLastName() + " is added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = "drop table employees;";

        try(Connection connection = Config.getConnection();
        Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);
            System.out.println("Employees table deleted");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void cleanTable() {
        String sql = "delete from employees";

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();

            System.out.println("Employees cleaned");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String sql = "update employees set first_name=?, last_name=?, age=?, email=?, job_id=? " +
                "where id = ?";

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setLong(5, employee.getJobId());
            preparedStatement.setLong(6, id);

            int updated = preparedStatement.executeUpdate();

            if (updated > 0) {
                System.out.println(employee.getFirstName() + " is updated");
            } else {
                System.out.println("No student with ID " + id + " found.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        String sql = "select * from employees";

        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        Employee employee = null;

        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employee;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> employeeJobMap = new HashMap<>();
        String sql = "SELECT employees.*, jobs.* " +
                "FROM employees " +
                "JOIN jobs ON employees.job_id = jobs.id " +
                "WHERE employees.id = ?";

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJobId(resultSet.getInt("job_id"));


                Job job = new Job();
                job.setId(resultSet.getLong("job_id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));

                employeeJobMap.put(employee, job);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return employeeJobMap;
    }


    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        String sql = "select employees.* from employees join jobs on employees.job_id = jobs.id where position = ?";

        try(Connection connection = Config.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, position);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getInt("job_id")
                ));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }
}
