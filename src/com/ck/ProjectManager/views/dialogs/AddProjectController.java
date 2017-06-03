package com.ck.ProjectManager.views.dialogs;

import com.ck.ProjectManager.database.handlers.ConfigurationHandler;
import com.ck.ProjectManager.database.handlers.ProjectHandler;
import com.ck.ProjectManager.database.helpers.ZipFileMethods;
import com.ck.ProjectManager.database.objects.Configuration;
import com.ck.ProjectManager.views.MasterController;
import com.ck.ProjectManager.projecttypes.P5;
import com.ck.ProjectManager.projecttypes.Processing;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cndet on 16/03/2017.
 */
public class AddProjectController extends MasterController{
    @FXML private TextField project,  base_directory;
    @FXML private Button get_dir, create_project, close, p5_settings;
    @FXML private ComboBox<String> project_type;
    private final String default_path = "C:\\Users\\cndet\\Desktop\\Code\\The Coding Train";

    private Stage dialog_stage;
    private HashMap<String, Configuration> hooks = new HashMap<>();

    @Override
    public void initialize(){
        this.project.textProperty().addListener((observable, oldValue, newValue) -> {
            this.create_project.setDisable(newValue.trim().isEmpty());
        });
        if (new File(this.default_path).exists()) {
            this.base_directory.setText(default_path);
        }
    }

    public void setConfigData(ArrayList<Configuration> configData){
        for (Configuration c: configData) {
            this.project_type.getItems().add(c.getProject_type());
            this.hooks.put(c.getProject_type(), c);
        }
        this.project_type.valueProperty().setValue(configData.get(0).getProject_type());
    }


    @FXML protected void openDirectory(MouseEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = new File(this.base_directory.getText());
        if(dir.exists()){
            directoryChooser.setInitialDirectory(dir);
        }
        final File selectedDir = directoryChooser.showDialog(this.stage);
        if(selectedDir != null){
            this.base_directory.setText(selectedDir.getAbsolutePath());
        }
    }

    @FXML protected void createProject(MouseEvent event){
        String dir = this.base_directory.getText().trim();
        String p = this.project.getText().trim();
        String pType = this.project_type.getValue();
        Configuration c = this.hooks.get(pType);
        File dir_file = new File(dir+File.separator+p);

        dir_file.mkdir();

        this.runHook(dir_file,c.getPre_install());
        ZipFileMethods.saveFolderFromZipobjArrayList(dir_file.getAbsolutePath(), c.getFile());
        this.runHook(dir_file,c.getPost_install());

        ProjectHandler.addProject(
                p,
                dir,
                pType
        );
        this.stage.close();
    }

    @Override
    public void goBack(MouseEvent event) {
        this.stage.close();
    }

    private void runHook(File dir_file, String hook){
        if(hook != null) {
            ArrayList<String[]> lines = this.parseInstallHook(hook);
            for (String[] line : lines) {
                this.runProcess(dir_file, line);
            }
        }
    }
}
