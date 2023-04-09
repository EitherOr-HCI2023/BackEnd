package EitherOr.backend.repository;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.ArticleCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArticleCategoryRepository {

    private final EntityManager em;

    public void save(ArticleCategory articleCategory){
        if (articleCategory.getId() == null) {
            em.persist(articleCategory);
        }else{
            em.merge(articleCategory);
        }
    }

    public ArticleCategory findOne(Long id){return em.find(ArticleCategory.class, id);}

    public List<ArticleCategory> findAll(){
        return em.createQuery("select ac from ArticleCategory ac", ArticleCategory.class)
                .getResultList();
    }
}
