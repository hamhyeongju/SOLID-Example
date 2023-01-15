package library.solid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class Book {

    private Long id;
    private String name;
    private String author;
    private int price;
    private int stockQuantity;

}
