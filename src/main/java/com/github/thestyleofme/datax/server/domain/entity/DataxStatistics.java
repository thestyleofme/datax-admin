package com.github.thestyleofme.datax.server.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/17 20:46
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel("datax同步任务执行统计表")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("datax_statistics")
public class DataxStatistics {

    public static final String FIELD_ID = "id";

    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "azkaban执行id")
    private Integer execId;
    @ApiModelProperty(value = "datax执行的json文件名")
    private String jsonFileName;
    @ApiModelProperty(value = "job名称")
    private String jobName;
    @ApiModelProperty(value = "reader插件名称")
    private String readerPlugin;
    @ApiModelProperty(value = "writer插件名称")
    private String writerPlugin;
    @ApiModelProperty(value = "任务启动时刻")
    private String startTime;
    @ApiModelProperty(value = "任务结束时刻")
    private String endTime;
    @ApiModelProperty(value = "任务总计耗时，单位s")
    private String totalCosts;
    @ApiModelProperty(value = "任务平均流量")
    private String byteSpeedPerSecond;
    @ApiModelProperty(value = "记录写入速度")
    private String recordSpeedPerSecond;
    @ApiModelProperty(value = "读出记录总数")
    private Long totalReadRecords;
    @ApiModelProperty(value = "读写失败总数")
    private Long totalErrorRecords;
    @ApiModelProperty(value = "datax执行的json路径")
    private String jobPath;
    @ApiModelProperty(value = "datax的json内容")
    private String jobContent;
    @ApiModelProperty(value = "脏数据即未同步成功的数据")
    private String dirtyRecords;
}