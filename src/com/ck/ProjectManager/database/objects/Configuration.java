package com.ck.ProjectManager.database.objects;

import com.ck.ProjectManager.database.helpers.ZipFileMethods;
import com.ck.ProjectManager.database.helpers.ZipObj;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by cndet on 16/03/2017.
 */
public class Configuration {

    public static final String createTable =
            "CREATE TABLE IF NOT EXISTS project_configurations(\n"+
                    " project_type TEXT PRIMARY KEY,\n"+
                    " file BLOB,\n"+
                    " pre_install TEXT,\n"+
                    " post_install TEXT"+
                    ");";
    public static final String updateRow =
            "UPDATE project_configurations "+
                    "SET project_type = ? , file = ? "+
                    "WHERE project_type = ? ;";
    public static final String getAll = "SELECT * FROM  project_configurations;";
    public static final String getAllTypes = "SELECT project_type FROM  project_configurations;";
    public static final String getFileByType = "SELECT file FROM  project_configurations WHERE project_type = ? ;";
    public static final String insertIntoTable =  "INSERT INTO project_configurations(project_type, file, pre_install, post_install) VALUES (?,?,?,?);";
    public static final String deleteTable = "DROP TABLE IF EXISTS project_configurations;";
    public static final String deleteRow = "DELETE FROM project_configurations WHERE project_type = ?;";

    private String project_type, pre_install, post_install;
    private ArrayList<ZipObj> file;

    public Configuration(String project_type, InputStream file, String pre_install, String post_install) {
        this.project_type = project_type;
        this.file = ZipFileMethods.readZipFromStream(file);
        this.pre_install = pre_install;
        this.post_install = post_install;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public ArrayList<ZipObj> getFile() {
        return file;
    }

    public void setFile(ArrayList<ZipObj> file) {
        this.file = file;
    }

    public String getPost_install() {
        return post_install;
    }

    public void setPost_install(String post_install) {
        this.post_install = post_install;
    }

    public String getPre_install() {
        return pre_install;
    }

    public void setPre_install(String pre_install) {
        this.pre_install = pre_install;
    }
}
