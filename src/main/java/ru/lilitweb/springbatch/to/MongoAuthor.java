package ru.lilitweb.springbatch.to;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class MongoAuthor {
    @NonNull
    private String fullname;
}
