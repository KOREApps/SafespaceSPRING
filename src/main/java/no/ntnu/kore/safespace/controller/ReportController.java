package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Image;
import no.ntnu.kore.safespace.entity.Report;
import no.ntnu.kore.safespace.repository.ImageRepository;
import no.ntnu.kore.safespace.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("reports")
public class ReportController implements RestService<Report, Long> {

    private ReportRepository reportRepository;

    @Autowired
    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }


    @Override
    public ResponseEntity getAll() {
        return new ResponseEntity<>(reportRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(@PathVariable(value = "id") Long id) {
        Report report = reportRepository.findOne(id);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity add(@RequestBody Report report) {
        ValidCheckResult validCheckResult = validPost(report);
        if (validCheckResult.isValid()) {
            report = reportRepository.save(report);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(Report newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return new ValidCheckResult(true, "Valid");
    }

    @Override
    public ResponseEntity update(@PathVariable(value = "id") Long id, @RequestBody Report report) {
        ValidCheckResult validCheckResult = validPut(report, id);
        if (validCheckResult.isValid()) {
            report = reportRepository.save(report);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(validCheckResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPut(Report newEntity, Long id) {
        Report currentReport = reportRepository.findOne(id);
        if (currentReport == null) {
            return new ValidCheckResult(false, "Id does not exist");
        } else if(newEntity.getId() == null || !newEntity.getId().equals(id)) {
            return new ValidCheckResult(false, "Id in json does not match id in path");
        }
        return ValidCheckResult.OK;
    }

    @RequestMapping(path = "{id}/images")
    public ResponseEntity<List<Image>> getImages(@PathVariable(value = "id") Long id) {
        Report report = reportRepository.findOne(id);
        if (report != null) {
            return new ResponseEntity<>(report.getImages(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
