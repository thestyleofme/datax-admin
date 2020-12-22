package com.github.thestyleofme.datax.server.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * <p>
 * 数据源表setting_info字段解析类
 * </p>
 *
 * @author thestyleofme 2020/12/21 13:48
 * @since 1.0.0
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataSourceSettingInfo {

    private String username;
    private String password;
    private String jdbcUrl;
    private String protocol;
    private String host;
    private String port;

}
