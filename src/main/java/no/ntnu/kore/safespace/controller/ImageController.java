package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Image;
import no.ntnu.kore.safespace.repository.ImageRepository;
import no.ntnu.kore.safespace.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("images")
public class ImageController {

    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

    private static final MediaType JPEG_TYPE = MediaType.IMAGE_JPEG;
    private static final MediaType PNG_TYPE = MediaType.IMAGE_PNG;
    private static final MediaType BYTE_TYPE = MediaType.APPLICATION_OCTET_STREAM;

    private ImageRepository imageRepository;
    private ImageService imageService;

    @Autowired
    public ImageController(ImageRepository imageRepository, ImageService imageService) {
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Image>> getAll(){
        List<Image> images = imageRepository.findAll();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<Image> getOne(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "data", defaultValue = "false") boolean withData) {
        Image image = imageRepository.findOne(id);
        if (withData) {
            image.setData(getB64DataFromDisk(image));
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Image> add(@RequestBody Image image) {
        image = imageRepository.save(image);
        if (image.getData() != null || image.getData().equals("")) {
            boolean success = saveImageToDisk(image);
            if (!success) {
                return new ResponseEntity<>(image, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Image> update(Long id, @RequestBody Image image) {
        if (imageRepository.exists(id)) {
            image = imageRepository.save(image);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(image, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "data/{id}", method = RequestMethod.POST)
    public ResponseEntity<Image> addImageData(@PathVariable(value = "id") Long id, @RequestBody byte data[]){
        Image image = imageRepository.findOne(id);
        boolean success = saveImageToDisk(image, data);
        if (success) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(image, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "data/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getImageData(@PathVariable(value = "id") Long id) {
        Image image = imageRepository.findOne(id);
        try {
            byte[] data = imageService.getDataFromDisk(image);
            return ResponseEntity.ok().contentType(getMediaType(image)).body(data);
        } catch (IOException ex) {
            LOG.warn("Failed to read image from disk", ex);
            return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean saveImageToDisk(Image image){
        try {
            imageService.saveB64ToDisk(image, image.getData());
            return true;
        } catch (IOException ex) {
            LOG.warn("Failed to save image to disk", ex);
            return false;
        }
    }

    private boolean saveImageToDisk(Image image, byte[] data) {
        try {
            imageService.saveToDisk(image, data);
            return true;
        } catch (IOException ex) {
            LOG.warn("Failed to save image to disk", ex);
            return false;
        }
    }

    private String getB64DataFromDisk(Image image){
        try {
            return imageService.getB64FromDisk(image);
        } catch (IOException ex) {
            LOG.warn("Failed to fetch image from disk", ex);
            return "";
        }
    }

    private MediaType getMediaType(Image image) {
        if (image.getFileExtension().equals("jpg") || image.getFileExtension().equals("jpeg")) {
            return JPEG_TYPE;
        } else if (image.getFileExtension().equals("png")) {
            return PNG_TYPE;
        } else {
            return BYTE_TYPE;
        }
    }


}

