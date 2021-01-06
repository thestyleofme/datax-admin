package com.github.thestyleofme;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2021/01/04 14:18
 * @since 1.0.0
 */
public class Test01 {

    /**
     * 给定一个整数数组，求其中出现次数最多的数
     */
    @Test
    public void test01() {
        // (1,1) (3,1)
        List<Integer> list = Stream.of(1, 3, 5, 4, 7, 7, 6, 7).collect(Collectors.toList());
        HashMap<Integer, Integer> map = new HashMap<>();
        list.forEach(integer -> {
                    if (map.containsKey(integer)) {
                        // key +1
                        map.put(integer, map.get(integer) + 1);
                    } else {
                        map.put(integer, 1);
                    }
                }
        );
        map.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .ifPresent(o -> {
                    System.out.println("最多出现次数的值: " + o.getKey() + ", 出现次数： " + o.getValue());
                });
    }

    @Test
    public void test02() {
        // 模仿矩阵
        TreeMap<Integer, List<Integer>> treeMap = new TreeMap<>();
        treeMap.put(0,new ArrayList<>(Arrays.asList(1,4,7)));
        treeMap.put(1,new ArrayList<>(Arrays.asList(2,5,8)));
        treeMap.put(2,new ArrayList<>(Arrays.asList(3,6,9)));
        SortedMap<Integer, List<Integer>> result = new TreeMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : treeMap.entrySet()) {
            List<Integer> temp = entry.getValue();
            Collections.reverse(temp);
            result.put(entry.getKey(),temp);
        }
        result.forEach((integer, integers) -> System.out.println(integers));
    }

}
