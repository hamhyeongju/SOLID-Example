package library.solid.repository;

import library.solid.domain.Book;

import java.util.Map;

public class BookRepositoryImpl implements BookRepository{

    public static Map<Long, Book> store;

    @Override
    public void save(Book book) {

    }

    @Override
    public Book findById(Long id) {
        return null;
    }
}
