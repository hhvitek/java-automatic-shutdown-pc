package model.db.repo;

import model.scheduledtasks.ScheduledTaskStatus;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Stream;

/**
 * Decorator over ScheduledTaskRepository.
 * No simultaneous access to DB.
 */
public class SynchronizedScheduledTaskRepository extends ScheduledTaskRepository {

    public SynchronizedScheduledTaskRepository(@NotNull EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Stream<ScheduledTaskEntity> findByStatus(@NotNull ScheduledTaskStatus status) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            return super.findByStatus(status);
        }
    }

    @Override
    public ScheduledTaskEntity findOneById(@NotNull Object id) throws ElemNotFoundException {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            return super.findOneById(id);
        }
    }

    @Override
    public List<ScheduledTaskEntity> findAll() {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            return super.findAll();
        }
    }

    @Override
    public Stream<ScheduledTaskEntity> findAllAsStream() {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            return super.findAllAsStream();
        }
    }

    @Override
    public void delete(@NotNull ScheduledTaskEntity entity) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            super.delete(entity);
        }
    }

    @Override
    public void deleteById(@NotNull Object id) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            super.deleteById(id);
        }
    }

    @Override
    public void deleteAll() {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            super.deleteAll();
        }
    }

    @Override
    public boolean containsById(@NotNull Object id) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            return super.containsById(id);
        }
    }

    @Override
    public void create(@NotNull ScheduledTaskEntity entity) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            super.create(entity);
        }
    }

    @Override
    public void update(@NotNull ScheduledTaskEntity entity) {
        synchronized (SynchronizedScheduledTaskRepository.class) {
            super.update(entity);
        }
    }
}
