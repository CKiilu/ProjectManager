package com.ck.ProjectManager;

import com.ck.ProjectManager.database.helpers.ZipFileMethods;

import java.io.*;

/**
 * Created by cndet on 18/03/2017.
 */
public class Test {
    public static void main(String... args) throws IOException {
        String dir = "C:\\Users\\cndet\\Desktop\\Code\\OldScripts";
        String ext_path = dir+File.separator+"test";
        String zip_path = dir+File.separator+"test.zip";
//        ZipFileMethods.archiveFolderToZip(dir,zip_path);
//        ZipFileMethods.saveZipFileFromStream(zip_path, ZipFileMethods.getZipByteStreamFromFolder(dir));
//        ZipFileMethods.saveFolderFromZipobjArrayList(ext_path,ZipFileMethods.readZipFromStream(ZipFileMethods.getZipByteStreamFromFolder(dir)));
//
//        ZipFileMethods.saveFolderFromZipobjArrayList(ext_path,ZipFileMethods.readZipFromStream(new ByteArrayInputStream(ZipFileMethods.getZipByteArrayFromFolder(dir))));
    }
}
