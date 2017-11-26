package no.ntnu.kore.safespace.service;

import no.ntnu.kore.safespace.entity.Image;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Class that handles storage and retrieval of image data.
 * @author robert
 */
@Service
public class ImageService {

    private final String PATH = "images";


    /**
     * Saves the image data to disk
     * @param image information about image that data belongs to
     * @param data data to be saved
     * @throws IOException error during file operations.
     */
    public void saveToDisk(Image image, byte[] data) throws IOException {
        Path file = getFilePath(image);
        if (!imageDirectoryExists()) {
            Files.createDirectory(Paths.get("", PATH));
        }
        Files.write(file, data);
    }

    /**
     * Saves b64 data to disk
     * @param image image the data belongs to
     * @param b64String image data as b64 string
     * @throws IOException error during file operations.
     */
    public void saveB64ToDisk(Image image, String b64String) throws IOException {
        String[] strings = b64String.split(",");
        byte[] data = null;
        if (strings.length == 1) {
            data = Base64.getDecoder().decode(strings[0]);
        } else {
            data = Base64.getDecoder().decode(strings[1]);
        }
        saveToDisk(image, data);
    }

    /**
     * Reads data from disk
     * @param image image the data belongs to
     * @return image data as byte array
     * @throws IOException error during file operations
     */
    public byte[] getDataFromDisk(Image image) throws IOException {
        Path file = getFilePath(image);
        return Files.readAllBytes(file);
    }

    /**
     * Reads data from disk and encodes it as base64
     * @param image image the data belongs to
     * @return image data as a base64 string
     * @throws IOException error during file operations
     */
    public String getB64FromDisk(Image image) throws IOException {
        byte[] data = getDataFromDisk(image);
        return Base64.getEncoder().encodeToString(data);
    }

    private Path getFilePath(Image image){
        return Paths.get("", getFilePathString(image).split("/"));
    }

    private String getFilePathString(Image image){
        return PATH + File.separatorChar + getImageFileName(image);
    }

    private String getImageFileName(Image image) {
        return image.getName() + "." + image.getFileExtension();
    }

    private boolean imageDirectoryExists(){
        Path imagePath = Paths.get("", PATH);
        return Files.exists(imagePath);
    }

}
