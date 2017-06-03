package com.ck.ProjectManager.projecttypes;

import com.ck.ProjectManager.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by cndet on 14/03/2017.
 */
interface P5Methods {
    public void addSketch(String sketch, String url);
    public void addRoute(String sketch, String url);
    public void startServer();
    public void stopServer();

}

public class P5 extends SketchBase implements P5Methods {
    private JsonObject data;
    private String npm, guard;
    private Process guardProcess, nodemonProcess;

    public P5(String project, String path) {
        super(project, path);
        this.project_type = "p5";
        this.npm = this.isWindows() ? "npm.cmd" : "npm";
        this.guard = this.isWindows() ? "guard.bat" : "guard";
    }

    private void editJSON(File file){
        JsonParser parser = new JsonParser();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            this.data = parser.parse(new FileReader(file)).getAsJsonObject();
            this.data.addProperty("name", this.project);
            this.saveFile(file.toPath(), gson.toJson(this.data));
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void createProject() {
        try {
            InputStream inputStream = Main.projectResourceStream("p5projectfiles.zip");
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry entry;
            this.project_dir.toFile().mkdir();
            byte[] buffer = new byte[1024];
            while ((entry = zipInputStream.getNextEntry()) != null){
                String filename = entry.getName();
                String filepath = this.project_dir + File.separator + filename;
                File new_file = new File(filepath);
                if(filepath.charAt(filepath.length()-1) == '/') {
                    new_file.mkdir();
                } else {
                    new_file.getParentFile().mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(new_file);
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0){
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    if(filename.equals("package.json")){
                        this.editJSON(new_file);
                    }
                }
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
            inputStream.close();
            this.runProcess(this.npm, "i", "express", "pug", "--save");
            this.runProcess(this.npm, "i", "nodemon", "--save-dev");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Object runProcess(String... cmd) throws IOException{
        try {
            ProcessBuilder process = new ProcessBuilder(cmd);
            return process.directory(this.project_dir.toFile()).start();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public void addSketch(String sketch, String url) {
        url = (!url.isEmpty()) ? url : sketch;
        Path sketch_path = Paths.get(
                this.project_dir.toString(),
                "static", "src", String.format("%s.js", sketch)
        );

        this.addRoute(sketch, url);
        this.createSketch(sketch_path);
    }


    @Override
    public void addRoute(String sketch, String url) {
        url = (!url.isEmpty()) ? url : sketch;
        String route_template = String.format(
                "route.get('/%s', renderSketch('%s'));",
                url, sketch
        );
        Path route_path = Paths.get(
                this.project_dir.toString(),
                "routes", "routes.js"
        );
        ArrayList<String> lines = this.getFile(route_path);
        lines.add((lines.size()-3), route_template);
        this.saveFile(route_path, lines);
    }

    @Override
    public void startServer() {
        try {
            this.guardProcess = (Process) this.runProcess(
                    this.guard
            );
            this.nodemonProcess = (Process) this.runProcess(
                this.npm, "run", "start"
            );
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stopServer() {
        try {
            String listCmd = this.isWindows() ? "tasklist" : "ps -ef";
            String killCmd = this.isWindows() ? "taskkill /F /PID %s" : "kill %s";
            Process process = Runtime.getRuntime().exec(listCmd);
            Scanner scanner = new Scanner(new InputStreamReader(process.getInputStream()));
            ArrayList<String> pids = new ArrayList<>();
            String t;
            Pattern p = Pattern.compile("(node|ruby)");
            while (scanner.hasNext()) {
                t = scanner.nextLine();
                Matcher m = p.matcher(t);
                if(m.find()){
                    pids.add(t.split("\\s+")[1]);
                }
            }
            scanner.close();

            for (String id : pids) {
                Runtime.getRuntime().exec(String.format(killCmd, id));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
