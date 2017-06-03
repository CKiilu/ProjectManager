package com.ck.ProjectManager.views;

import com.ck.ProjectManager.database.handlers.ConfigurationHandler;
import com.ck.ProjectManager.database.helpers.ZipFileMethods;
import com.ck.ProjectManager.database.helpers.ZipObj;
import com.ck.ProjectManager.views.dialogs.HookHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.util.ArrayList;

/**
 * Created by cndet on 15/03/2017.
 */
public class EditConfigController extends MasterController {
    @FXML private Label project_type;
    @FXML private ListView<String> file_paths;
    @FXML private TabPane editor_tab_pane;

    private ObservableList<String> filepathlist;
    private ObservableList<ZipObj> filepath_zipobjs;
    private ObservableList<String> editor_tabs = FXCollections.observableArrayList();
    private String project_type_text;
    private String post_install = null, pre_install = null;


    @Override
    public void initialize() {
        this.file_paths.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    String val = observable.getValue();
                    if(this.editor_tabs.indexOf(newValue) == -1  && val.charAt(val.length()-1) != '/') {
                        TextArea area = new TextArea(this.getSelectedListViewContents());
                        area.setOnKeyReleased(this::keyPressInTextArea);
                        Tab t = new Tab(newValue, area);
                        t.setOnCloseRequest(this::onCloseTab);
                        this.editor_tab_pane.getTabs().add(t);
                        this.editor_tab_pane.getSelectionModel().select(t);
                        this.editor_tabs.add(newValue);
                    }
                }
        );
    }

    public void zipSetup(String project_type, ObservableList<ZipObj> entries, String pre_install, String post_install){
        this.pre_install = pre_install;
        this.post_install = post_install;
        this.filepathlist = FXCollections.observableArrayList();
        this.filepath_zipobjs = FXCollections.observableArrayList(entries);
        this.project_type.setText(project_type);
        this.project_type_text = project_type;
        for (ZipObj z : entries){
            this.filepathlist.add(z.getRel_path());
        }
        this.file_paths.setItems(this.filepathlist);
    }

    private String getSelectedListViewContents(){
        return this.filepath_zipobjs.get(this.file_paths.getSelectionModel().getSelectedIndex()).getFile_contents();
    }

    private String getSelectedTabContents(){
        return this.filepath_zipobjs.get(this.filepathlist.indexOf(this.editor_tab_pane.getSelectionModel().getSelectedItem().getText())).getFile_contents();
    }

    private void saveSelectedTabContents (String text){
        this.filepath_zipobjs.get(this.filepathlist.indexOf(this.editor_tab_pane.getSelectionModel().getSelectedItem().getText())).setFile_contents(text);
        ConfigurationHandler.editConfig(this.project_type_text, ZipFileMethods.zipByteArrayFromArraylist(new ArrayList<>(this.filepath_zipobjs)));
    }

    private void onCloseTab(Event event){
        Tab tab = (Tab) event.getSource();
        this.editor_tabs.remove(tab.getText());
        this.file_paths.getSelectionModel().clearSelection();
    }

    @FXML private void keyPressInTextArea(KeyEvent event){
        TextArea area = (TextArea) event.getSource();
        if(event.isControlDown()){
            if (event.getCode() == KeyCode.S) {
                saveSelectedTabContents(area.getText());
            }
            if(event.getCode() == KeyCode.W) {
                this.editor_tab_pane.getTabs().remove(this.editor_tab_pane.getSelectionModel().getSelectedIndex());
            }
        } else {
            System.out.println(this.getSelectedTabContents().equals(area.getText()));
        }
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
