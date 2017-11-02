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
        Path file = getFilePath(image);
        Files.write(file, data);
    }

    public byte[] getDataFromDisk(Image image) throws IOException {
        Path file = getFilePath(image);
        return Files.readAllBytes(file);
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
