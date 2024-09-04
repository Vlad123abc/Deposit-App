package deposit.repository;

import deposit.domain.MyEntity;

import java.util.List;

public interface Repository <ID, E extends MyEntity<ID>> {
    E getById(ID id);
    List<E> getAll();
    void save(E entity);
    void delete(ID id);
    void update(E entity);
}
