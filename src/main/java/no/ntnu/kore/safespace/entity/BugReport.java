package no.ntnu.kore.safespace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class representing a entity of the table bug_report
 * @author robert
 */
@Entity
@Table(name = "bug_report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

}
