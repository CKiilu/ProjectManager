package com.ck.ProjectManager.views.dialogs;

import com.ck.ProjectManager.Main;
import com.ck.ProjectManager.database.handlers.ConfigurationHandler;
import com.ck.ProjectManager.database.handlers.ProjectHandler;
import com.ck.ProjectManager.database.helpers.ZipFileMethods;
import com.ck.ProjectManager.table_properties.ConfigTableProperty;
import com.ck.ProjectManager.views.EditConfigController;
import com.ck.ProjectManager.views.MasterController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by cndet on 16/03/2017.
 */
public class AddConfigController extends MasterController{
    @FXML private TextField project_type,  path;
    @FXML private Button  create_config, get_dir;
    @FXML private ToggleGroup cType;
    @FXML private CheckBox pre, post;
    private final String default_path = "C:\\Users\\cndet\\Desktop\\Code\\The Coding Train";
    private String post_install = null, pre_install = null;

    @Override
    public void initialize(){
        this.project_type.textProperty().addListener((observable, oldValue, newValue) -> {
            this.create_config.setDisable(newValue.trim().isEmpty());
        });
        this.path.textProperty().addListener((observable, oldValue, newValue) -> {
            this.create_config.setDisable(newValue.trim().isEmpty());
        });
        this.cType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton rb = (RadioButton)this.cType.getSelectedToggle();
            switch (rb.getText()){
                case "Directory":
                    this.get_dir.setOnMouseClicked(this::getDirectory);
                    break;
                case "Zip File":
                    this.get_dir.setOnMouseClicked(this::getZipPath);
                    break;
            }
        });
        if (new File(this.default_path).exists()) {
            this.path.setText(default_path);
        }
    }


    @FXML protected void getDirectory(MouseEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File dir = new File(this.path.getText());
        if(dir.exists()){
            directoryChooser.setInitialDirectory(dir);
        }
        final File selectedDir = directoryChooser.showDialog(this.stage);
        if(selectedDir != null){
            this.path.setText(selectedDir.getAbsolutePath());
        }
    }

    @FXML protected void getZipPath(MouseEvent event){
        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip Files", "*.zip"));
        File dir = new File(this.path.getText());
        if(dir.exists()){
            fileChooser.setInitialDirectory(dir);
        }
        final File selectedDir = fileChooser.showOpenDialog(this.stage);
        if(selectedDir != null){
            this.path.setText(selectedDir.getAbsolutePath());
        }
    }

    @FXML protected void createConfiguration(MouseEvent event){
        RadioButton rb = (RadioButton) cType.getSelectedToggle();
        String dir = this.path.getText().trim();
        String type = this.project_type.getText().trim();
        byte[] file = new byte[0];
        try {
            switch (rb.getText()) {
                case "Directory":
                    file = ZipFileMethods.getZipByteArrayFromFolder(dir);
                    break;
                case "Zip File":
                    file = IOUtils.toByteArray(new FileInputStream(dir));
                    break;
            }
            ConfigurationHandler.addConfig(
                    type,
                    file,
                    (this.pre.isSelected()) ? this.pre_install : null,
                    (this.post.isSelected()) ? this.post_install : null
            );
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            this.stage.close();
        }
    }

    @Override
    public void goBack(MouseEvent event) {
        this.stage.close();
    }

    @FXML private void openPreInstall(ActionEvent event){
        CheckBox c = (CheckBox) event.getSource();
        if(c.isSelected())
            this.pre_install = HookHelper.getInstallHook(this.pre_install, stage);
    }

    @FXML private void openPostInstall(ActionEvent event){
        CheckBox c = (CheckBox) event.getSource();
        if(c.isSelected())
            this.post_install = HookHelper.getInstallHook(this.post_install, stage);
    }
}
