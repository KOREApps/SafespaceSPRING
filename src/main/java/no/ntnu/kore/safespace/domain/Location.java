package no.ntnu.kore.safespace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Location {

    private final double longitude;
    private final double latitude;

}
