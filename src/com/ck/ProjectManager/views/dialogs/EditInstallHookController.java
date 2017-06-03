package com.ck.ProjectManager.views.dialogs;

import com.ck.ProjectManager.views.MasterController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

/**
 * Created by cndet on 18/03/2017.
 */
public class EditInstallHookController extends MasterController {
    @FXML private TextArea hook_text;
    @FXML private Button confirm_hook;
    private boolean returnData = false;

    @Override
    public void initialize(){
        this.hook_text.textProperty().addListener((observable, oldValue, newValue) -> {
            this.confirm_hook.setDisable(newValue.isEmpty());
        });
    }

    public void setHookText(String text){
        text = (text != null) ? text : "";
        this.hook_text.setText(text);
    }

    @FXML
    private void finish(MouseEvent event){
        this.returnData = true;
        this.stage.close();
    }

    @Override
    public void goBack(MouseEvent event) {
        this.stage.close();
    }

    public String getHook(){
        return (this.returnData) ?
                this.hook_text.getText().trim() :
                null;
    }
}
