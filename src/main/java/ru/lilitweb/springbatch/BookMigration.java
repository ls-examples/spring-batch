package ru.lilitweb.springbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.lilitweb.springbatch.from.Book;
import ru.lilitweb.springbatch.from.BookRepository;
import ru.lilitweb.springbatch.from.Genre;
import ru.lilitweb.springbatch.from.User;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.IntStream;

@Component
public class BookMigration {
    private BookRepository repository;

    @Autowired
    public BookMigration(BookRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public String init() {

        IntStream.range(0,100).
                forEach(i -> {
                    repository.save(makeBook(String.format("book_%d", i)));
                });
        return "books created";
    }

    private Book makeBook(String name) {
       Book book = new  Book(name, 2017, "description", new User("Author for book " + name));
       book.setGenres(Arrays.asList(new Genre("type1"), new Genre("type2")));
       return  book;
    }


}
