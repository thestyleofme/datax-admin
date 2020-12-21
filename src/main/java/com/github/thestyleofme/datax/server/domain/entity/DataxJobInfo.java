package com.github.thestyleofme.datax.server.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/11 10:43
 * @since 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataxJobInfo {

    /**
     * mode/jobId/job是datax规定的三个参数
     * <p>
     * jobJson是自行添加的，可直接写json信息进行同步
     */
    private String mode = "standalone";
    private Long jobId = -1L;
    /**
     * 其实是json配置文件的路径
     */
    private String job;
    private String jobJson;

}
