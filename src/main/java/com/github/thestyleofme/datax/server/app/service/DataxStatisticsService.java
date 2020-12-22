package com.github.thestyleofme.datax.server.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.thestyleofme.datax.server.api.dto.DataxStatisticsDTO;
import com.github.thestyleofme.datax.server.domain.entity.DataxStatistics;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/18 15:27
 * @since 1.0.0
 */
public interface DataxStatisticsService extends IService<DataxStatistics> {

    /**
     * 分页条件查询datax任务执行统计信息
     *
     * @param page               分页
     * @param dataxStatisticsDTO DataxStatisticsDTO
     * @return IPage<DataxStatisticsDTO>
     */
    IPage<DataxStatisticsDTO> list(Page<DataxStatistics> page, DataxStatisticsDTO dataxStatisticsDTO);

    /**
     * 保存datax任务执行统计信息
     *
     * @param dataxStatisticsDTO DataxStatisticsDTO
     * @return DataxSyncDTO
     */
    DataxStatisticsDTO save(DataxStatisticsDTO dataxStatisticsDTO);
}
