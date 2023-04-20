package EitherOr.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {


    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @OneToMany
    private List<ArticleCategory> articles = new ArrayList<>();
    private String categoryName;

    public void addArticle(ArticleCategory articleCategory) {
        articles.add(articleCategory);
    }

    public static Category createCategory(String name) {
        Category result = new Category();
        result.categoryName = name;
        return result;
    }
}
