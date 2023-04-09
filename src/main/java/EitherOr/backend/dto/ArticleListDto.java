package EitherOr.backend.dto;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ArticleListDto {
    private Long id;
    private Long hits;
    private String name;
    private List<Category> category;

    @Builder
    public ArticleListDto(Article article, List<Category> category) {
        this.id = article.getId();
        this.category = category;
        this.hits = article.getHits();
        this.name = article.getName();
    }
}
