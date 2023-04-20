package EitherOr.backend.controller;

import EitherOr.backend.domain.Article;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.dto.ArticleListDto;
import EitherOr.backend.service.ArticleService;
import EitherOr.backend.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@Slf4j
@RequestMapping("/article")
@ResponseBody
@RequiredArgsConstructor
public class ArticleController {

    private final ChatGptService chatGptService;
    private final ArticleService articleService;

    @ResponseBody
    @RequestMapping("/add")
    public Long getRecommendedText(ArticleForm articleForm) throws Exception {
        log.info("inputCat = {} / {}", articleForm.getCategories().toString(), articleForm.getCategories().size());
        Long articleId = articleService.saveArticle(articleForm);
        log.info("input = {}", articleForm.generateQuestion());
        chatGptService.sendRequest(articleId, articleForm.generateQuestion());
        return articleId;
    }

    @PutMapping("/hit/{articleId}")
    public void articleHitUp(@PathVariable("articleId") Long articleId) {
        articleService.hit(articleId);
    }

    @PutMapping("/unhit/{articleId}")
    public void articleHitDown(@PathVariable("articleId") Long articleId) {
        articleService.unHit(articleId);

    }

    @ResponseBody
    @GetMapping("/{articleId}")
    public ArticleDto articleSpecificSingle(@PathVariable("articleId") Long articleId) {
        return articleService.getArticle(articleId);
    }

    @ResponseBody
    @GetMapping("/list/{page}")
    public List<ArticleListDto> articleList(@PathVariable("page") Long page) {
        return articleService.getArticleListSortByTime(page);
    }

}
