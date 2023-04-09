package EitherOr.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCategory {
    @Id
    @GeneratedValue
    @Column(name = "article_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    private Category category;

    public static ArticleCategory createArticleCategory(Article article, Category category) {
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticle(article);
        articleCategory.setCategory(category);

        return articleCategory;
    }
}
