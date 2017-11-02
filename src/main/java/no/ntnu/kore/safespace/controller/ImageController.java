package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Image;
import no.ntnu.kore.safespace.repository.ImageRepository;
import no.ntnu.kore.safespace.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("images")
public class ImageController implements RestService<Image, Long> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageController.class);

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

    @Override
    public ResponseEntity<Image> getOne(Long id) {
        return new ResponseEntity<>(imageRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Image> add(@RequestBody Image image) {
        image = imageRepository.save(image);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @Override
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
        try {
            imageService.saveToDisk(image, data);
            return new ResponseEntity<Image>(image, HttpStatus.OK);
        } catch (IOException ex) {
            LOG.warn("Failed to save image to disk", ex);
            return new ResponseEntity<Image>(image, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}

