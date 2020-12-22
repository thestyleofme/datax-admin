package com.github.thestyleofme.datax.server.domain.entity.reader;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * DataX Reader
 * </p>
 *
 * @author thestyleofme 2019/04/29 14:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseReader {
    /**
     * DataX reader插件名称
     */
    protected String name;

    /**
     * reader表所在的数据库
     */
    protected String schema;
}
