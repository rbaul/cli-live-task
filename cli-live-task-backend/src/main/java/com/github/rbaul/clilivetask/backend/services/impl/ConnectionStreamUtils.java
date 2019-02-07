package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.services.WriteFileListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Arrays;

@Slf4j
public class ConnectionStreamUtils {

    private static long timeout = 10000; // [msec]

    public static String read(InputStream inputStream){
        return readUntil(inputStream, null,null, null);
    }

    public static String read(InputStream inputStream, FileOutputStream fileOutputStream, WriteFileListener writeFileListener){
        return readUntil(inputStream, fileOutputStream,null, writeFileListener);
    }

    public static String readUntil(InputStream inputStream, String searchStr){
        return readUntil(inputStream, null,searchStr, null);
    }

    public static String readUntil(InputStream inputStream, FileOutputStream fileOutputStream, String searchStr, WriteFileListener writeFileListener){
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;

        try{
//            Thread.sleep(3000); // wait 3 seconds for input.
            long start = System.currentTimeMillis();

            while (System.currentTimeMillis() - start < timeout && length != -1) {
                while ((inputStream.available() > 0) && (length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);

                    // Write to file
                    if (fileOutputStream != null) {
                        fileOutputStream.write(buffer, 0, length);
                        if (writeFileListener != null) {
                            writeFileListener.writeFileUpdate(new String(Arrays.copyOfRange(buffer, 0, length)));
                        }
                    }

                    // Search string
                    if (!StringUtils.isEmpty(searchStr) && result.toString().contains(searchStr)) {
                        return result.toString();
                    }
                    start = System.currentTimeMillis();
                }
            }

        } catch (IOException e){
            log.error("Can't read/write stream", e);
        }

        return result.toString();
    }

    public static void write(OutputStream outputStream, String writeString){
        PrintStream printStream = new PrintStream(outputStream);
        printStream.println(writeString);
        printStream.flush();
    }

}
