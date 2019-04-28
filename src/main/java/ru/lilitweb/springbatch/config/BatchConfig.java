package ru.lilitweb.springbatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.lilitweb.springbatch.from.Book;
import ru.lilitweb.springbatch.from.BookRepository;
import ru.lilitweb.springbatch.to.MongoAuthor;
import ru.lilitweb.springbatch.to.MongoBook;
import ru.lilitweb.springbatch.to.MongoGenre;

import java.util.HashMap;
import java.util.stream.Collectors;


@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {
    private final Logger logger = LoggerFactory.getLogger("Batch");


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Book> reader(BookRepository repository) {
        return new RepositoryItemReaderBuilder<Book>()
                .name("bookReader")
                .pageSize(10)
                .sorts(new HashMap<>())
                .repository(repository)
                .methodName("findAll")
                .build();
    }

    @Bean
    public ItemProcessor processor() {
        return (ItemProcessor<Book, MongoBook>) book -> {
            MongoBook mongoBook = new MongoBook(
                    book.getTitle(),
                    book.getYear(),
                    book.getDescription(),
                    book.getAuthor() != null ? new MongoAuthor(book.getAuthor().getFullname()) : null
            );

            mongoBook.setGenres(book.getGenres()
                    .stream()
                    .map(
                            g -> new MongoGenre(g.getName())
                    )
                    .collect(Collectors.toList())
            );

            return mongoBook;
        };
    }

    @Bean
    public ItemWriter<MongoBook> writer(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBook>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step step1(ItemWriter writer, ItemReader reader, ItemProcessor itemProcessor) {
        return stepBuilderFactory.get("step1")
                .chunk(5)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
