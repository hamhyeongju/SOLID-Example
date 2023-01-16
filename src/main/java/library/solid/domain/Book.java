package library.solid.domain;

import lombok.Getter;

@Getter
public class Book {

    private Long id;
    private String name;
    private String author;
    private int price;
    private int stockQuantity;

    public static Book createBook(Long id, String name, String author, int price, int stockQuantity) {
        Book book = new Book();
        book.id = id;
        book.name = name;
        book.author = author;
        book.price = price;
        book.stockQuantity = stockQuantity;
        return book;
    }

    public void minusStockQuantity() {
        this.stockQuantity--;
    }

    public void plusStockQuantity() {
        this.stockQuantity++;
    }
}
