package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.domain.Location;
import no.ntnu.kore.safespace.entity.KnownLocation;
import no.ntnu.kore.safespace.repository.KnownLocationRepository;
import no.ntnu.kore.safespace.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("locations")
public class LocationController implements RestService<KnownLocation, Long> {

    private KnownLocationRepository locationRepository;
    private DistanceService distanceService;

    @Autowired
    public LocationController(DistanceService distanceService, KnownLocationRepository locationRepository) {
        this.distanceService = distanceService;
        this.locationRepository = locationRepository;
    }

    @RequestMapping(value = "distance",method = RequestMethod.POST)
    public ResponseEntity getDistance(@RequestBody List<Location> locations) {
        return new ResponseEntity<Double>(distanceService.getDistance(locations.get(0), locations.get(1)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getAll() {
        return new ResponseEntity<>(locationRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getOne(Long id) {
        KnownLocation location = locationRepository.findOne(id);
        if (location != null) {
            return new ResponseEntity<>(location, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity add(@RequestBody KnownLocation knownLocation) {
        ValidCheckResult checkResult = validPost(knownLocation);
        if (checkResult.isValid()) {
            knownLocation = locationRepository.save(knownLocation);
            return new ResponseEntity<>(knownLocation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(checkResult, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ValidCheckResult validPost(KnownLocation newEntity) {
        if (newEntity.getId() != null) {
            return new ValidCheckResult(false, "New entity id must be null");
        }
        return ValidCheckResult.OK;
    }

    @Override
    public ResponseEntity update(Long id, KnownLocation knownLocation) {
        return null;
    }

    @Override
    public ValidCheckResult validPut(KnownLocation newEntity, Long id) {
        return null;
    }
}
