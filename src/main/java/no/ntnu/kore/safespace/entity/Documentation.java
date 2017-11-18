package no.ntnu.kore.safespace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Documentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    @JoinColumn(name = "project")
    private Project project;
    @ManyToOne
    @JoinColumn(name = "user_account")
    private User user;
    @JsonIgnore
    @OneToMany(mappedBy = "documentation")
    private List<Image> images;

}
