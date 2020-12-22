package com.github.thestyleofme.datax.server.infra.constant;

/**
 * <p>
 * hive 默认配置
 * </p>
 *
 * @author thestyleofme 2020/12/21 11:24
 * @since 1.0.0
 */
public final class HdfsConstants {

    private HdfsConstants() {
    }

    // hadoopConfig

    public static final String NAME_SERVICES = "nameServices";
    public static final String PROVIDER = "provider";
    public static final String RPC1 = "rpc1";
    public static final String RPC2 = "rpc2";
    public static final String NAME_NODES = "nameNodes";

    // hdfs config

    public static final String HDFS_URL = "hdfsUrl";
    public static final String WAREHOUSE = "warehouse";
    public static final String FORMAT = "format";
    public static final String DELIM = "delim";
    public static final String PARTITION_COLUMN = "partitionColumn";

    // s3 datasource Extra config key

    public static final String DS_S3_ENDPOINT = "s3Endpoint";
    public static final String DS_S3_ACCESS_KEY_ID = "s3AccessKey";
    public static final String DS_S3_ACCESS_KEY_SECRET = "s3SecretKey";

    // s3 config

    public static final String S3_ENDPOINT = "fs.s3a.endpoint";
    public static final String S3_ACCESS_KEY_ID = "fs.s3a.access.key";
    public static final String S3_ACCESS_KEY_SECRET = "fs.s3a.secret.key";

    // default value

    public static final String DEFAULT_DELIMITER = ",";
    public static final String DEFAULT_FORMAT = "text";
    public static final String DEFAULT_WRITE_MODE = "append";


}
