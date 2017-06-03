package com.ck.ProjectManager.views;

import com.ck.ProjectManager.projecttypes.P5;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Created by cndet on 14/03/2017.
 */
public class P5WindowController extends MasterController {
    @FXML private TextField url, sketch;

    private String project, base_directory;

    public void setDefaults(P5 p5, Stage stage, Scene currentScene, Scene previous, String project, String base_directory){
        this.p5 = p5;
        this.stage = stage;
        this.currentScene = currentScene;
        this.previousScene = previous;
        this.project = project;
        this.base_directory = base_directory;
    }

    @FXML protected void runServer(MouseEvent event){
        this.p5.startServer();
        this.defaultInfo("Started a server in %s");
    }

    @FXML protected void stopServer(MouseEvent event){
        this.p5.stopServer();
        this.defaultInfo("Stopped a server in %s");
    }

    @FXML protected void returnToMain(MouseEvent event){
//        try {
//            FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/create_project.fxml"));
//            Parent root = loader.load();
//            CreateProjectController c = loader.getController();
//            Scene scene = new Scene(root, this.currentScene.getWidth(), this.currentScene.getHeight());
//            c.setDefaults(this.stage, scene);
//            c.setFieldValues(this.project,this.base_directory);
            this.stage.setScene(this.previousScene);
            this.stage.show();
//        } catch (IOException e){
//            e.printStackTrace();
//        }
    }

    @FXML protected void makeSketch(MouseEvent event){
        this.p5.addSketch(this.sketch.getText().trim(), this.url.getText().trim());
        this.defaultInfo("Created a sketch in %s");
    }
    @FXML protected void makeRoute(MouseEvent event){
        this.p5.addRoute(this.sketch.getText().trim(), this.url.getText().trim());
        this.defaultInfo("Created a route in %s");

    }

    @Override
    public void initialize() {

    }
}
