package no.ntnu.kore.safespace.service;

import no.ntnu.kore.safespace.entity.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

@Service
public class ImageService {

    @Value("${image.dir}")
    private String path;


    public void saveToDisk(Image image, byte[] data) throws IOException {
        String filePath = getFilePathString(image);
        Path file = getFilePath(image);
        System.out.println(file.toUri());
        System.out.println(file.getFileName());
        Files.write(file, data);
    }

    public void getDataFromDisk(Image image) throws IOException {
        Path file = getFilePath(image);
    }

    private Path getFilePath(Image image){
        return Paths.get("/", getFilePathString(image).split("/"));
    }

    private String getFilePathString(Image image){
        return path + getImageFileName(image);
    }

    private String getImageFileName(Image image) {
        return image.getName() + "." + image.getFileExtension();
    }

}
