package com.ck.ProjectManager.views.dialogs;

import com.ck.ProjectManager.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by cndet on 18/03/2017.
 */
public class HookHelper {
    public static String getInstallHook(String text, Stage stage){
        Stage dialog_stage = new Stage();
        dialog_stage.setTitle("Enter your install hook");
        dialog_stage.initModality(Modality.WINDOW_MODAL);
        dialog_stage.initOwner(stage);
        FXMLLoader loader = new FXMLLoader(Main.projectResourcePath("views/dialogs/edit_install_hook.fxml"));
        try {
            Scene dialog_scene = new Scene(loader.load());
            dialog_stage.setScene(dialog_scene);
            EditInstallHookController controller = loader.getController();
            controller.setDefaults(dialog_stage, dialog_scene);
            controller.setHookText(text);
            dialog_stage.showAndWait();
            return controller.getHook();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
