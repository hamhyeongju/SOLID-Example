package library.solid.repository;

import library.solid.domain.Book;

public interface BookRepository {

    void save(String name, String author, int price, int stockQuantity);

    Book findById(Long id);
}
