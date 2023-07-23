package org.adilet.dao;

import org.adilet.config.Config;
import org.adilet.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDaoImpl implements JobDao{
    @Override
    public void createJobTable() {
        String sql = "create table if not exists jobs(" +
                "id serial primary key," +
                "position varchar," +
                "profession varchar," +
                "description varchar," +
                "experience int )";

        try (Connection connection = Config.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);
            System.out.println("Table is created!!!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addJob(Job job) {

        String sql = "insert into jobs(" +
                "position, profession, description, experience)" +
                "values (?, ?, ?, ?)";

        try(Connection connection = Config.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setString(1, job.getPosition());
            preparedStatement.setString(2, job.getProfession());
            preparedStatement.setString(3, job.getDescription());
            preparedStatement.setInt(4, job.getExperience());
             preparedStatement.executeUpdate();
            System.out.println("new " + job.getPosition() + " is added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public Job getJobById(Long jobId) {
        Job job = null;
        String sql = "SELECT * FROM jobs WHERE id = ?";

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, jobId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return job;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobList = new ArrayList<>();
        String sql = "SELECT * FROM jobs ORDER BY experience " + (ascOrDesc.equalsIgnoreCase("desc") ? "DESC" : "ASC");

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Job job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));

                jobList.add(job);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return jobList;
    }


    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        String sql = "SELECT jobs.* FROM jobs " +
                "JOIN employees ON employees.job_id = jobs.id " +
                "WHERE employees.id = ?";
        Job job = null;

        try (Connection connection = Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                job = new Job();
                job.setId(resultSet.getLong("id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
                job.setExperience(resultSet.getInt("experience"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return job;
    }


    @Override
    public void deleteDescriptionColumn() {
        String sql = "alter table jobs drop column description;";

        try(Connection connection = Config.getConnection();
        Statement statement = connection.createStatement()){

            statement.executeUpdate(sql);

            System.out.println("Description column is deleted");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
