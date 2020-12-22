package com.github.thestyleofme.datax.server.app.service.impl;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.thestyleofme.datax.server.api.dto.DataxStatisticsDTO;
import com.github.thestyleofme.datax.server.app.service.DataxStatisticsService;
import com.github.thestyleofme.datax.server.domain.entity.DataxStatistics;
import com.github.thestyleofme.datax.server.infra.converter.BaseDataxStatisticsConvert;
import com.github.thestyleofme.datax.server.infra.mapper.DataxStatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/18 15:30
 * @since 1.0.0
 */
@Service
@Slf4j
public class DataxStatisticsServiceImpl extends ServiceImpl<DataxStatisticsMapper, DataxStatistics> implements DataxStatisticsService {

    @Override
    public IPage<DataxStatisticsDTO> list(Page<DataxStatistics> page, DataxStatisticsDTO dataxStatisticsDTO) {
        QueryWrapper<DataxStatistics> queryWrapper = new QueryWrapper<>(
                BaseDataxStatisticsConvert.INSTANCE.dtoToEntity(dataxStatisticsDTO));
        Page<DataxStatistics> entityPage = page(page, queryWrapper);
        final Page<DataxStatisticsDTO> dtoPage = new Page<>();
        org.springframework.beans.BeanUtils.copyProperties(entityPage, dtoPage);
        dtoPage.setRecords(entityPage.getRecords().stream()
                .map(BaseDataxStatisticsConvert.INSTANCE::entityToDTO)
                .collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataxStatisticsDTO save(DataxStatisticsDTO dataxStatisticsDTO) {
        DataxStatistics entity = BaseDataxStatisticsConvert.INSTANCE.dtoToEntity(dataxStatisticsDTO);
        saveOrUpdate(entity);
        return BaseDataxStatisticsConvert.INSTANCE.entityToDTO(entity);
    }
}
