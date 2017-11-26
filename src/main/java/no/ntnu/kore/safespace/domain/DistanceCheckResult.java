package no.ntnu.kore.safespace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.kore.safespace.entity.KnownLocation;
import no.ntnu.kore.safespace.entity.Location;

import java.util.Comparator;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistanceCheckResult {

    private Location origin;
    private KnownLocation target;
    private double distance;

}
