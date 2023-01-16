package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.Book;
import library.solid.repository.BookRepository;
import library.solid.domain.Sequence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class BookTest {

    ApplicationInit init = new ApplicationInit();
    private final BookRepository bookRepository = init.bookRepository();

    @Test
    public void createBook() throws Exception {
        //given
        Long long1 = bookRepository.save(new Book(Sequence.getSequence(), "book " + 1, "author " + 1, 3000 * 4, 1));
        Long long2 = bookRepository.save(new Book(Sequence.getSequence(), "book " + 2, "author " + 2, 3000 * 5, 2));
        Long long3 = bookRepository.save(new Book(Sequence.getSequence(), "book " + 3, "author " + 3, 3000 * 6, 3));
        Long long4 = bookRepository.save(new Book(Sequence.getSequence(), "book " + 4, "author " + 4, 3000 * 7, 4));
        Long long5 = bookRepository.save(new Book(Sequence.getSequence(), "book " + 5, "author " + 5, 3000 * 8, 5));


        //when
        List<Book> books = bookRepository.findAll();

        //then
        assertThat(books).extracting("id").contains(long1,long2, long3, long4, long5);

    }
}
