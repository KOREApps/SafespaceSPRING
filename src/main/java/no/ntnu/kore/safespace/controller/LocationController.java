package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.entity.Location;
import no.ntnu.kore.safespace.entity.KnownLocation;
import no.ntnu.kore.safespace.repository.KnownLocationRepository;
import no.ntnu.kore.safespace.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("locations")
public class LocationController implements RestService<KnownLocation, Long> {

    private KnownLocationRepository locationRepository;
    private LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService, KnownLocationRepository locationRepository) {
        this.locationService = locationService;
        this.locationRepository = locationRepository;
    }

    @RequestMapping(value = "distance", method = RequestMethod.POST)
    public ResponseEntity getDistance(@RequestBody List<Location> locations) {
        return new ResponseEntity<>(locationService.getDistance(locations.get(0), locations.get(1)), HttpStatus.OK);
    }

    @RequestMapping(value = "nearest", method = RequestMethod.POST)
    public ResponseEntity getNearestLocation(@RequestBody Location location, @RequestParam(value = "number") Integer number) {
        if (number == null) {
            number = 1;
        }
        ValidCheckResult checkResult = validNearestLocationRequest(location, number);
        if (checkResult.isValid()) {
            if (number == 1) {
                return new ResponseEntity<>(locationService.getNearest(location), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(locationService.getNearestN(location, number), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(checkResult, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "current", method = RequestMethod.POST)
    public ResponseEntity getCurrentLocation(@RequestBody Location location) {
        ValidCheckResult checkResult = validLocation(location);
        if (checkResult.isValid()) {
            Optional<KnownLocation> locationOptional = locationService.getCurrentLocation(location);
            if (locationOptional.isPresent()) {
                return new ResponseEntity<>(locationOptional.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(checkResult, HttpStatus.BAD_REQUEST);
        }
    }

    private ValidCheckResult validNearestLocationRequest(Location location, Integer number){
        if (number <= 0) {
            return new ValidCheckResult(false, "Number of locations cant be less than 1");
        }  else {
            return validLocation(location);
        }
    }

    private ValidCheckResult validLocation(Location location) {
        if (locationRepository.count() == 0) {
            return new ValidCheckResult(false, "No known locations");
        }else if (location.getLatitude() == null || location.getLongitude() == null) {
            return new ValidCheckResult(false, "Both latitude and longitude must be defined");
        } else if (location.getLongitude() > 180.0 || location.getLongitude() < -180.0 ) {
            return new ValidCheckResult(false, "Longitude value is not valid");
        } else if (location.getLatitude() > 90.0 || location.getLatitude() < -180.0) {
            return new ValidCheckResult(false, "Latitude value is not valid");
        } else {
            return ValidCheckResult.OK;
        }
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