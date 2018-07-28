package com.xlong.tupin.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Entity;
import java.io.Serializable;


@Data
@Document(indexName = "blog", type = "summary")
public class Summary implements Serializable {
    @Id
    private Long id;

    @Field(type = FieldType.String, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;

    private String publicTime;

    private String theme;

    @Field(type = FieldType.String, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String blogSummary;

    private Long blogId;
}
