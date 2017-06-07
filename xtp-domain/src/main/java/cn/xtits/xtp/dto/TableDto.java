package cn.xtits.xtp.dto;

import java.util.List;

/**
 * Created by HaiL on 2017/5/16.
 */
public class TableDto {
    private String tableName;

    private List<ColumnDto> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDto> columns) {
        this.columns = columns;
    }
}
