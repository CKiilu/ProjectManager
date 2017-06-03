package com.ck.ProjectManager.table_properties;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;

/**
 * Created by cndet on 15/03/2017.
 */
public class ProjectTableProperty {
    private SimpleStringProperty project_name, path;
    private SimpleIntegerProperty id;
    private SimpleBooleanProperty edit;

    public ProjectTableProperty(int id, String project, String path) {
        this.project_name = new SimpleStringProperty(project);
        this.id = new SimpleIntegerProperty(id);
        this.path = new SimpleStringProperty(path);
        this.edit = new SimpleBooleanProperty(false);
    }

    public String getProject_name() {
        return project_name.get();
    }

    public SimpleStringProperty project_nameProperty() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name.set(project_name);
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return new SimpleStringProperty(path.get() + File.separator + project_name.get());
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public boolean isEdit() {
        return edit.get();
    }

    public SimpleBooleanProperty editProperty() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit.set(edit);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }
}
