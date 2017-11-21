package no.ntnu.kore.safespace.service;

import no.ntnu.kore.safespace.entity.Location;
import no.ntnu.kore.safespace.domain.DistanceCheckResult;
import no.ntnu.kore.safespace.entity.KnownLocation;
import no.ntnu.kore.safespace.repository.KnownLocationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DistanceService {

    private KnownLocationRepository locationRepository;

    public DistanceService(KnownLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public KnownLocation getNearest(Location location){
        return getNearestN(location, 1).get(0);
    }

    public List<KnownLocation> getNearestN(Location location, int n) {
        List<KnownLocation> locations = locationRepository.findAll();
        List<DistanceCheckResult> results = locations
                .stream()
                .map((knownLocation -> {
                    return new DistanceCheckResult(location, knownLocation, getDistance(location, new Location(knownLocation)));
                })).collect(Collectors.toList());
        Collections.sort(results, Comparator.comparingDouble(DistanceCheckResult::getDistance));
        List<KnownLocation> knownLocations = results
                .stream()
                .map(DistanceCheckResult::getTarget)
                .collect(Collectors.toList());
        if (results.size() > n) {
            knownLocations = knownLocations.subList(0, n+1);
        }
        return knownLocations;
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.609344) * 1000;
    }

    public double getDistance(Location from, Location to) {

        return getDistance(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
