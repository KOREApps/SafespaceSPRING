package no.ntnu.kore.safespace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Class representing a entity of the table report
 * @author robert
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;
    @JsonIgnore
    @OneToMany(mappedBy = "report")
    private List<Image> images;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location")
    private Location location;

}