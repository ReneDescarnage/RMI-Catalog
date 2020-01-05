package client;


import common.FileDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtility {

    private static final String root = "./localFiles";
    private static Path workingDir = Paths.get(root);

    public int readFile(String filename){
        Path path = workingDir.resolve(Paths.get(filename));
        File file = new File(path.toString());
        return (int)file.length();
    }

}
