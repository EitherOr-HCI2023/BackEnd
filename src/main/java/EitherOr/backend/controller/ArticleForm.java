package EitherOr.backend.controller;

import EitherOr.backend.domain.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleForm {
    @NotEmpty(message = "게시글 제목은 필수입니다.")
    private String name;
    @NotEmpty(message = "게시글 비밀번호는 필수입니다.")
    private String password;
    @NotEmpty(message = "첫 번째 선택지를 입력해주세요.")
    private String choice1;
    @NotEmpty(message = "두 번째 선택지를 입력해주세요.")
    private String choice2;

    private List<String> categories;

    public String generateQuestion() {
        return "\""+name+"\" 이라는 주제로 다음 두 선택지에서 딱 하나를 선택하고, 그 이유를 알려줘\n 1. "+choice1+"\n 2. "+choice2;
    }

}
