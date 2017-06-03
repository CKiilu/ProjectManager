package com.ck.ProjectManager.database.helpers;

/**
 * Created by cndet on 16/03/2017.
 */
public class ZipObj {
    private String rel_path, file_contents;

    public ZipObj(String rel_path, String file_contents) {
        this.rel_path = rel_path;
        this.file_contents = file_contents;
    }

    public String getRel_path() {
        return rel_path;
    }

    public void setRel_path(String rel_path) {
        this.rel_path = rel_path;
    }

    public String getFile_contents() {
        return file_contents;
    }

    public void setFile_contents(String file_contents) {
        this.file_contents = file_contents;
    }
}
