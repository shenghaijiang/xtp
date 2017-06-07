package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.MenuOperation;

import java.util.List;

/**
 * Created by HaiL on 2017/5/12.
 */
public class MenuDto {


    private Integer id;

    private Integer appId;

    private String code;

    private String name;

    private String url;

    private Integer parentId;

    private String icon;

    private Boolean parentFlag;

    private Boolean displayFlag;

    private Integer sort;

    private Integer type;

    private String appName;

    private List<MenuOperation> operationList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(Boolean parentFlag) {
        this.parentFlag = parentFlag;
    }

    public Boolean getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(Boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<MenuOperation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<MenuOperation> operationList) {
        this.operationList = operationList;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
