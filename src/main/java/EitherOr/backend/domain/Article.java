package EitherOr.backend.domain;

import EitherOr.backend.controller.ArticleForm;
import EitherOr.backend.dto.ArticleDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Slf4j
public class Article {
    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    private String name;
    private LocalDateTime creationTime;
    private String password;
    private Long hits;
    private String choice1;
    private Long choice1SelectionNum;
    private String choice2;
    private Long choice2SelectionNum;
    private String chatGPTComment;

    public static Article createArticle(String name, String password, String contents, String choice1, String choice2, Category... articleCategories){
        Article article = new Article();
        article.setName(name);
        article.setCreationTime(LocalDateTime.now());
        article.setPassword(password);
        article.setHits(0L);
        article.setChoice1(choice1);
        article.setChoice2(choice2);
        article.setChoice1SelectionNum(0L);
        article.setChoice2SelectionNum(0L);
        article.setChatGPTComment("chatGPTComment");
        for (Category articleCategory : articleCategories) {
            ArticleCategory.createArticleCategory(article, articleCategory);
            log.info("articleID: {}, category: {}", article.getId(), articleCategory);
        }
        return article;
    }

    public static Article createArticleFromDto(ArticleDto articleDto) {
        Article article = new Article();
        article.setName(articleDto.getName());
        article.setPassword(articleDto.getPassword());
        article.setHits(articleDto.getHits());
        article.setChoice1(articleDto.getChoice1());
        article.setChoice2(articleDto.getChoice2());
        article.setChoice1SelectionNum(articleDto.getChoice1SelectionNum());
        article.setChoice2SelectionNum(articleDto.getChoice2SelectionNum());
        article.setChatGPTComment(article.getChatGPTComment());
        return article;
    }

}
