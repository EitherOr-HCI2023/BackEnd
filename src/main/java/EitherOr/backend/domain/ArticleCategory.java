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

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public static ArticleCategory createArticleCategory(Article article, Category category) {
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setCategory(category);
        articleCategory.setArticle(article);
        article.addCategory(articleCategory);
        category.addArticle(articleCategory);
        return articleCategory;
    }
}
