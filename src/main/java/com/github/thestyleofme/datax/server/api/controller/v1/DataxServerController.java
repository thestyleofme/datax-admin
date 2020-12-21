package com.github.thestyleofme.datax.server.api.controller.v1;

import java.util.List;

import com.github.thestyleofme.datax.server.app.service.DataxServerService;
import com.github.thestyleofme.datax.server.domain.entity.DataxJobInfo;
import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.datax.server.domain.entity.Result;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/14 16:59
 * @since 1.0.0
 */
@RestController("dataxServerController.v1")
@RequestMapping("/v1/datax")
public class DataxServerController {

    private final DataxServerService dataxServerService;

    public DataxServerController(DataxServerService dataxServerService) {
        this.dataxServerService = dataxServerService;
    }

    @GetMapping("/index")
    public String index() {
        return dataxServerService.index();
    }

    @GetMapping("/nodes")
    public List<RegisterDataxInfo> getAllDataxNode() {
        return dataxServerService.getAllDataxNode();
    }

    @PostMapping("/submit")
    public List<Result<String>> executeDataxJobList(@RequestBody List<DataxJobInfo> dataxJobInfoList) {
        return dataxServerService.executeDataxJobList(dataxJobInfoList);
    }
}