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
    public ResponseEntity<Project> getOne(@PathVariable(value = "id") Long id) {
        Project project = projectRepository.findOne(id);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Project> add(@RequestBody Project project) {
        project = projectRepository.save(project);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Project> update(@PathVariable(value = "id") Long id, @RequestBody Project project) {
        if (projectRepository.findOne(id) != null && id.equals(project.getId())) {
            project = projectRepository.save(project);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
        }
    }
}
