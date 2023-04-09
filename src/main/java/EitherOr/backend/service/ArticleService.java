package EitherOr.backend.service;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import EitherOr.backend.domain.Category;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.dto.ArticleListDto;
import EitherOr.backend.repository.ArticleCategoryRepository;
import EitherOr.backend.repository.ArticleRepository;
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

    @Transactional//readOnly 해제
    public Long saveArticle(ArticleDto articleDto){
        Article article = Article.createArticleFromDto(articleDto);
        articleRepository.save(article);
        for (Category category : articleDto.getCategories()) {
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

    public Article getArticle(Long articleId){
        return articleRepository.findOne(articleId);
    }

    public List<ArticleListDto> getArticleListSortByTime(Long page){
        List<ArticleListDto> result = new ArrayList<>();
        List<Article> articleList = articleRepository.getTenRecent(page);
        if (articleList != null) {
            for (Article article : articleList) {
                List<Category> categories = articleCategoryRepository.findByArticleId(article.getId());
                result.add(new ArticleListDto(article, categories));
            }
        }
        return result;

    }

}
