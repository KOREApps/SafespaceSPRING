package no.ntnu.kore.safespace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class representing a entity of the table location
 * @author robert
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double latitude;
    private Double longitude;
    private Integer accuracy;

    public Location (KnownLocation knownLocation) {
        this.id = null;
        this.latitude = knownLocation.getLatitude();
        this.longitude = knownLocation.getLongitude();
        this.accuracy = 0;
    }

}
