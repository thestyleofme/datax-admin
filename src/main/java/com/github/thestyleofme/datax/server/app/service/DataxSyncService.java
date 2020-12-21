package com.github.thestyleofme.datax.server.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.domain.entity.DataxSync;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/18 15:27
 * @since 1.0.0
 */
public interface DataxSyncService extends IService<DataxSync> {

    /**
     * 分页条件查询datax同步任务
     *
     * @param page         分页
     * @param dataxSyncDTO DataxSyncDTO
     * @return IPage<DataxSyncDTO>
     */
    IPage<DataxSyncDTO> list(Page<DataxSync> page, DataxSyncDTO dataxSyncDTO);

    /**
     * 保存datax同步任务
     *
     * @param dataxSyncDTO DataxSyncDTO
     * @return DataxSyncDTO
     */
    DataxSyncDTO save(DataxSyncDTO dataxSyncDTO);
}
