package com.ck.ProjectManager.views;

import com.ck.ProjectManager.projecttypes.P5;
import com.ck.ProjectManager.projecttypes.Processing;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cndet on 14/03/2017.
 */
public abstract class MasterController {
    protected Stage stage;
    protected P5 p5;
    protected Processing processing;
    protected Scene currentScene, previousScene;
    public abstract void initialize();


    public void setDefaults(Stage stage, Scene scene) {
        this.stage = stage;
        this.currentScene = scene;
    }
    public void setDefaults(Stage stage, Scene scene, Scene previous) {
        this.stage = stage;
        this.currentScene = scene;
        this.previousScene = previous;
    }

    @FXML
    protected void closeWindow(MouseEvent event){
        this.stage.close();
    }

    protected void alertInfo(String header, String content){
        Alert  alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SketchBase Manager");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.show();
    }

    public void goBack(MouseEvent event){
        this.stage.setScene(this.previousScene);
        this.stage.show();
    }

    protected void defaultInfo(String prefix){
        this.alertInfo("Info", String.format(prefix, this.p5.project_dir));
    }

    public ArrayList<String[]> parseInstallHook(String text){
        String[] lines = text.split("\n");
        Pattern p = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        ArrayList<String[]> commands = new ArrayList<>();
        for (String line : lines){
            Matcher m = p.matcher(line);
            ArrayList<String> s = new ArrayList<>();
            while (m.find()){
                s.add(m.group());
            }
            commands.add(s.toArray(new String[s.size()]));
        }
        return commands;
    }


    public void runProcess(File dir, String[] cmd) {
        try {
            ProcessBuilder process = new ProcessBuilder(cmd);
            process.directory(dir).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
