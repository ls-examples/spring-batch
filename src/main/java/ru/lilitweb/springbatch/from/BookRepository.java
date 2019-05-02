package ru.lilitweb.springbatch.from;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends PagingAndSortingRepository<Book, Long> {
    @Override
    Page<Book> findAll(Pageable pageable);


}
