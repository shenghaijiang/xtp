package cn.xtits.xtp.controller;

import cn.xtits.xtf.common.utils.JsonUtil;
import cn.xtits.xtf.common.web.AjaxResult;
import cn.xtits.xtp.dto.ColumnDto;
import cn.xtits.xtp.dto.TableDto;
import cn.xtits.xtp.entity.App;
import cn.xtits.xtp.entity.AppExample;
import cn.xtits.xtp.enums.DictTypeEnums;
import cn.xtits.xtp.enums.ErrorCodeEnums;
import cn.xtits.xtp.query.Pagination;
import cn.xtits.xtp.service.AppService;
import cn.xtits.xtp.service.DatabaseService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import io.swagger.annotations.*;

//@Api(value ="/app",description = "")
@Controller
@RequestMapping("/database")
public class DatabaseController {

    Gson gson = new GsonBuilder().serializeNulls().create();

    @Autowired
    private DatabaseService service;

    @RequestMapping(value = "getDatabaseTable")
    @ResponseBody
    public AjaxResult listDictType() {
        String url = "url=jdbc:mysql://139.196.125.197:3306/xtp?ssl=false|username=xtf|password=`1234567890-=|driverClassName=com.mysql.jdbc.Driver|maxWait=5000";

        List<Map<String, Object>> list = service.listByExample("xtp", "mysql", url);

        List<TableDto> rList = new ArrayList<>();

        TableDto dto = new TableDto();

        for (Map<String, Object> map : list) {
            ColumnDto columnDto = new ColumnDto();
            columnDto.setColumnComment(map.get("columnComment").toString());
            columnDto.setColumnKey(map.get("columnKey").toString());
            columnDto.setColumnName(map.get("columnName").toString());
            columnDto.setDataType(map.get("dataType").toString());
            columnDto.setNullable(map.get("nullable").toString().equals("YES"));

            if (dto.getTableName() == null || !dto.getTableName().equals(map.get("tableName").toString())) {
                dto = new TableDto();
                dto.setColumns(new ArrayList<>());
                dto.setTableName(map.get("tableName").toString());
                rList.add(dto);
            }
            dto.getColumns().add(columnDto);
        }
        return new AjaxResult(rList);
    }
}