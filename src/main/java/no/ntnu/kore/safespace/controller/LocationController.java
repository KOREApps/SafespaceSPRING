package no.ntnu.kore.safespace.controller;

import no.ntnu.kore.safespace.domain.Location;
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
public class LocationController {

    private DistanceService distanceService;

    @Autowired
    public LocationController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @RequestMapping(value = "distance",method = RequestMethod.POST)
    public ResponseEntity getDistance(@RequestBody List<Location> locations) {
        return new ResponseEntity<Double>(distanceService.getDistance(locations.get(0), locations.get(1)), HttpStatus.OK);
    }

}
