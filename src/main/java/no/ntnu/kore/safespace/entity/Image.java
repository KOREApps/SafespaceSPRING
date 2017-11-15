package no.ntnu.kore.safespace.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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