package EitherOr.backend.dto;

import EitherOr.backend.controller.ArticleForm;
import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Getter
@NoArgsConstructor
public class ArticleDto {

    private String name;
    private Long id;
    private Long hits;
    private String choice1;
    private Long choice1SelectionNum;
    private String choice2;
    private Long choice2SelectionNum;
    private String chatGPTComment;
    private List<String> categories;

    public ArticleDto(Article article, List<String> categories) {
        this.id = article.getId();
        this.name = article.getName();
        this.hits = article.getHits();
        this.choice1 = article.getChoice1();
        this.choice2 = article.getChoice2();
        this.choice1SelectionNum = article.getChoice1SelectionNum();
        this.choice2SelectionNum = article.getChoice2SelectionNum();
        this.chatGPTComment = article.getChatGPTComment();
        this.categories = categories;

    }

}
