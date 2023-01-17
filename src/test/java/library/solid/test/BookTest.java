package library.solid.test;

import library.solid.ApplicationInit;
import library.solid.domain.Book;
import library.solid.domain.Sequence;
import library.solid.repository.BookRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BookTest {

    ApplicationInit init = new ApplicationInit();
    private final BookRepository bookRepository = init.bookRepository();

    @Test
    public void createBook() {
        /****** given - 책 생성 *************/
        Book book1 = Book.createBook(Sequence.getSequence(), "book1", "author1", 12000, 1);
        Long long1 = bookRepository.save(book1);
        /****** given - 책 생성 *************/

        /****** when - 책 조회 *************/
        Book findBook = bookRepository.findById(long1);
        /****** when - 책 조회 *************/

        /****** then - 책 비교 *************/
        assertThat(findBook).isEqualTo(book1);
        /****** then - 책 비교 *************/
    }
}
