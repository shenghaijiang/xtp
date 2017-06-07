package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.Menu;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/15.
 */
public class UserMenuDto {
    List<Menu> parentMenu;
    List<Menu> allMenu;
    List<Menu> appFavoriteMenu;
    List<Menu> userFavoriteMenu;

    public List<Menu> getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(List<Menu> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<Menu> getAllMenu() {
        return allMenu;
    }

    public void setAllMenu(List<Menu> allMenu) {
        this.allMenu = allMenu;
    }

    public List<Menu> getAppFavoriteMenu() {
        return appFavoriteMenu;
    }

    public void setAppFavoriteMenu(List<Menu> appFavoriteMenu) {
        this.appFavoriteMenu = appFavoriteMenu;
    }

    public List<Menu> getUserFavoriteMenu() {
        return userFavoriteMenu;
    }

    public void setUserFavoriteMenu(List<Menu> userFavoriteMenu) {
        this.userFavoriteMenu = userFavoriteMenu;
    }
}
