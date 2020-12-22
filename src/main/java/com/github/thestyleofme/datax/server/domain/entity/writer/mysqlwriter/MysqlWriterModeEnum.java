package com.github.thestyleofme.datax.server.domain.entity.writer.mysqlwriter;

/**
 * <p>
 * MysqlWriter Mode
 * </p>
 *
 * @author thestyleofme 2019/04/28 14:25
 */
public enum MysqlWriterModeEnum {
    /**
     * insert into
     */
    INSERT("insert"),
    /**
     * replace into
     */
    REPLACE("replace"),
    /**
     * on duplicate key update
     */
    UPDATE("replace");

    /**
     * mysql write mode
     */
    private final String writeMode;

    public String getWriteMode() {
        return writeMode;
    }

    MysqlWriterModeEnum(String writeMode) {
        this.writeMode = writeMode;
    }

}
