package com.libs.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zip {
    private static final String TAG = Zip.class.getSimpleName();
    private static final int BUFFER = 0x10000;

    public static void unzip(String zipFile, String targetDir) throws IOException {
        String strEntry;
        BufferedOutputStream dest = null;
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry;

        while ((entry = zis.getNextEntry()) != null) {
            LogUtil.d(TAG, "Unzip: " + "=" + entry);
            int count;
            byte data[] = new byte[BUFFER];
            strEntry = entry.getName();

            File entryFile = new File(targetDir, strEntry);
            if (entry.isDirectory()) {
                entryFile.mkdirs();
            }
            else {
                File entryDir = new File(entryFile.getParent());
                if (!entryDir.exists()) {
                    entryDir.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(entryFile);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
        }
        zis.close();
    }

    public static byte[] unGZIP(byte[] zipped) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(zipped);
        GZIPInputStream zipIn = new GZIPInputStream(in);
        byte[] orginal = new byte[BUFFER];
        int len = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = zipIn.read(orginal)) > 0) {
            out.write(orginal, 0, len);
        }
        out.flush();
        out.close();
        zipIn.close();
        in.close();
        return out.toByteArray();
    }

    public static byte[] gzip(byte[] content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream zipOut;
        zipOut = new GZIPOutputStream(out);
        zipOut.write(content);
        zipOut.finish();
        zipOut.close();
        out.close();
        return out.toByteArray();
    }
    
}
