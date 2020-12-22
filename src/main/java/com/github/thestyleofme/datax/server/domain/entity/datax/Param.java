package com.github.thestyleofme.datax.server.domain.entity.datax;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * <p>
 * DataX json参数
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:16
 * @since 1.0.0
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Param {

    private Job job;

}
