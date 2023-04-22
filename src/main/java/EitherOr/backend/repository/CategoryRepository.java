package EitherOr.backend.repository;

import EitherOr.backend.domain.Article;
import EitherOr.backend.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    @PersistenceContext
    private final EntityManager em;

    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        }else{
            em.merge(category);
        }
    }

    public Category findByName(String name){
        return em.createQuery("select c from Category  c where c.categoryName = :name", Category.class)
                .setParameter("name", name)
                .getResultList().stream().findAny().orElse(null);
    }

    public Category getCategory(String name){
        Category result = em.createQuery("select c from Category  c where c.categoryName = :name", Category.class)
                .setParameter("name", name)
                .getResultList().stream().findAny().orElse(null);

        if (result == null) {
            result = Category.createCategory(name);
            this.save(result);
        }

        return result;

    }
    public Category findOne(Long id){return em.find(Category.class, id);}
    public List<String> getAllCategoryName(){
        return em.createQuery("select c.categoryName from Category c", String.class).getResultList();
    }

    public void deleteCategory(Long categoryId) {
        Category category = em.find(Category.class, categoryId);
        em.remove(category);
    }
}
