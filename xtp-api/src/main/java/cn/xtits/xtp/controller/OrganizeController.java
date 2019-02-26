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
import cn.xtits.xtp.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions({"organize:insert"})
    @RequestMapping(value = "insertOrganize")
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

    @RequiresPermissions({"organize:delete"})
    @RequestMapping(value = "deleteOrganize")
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

    @RequiresPermissions({"organize:update"})
    @RequestMapping(value = "updateOrganize")
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

    //@RequiresPermissions({"organize:list"})
    @RequestMapping(value = "listOrganize")
    @ResponseBody
    public AjaxResult listOrganize(
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "code", required = false) String code) {
        OrganizeExample example = new OrganizeExample();
        example.setPageIndex(pageIndex);
        example.setPageSize(pageSize);
        OrganizeExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        if (parentId != null) {
            criteria.andParentIdEqualTo(parentId);
        }
        if(StringUtils.isNotBlank(code)){
            criteria.andCodeLike(code);
        }
        List<Organize> list = service.listByExample(example);
        Pagination<Organize> pList = new Pagination<>(example, list, example.getCount());
        return new AjaxResult(pList);
    }

    @RequiresPermissions({"organize:insert"})
    @RequestMapping(value = "getOrganize")
    public AjaxResult getOrganize(
            @RequestParam(value = "id", required = false) Integer id) {
        Organize res = service.getByPrimaryKey(id);
        return new AjaxResult(res);
    }

    /**
     * 树型结构
     *
     * @return
     */
    //@RequiresPermissions({"organize:list"})
    @RequestMapping(value = "listOrganizeTree")
    @ResponseBody
    public AjaxResult listOrganizeWithDetailsTree() {
        OrganizeExample example = new OrganizeExample();
        example.setPageSize(Integer.MAX_VALUE);
        OrganizeExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteFlagEqualTo(false);
        List<Organize> list = service.listByExample(example);
        List<OrganizeDto> organizeDtoList = new ArrayList<>();
        for (Organize organize : list) {
            OrganizeDto organizeDto = new OrganizeDto();
            MapperUtil.copyProperties(organize, organizeDto);
            organizeDtoList.add(organizeDto);
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
    //@RequiresPermissions({"organize:list"})
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
                record.setChildren(getTreeChildRecord(listNotParentRecord, record.getId()));
            }
        }
        return listParentRecord;
    }

    //@RequiresPermissions({"organize:list"})
    @RequestMapping(value = "getOrganizeCoding")
    @ResponseBody
    public AjaxResult getOrganizeCoding(
            @RequestParam(value = "code", required = false) String code) {
        List<Organize> organizeList = null;
        {
            OrganizeExample example = new OrganizeExample();
            example.setPageSize(1);
            OrganizeExample.Criteria criteria = example.createCriteria();
            criteria.andDeleteFlagEqualTo(false);
            criteria.andCodeEqualTo(code);
            //example.setOrderByClause("code desc");
            organizeList = service.listByExample(example);
        }
        {
            if (organizeList != null && organizeList.size() > 0) {
                Organize organize = organizeList.get(0);
                OrganizeExample example = new OrganizeExample();
                example.setPageSize(Integer.MAX_VALUE);
                OrganizeExample.Criteria criteria = example.createCriteria();
                criteria.andDeleteFlagEqualTo(false);
                criteria.andParentIdEqualTo(organize.getId());
                example.setOrderByClause("code desc");
                List<Organize> organizesCodeList = service.listByExample(example);

                if (organizesCodeList != null && organizesCodeList.size() > 0) {
                    Organize organizeCode = organizesCodeList.get(0);
                    String oldCode = organizeCode.getCode();
                    if (oldCode.startsWith(code)) {
                        String strCode = oldCode.substring(code.length(), oldCode.length());
                        int i = 0;
                        try {
                            i = Integer.parseInt(strCode);
                            return new AjaxResult(code + CommonUtil.getSplitCode(i + 1));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return new AjaxResult(ErrorCodeEnums.SERVER_ERROR.value, "编码格式错误...");
                        }
                    } else {
                        return new AjaxResult(ErrorCodeEnums.SERVER_ERROR.value, "编码异常?");
                    }

                } else {
                    String newCode = code + CommonUtil.getSplitCode(1);

                    return new AjaxResult(newCode);
                }

            } else {
                return new AjaxResult(ErrorCodeEnums.SERVER_ERROR.value, "上级编码不存在?");
            }
        }

    }



}