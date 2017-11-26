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

/**
 * Class that provides functionality for locations
 */
@Service
public class LocationService {

    private KnownLocationRepository locationRepository;

    public LocationService(KnownLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /**
     * Tries to find a known location within the given locations range
     * @param location origin location
     * @return Optional containing KnownLocation if one is found otherwise empty
     */
    public Optional<KnownLocation> getCurrentLocation(Location location) {
        List<KnownLocation> knownLocationsInRange = getKnownLocationsWithinRange(location);
        if (knownLocationsInRange.size() > 0) {
            return Optional.ofNullable(knownLocationsInRange.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Finds KnownLocations within range of given location
     * @param location origin location
     * @return List of known locations within range of given location
     */
    private List<KnownLocation> getKnownLocationsWithinRange(Location location){
        List<DistanceCheckResult> results = getDistanceToKnownLocations(location, locationRepository.findAll());
        results = results.stream()
                .filter(distanceCheckResult -> {
                    Double radius = distanceCheckResult.getTarget().getRadius().doubleValue();
                    return distanceCheckResult.getDistance() <= radius;
                }).collect(Collectors.toList());
        sortDistanceResults(results);
        return results.stream().map(DistanceCheckResult::getTarget).collect(Collectors.toList());
    }

    /**
     * Finds the nearest KnownLocation
     * @param location origin location
     * @return Nearest known location
     */
    public KnownLocation getNearest(Location location){
        return getNearestN(location, 1).get(0);
    }

    /**
     * Find the nearest n KnownLocations
     * @param location origin location
     * @param n number of locations to find
     * @return List of known locations within range
     */
    public List<KnownLocation> getNearestN(Location location, int n) {
        List<KnownLocation> locations = locationRepository.findAll();
        List<DistanceCheckResult> results = getDistanceToKnownLocations(location, locations);
        sortDistanceResults(results);
        List<KnownLocation> knownLocations = results
                .stream()
                .map(DistanceCheckResult::getTarget)
                .collect(Collectors.toList());
        if (results.size() > n) {
            knownLocations = knownLocations.subList(0, n);
        }
        return knownLocations;
    }

    /***
     * Finds the distance between given location and all given KnownLocations
     * @param origin origin location
     * @param knownLocations list of known locations to calculate distance to
     * @return List of DistanceCheckResults
     */
    private List<DistanceCheckResult> getDistanceToKnownLocations(Location origin, List<KnownLocation> knownLocations){
        return knownLocations
                .stream()
                .map(knownLocation -> {
                    return new DistanceCheckResult(origin, knownLocation, getDistance(origin, new Location(knownLocation)));
                }).collect(Collectors.toList());
    }

    /**
     * Sorts given list of DistanceCheckResults from lowest to highest distance
     * @param results sorted list of DistanceCheckResults
     */
    private void sortDistanceResults(List<DistanceCheckResult> results){
        results.sort(Comparator.comparingDouble(DistanceCheckResult::getDistance));
    }

    /**
     * Calculates the distance between given locations
     * @param lat1 Latitude of location 1
     * @param lon1 Longitude of location 1
     * @param lat2 Latitude of location 2
     * @param lon2 Longitude of location 2
     * @return double representing the distance between the given locations.
     */
    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.609344) * 1000;
    }

    /**
     * Calculates the distance between given locations
     * @param from Location 1
     * @param to Location 2
     * @return double representing the distance between the given locations.
     */
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
