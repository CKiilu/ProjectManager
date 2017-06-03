package com.ck.ProjectManager.views;

import com.ck.ProjectManager.Main;
import com.ck.ProjectManager.database.handlers.ConfigurationHandler;
import com.ck.ProjectManager.database.handlers.ProjectHandler;
import com.ck.ProjectManager.database.objects.Configuration;
import com.ck.ProjectManager.database.objects.Project;
import com.ck.ProjectManager.projecttypes.P5;
import com.ck.ProjectManager.table_properties.ConfigTableProperty;
import com.ck.ProjectManager.table_properties.ProjectTableProperty;
import com.ck.ProjectManager.views.dialogs.AddConfigController;
import com.ck.ProjectManager.views.dialogs.AddProjectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by cndet on 16/03/2017.
 */
public class MainController extends MasterController {
    @FXML private TableView<ProjectTableProperty> project_table;
    @FXML private TableView<ConfigTableProperty> config_table;
    @FXML private TableColumn<ProjectTableProperty, String> project_col, path_col;
    @FXML private TableColumn<ConfigTableProperty, String> project_type_col;
    @FXML private MenuButton project_filter;
    @FXML private Button add_project_btn;

    private ObservableList<ProjectTableProperty> projectTableProperties;
    private ObservableList<ConfigTableProperty> configTableProperties;

    private final String defaultMenuItem = "DEFAULT";
    private ArrayList<Configuration> config_data;
     @Override
     public void initialize(){
        this.project_col.setCellValueFactory(p -> p.getValue().project_nameProperty() );
        this.path_col.setCellValueFactory(p -> p.getValue().pathProperty() );
        this.project_type_col.setCellValueFactory(p -> p.getValue().project_typeProperty() );
        this.setProjectListItems();
        this.setConfigListItems();
    }

    @FXML private void openP5Extras(MouseEvent event){
        try {
            int i = this.project_table.getSelectionModel().getSelectedIndex();
            if(i >= 0) {
                ProjectTableProperty p = projectTableProperties.get(i);
                p5 = new P5(
                        p.getProject_name(), p.getPath()
                );
                FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/p5_extras.fxml"));
                Parent root = loader.load();
                P5WindowController c = loader.getController();
                Scene p5Scene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
                c.setDefaults(p5, stage, p5Scene, currentScene, p.getProject_name(), p.getPath());
                stage.setScene(p5Scene);
                stage.show();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML private void deleteProject(MouseEvent event){
        int i = this.project_table.getSelectionModel().getSelectedIndex();
        if(i >= 0) {
            ProjectTableProperty p = projectTableProperties.get(i);
            ProjectHandler.deleteRow(p.getId());
            this.setProjectListItems();
        }
    }
    @FXML private void deleteConfig(MouseEvent event){
        int i = this.config_table.getSelectionModel().getSelectedIndex();
        if(i >= 0) {
            ConfigTableProperty p = configTableProperties.get(i);
            ConfigurationHandler.deleteRow(p.getProject_type());
            this.setConfigListItems();
        }
    }
    private void setProjectListItems(){
        this.projectTableProperties = FXCollections.observableArrayList();

        for (Project p : ProjectHandler.getProjectsByType()) {
            this.projectTableProperties.add(
                    new ProjectTableProperty(
                            p.getId(),
                            p.getProject_name(),
                            p.getProject_path()
                    )
            );
        }
        this.project_table.setItems(this.projectTableProperties);
    }
    private void setConfigListItems(){
        this.config_data = ConfigurationHandler.getConfigurations();
        this.configTableProperties = FXCollections.observableArrayList();
        this.project_filter.getItems().clear();
        MenuItem def = new MenuItem(defaultMenuItem);
        def.setOnAction(this::triggerFilter);
        this.project_filter.getItems().add(def);

        for (Configuration c : this.config_data) {
            this.configTableProperties.add(
                    new ConfigTableProperty(
                            c.getProject_type(),
                            c.getFile(),
                            c.getPre_install(),
                            c.getPost_install()
                    )
            );
            MenuItem m = new MenuItem(c.getProject_type());
            m.setOnAction(this::triggerFilter);
            this.project_filter.getItems().add(m);
        }
        this.config_table.setItems(this.configTableProperties);
        this.add_project_btn.setDisable(this.config_data.size() == 0);
    }
    private void triggerFilter(ActionEvent event){
        MenuItem m = (MenuItem) event.getSource();
        String t = m.getText();
        if(t.equals(defaultMenuItem)){
            this.setProjectListItems();
        } else {
            this.filterProjectListItems(t);
        }
    }

    @FXML private void filterProjectListItems(String type){
        this.projectTableProperties = FXCollections.observableArrayList();
        for (Project p : ProjectHandler.getProjectsByType(type)) {
            this.projectTableProperties.add(
                    new ProjectTableProperty(
                            p.getId(),
                            p.getProject_name(),
                            p.getProject_path()
                    )
            );
        }
        this.project_table.setItems(this.projectTableProperties);
    }

    @FXML private void addProjectDialog(){
        Stage dialog_stage = new Stage();
        dialog_stage.setTitle("Add Project");
        dialog_stage.initModality(Modality.WINDOW_MODAL);
        dialog_stage.initOwner(stage);
        FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/dialogs/add_project.fxml"));
        try {
            Scene dialog_scene = new Scene(loader.load());
            dialog_stage.setScene(dialog_scene);
            AddProjectController controller = loader.getController();
            controller.setDefaults(dialog_stage, dialog_scene);
            controller.setConfigData(this.config_data);
            dialog_stage.showAndWait();

            this.setProjectListItems();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML private void addConfigDialog(){
        Stage dialog_stage = new Stage();
        dialog_stage.setTitle("Add Configuration");
        dialog_stage.initModality(Modality.WINDOW_MODAL);
        dialog_stage.initOwner(stage);
        FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/dialogs/add_config.fxml"));
        try {
            Scene dialog_scene = new Scene(loader.load());
            dialog_stage.setScene(dialog_scene);
            AddConfigController controller = loader.getController();
            controller.setDefaults(dialog_stage, dialog_scene);
            dialog_stage.showAndWait();

            this.setConfigListItems();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void openEditConfig(MouseEvent event){
        try {
            int i = this.config_table.getSelectionModel().getSelectedIndex();
            FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/edit_config.fxml"));
            if(i >= 0) {
                ConfigTableProperty configTableProperty = configTableProperties.get(i);
                Parent root = loader.load();
                Scene scene = new Scene(root, currentScene.getWidth(), currentScene.getHeight());
                EditConfigController c = loader.getController();
                c.setDefaults(stage, scene, currentScene);
                c.zipSetup(configTableProperty.getProject_type(), configTableProperty.getZipObjs(), configTableProperty.getPre_install(), configTableProperty.getPost_install());
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
