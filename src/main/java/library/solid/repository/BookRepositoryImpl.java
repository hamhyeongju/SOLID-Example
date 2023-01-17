package library.solid.repository;

import library.solid.domain.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookRepositoryImpl implements BookRepository{

    public static Map<Long, Book> store = new HashMap<>();

    @Override
    public Long save(Book book) {
        store.put(book.getId(), book);
        return book.getId();
    }

    @Override
    public Book findById(Long id) {
        return store.get(id);
    }

}
