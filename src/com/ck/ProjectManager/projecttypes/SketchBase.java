package com.ck.ProjectManager.projecttypes;

import com.ck.ProjectManager.Main;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by cndet on 14/03/2017.
 */


abstract class SketchBase {
    protected String project;
    protected Path base_dir;
    public Path project_dir;
    protected String project_type;
    protected String cwd;

    public abstract void createProject();

    public SketchBase(String project, String path) {
        this.project = project;
        this.base_dir = Paths.get(path);
        this.project_dir = Paths.get(path, this.project);
        this.cwd = System.getProperty("user.dir");
    }

    protected ArrayList<String> sketchTemplate(){
        return this.getProjectFile(String.format("conf/%s.txt", this.project_type));
    }


    protected ArrayList<String> getProjectFile(String path)  {
        try {
            InputStream inputStream = Main.projectResourceStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            ArrayList<String> text = new ArrayList<String>();
            String t;
            while((t = br.readLine()) != null){
                text.add(t);
            }
            inputStream.close();
            inputStreamReader.close();
            br.close();

            return text;
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }
    protected ArrayList<String> getFile(Path path)  {
        try {
            FileReader fr = new FileReader(path.toFile());
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> text = new ArrayList<String>();
            String t;
            while((t = br.readLine()) != null){
                text.add(t);
            }
            br.close();
            fr.close();

            return text;
        }catch (IOException e){
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }


    protected void saveFile(Path path, ArrayList<String> lines){
        try {
            File file = path.toFile();
            FileWriter writer = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            for(String s : lines){
                sb.append(s);
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    protected void saveFile(Path path, String data){
        try {
            File file = path.toFile();
            FileWriter writer = new FileWriter(file);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    protected void createSketch(Path path){
        this.saveFile(path, this.sketchTemplate());
    }

    protected boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
