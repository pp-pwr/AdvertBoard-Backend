package ppztw.AdvertBoard.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adverts")
@Getter
@Setter
@NoArgsConstructor
public class Advert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tags;

    @Column(nullable = false)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImgUrl> imgUrls;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Subcategory subcategory;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private Status status;


    public Advert(String title, List<String> tags, String description, List<String> imgUrls,
                  Subcategory subcategory) {
        this.title = title;
        this.tags = new ArrayList<>();
        this.imgUrls = new ArrayList<>();
        this.description = description;

        for (String tag : tags)
            this.tags.add(new Tag(tag));

        for (String imgUrl : imgUrls)
            this.imgUrls.add(new ImgUrl(imgUrl));

        this.subcategory = subcategory;

        this.date = LocalDate.now();

        status = Status.OK;
    }

    private enum Status {OK, EDITED, ARCHIVED, BANNED}
}