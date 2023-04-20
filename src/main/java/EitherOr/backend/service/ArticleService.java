package EitherOr.backend.service;

import EitherOr.backend.controller.ArticleForm;
import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import EitherOr.backend.domain.Category;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.dto.ArticleListDto;
import EitherOr.backend.repository.ArticleCategoryRepository;
import EitherOr.backend.repository.ArticleRepository;
import EitherOr.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArticleService {
    public final ArticleRepository articleRepository;
    public final ArticleCategoryRepository articleCategoryRepository;
    public final CategoryRepository categoryRepository;

    @Transactional//readOnly 해제
    public Long saveArticle(ArticleForm articleForm){
        Article article = Article.createArticle(articleForm);
        articleRepository.save(article);
        for (String categoryName : articleForm.getCategories()) {
            Category category = categoryRepository.getCategory(categoryName);
            ArticleCategory articleCategory = ArticleCategory.createArticleCategory(article, category);
            articleCategoryRepository.save(articleCategory);
        }
        return article.getId();
    }

    @Transactional
    public void updateGptComment(Long articleId, String gptComment) {
        Article findArticle = articleRepository.findOne(articleId);

        findArticle.setChatGPTComment(gptComment);
    }

    @Transactional
    public void hit(Long articleId){
        Article findArticle = articleRepository.findOne(articleId);

        findArticle.setHits(findArticle.getHits()+1);
    }


    @Transactional
    public void unHit(Long articleId){
        Article findArticle = articleRepository.findOne(articleId);

        findArticle.setHits(findArticle.getHits()-1);
    }

    public ArticleDto getArticle(Long articleId){
        Article article =  articleRepository.findOne(articleId);
        if (article != null) {
            return new ArticleDto(article, article.getCategoryList());
        }
        return new ArticleDto();
    }

    public List<ArticleListDto> getArticleListSortByTime(Long page){
        List<ArticleListDto> result = new ArrayList<>();
        List<Article> articleList = articleRepository.getTenRecent(page);
        if (articleList != null) {
            for (Article article : articleList) {
                result.add(new ArticleListDto(article, article.getCategoryList()));
            }
        }
        return result;
    }

    public List<ArticleListDto> getArticleInCategoryListSortByTime(Long page, String categoryName){
        List<ArticleListDto> result = new ArrayList<>();
        Category category = categoryRepository.findByName(categoryName);
        if (category != null) {
            Long num = (page -1L) * ServiceConst.PAGE_CONTENTS_QUANTITY;
            Long count = 0L;
            for (ArticleCategory articleCategory : category.getArticles()) {
                count++;
                if (count > num && count <= num + ServiceConst.PAGE_CONTENTS_QUANTITY) {
                    result.add(new ArticleListDto(articleCategory.getArticle(), articleCategory.getArticle().getCategoryList()));
                }
            }
        }
        return result;
    }

    public List<String> getAllCategoryNameList() {
        return categoryRepository.getAllCategoryName();
    }

}
