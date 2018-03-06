package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.utils.MapperUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.dto.OrganizeDto;
import cn.xtits.xtp.entity.Organize;
import cn.xtits.xtp.entity.OrganizeExample;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.OrganizeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/organize")
public class OrganizeController extends BaseController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private OrganizeService service;

    @RequestMapping(value = "insertOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult insertOrganize(
            @RequestParam(value = "data", required = false) String data) {
        Organize record = JsonUtil.fromJson(data, Organize.class);
        Date dt = getDateNow();
        record.setCreateDate(dt);
        record.setMakeBillMan(getUserName());
        record.setModifier(getUserName());
        record.setModifyDate(dt);
        record.setDeleteFlag(false);
        service.insert(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "deleteOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult deleteOrganize(
            @RequestParam(value = "id", required = false) int id) {
        Organize record = new Organize();
        record.setId(id);
        record.setDeleteFlag(true);
        record.setModifier(getUserName());
        record.setModifyDate(getDateNow());
        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "updateOrganize", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult updateOrganize(
            @RequestParam(value = "data", required = false) String data) {
        Organize record = JsonUtil.fromJson(data, Organize.class);
        record.setCreateDate(null);
        record.setMakeBillMan(null);
        record.setModifyDate(getDateNow());
        record.setModifier(getUserName());
        record.setDeleteFlag(false);
        service.updateByPrimaryKeySelective(record);
        return new AjaxResult(ErrorCodeEnums.NO_ERROR.value);
    }

    @RequestMapping(value = "listOrganize")
    @ResponseBody
    public AjaxResult listOrganize(
            @RequestParam(value = "parentId", required = false) String parentId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        OrganizeExample example = new OrganizeExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        OrganizeExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        if (StringUtils.isNotBlank(parentId)) {
            criteria.andParentIdEqualTo(parentId);
        }
        List<Organize> list = service.listByExample(example);
        Pagination<Organize> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

    /**
     * 树型结构
     *
     * @return
     */
    @RequestMapping(value = "listOrganizeWithDetailsTree")
    @ResponseBody
    public AjaxResult listAccessoryTypeWithDetailsTree() {
        OrganizeExample example = new OrganizeExample();
        example.setPageSize(Integer.MAX_VALUE);
        OrganizeExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        List<Organize> list = service.listByExample(example);
        List<OrganizeDto> organizeDtoList = new ArrayList<>();
        for (Organize organize : list) {
            OrganizeDto materialCategoryDto = new OrganizeDto();
            MapperUtil.copyProperties(organize, materialCategoryDto);
            organizeDtoList.add(materialCategoryDto);
        }
        List<OrganizeDto> treeChildRecord = getTreeChildRecord(organizeDtoList, 0);

        return new AjaxResult(treeChildRecord);
    }

    /**
     * 说明方法描述：递归查询子节点
     *
     * @param allList  所有节点
     * @param parentId 父节点id
     * @time 2017-9-26
     * @author Dan
     */
    private List<OrganizeDto> getTreeChildRecord(List<OrganizeDto> allList, Integer parentId) {
        List<OrganizeDto> listParentRecord = new ArrayList<>();
        List<OrganizeDto> listNotParentRecord = new ArrayList<>();
        // allList，找出所有的根节点和非根节点
        if (allList != null && allList.size() > 0) {
            for (OrganizeDto record : allList) {
                // 对比找出父节点
                if (record.getParentId().equals(parentId)) {
                    listParentRecord.add(record);
                } else {
                    listNotParentRecord.add(record);
                }
            }
        }
        // 查询子节点
        if (listParentRecord.size() > 0) {
            for (OrganizeDto record : listParentRecord) {
                // 递归查询子节点
                record.setChildrenList(getTreeChildRecord(listNotParentRecord, record.getId()));
            }
        }
        return listParentRecord;
    }

}