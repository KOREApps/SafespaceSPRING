package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Documentation;
import no.ntnu.kore.safespace.entity.Image;
import no.ntnu.kore.safespace.entity.Report;
import no.ntnu.kore.safespace.repository.DocumentationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("documentations")
public class DocumentationController implements RestService<Documentation, Long> {

    private DocumentationRepository documentationRepository;

    @Autowired
    public DocumentationController(DocumentationRepository documentationRepository){
        this.documentationRepository = documentationRepository;
    }

    @Override
    public ResponseEntity getAll() {
        return new ResponseEntity<>(documentationRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(@PathVariable(value = "id") Long id) {
        Documentation documentation = documentationRepository.findOne(id);
        if (documentation == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(documentation, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity add(@RequestBody Documentation documentation) {
        ValidCheckResult validCheckResult = validPost(documentation);
        if (validCheckResult.isValid()) {
            documentation = documentationRepository.save(documentation);
            return new ResponseEntity<>(documentation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(Documentation newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return new ValidCheckResult(true, "Valid");
    }

    @Override
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody Documentation documentation) {
        ValidCheckResult validCheckResult = validPut(documentation, id);
        if (validCheckResult.isValid()) {
            documentation = documentationRepository.save(documentation);
            return new ResponseEntity<>(documentation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPut(Documentation newEntity, Long id) {
        Documentation currentDocumentation = documentationRepository.findOne(id);
        if (currentDocumentation == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }

    @RequestMapping(path = "{id}/images")
    public ResponseEntity<List<Image>> getImages(@PathVariable(value = "id") Long id) {
        Documentation documentation = documentationRepository.findOne(id);
        if (documentation != null) {
            return new ResponseEntity<>(documentation.getImages(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
