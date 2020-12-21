package com.github.thestyleofme;

import org.junit.Assert;
import org.junit.Test;
import org.noear.snack.ONode;

/**
 * <p>
 * Snack3 Json path测试
 * </p>
 *
 * @author isaac 2020/12/16 16:58
 * @since 1.0.0
 */
public class Snack3Test {

    private final String testJson = "{\n" +
            "  \"job\": {\n" +
            "    \"setting\": {\n" +
            "      \"speed\": {\n" +
            "        \"channel\": 3\n" +
            "      },\n" +
            "      \"errorLimit\": {\n" +
            "        \"record\": 0,\n" +
            "        \"percentage\": 0.02\n" +
            "      }\n" +
            "    },\n" +
            "    \"content\": [\n" +
            "      {\n" +
            "        \"reader\": {\n" +
            "          \"name\": \"mysqlreader\",\n" +
            "          \"parameter\": {\n" +
            "            \"username\": \"hdsp_dev\",\n" +
            "            \"password\": \"hdsp_dev123$%^\",\n" +
            "            \"splitPk\": \"\",\n" +
            "            \"column\": [\n" +
            "              \"id\",\n" +
            "              \"name\",\n" +
            "              \"sex\",\n" +
            "              \"phone\",\n" +
            "              \"address\",\n" +
            "              \"education\",\n" +
            "              \"state\"\n" +
            "            ],\n" +
            "            \"connection\": [\n" +
            "              {\n" +
            "                \"table\": [\n" +
            "                  \"resume\"\n" +
            "                ],\n" +
            "                \"jdbcUrl\": [\n" +
            "                  \"jdbc:mysql://172.23.16.63:23306/hdsp_test?useUnicode=true&characterEncoding=utf-8&useSSL=false\"\n" +
            "                ]\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"writer\": {\n" +
            "          \"name\": \"mysqlwriter\",\n" +
            "          \"parameter\": {\n" +
            "            \"writeMode\": \"replace\",\n" +
            "            \"username\": \"hdsp_dev\",\n" +
            "            \"password\": \"hdsp_dev123$%^\",\n" +
            "            \"batchSize\": 1024,\n" +
            "            \"column\": [\n" +
            "              \"id\",\n" +
            "              \"name\",\n" +
            "              \"sex\",\n" +
            "              \"phone1\",\n" +
            "              \"address\",\n" +
            "              \"education\",\n" +
            "              \"state\"\n" +
            "            ],\n" +
            "            \"session\": [\n" +
            "            ],\n" +
            "            \"preSql\": [\n" +
            "            ],\n" +
            "            \"connection\": [\n" +
            "              {\n" +
            "                \"table\": [\n" +
            "                  \"resume_bak\"\n" +
            "                ],\n" +
            "                \"jdbcUrl\": \"jdbc:mysql://172.23.16.63:23306/hdsp_test?useUnicode=true&characterEncoding=utf-8&useSSL=false\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"postSql\": [\n" +
            "            ]\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}\n";

    @Test
    public void testSelect() {
        ONode oNode = ONode.loadStr(testJson);
        String table = oNode.select("$.job.content[0].reader.parameter.connection[0].table[0]").getString();
        Assert.assertEquals("resume", table);
    }

    @Test
    public void testPush() {
        ONode oNode = ONode.loadStr(testJson);
        ONode parameterNode = oNode.select("$.job.content[0].reader.parameter");
        parameterNode.set("where","id >= 1 and id < 1000");
        System.out.println(oNode.toJson());
        Assert.assertEquals("id >= 1 and id < 1000",parameterNode.getOrNull("where").getString());
    }
}
