package EitherOr.backend.service;

import EitherOr.backend.controller.ArticleForm;
import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import EitherOr.backend.domain.Category;
import EitherOr.backend.dto.ArticleDto;
import EitherOr.backend.dto.ArticleListDto;
import EitherOr.backend.dto.CategoriesDto;
import EitherOr.backend.repository.ArticleCategoryRepository;
import EitherOr.backend.repository.ArticleRepository;
import EitherOr.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    public final ArticleRepository articleRepository;
    public final ArticleCategoryRepository articleCategoryRepository;
    public final CategoryRepository categoryRepository;

    @Transactional//readOnly 해제
    public Long saveArticle(ArticleForm articleForm) {
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
        if (findArticle != null)
            findArticle.setChatGPTComment(gptComment);
        log.info("Id({})의 게시글의 부재로 chatGPTComment 등록 실패", articleId);
    }

    @Transactional
    public void hit(Long articleId) {
        Article findArticle = articleRepository.findOne(articleId);

        findArticle.setHits(findArticle.getHits() + 1);
    }

    @Transactional
    public void userSelect(Long articleId, int choiceNumber) {

        Article findArticle = articleRepository.findOne(articleId);

        if (choiceNumber == 1)
            findArticle.setChoice1SelectionNum(findArticle.getChoice1SelectionNum() + 1);
        else
            findArticle.setChoice2SelectionNum(findArticle.getChoice2SelectionNum() + 1);
    }


    @Transactional
    public void unHit(Long articleId) {
        Article findArticle = articleRepository.findOne(articleId);

        findArticle.setHits(findArticle.getHits() - 1);
    }

    public ArticleDto getArticle(Long articleId) {
        Article article = articleRepository.findOne(articleId);
        if (article != null) {
            return new ArticleDto(article, article.getCategoryList());
        }
        return new ArticleDto();
    }

    public List<ArticleListDto> getArticleListSortByTime(Long page) {
        List<ArticleListDto> result = new ArrayList<>();
        List<Article> articleList = articleRepository.getTenRecent(page);
        if (articleList != null) {
            for (Article article : articleList) {
                result.add(new ArticleListDto(article, article.getCategoryList()));
            }
        }
        return result;
    }

    public List<ArticleListDto> getArticleInCategoryListSortByTime(Long page, String categoryName) {
        List<ArticleListDto> result = new ArrayList<>();
        Category category = categoryRepository.findByName(categoryName);
        if (category != null) {
            Long num = (page - 1L) * ServiceConst.PAGE_CONTENTS_QUANTITY;
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

    public CategoriesDto getAllCategoryNameList() {
        return new CategoriesDto(categoryRepository.getAllCategoryName());
    }

    public ArticleDto getArbitraryArticle(String categoryName){
        Random rand = new Random(System.currentTimeMillis());
        Category category;
        if (categoryName.equals("all")) {
            int size = categoryRepository.getAllCategoryName().size();
            category = categoryRepository.getCategory(categoryRepository.getAllCategoryName().get(rand.nextInt(size)));
        }
        else
            category = categoryRepository.findByName(categoryName);
        if (category != null) {
            int num = category.getArticles().size();
            if (num == 0) {
                return new ArticleDto();
            }

            Article article = category.getArticles().get(rand.nextInt(num)).getArticle();
            return new ArticleDto(article, article.getCategoryList());
        }
        return new ArticleDto();
    }

    @Transactional
    public String deleteArticle(Long articleId, String password) {
        Article article = articleRepository.findOne(articleId);
        if (article == null) {
            return "fail : 요청한 id의 게시글 부재";
        }
        if (!article.getPassword().equals(password)) {
            log.info("게시글 비밀번호:[{}]\n입력 비밀번호:[{}]", article.getPassword(), password);
            return "fail : 비밀번호 불일치";
        }
        List<Category> categoryList = new ArrayList<>();
        for(ArticleCategory articleCategory : article.getArticleCategories()){
            categoryList.add(articleCategory.getCategory());
        }
        articleRepository.deleteArticle(articleId);
        for (Category category : categoryList) { // 빈 카테고리 제거하는 과정
            if (category.getArticles().size() == 0) {
                categoryRepository.deleteCategory(category.getId());
            }
        }
        return "ok";
    }

}
