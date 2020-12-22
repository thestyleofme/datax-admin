package com.github.thestyleofme.datax.server.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/17 20:36
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ApiModel("datax同步任务表")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("datax_sync")
public class DataxSync {

    public static final String FIELD_SYNC_ID = "sync_id";

    @TableId(type = IdType.AUTO)
    private Long syncId;
    @ApiModelProperty(value = "同步名称")
    private String syncName;
    @ApiModelProperty(value = "同步类型(OPTION:手工配置/IMPORT:脚本配置)")
    private String syncType;
    @ApiModelProperty(value = "来源数据源类型")
    private String sourceDatasourceType;
    @ApiModelProperty(value = "来源数据源编码")
    private String sourceDatasourceCode;
    @ApiModelProperty(value = "来源数据库")
    private String sourceSchema;
    @ApiModelProperty(value = "来源表（或视图/文件路径）")
    private String sourceTable;
    @ApiModelProperty(value = "写入数据源类型")
    private String writeDatasourceType;
    @ApiModelProperty(value = "写入数据源编码")
    private String writeDatasourceCode;
    @ApiModelProperty(value = "写入数据库")
    private String writeSchema;
    @ApiModelProperty(value = "写入表（文件路径）")
    private String writeTable;
    @ApiModelProperty(value = "来源对象类型 TABLE/VIEW/SQL")
    private String sourceObjectType;
    @ApiModelProperty(value = "来源sql")
    private String querySql;
    @ApiModelProperty(value = "datax json信息")
    private String settingInfo;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;
    @ApiModelProperty(hidden = true)
    @Version
    private Long objectVersionNumber;
    @ApiModelProperty(hidden = true)
    private LocalDateTime creationDate;
    @ApiModelProperty(hidden = true)
    private Long createdBy;
    @ApiModelProperty(hidden = true)
    private LocalDateTime lastUpdateDate;
    @ApiModelProperty(hidden = true)
    private Long lastUpdatedBy;
}