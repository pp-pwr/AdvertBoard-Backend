package ppztw.AdvertBoard.Model;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subcategories", uniqueConstraints = {
        @UniqueConstraint(columnNames = "subcategory_name")
})
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC)
    @Column(nullable = false)
    private String subcategory_name;

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC)
    private String description;

    @Getter(value = AccessLevel.PUBLIC)
    @Setter(value = AccessLevel.PUBLIC)
    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category parentCategory;

    public void setCategory(Category category) {
        this.parentCategory = category;
    }

    public Category getCategory() {
        return this.parentCategory;
    }
}
