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
    private String password;
    private Long hits;
    private String choice1;
    private Long choice1SelectionNum;
    private String choice2;
    private Long choice2SelectionNum;
    private String chatGPTComment;
    private List<Category> categories;

    @Builder
    public ArticleDto(ArticleForm articleForm) {
        this.name = articleForm.getName();
        this.password = articleForm.getPassword();
        this.hits = 0L;
        this.choice1 = articleForm.getChoice1();
        this.choice2 = articleForm.getChoice2();
        this.choice1SelectionNum = 0L;
        this.choice2SelectionNum = 0L;
        this.chatGPTComment = "chatGPTComment";
        this.categories = articleForm.getCategory();

    }

    public String generateQuestion() {
        return "\""+name+"\" 이라는 주제로 다음 두 선택지에서 선택을 하고, 그 이유를 알려줘\n 1. "+choice1+"\n 2. "+choice2;
    }
}
