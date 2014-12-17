package com.libs.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.DirectoryWalker;

public class FileMy {

    public static String readFile(final String file) throws IOException {
        if (file == null || file.equals(""))
            return null;
        return readFile(new File(file));
    }

    public static String readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel channel = fis.getChannel();
        ByteArrayOutputStream os = new ByteArrayOutputStream(0x10000);
        WritableByteChannel wchannel = Channels.newChannel(os);
        channel.transferTo(0, file.length(), wchannel);
        channel.close();
        wchannel.close();
        return os.toString();

//        ByteBuffer buffer = ByteBuffer.allocate(4096);
//        StringBuilder sb = new StringBuilder();
//        while (channel.read(buffer) > 0) {
//            byte[] bytes = buffer.array();
//            sb.append(new String(bytes, 0, buffer.position()));
//            buffer.clear();
//        }
//        return sb.toString();
    }

    public static void writeFile(final String file, final String content) throws IOException {
        if (file == null || file.equals(""))
            return;
        writeFile(new File(file), content);
    }
    
    public static void writeFile(File file, final String content) throws IOException {
        if (!file.exists())
            file.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel channel = fos.getChannel();
        byte[] bytes = content.getBytes();
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ReadableByteChannel rchannel = Channels.newChannel(is);
        channel.transferFrom(rchannel, 0, bytes.length);
        channel.close();
        rchannel.close();
        
//        final int LEN = content.length();
//        int start = 0;
//        while (start < LEN) {
//            int end = Math.min(LEN, start + 4096);
//            String t = content.substring(start, end);
//            ByteBuffer buffer = ByteBuffer.wrap(t.getBytes());
//            start += channel.write(buffer);
//        }
    }

    public static void writeFile(File file, final InputStream content) throws IOException {
        if (!file.exists())
            file.createNewFile();
        
        FileOutputStream fos = new FileOutputStream(file);
        FileChannel channel = fos.getChannel();

        byte[] b = new byte[4096];
        int count = 0;
        while ((count = content.read(b, 0, b.length)) > 0) {
            ByteBuffer buffer = ByteBuffer.wrap(b, 0, count);
            channel.write(buffer);
        }
    }

    public static boolean deleteDir(String directory) {
        return deleteTree(new File(directory), false);
    }

    private static boolean deleteTree(File base, boolean deleteBase) {
        boolean result = true;
        if (base.isDirectory()) {
            for (File child : base.listFiles()) {
                result &= deleteTree(child, true);
            }
        }
        if (deleteBase) {
            result &= base.delete();
        }
        return result;
    }

//    public static void mkdir(String dir) throws IOException, InterruptedException {
////        String cmd[] = {
////            "busybox",
////            "mkdir", "-p", dir
////        };
////        Process process = Runtime.getRuntime().exec(cmd);
////        process.waitFor();
//        File file = new File(dir);
//        if (file != null && !file.exists()) {
//            file.mkdirs();
//        }
//    }

//    public static void cp(String src, String dest) throws IOException, InterruptedException {
//        if (src.endsWith("*")) {
//            copyDirectory(src.substring(0, src.length() - 1), dest);
//            return;
//        }
//
//        File srcFile = new File(src);
//        File destFile = new File(dest);
//        if (srcFile.isFile()) {
//            if (destFile.isFile()) {
//                copyFile(srcFile, destFile);
//            } else {
//                copyFile(srcFile, new File(dest, srcFile.getName()));
//            }
//        } else {
//            String d = dest.endsWith("/") ? dest : dest + "/";
//            File dir = new File(dest + srcFile.getName());
//            if (dir != null && !dir.exists()) {
//                dir.mkdirs();
//            }
//            copyDirectory(src, dir.getPath());
//        }
//    }
    
