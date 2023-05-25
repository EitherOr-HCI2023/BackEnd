package EitherOr.backend.controller;

import EitherOr.backend.domain.Article;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.dto.ArticleListDto;
import EitherOr.backend.dto.CategoriesDto;
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
    public Long getRecommendedText(@RequestBody ArticleForm articleForm) throws Exception {
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

    @ResponseBody
    @GetMapping("category")
    public CategoriesDto allCategories() {
        return articleService.getAllCategoryNameList();
    }

    @ResponseBody
    @GetMapping("/category/{categoryName}/{pageNum}")
    public List<ArticleListDto> articleCategoryList(@PathVariable("categoryName") String categoryName, @PathVariable("pageNum") Long pageNum) {
        return articleService.getArticleInCategoryListSortByTime(pageNum, categoryName);
    }

    @ResponseBody
    @PutMapping("/response/{articleId}/{choiceNumber}")
    public String articleResponse(@PathVariable("articleId") Long articleId, @PathVariable("choiceNumber") int choiceNumber) {
        articleService.userSelect(articleId, choiceNumber);
        return "ok";
    }

    @ResponseBody
    @GetMapping("/arbitrary/{categoryName}")
    public ArticleDto arbitraryArticle(@PathVariable("categoryName") String categoryName) {
        return articleService.getArbitraryArticle(categoryName);
    }

    @ResponseBody
    @PostMapping("/delete/{articleId}/{password}")
    public String deleteArticle(@PathVariable("articleId") Long articleId, @PathVariable("password") String password) {
        log.info("articleId, 비밀번호: [{}][{}]", articleId, password);
        return articleService.deleteArticle(articleId, password);
    }

    @ResponseBody
    @GetMapping("/popular/{pageNum}")
    public List<ArticleListDto> popularArticleList(@PathVariable("pageNum") Long pageNum) {
        return articleService.getArticleListSortByHits(pageNum);
    }

}
