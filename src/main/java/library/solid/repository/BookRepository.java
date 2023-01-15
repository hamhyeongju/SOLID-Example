package library.solid.repository;

import library.solid.domain.Book;

public interface BookRepository {

    void save(Book book);

    Book findById(Long id);
}
