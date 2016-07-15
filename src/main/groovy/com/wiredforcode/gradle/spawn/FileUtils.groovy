package com.wiredforcode.gradle.spawn

public class FileUtils {

    public static void writeToFile(File file, String data, boolean append) throws IOException {
        if (file == null) return
        if (!file.exists()) {
            file.createNewFile()
        }
        FileWriter writer = new FileWriter(file, append)
        writer.write(data + "\n")
        writer.close()
    }

    public static void deleteCompletely(File inputFile) {
        if (inputFile != null && inputFile.exists()) {
            inputFile.delete()
        }
    }

}