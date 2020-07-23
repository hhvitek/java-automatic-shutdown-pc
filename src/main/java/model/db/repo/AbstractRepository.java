package model.db.repo;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractRepository<T> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

    protected final EntityManager entityManager;

    protected AbstractRepository(@NotNull EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract Class<T> getEntityClazz();

    public T findOneById(@NotNull Object id) throws ElemNotFoundException {
        T entity = entityManager.find(getEntityClazz(), id);
        if (entity != null) {
            return entity;
        } else {
            throw new ElemNotFoundException(getEntityClazz(), id);
        }
    }

    public List<T> findAll() {
        String query = String.format("SELECT a FROM %s a", getEntityClazz().getName());
        return entityManager.createQuery(query).getResultList();
    }

    public Stream<T> findAllAsStream() {
        String query = String.format("SELECT a FROM %s a", getEntityClazz().getName());
        return entityManager.createQuery(query).getResultStream();
    }

    public void delete(@NotNull T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entity);
        } catch (Exception ex) {
            logger.error("Failed to remove {} from DB. {}.", getEntityClazz(), entity, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }

    public void deleteById(@NotNull Object id) {
        try {
            T entity = findOneById(id);
            delete(entity);
        } catch (ElemNotFoundException e) {
            logger.info("Element: {} was not found in {}.", id, getEntityClazz());
        }

    }

    public void deleteAll() {
        String query = String.format("DELETE FROM %s", getEntityClazz().getName());

        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery(query).executeUpdate();
        } catch(Exception ex) {
            logger.error("Failed to remove from DB <{}>.", getEntityClazz(), ex);
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
            } catch (Exception ex) {
                logger.error("Failed to persist/create {} into DB. {}.", getEntityClazz(), entity, ex);
            } finally {
                entityManager.getTransaction().commit();
            }
    }

    public void update(@NotNull T entity) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
        } catch (Exception ex) {
            logger.error("Failed to merge/update {} into DB. {}.", getEntityClazz(), entity, ex);
        } finally {
            entityManager.getTransaction().commit();
        }
    }
}
