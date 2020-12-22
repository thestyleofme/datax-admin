package com.github.thestyleofme.datax.server.infra.converter;

import com.github.thestyleofme.datax.server.api.dto.DataxStatisticsDTO;
import com.github.thestyleofme.datax.server.domain.entity.DataxStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/18 15:35
 * @since 1.0.0
 */
@Mapper
public abstract class BaseDataxStatisticsConvert {

    public static final BaseDataxStatisticsConvert INSTANCE = Mappers.getMapper(BaseDataxStatisticsConvert.class);

    /**
     * entityToDTO
     *
     * @param entity DataxStatistics
     * @return DataxStatisticsDTO
     */
    public abstract DataxStatisticsDTO entityToDTO(DataxStatistics entity);

    /**
     * dtoToEntity
     *
     * @param dto DataxStatisticsDTO
     * @return DataxStatistics
     */
    public abstract DataxStatistics dtoToEntity(DataxStatisticsDTO dto);

}
