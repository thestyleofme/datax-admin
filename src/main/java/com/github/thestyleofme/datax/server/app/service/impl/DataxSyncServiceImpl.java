package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.app.service.DataxSyncService;
import com.github.thestyleofme.datax.server.domain.entity.DataxSync;
import com.github.thestyleofme.datax.server.infra.converter.BaseDataxSyncConvert;
import com.github.thestyleofme.datax.server.infra.mapper.DataxSyncMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/18 15:30
 * @since 1.0.0
 */
@Service
@Slf4j
public class DataxSyncServiceImpl extends ServiceImpl<DataxSyncMapper, DataxSync>  implements DataxSyncService {

    @Override
    public IPage<DataxSyncDTO> list(Page<DataxSync> page, DataxSyncDTO dataxSyncDTO) {
        QueryWrapper<DataxSync> queryWrapper = new QueryWrapper<>(
                BaseDataxSyncConvert.INSTANCE.dtoToEntity(dataxSyncDTO));
        Page<DataxSync> entityPage = page(page, queryWrapper);
        final Page<DataxSyncDTO> dtoPage = new Page<>();
        org.springframework.beans.BeanUtils.copyProperties(entityPage, dtoPage);
        dtoPage.setRecords(entityPage.getRecords().stream()
                .map(BaseDataxSyncConvert.INSTANCE::entityToDTO)
                .collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataxSyncDTO save(DataxSyncDTO dataxSyncDTO) {
        DataxSync entity = BaseDataxSyncConvert.INSTANCE.dtoToEntity(dataxSyncDTO);
        saveOrUpdate(entity);
        return BaseDataxSyncConvert.INSTANCE.entityToDTO(entity);
    }
}
