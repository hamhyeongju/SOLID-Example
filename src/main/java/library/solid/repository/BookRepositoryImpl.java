package library.solid.repository;

import library.solid.domain.Book;

import java.util.HashMap;
import java.util.Map;

public class BookRepositoryImpl implements BookRepository{

    public static Map<Long, Book> store = new HashMap<>();

    @Override
    public void save(String name, String author, int price, int stockQuantity) {
        Book book = new Book(Sequence.getSequence(), name, author, price, stockQuantity);
        store.put(book.getId(), book);
    }

    @Override
    public Book findById(Long id) {
        return store.get(id);
    }
}
