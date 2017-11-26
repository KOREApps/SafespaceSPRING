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
import java.util.UUID;

/**
 * Controller for images. Can handle requests for image info and image data separately.
 * @author robert
 */
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

    /**
     * Returns info about all images
     * @return info about all images
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getAll(){
        List<Image> images = imageRepository.findAll();
        return new ResponseEntity<>(images, HttpStatus.OK);
    }

    /**
     * Returns info about one image. If RequestParam/QueryParam "withData" is set to true the images data will be
     * included in the image object.
     * @param id id of the image
     * @param withData boolean to indicate if data should be retrieved or not.
     * @return ResponseEntity containing an image object
     */
    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity getOne(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "data", defaultValue = "false") boolean withData) {
        Image image = imageRepository.findOne(id);
        if (withData) {
            image.setData(getB64DataFromDisk(image));
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Adds a new image. Checks if image contains data.
     * @param image image object to add
     * @return ResponseEntity containing info about the newly added image and code 200 OK, if an error occured the
     * response entity will return code 500 INTERNAL_SERVER_ERROR
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody Image image) {
        if (image != null && (image.getName() == null || image.getName().equals(""))) {
            image.setName(UUID.randomUUID().toString());
        }
        image = imageRepository.save(image);
        if (image.getData() != null || image.getData().equals("")) {
            boolean success = saveImageToDisk(image);
            if (!success) {
                return new ResponseEntity<>(image, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    /**
     * Updates an existing image object
     * @param id id of existing image to update
     * @param image the new image update to update the old
     * @return ResponseEntity containing the updated image object and code 200 OK, if an error occured the response entity
     * will have code 400 BAD_REQUEST
     */
    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity update(Long id, @RequestBody Image image) {
        if (imageRepository.exists(id)) {
            image = imageRepository.save(image);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(image, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adds given image data to an existing image object
     * @param id id of image to add data to
     * @param data data to add
     * @return ResponseEntity containing the image object the data was added to with code 200 OK, if an error occured
     * code 500 INTERNAL_SERVER_ERROR will be returned.
     */
    @RequestMapping(path = "data/{id}", method = RequestMethod.POST)
    public ResponseEntity addImageData(@PathVariable(value = "id") Long id, @RequestBody byte data[]){
        Image image = imageRepository.findOne(id);
        boolean success = saveImageToDisk(image, data);
        if (success) {
            return new ResponseEntity<>(image, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(image, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns the image data for the image object with the given id
     * @param id id of the image object to retrieve data for
     * @return data of the image with given id. Media type depends on image info
     */
    @RequestMapping(path = "data/{id}", method = RequestMethod.GET)
    public ResponseEntity getImageData(@PathVariable(value = "id") Long id) {
        Image image = imageRepository.findOne(id);
        try {
            byte[] data = imageService.getDataFromDisk(image);
            return ResponseEntity.ok().contentType(getMediaType(image)).body(data);
        } catch (IOException ex) {
            LOG.warn("Failed to read image from disk", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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

    /**
     * Returns the media type of image. If none is found BYTE_TYPE is returned.
     * @param image
     * @return
     */
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

