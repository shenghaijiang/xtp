package cn.xtits.xtp.dto;

import cn.xtits.xtp.entity.Organize;

import java.util.List;

/**
 * @fileName: OrganizeDto
 * @author: Dan
 * @createDate: 2018-03-06 14:17.
 * @description:
 */
public class OrganizeDto extends Organize {

    private List<OrganizeDto> children;

    public List<OrganizeDto> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizeDto> children) {
        this.children = children;
    }
}
