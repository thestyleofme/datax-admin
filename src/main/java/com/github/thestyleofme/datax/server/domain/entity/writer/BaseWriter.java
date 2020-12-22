package com.github.thestyleofme.datax.server.domain.entity.writer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DataX Writer
 * </p>
 *
 * @author thestyleofme 2019/04/29 14:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseWriter {
    /**
     * DataX writer插件名称
     */
    protected String name;

    /**
     * writer表所在的数据库
     */
    protected String schema;
}
