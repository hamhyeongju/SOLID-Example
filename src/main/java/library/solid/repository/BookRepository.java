package library.solid.repository;

import library.solid.domain.Book;

import java.util.List;

public interface BookRepository {

    Long save(Book book);

    Book findById(Long id);

    List<Book> findAll();
}
