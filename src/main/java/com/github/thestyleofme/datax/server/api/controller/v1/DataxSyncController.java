package com.github.thestyleofme.datax.server.api.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.thestyleofme.datax.server.api.dto.DataxSyncDTO;
import com.github.thestyleofme.datax.server.app.service.DataxSyncService;
import com.github.thestyleofme.datax.server.domain.entity.DataxSync;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author thestyleofme 2020/12/18 15:21
 * @since 1.0.0
 */
@RestController("dataxSyncController.v1")
@RequestMapping("/v1/{organizationId}/datax-sync")
@Slf4j
public class DataxSyncController {

    private final DataxSyncService dataxSyncService;

    public DataxSyncController(DataxSyncService dataxSyncService) {
        this.dataxSyncService = dataxSyncService;
    }

    @ApiOperation(value = "查询datax同步任务列表")
    @GetMapping
    public ResponseEntity<IPage<DataxSyncDTO>> list(@PathVariable(name = "organizationId") Long tenantId,
                                                    Page<DataxSync> page,
                                                    DataxSyncDTO dataxSyncDTO) {
        dataxSyncDTO.setTenantId(tenantId);
        page.addOrder(OrderItem.desc(DataxSync.FIELD_SYNC_ID));
        return ResponseEntity.ok(dataxSyncService.list(page, dataxSyncDTO));
    }

    @ApiOperation(value = "保存datax同步任务")
    @PostMapping
    public ResponseEntity<DataxSyncDTO> save(@PathVariable(name = "organizationId") Long tenantId,
                                                 @RequestBody DataxSyncDTO dataxSyncDTO) {
        dataxSyncDTO.setTenantId(tenantId);
        return ResponseEntity.ok(dataxSyncService.save(dataxSyncDTO));
    }

    @ApiOperation(value = "删除datax同步任务")
    @DeleteMapping("/{syncId}")
    public ResponseEntity<Void> delete(@PathVariable(name = "organizationId")  Long tenantId,
                                       @PathVariable Long syncId) {
        dataxSyncService.removeById(syncId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
