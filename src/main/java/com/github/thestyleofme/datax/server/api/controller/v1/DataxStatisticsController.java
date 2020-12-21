package com.github.thestyleofme.datax.server.api.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.thestyleofme.datax.server.api.dto.DataxStatisticsDTO;
import com.github.thestyleofme.datax.server.app.service.DataxStatisticsService;
import com.github.thestyleofme.datax.server.domain.entity.DataxStatistics;
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
 * @author isaac 2020/12/18 15:21
 * @since 1.0.0
 */
@RestController("dataxStatisticsController.v1")
@RequestMapping("/v1/{organizationId}/datax-statistics")
@Slf4j
public class DataxStatisticsController {

    private final DataxStatisticsService dataxStatisticsService;

    public DataxStatisticsController(DataxStatisticsService dataxStatisticsService) {
        this.dataxStatisticsService = dataxStatisticsService;
    }

    @ApiOperation(value = "查询datax任务执行统计信息列表")
    @GetMapping
    public ResponseEntity<IPage<DataxStatisticsDTO>> list(@PathVariable(name = "organizationId") Long tenantId,
                                                          Page<DataxStatistics> page,
                                                          DataxStatisticsDTO dataxStatisticsDTO) {
        page.addOrder(OrderItem.desc(DataxStatistics.FIELD_ID));
        return ResponseEntity.ok(dataxStatisticsService.list(page, dataxStatisticsDTO));
    }

    @ApiOperation(value = "保存datax任务执行统计信息")
    @PostMapping
    public ResponseEntity<DataxStatisticsDTO> save(@PathVariable(name = "organizationId") Long tenantId,
                                                   @RequestBody DataxStatisticsDTO dataxStatisticsDTO) {
        return ResponseEntity.ok(dataxStatisticsService.save(dataxStatisticsDTO));
    }

    @ApiOperation(value = "删除datax任务执行统计信息")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "organizationId") Long tenantId,
                                       @PathVariable Long id) {
        dataxStatisticsService.removeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
