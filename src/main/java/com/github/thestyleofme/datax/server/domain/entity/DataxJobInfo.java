package com.github.thestyleofme.datax.server.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/11 10:43
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

    //===============================================================================
    //  other
    //===============================================================================

    public static final String DATE_SPLIT_TYPE = "DATE";
    public static final String PK_SPLIT_TYPE = "PK";
    /**
     * reader分片必须传，因为需要使用到数据源
     */
    private Long syncId;
    /**
     * reader分片类型，取值[DATE/PK]
     */
    private String splitType = PK_SPLIT_TYPE;
    /**
     * reader分片字段，只能是时间或主键字段
     */
    private String splitCol;
    /**
     * 默认将一个job分成三个子job
     */
    private int splitNumber = 3;

    public static DataxJobInfo build(DataxJobInfo dataxJobInfo) {
        DataxJobInfo result = new DataxJobInfo();
        BeanUtils.copyProperties(dataxJobInfo,result);
        return result;
    }

}
