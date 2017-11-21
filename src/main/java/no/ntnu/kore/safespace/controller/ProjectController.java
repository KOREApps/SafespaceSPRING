package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Project;
import no.ntnu.kore.safespace.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("projects")
public class ProjectController implements RestService<Project, Long> {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository){
        this.projectRepository = projectRepository;
    }

    @Override
    public ResponseEntity<List<Project>> getAll() {
        return new ResponseEntity<>(projectRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(@PathVariable(value = "id") Long id) {
        Project project = projectRepository.findOne(id);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity add(@RequestBody Project project) {
        ValidCheckResult validCheckResult = validPost(project);
        if (validCheckResult.isValid()) {
            project = projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(Project newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return ValidCheckResult.OK;
    }

    @Override
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody Project project) {
        ValidCheckResult validCheckResult = validPut(project, id);
        if (validCheckResult.isValid()) {
            project = projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPut(Project newEntity, Long id) {
        Project currentProject = projectRepository.findOne(id);
        if (currentProject == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }
}
