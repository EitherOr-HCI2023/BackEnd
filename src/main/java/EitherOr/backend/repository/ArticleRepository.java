package EitherOr.backend.repository;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(Article article){
        if (article.getId() == null) {
            em.persist(article);
        }else{
            em.merge(article);
        }
    }

    public Article findOne(Long id){return em.find(Article.class, id);}

    public List<Article> findAll(){
        return em.createQuery("select a from Article a", Article.class)
                .getResultList();
    }

    public List<Article> getTenRecent(Long page){

        return em.createQuery("select a from Article a order by a.creationTime", Article.class).setFirstResult((int)(10*(page-1))).setMaxResults(10)
                .getResultList();
    }
    public List<Article> getTenPopular(Long page){

        return em.createQuery("select a from Article a order by a.hits desc ", Article.class).setFirstResult((int)(10*(page-1))).setMaxResults(10)
                .getResultList();
    }

    public void deleteArticle(Long articleId) {
        Article article = em.find(Article.class, articleId);
        for (ArticleCategory articleCategory : article.getArticleCategories())
            em.remove(articleCategory);
        em.remove(article);
    }

}
