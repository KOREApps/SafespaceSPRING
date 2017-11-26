package no.ntnu.kore.safespace.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class representing a entity of the image table. Has one database transient field to hold data that should not be
 * included in the database entity.
 * @author robert
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "file_extension")
    private String fileExtension;
    private String description;
    @ManyToOne
    @JoinColumn(name = "documentation")
    private Documentation documentation;
    @ManyToOne
    @JoinColumn(name = "report")
    private Report report;
    @Transient
    private String data;

}