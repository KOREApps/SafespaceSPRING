package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Report;
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
    public ResponseEntity<List<Report>> getAll() {
        return new ResponseEntity<>(reportRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Report> getOne(@PathVariable(value = "id") Long id) {
        Report report = reportRepository.findOne(id);
        if (report != null) {
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<Report> add(@RequestBody Report report) {
        report = reportRepository.save(report);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Report> update(@PathVariable(value = "id") Long id, @RequestBody Report report) {
        if (reportRepository.findOne(id) != null && id.equals(report.getId())) {
            report = reportRepository.save(report);
            return new ResponseEntity<>(report, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
