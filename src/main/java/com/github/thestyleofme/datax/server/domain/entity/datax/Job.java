package com.github.thestyleofme.datax.server.domain.entity.datax;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * DataX job
 *
 * @author 奔波儿灞
 * @since 1.0
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Job {

    private Setting setting;

    private List<Content> content;

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Setting {

        /**
         * speed
         */
        private Speed speed;

        /**
         * errorLimit
         */
        private ErrorLimit errorLimit;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Speed {

        /**
         * record
         */
        private String record;

        /**
         * channel
         */
        private String channel;

        /**
         * byte
         */
        private String speedByte;

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorLimit {

        /**
         * record
         */
        private String record;

        /**
         * percentage
         */
        private String percentage;
    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Content {

        /**
         * reader
         */
        private Reader reader;

        /**
         * writer
         */
        private Writer writer;

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Reader {

        /**
         * name
         */
        private String name;

        /**
         * parameter
         */
        private Object parameter;

    }

    @Builder
    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Writer {

        /**
         * name
         */
        private String name;

        /**
         * parameter
         */
        private Object parameter;

    }

}
