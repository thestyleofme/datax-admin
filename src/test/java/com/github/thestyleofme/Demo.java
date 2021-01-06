package com.github.thestyleofme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/12/26 20:22
 * @since 1.0.0
 */
public class Demo {

    public static void main(String[] args) throws IOException {
        HJ5();
    }

    /**
     * 16进制转10进制
     */
    private static void HJ5() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String input;
        while ((input = bf.readLine()) != null) {
            String temp = input.substring(2);
            int sum = 0;
            int length = temp.length();
            for (int i = length - 1; i >= 0; i--) {
                int tempNum = temp.charAt(i);
                if (tempNum >= 65) {
                    tempNum = tempNum - 65 + 10; //A 65
                } else {
                    tempNum = tempNum - 48;// 0 48
                }
                sum = sum + (int) Math.pow(16, length - i - 1) * tempNum;
            }
            System.out.println(sum);
        }
    }

    /**
     * 连续输入字符串，请按长度为8拆分每个字符串后输出到新的字符串数组；
     * 长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。
     * 输入：
     * abc
     * 123456789
     * 输出：
     * abc00000
     * 12345678
     * 90000000
     */
    private static void HJ4() {
        Scanner sc = new Scanner(System.in);
        List<String> list;
        while (sc.hasNext()) {
            list = new ArrayList<>();
            String line = sc.nextLine();
            if (line.length() <= 8) {
                fillZero(list, line);
            } else {
                char[] chars = line.toCharArray();
                int number = chars.length / 8;
                for (int i = 0; i < number * 8; i += 8) {
                    list.add(String.valueOf(chars, i, 8));
                }
                if (8 * number != chars.length) {
                    fillZero(list, String.valueOf(chars, 8 * number, chars.length - 8 * number));
                }
            }
            list.forEach(System.out::println);
        }
    }

    private static void fillZero(List<String> list, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < 8 - line.length(); i++) {
            sb.append("0");
        }
        list.add(sb.toString());
    }

    /**
     * 计算字符串最后一个单词的长度，单词以空格隔开。
     */
    private static void HJ1() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.length() < 5000) {
                String[] words = line.split(" ");
                if (words.length > 1) {
                    System.out.println(words[words.length - 1].length());
                } else {
                    System.out.println(words[0].length());
                }
            }
        }
    }

    /**
     * 接受一个由字母、数字和空格组成的字符串，和一个字母，然后输出输入字符串中该字母的出现次数。
     */
    private static void HJ2() {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine().toLowerCase();
        String w = sc.nextLine().toLowerCase();
        System.out.println(line.length() - line.replace(w, "").length());
    }

    /**
     * n n个1000以内数去重排序
     */
    private static void HJ3() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = bf.readLine()) != null) {
            boolean[] container = new boolean[1001];
            StringBuilder sb = new StringBuilder();
            try {
                int n = Integer.parseInt(str);
                for (int i = 0; i < n; i++) {
                    container[Integer.parseInt(bf.readLine())] = true;
                }
            } catch (NumberFormatException e) {
                continue;
            }
            for (int i = 1; i < 1001; i++) {
                if (container[i]) {
                    sb.append(i).append("\n");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb.toString());
        }
    }
}
