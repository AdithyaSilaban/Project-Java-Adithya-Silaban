package dao;

import java.util.List;
import java.util.Optional;

/**
 * [KONSEP: INTERFACE sebagai kontrak DAO]
 * IBookingDAO mendefinisikan operasi yang harus disediakan oleh
 * implementasi DAO manapun, baik InMemory maupun JDBC.
 *
 * Pola ini adalah pondasi dari Dependency Inversion Principle (DIP):
 * Service layer bergantung pada abstraksi (interface), bukan implementasi konkret.
 * Hasilnya: bisa swap dari InMemory ke JDBC tanpa mengubah service.
 *
 * @param <T> Tipe entitas yang dikelola DAO ini
 */
public interface IBookingDAO<T> {
    void insert(T entitas) throws Exception;
    Optional<T> findById(String id) throws Exception;
    List<T> findAll() throws Exception;
    void update(T entitas) throws Exception;
    boolean delete(String id) throws Exception;
}
