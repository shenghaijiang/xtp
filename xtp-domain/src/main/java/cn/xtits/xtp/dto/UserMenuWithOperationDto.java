package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.Menu;

import java.util.List;

/**
 * Created by ShengHaiJiang on 2017/3/15.
 */
public class UserMenuWithOperationDto {
    List<MenuWithOperationDto> parentMenu;
    List<MenuWithOperationDto> allMenu;
    List<MenuWithOperationDto> appFavoriteMenu;
    List<MenuWithOperationDto> userFavoriteMenu;

    public List<MenuWithOperationDto> getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(List<MenuWithOperationDto> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<MenuWithOperationDto> getAllMenu() {
        return allMenu;
    }

    public void setAllMenu(List<MenuWithOperationDto> allMenu) {
        this.allMenu = allMenu;
    }

    public List<MenuWithOperationDto> getAppFavoriteMenu() {
        return appFavoriteMenu;
    }

    public void setAppFavoriteMenu(List<MenuWithOperationDto> appFavoriteMenu) {
        this.appFavoriteMenu = appFavoriteMenu;
    }

    public List<MenuWithOperationDto> getUserFavoriteMenu() {
        return userFavoriteMenu;
    }

    public void setUserFavoriteMenu(List<MenuWithOperationDto> userFavoriteMenu) {
        this.userFavoriteMenu = userFavoriteMenu;
    }
}
