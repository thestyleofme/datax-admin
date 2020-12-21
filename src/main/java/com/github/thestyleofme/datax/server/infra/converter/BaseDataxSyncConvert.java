package com.github.thestyleofme.datax.server.infra.converter;

import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.domain.entity.DataxSync;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/18 15:33
 * @since 1.0.0
 */
@Mapper
public abstract class BaseDataxSyncConvert {

    public static final BaseDataxSyncConvert INSTANCE = Mappers.getMapper(BaseDataxSyncConvert.class);

    /**
     * entityToDTO
     *
     * @param entity DataxSync
     * @return DataxSyncDTO
     */
    public abstract DataxSyncDTO entityToDTO(DataxSync entity);

    /**
     * dtoToEntity
     *
     * @param dto DataxSyncDTO
     * @return DataxSync
     */
    public abstract DataxSync dtoToEntity(DataxSyncDTO dto);

}
