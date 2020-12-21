package com.github.thestyleofme.datax.server.app.service;

import java.util.List;

import com.github.thestyleofme.datax.server.domain.entity.DataxJobInfo;
import com.github.thestyleofme.datax.server.domain.entity.RegisterDataxInfo;
import com.github.thestyleofme.datax.server.domain.entity.Result;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/14 17:07
 * @since 1.0.0
 */
public interface DataxServerService {

    /**
     * 测试
     *
     * @return String
     */
    String index();

    /**
     * 获取所有datax node
     *
     * @return List<RegisterDataxInfo>
     */
    List<RegisterDataxInfo> getAllDataxNode();

    /**
     * 执行datax job集合
     *
     * @param dataxJobInfoList List<DataxJobInfo>
     * @return List<Result>
     */
    List<Result<String>> executeDataxJobList(List<DataxJobInfo> dataxJobInfoList);
}
