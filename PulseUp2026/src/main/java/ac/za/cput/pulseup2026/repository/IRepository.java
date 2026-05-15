package ac.za.cput.pulseup2026.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {
    T create(T entity);
    T read(ID id);
    T update(T entity);
    void delete(ID id);
    List<T> readAll();
}
