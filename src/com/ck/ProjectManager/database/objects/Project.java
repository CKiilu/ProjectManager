package com.ck.ProjectManager.database.objects;

/**
 * Created by cndet on 16/03/2017.
 */
public class Project {
    public static final String createTable =
            "CREATE TABLE IF NOT EXISTS projects(\n"+
                    " id integer PRIMARY KEY,\n"+
                    " project TEXT,\n"+
                    " path TEXT,\n"+
                    " project_type TEXT,\n"+
                    "FOREIGN KEY(project_type) REFERENCES project_configurations(project_type)"+
                    ");";

    public static final String updateRow =
            "UPDATE projects "+
                    "SET project = ? , path = ? , project_type = ?"+
                    "WHERE id = ? ;";
    public static final String getAll = "SELECT * FROM  projects;";
    public static final String getAllByType = "SELECT * FROM  projects WHERE project_type = ? ;";
    public static final String insertIntoTable =  "INSERT INTO projects(project, path, project_type) VALUES (?,?,?);";
    public static final String deleteTable = "DROP TABLE IF EXISTS projects;";
    public static final String deleteRow = "DELETE FROM projects WHERE id = ?;";

    private String project_name, project_path, project_type;
    private int id;

    public Project(int id, String project_name, String project_path, String project_type) {
        this.id = id;
        this.project_name = project_name;
        this.project_path = project_path;
        this.project_type = project_type;
    }


    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_path() {
        return project_path;
    }

    public void setProject_path(String project_path) {
        this.project_path = project_path;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
