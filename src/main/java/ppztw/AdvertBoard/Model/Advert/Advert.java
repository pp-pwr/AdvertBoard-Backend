package ppztw.AdvertBoard.Model.Advert;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Model.User.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adverts")
@Getter
@Setter
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Tag> tags;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    private String imagePath;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;


    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Status status;

    @ManyToOne
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<AdvertInfo> additionalInfo;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "advert")
    private AdvertStats advertStats;

    public Advert() {
        this.status = Status.OK;
        this.tags = new ArrayList<>();
        this.date = LocalDate.now();
        AdvertStats advertStats = new AdvertStats();
        advertStats.setAdvert(this);
        this.advertStats = advertStats;
    }

    public Advert(String title, List<Tag> tags, String description, String imagePath,
                  Category subcategory, User user, List<AdvertInfo> additionalInfo) {
        this();
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.tags = tags;
        this.category = subcategory;
        this.user = user;
        this.additionalInfo = additionalInfo;
    }

    public enum Status {OK, EDITED, ARCHIVED, BANNED}
}
