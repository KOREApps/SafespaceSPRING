package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.BugReport;
import no.ntnu.kore.safespace.repository.BugReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for bug reports. Currently handles Create, Read and Update operations on bug reports
 * @author robert
 */
@RestController
@RequestMapping("bugs")
public class BugReportController implements RestService<BugReport, Long> {

    private BugReportRepository bugReportRepository;

    @Autowired
    public BugReportController(BugReportRepository bugReportRepository) {
        this.bugReportRepository = bugReportRepository;
    }

    @Override
    public ResponseEntity getAll() {
        return new ResponseEntity<>(bugReportRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<>(bugReportRepository.findOne(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity add(@RequestBody BugReport bugReport) {
        ValidCheckResult validCheckResult = validPost(bugReport);
        if (validCheckResult.isValid()) {
            bugReport.setId(null);
            bugReport = bugReportRepository.save(bugReport);
            return new ResponseEntity<>(bugReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(BugReport newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return ValidCheckResult.OK;
    }

    @Override
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody BugReport bugReport) {
        ValidCheckResult validCheckResult = validPut(bugReport, id);
        if (validCheckResult.isValid()) {
            bugReport = bugReportRepository.save(bugReport);
            return new ResponseEntity<>(bugReport, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPut(BugReport newEntity, Long id) {
        BugReport currentBugReport = bugReportRepository.findOne(id);
        if (currentBugReport == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }
}
