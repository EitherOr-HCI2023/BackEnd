package EitherOr.backend.repository;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import EitherOr.backend.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleCategoryRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(ArticleCategory articleCategory){
        if (articleCategory.getId() == null) {
            em.persist(articleCategory);
        }else{
            em.merge(articleCategory);
        }
    }


    public List<Category> findByArticleId(Long articleId) {
        return em.createQuery("select ac.category from ArticleCategory ac where ac.article.id = :articleId", Category.class)
                .setParameter("articleId", articleId).getResultList();
    }

    public List<ArticleCategory> findAll(){
        return em.createQuery("select ac from ArticleCategory ac", ArticleCategory.class)
                .getResultList();
    }

    public void deleteArticleCategory(Long articleCategoryId) {
        ArticleCategory articleCategory = em.find(ArticleCategory.class, articleCategoryId);
        articleCategory.getCategory().removeArticleCategory(articleCategory);
        em.remove(articleCategory);
    }
}
