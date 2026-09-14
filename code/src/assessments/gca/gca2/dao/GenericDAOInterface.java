package assessments.gca.gca2.dao;

import java.util.List;
import java.util.Optional;

/**
 * Generic DAO contract for basic CRUD operations.
 *
 * @param <T> the entity type managed by this DAO
 * @param <K> the primary key type
 * @author OOP Teaching Team
 */
public interface GenericDAOInterface<T, K> {

    // Inserts: a new entity and returns it with its generated primary key populated
    T insert(T entity) throws Exception;

    // Gets: the entity with the given key, or Optional.empty() if not found
    Optional<T> findById(K id) throws Exception;

    // Gets: all entities in the table as an unordered list; never returns null
    List<T> findAll() throws Exception;

    // Deletes: the entity with the given key; returns true if a row was removed
    boolean deleteById(K id) throws Exception;
}
