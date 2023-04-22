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
    @Column(length = 1000)
    private String chatGPTComment;

    @OneToMany(mappedBy = "article")
    private List<ArticleCategory> articleCategories = new ArrayList<>();

    public void addCategory(ArticleCategory articleCategory) {
        articleCategories.add(articleCategory);
    }
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

    public static Article createArticle(ArticleForm articleForm) {
        Article article = new Article();
        article.name = articleForm.getName();
        article.creationTime = LocalDateTime.now();
        article.password = articleForm.getPassword();
        article.hits = 0L;
        article.choice1 = articleForm.getChoice1();
        article.choice2 = articleForm.getChoice2();
        article.choice1SelectionNum = 0L;
        article.choice2SelectionNum = 0L;
        article.chatGPTComment = "chatGPTComment 생성중입니다. (약 1분 소요)";
        return article;
    }

    public List<String> getCategoryList(){
        List<String> result =new ArrayList<>();
        for (ArticleCategory articleCategory : articleCategories) {
            result.add(articleCategory.getCategory().getCategoryName());
        }
        return result;
    }

}
