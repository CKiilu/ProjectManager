package com.ck.ProjectManager.projecttypes;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cndet on 14/03/2017.
 */
public class Processing extends SketchBase {
    private Path file_path;
    public Processing(String project, String path){
        super(project, path);
        this.project_type = "processing";
        this.file_path = Paths.get(this.project_dir.toString(), String.format("%s.pde", this.project));
    }

    @Override
    public void createProject(){
        this.project_dir.toFile().mkdir();
        this.createSketch(this.file_path);
    }
}
