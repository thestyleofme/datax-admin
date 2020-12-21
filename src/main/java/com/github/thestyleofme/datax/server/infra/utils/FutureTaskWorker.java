package com.github.thestyleofme.datax.server.infra.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/16 15:51
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class FutureTaskWorker<T, R> {

    /**
     * 需要异步执行的任务
     */
    private List<T> taskList;

    /**
     * 需要执行的方法
     */
    private Function<T, CompletableFuture<R>> workFunction;

    public CompletableFuture<Void> getAllCompletableFuture(){
        return CompletableFuture.allOf(taskList.stream().map(workFunction).toArray(CompletableFuture[]::new));
    }

    public List<CompletableFuture<R>> getFutureList(){
        return taskList.stream().map(workFunction).collect(Collectors.toList());
    }
}