//    public static void copyFile(File srcFile, File desFile) {
//        if (srcFile.isDirectory()) {
//            desFile.mkdirs();
//        } else {
//            File pf = desFile.getParentFile();
//            if (pf != null && !pf.exists()) {
//                pf.mkdirs();
//            }
//
//            try {
//                FileInputStream inStream = new FileInputStream(srcFile);
//                FileOutputStream outStream = new FileOutputStream(desFile);
//    
//                byte[] buffer = new byte[1024];
//                int length = -1;
//                while ((length = inStream.read(buffer)) != -1) {
//                    outStream.write(buffer, 0, length);
//                }
//                outStream.close();
//                inStream.close();
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void copyFile(String src, String des) {
//        File srcFile = new File(src);
//        if (srcFile == null) {
//            return;
//        }
//        File desFile = new File(des);
//        if (desFile == null) {
//            return;
//        }
//        copyFile(srcFile, desFile);
//    }
//    
//    public static void copyDirectory(String src, String des) {
//        ArrayList<File> files = new ArrayList<File>();
//
//        parseDirectory(src, files);
//
//        if (!files.isEmpty()) {
//            files.remove(0);
//        }
//
//        for (File f : files) {
//            String d = des.endsWith("/") ? des : des + "/";
//            String path = d + f.getPath().substring(src.length());
//            copyFile(f, new File(path));
//        }
//    }
    
//    public static void cp(String src, String dest) throws IOException, InterruptedException {
//        String cmd[] = {
//            "sh",
//            "-c",
//            "cp -r " + src + " " + dest
//        };
//        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
//    }

//    public static void rm(String file) throws IOException, InterruptedException {
//        String cmd[] = {
//            "sh",
//            "-c",
//            "rm -r " + file
//        };
//        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
//    }

//    private static void parseDirectory(String dir, ArrayList<File> files) throws SecurityException {
//        File file = new File(dir);
//
//        if (file.isFile()) {
//            file.delete();
//            return;
//        }
//
//        int index = 0;
//        files.add(file);
//        while (files.size() > index) {
//            if (files.get(index).isFile()) {
//                index++;
//                continue;
//            }
//            File[] fs = files.get(index).listFiles();
//            if (fs != null) {
//                int len = fs.length;
//                for (int i = 0; i < len; ++i) {
//                    files.add(fs[i]);
//                }
//            }
//            index++;
//        }
//    }

//    // delete all files and sub directories, exclude itself
//    public static void rm(String name) throws SecurityException {
//        ArrayList<File> files = new ArrayList<File>();
//
//        parseDirectory(name, files);
//
//        int len = files.size();
//        for (int i = len - 1; i > 0; --i) {
//            if (!files.get(i).delete()) {
//                return;
//            }
//        }
//    }

//    public static void rmAll(String name) throws SecurityException {
//        rm(name);
//        new File(name).delete();
//    }
//
//    public static void rmFile(String name) throws SecurityException {
//        new File(name).delete();
//    }

//    public static void chmod(String file, String mode) throws IOException, InterruptedException {
//        ArrayList<File> files = new ArrayList<File>();
//
//        parseDirectory(file, files);
//
//        for (File f : files) {
//            chmod(f, mode);
//        }
//        File dir = new File(file);
//
//        if (dir.isDirectory()) {
//            File[] fs = dir.listFiles();
//            if (fs != null) {
//                int len = fs.length;
//                for (int i = 0; i < len; ++i) {
//                    chmod(fs[i], mode);
//                }
//            }
//        }
//
//        String cmd[] = {
//            "busybox",
//            "chmod", mode, file
//        };
//        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
//    }

    private static void chmod(ArrayList<String> path, String mode) {
        try {
            ArrayList<String> cmd = new ArrayList<String>();
            cmd.add("chmod");
            cmd.add(mode);
            cmd.addAll(path);
            String[] progArray = new String[cmd.size()];
            cmd.toArray(progArray);
            Process process = Runtime.getRuntime().exec(progArray);
            process.waitFor();
        } catch (IOException e) {
            Utils.handleException(e);
        } catch (InterruptedException e) {
            Utils.handleException(e);
        }
    }
    
    private static class MyDirectoryWalker extends DirectoryWalker {

        @Override
        protected boolean handleDirectory(File directory, int depth, Collection results)
                throws IOException {
            results.add(directory.getAbsolutePath());
            return true;
        }
        
        public void go(File startDirectory, Collection results) {
            try {
                walk(startDirectory, results);
            } catch (IOException e) {
                Utils.handleException(e);
            }
        }
        
    }
    
    public static void chmod(File file, String mode) throws IOException, InterruptedException {
        MyDirectoryWalker walker = new MyDirectoryWalker();
        ArrayList<String> dirlist = new ArrayList<String>();
        walker.go(file, dirlist);
        for (String dir : dirlist) {
            ArrayList<String> filelist = new ArrayList<String>();
            filelist.add(dir);
            String[] files = new File(dir).list();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    filelist.add(dir + File.separator + files[i]);
                }
            }
            chmod(filelist, mode);
        }
    }

//    public static void chmod(File file, String mode) throws IOException, InterruptedException {
//        String path = file.getPath();
//        String cmd[] = {
//            "busybox",
//            "chmod", "-R", mode, path
//        };
//        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
//    }

//    public static void unzip(String file, String dest) throws IOException, InterruptedException {
//        String cmd[] = {
//            "busybox",
//            "unzip", "-o", file, "-d", dest
//        };
//        Process process = Runtime.getRuntime().exec(cmd);
//        process.waitFor();
//    }

}
