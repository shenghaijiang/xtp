package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.MenuOperation;

import java.util.List;

/**
 * Created by HaiL on 2017/5/12.
 */
public class MenuWithOperationDto {

    private Integer id;

    private String code;

    private String name;

    private String url;

    private String icon;

    private Integer parentId;

    private Integer operationId;

    private Boolean parentFlag;

    private String operationCoce;

    private String operationName;

    private List<MenuOperation> operationList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public Boolean getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(Boolean parentFlag) {
        this.parentFlag = parentFlag;
    }

    public String getOperationCoce() {
        return operationCoce;
    }

    public void setOperationCoce(String operationCoce) {
        this.operationCoce = operationCoce;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public List<MenuOperation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<MenuOperation> operationList) {
        this.operationList = operationList;
    }
}
