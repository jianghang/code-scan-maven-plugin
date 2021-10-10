package com.github.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static void getAllFile(Path filePath, List<Path> filePathList) throws IOException {
        if (Files.isDirectory(filePath)) {
            List<Path> pathList = Files.list(filePath).collect(Collectors.toList());
            for (Path path : pathList) {
                getAllFile(path, filePathList);
            }
        } else {
            filePathList.add(filePath);
        }
    }
}
