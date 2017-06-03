package com.ck.ProjectManager.database.helpers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.input.ReaderInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by cndet on 15/03/2017.
 */
public class ZipFileMethods {
    public static void archiveFolderToZip(String base_path, String zip_path) throws IOException {
        File dir = new File(base_path);
//        Iterator<File> files = FileUtils.iterateFilesAndDirs(dir, TrueFileFilter.TRUE, TrueFileFilter.TRUE);
        ArrayList<String> files = listSubFiles(dir,new ArrayList<>());
//        Iterator<File> files = FileUtils.iterateFiles(dir,null,true);
        FileOutputStream fos = new FileOutputStream(zip_path);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (String f : files){
            File file = new File(f);
            addToZipFile(file, base_path, zos);
        }
//        while (files.hasNext()){
//            File f = files.next();
//            addToZipFile(f, base_path, zos);
//        }
        zos.close();
        fos.close();
    }

    private static void addToZipFile(File file, String base_path, ZipOutputStream zos) throws IOException {
        String rel_path = file.getPath().replace(base_path+File.separator,"");
        System.out.println("Writing '" + rel_path + "' to zip file");

        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(rel_path);
        zos.putNextEntry(zipEntry);
        int buffer = 1024;
        byte[] bytes = new byte[buffer];
        int length;
        while ((length = fis.read(bytes,0,buffer)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }


    public static byte[] getZipByteArrayFromFolder(String base_path) throws IOException {
        File dir = new File(base_path);
        Iterator<File> files = FileUtils.iterateFiles(dir,null,true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos);
        while (files.hasNext()){
            File f = files.next();
            addToZipFile(f, base_path, zos);
        }
        zos.close();
        bos.close();
        return bos.toByteArray();
    }

    public static ByteArrayInputStream getZipByteStreamFromFolder(String base_path) throws IOException {
        return new ByteArrayInputStream(ZipFileMethods.getZipByteArrayFromFolder(base_path));
    }

    public static byte[] zipByteArrayFromArraylist(ArrayList<ZipObj> arrayList){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
            for (ZipObj zipObj : arrayList) {
                zipOutputStream.putNextEntry(new ZipEntry(zipObj.getRel_path()));
                zipOutputStream.write(zipObj.getFile_contents().getBytes());
                zipOutputStream.closeEntry();
            }
            zipOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return new byte[0];
    }


    public static void saveFolderFromZipobjArrayList(String dir, ArrayList<ZipObj> list){
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(ZipFileMethods.zipByteArrayFromArraylist(list));
            ZipInputStream zis = new ZipInputStream(bis);
            FileOutputStream fileOutputStream = null;
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                int len;
                File f = new File(dir + File.separator + entry.getName());
                new File(f.getParent()).mkdirs();
                fileOutputStream = new FileOutputStream(f);

                while ((len = zis.read(buffer)) > 0) {
                    fileOutputStream.write(buffer,0,len);
                }
                zis.closeEntry();
            }
            zis.close();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
//            for(ZipObj z : list){
//                String path = z.getRel_path();
//                File f = new File(dir+File.separator+path);
//                if(path.charAt(path.length()-1)=='/'){
//                    f.mkdirs();
//                    f.mkdir();
//                }else {
//                    f.getParentFile().mkdirs();
//                    f.createNewFile();
////                    System.out.println(z.getFile_contents());
////                    FileUtils.write(f, f.get);
////                    System.out.println(z.getFile_contents());
//                    FileUtils.write(f, z.getFile_contents().trim(), Charset.defaultCharset());
////                    Files.write(f.toPath(), z.getFile_contents().getBytes());
////                    FileUtils.writeByteArrayToFile(f, z.getFile_contents().getBytes());
//                }
//            }
        } catch (IOException | NullPointerException e){
            e.printStackTrace();
        }
    }

    public static void saveZipFileFromStream(String file_path, ByteArrayInputStream input){
        try {
            ZipInputStream zis = new ZipInputStream(input);
            FileOutputStream fos = new FileOutputStream(file_path);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry entry;
            byte[] buffer = new byte[1024];
            while ((entry = zis.getNextEntry()) != null) {
                int len;
                zos.putNextEntry(entry);
                while ((len = zis.read(buffer)) > 0) {
                    zos.write(buffer,0,len);
                }
                zos.closeEntry();
            }
            zos.close();
            fos.close();
            zis.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public static ArrayList<ZipObj> readZipFromStream(InputStream input)  {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(input);
            ZipEntry entry;
            ArrayList<ZipObj> zipfiledata = new ArrayList<>();

            while ((entry = zipInputStream.getNextEntry()) != null) {
                int len;
                StringBuilder sb = new StringBuilder();
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) > 0) {
                    sb.append(new String(buffer));
                }
                zipfiledata.add(new ZipObj(entry.getName(), sb.toString()));
            }
            return zipfiledata;
        } catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static ArrayList<String> listSubFiles(File file, ArrayList<String> filepaths){
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files){
                listSubFiles(f, filepaths);
            }
        } else {
            filepaths.add(file.getAbsolutePath());
        }
        return filepaths;
    }
}
