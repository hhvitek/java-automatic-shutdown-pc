package model.db.repo;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

/**
 * Basic hibernate database operations
 */
public abstract class AbstractRepository<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    protected final Class<T> clazz;

    protected final EntityManager entityManager;

    protected AbstractRepository(@NotNull EntityManager entityManager, @NotNull Class<T> clazz) {
        this.entityManager = entityManager;
        this.clazz = clazz;
    }

    public @NotNull EntityManager getEntityManager() {
        return entityManager;
    }

    public @NotNull T findOneById(@NotNull Object id) throws ElemNotFoundException {
        T entity = entityManager.find(clazz, id);
        if (entity != null) {
            return entity;
        } else {
            throw new ElemNotFoundException(clazz, id);
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull List<T> findAll() {
        String query = String.format("SELECT a FROM %s a", clazz.getName());
        return entityManager.createQuery(query).getResultList();
    }

    public void delete(@NotNull T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException ex) {
            logger.error("Failed to remove {} from DB. {}.", clazz, entity, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }

    public void deleteById(@NotNull Object id) {
        try {
            T entity = findOneById(id);
            delete(entity);
        } catch (ElemNotFoundException e) {
            logger.info("Element: {} was not found in {}.", id, clazz);
        }
    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s", clazz.getName());

        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery(query).executeUpdate();
        } catch (IllegalStateException | IllegalArgumentException | PersistenceException ex) {
            logger.error("Failed to remove from DB <{}>.", clazz, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }

    public boolean containsById(@NotNull Object id) {
        try {
            T entity = findOneById(id);
            return true;
        } catch (ElemNotFoundException e) {
            return false;
        }
    }

    public void create(@NotNull T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
        } catch (IllegalStateException | EntityExistsException | IllegalArgumentException | TransactionRequiredException ex) {
            logger.error("Failed to persist/create {} into DB. {}.", clazz, entity, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }

    public void update(@NotNull T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
        } catch (IllegalStateException | IllegalArgumentException | TransactionRequiredException  ex) {
            logger.error("Failed to merge/update {} into DB. {}.", clazz, entity, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }
}
