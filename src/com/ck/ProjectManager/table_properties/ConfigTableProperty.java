package com.ck.ProjectManager.table_properties;

import com.ck.ProjectManager.database.helpers.ZipObj;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Created by cndet on 15/03/2017.
 */
public class ConfigTableProperty {
    private SimpleStringProperty project_type;
    private SimpleListProperty zipObjs;
    private String pre_install, post_install;

    public ConfigTableProperty(String type, ArrayList<ZipObj> zipObjs, String pre_install, String post_install) {
        this.pre_install = pre_install;
        this.post_install = post_install;
        ObservableList<ZipObj> ls = FXCollections.observableArrayList();
        ls.addAll(zipObjs);
        this.project_type = new SimpleStringProperty(type);
        this.zipObjs = new SimpleListProperty(ls);
    }

    public String getProject_type() {
        return project_type.get();
    }

    public SimpleStringProperty project_typeProperty() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type.set(project_type);
    }

    public ObservableList<ZipObj> getZipObjs() {
        return zipObjs.get();
    }

    public SimpleListProperty zipObjsProperty() {
        return zipObjs;
    }

    public void setZipObjs(ArrayList<ZipObj> zipObjs) {
        this.zipObjs.clear();
        this.zipObjs.addAll(zipObjs);
    }

    public String getPre_install() {
        return pre_install;
    }

    public String getPost_install() {
        return post_install;
    }
}
