package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.RoleMenu;

/**
 * Created by HaiL on 2017/5/20.
 */
public class RoleMenuDto extends RoleMenu {
    private String menuName;
    private String menuCode;
    private Integer parentId;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
