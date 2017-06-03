package com.ck.ProjectManager.database.handlers;

import com.ck.ProjectManager.database.objects.Project;

import java.sql.*;
import java.util.ArrayList;


/**
 * Created by cndet on 16/03/2017.
 */
public class ProjectHandler extends MasterHandler {

    public static ArrayList<Project> getProjectsByType(){
        try {
            startConnection(Project.createTable);
            ArrayList<Project> projects = new ArrayList<>();
            ResultSet resultSet = c.createStatement().executeQuery(Project.getAll);
            while (resultSet.next()){
                projects.add(new Project(resultSet.getInt("id"), resultSet.getString("project"), resultSet.getString("path"), resultSet.getString("project_type")));
            }
            c.close();
            return projects;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return new ArrayList<>();
    }

    public static ArrayList<Project> getProjectsByType(String type){
        try {
            startConnection(Project.createTable);
            ArrayList<Project> projects = new ArrayList<>();
            PreparedStatement p = c.prepareStatement(Project.getAllByType);
            p.setString(1, type);
            ResultSet resultSet = p.executeQuery();
            while (resultSet.next()){
                projects.add(new Project(resultSet.getInt("id"), resultSet.getString("project"), resultSet.getString("path"), resultSet.getString("project_type")));
            }
            c.close();
            return projects;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return new ArrayList<>();
    }
    public static void deleteRow(int id){
        try {
            startConnection(Project.createTable);
            ArrayList<Project> projects = new ArrayList<>();
            PreparedStatement p = c.prepareStatement(Project.deleteRow);
            p.setInt(1, id);
            p.executeUpdate();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void addProject(String name, String path, String type){
        try{
            startConnection();
            PreparedStatement p = c.prepareStatement(Project.insertIntoTable);
            p.setString(1, name);
            p.setString(2, path);
            p.setString(3, type);
            p.executeUpdate();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
