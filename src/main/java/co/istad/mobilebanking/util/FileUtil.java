package co.istad.mobilebanking.util;

import java.util.UUID;

public class FileUtil {

    public static String generateFileName(String originalName){
        String newName= UUID.randomUUID().toString();
        String extension=extractionFileName(extractionFileName(originalName));

          return String.format("%s.%s", newName, extension);
    }

    public static String extractionFileName(String originalFileName){

        int lastDotIndex=originalFileName.lastIndexOf(".");

        return originalFileName.substring(lastDotIndex+1);
    }

}
