package com.ck.ProjectManager.database.handlers;

import com.ck.ProjectManager.database.objects.Configuration;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by cndet on 16/03/2017.
 */
public class ConfigurationHandler  extends MasterHandler {
    public static ArrayList<Configuration> getConfigurations(){
        try {
            startConnection(Configuration.createTable);
            ArrayList<Configuration> configurations = new ArrayList<>();
            ResultSet resultSet = c.createStatement().executeQuery(Configuration.getAll);
            while (resultSet.next()){
                configurations.add(new Configuration(
                        resultSet.getString("project_type"),
                        resultSet.getBinaryStream("file"),
                        resultSet.getString("pre_install"),
                        resultSet.getString("post_install")
                        )
                );
            }
            c.close();
            return configurations;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return new ArrayList<>();
    }
    public static InputStream getConfigurationFileByType(String type){
        try {
            startConnection(Configuration.createTable);
            PreparedStatement p = c.prepareStatement(Configuration.getFileByType);
            p.setString(1, type);
            ResultSet resultSet = p.executeQuery();

            return resultSet.getBinaryStream(1);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
    public static void deleteRow(String type){
        try {
            startConnection(Configuration.createTable);
            ArrayList<Configuration> projects = new ArrayList<>();
            PreparedStatement p = c.prepareStatement(Configuration.deleteRow);
            p.setString(1, type);
            p.executeUpdate();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void addConfig(String name, byte[] file, String pre, String post){
        try{
            startConnection();
            PreparedStatement p = c.prepareStatement(Configuration.insertIntoTable);
            p.setString(1, name);
            p.setBytes(2, file);
            p.setString(3, pre);
            p.setString(4, post);
            p.executeUpdate();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void editConfig(String name, byte[] file){
        try{
            startConnection();
            PreparedStatement p = c.prepareStatement(Configuration.updateRow);
            p.setString(1, name);
            p.setBytes(2, file);
            p.executeUpdate();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
