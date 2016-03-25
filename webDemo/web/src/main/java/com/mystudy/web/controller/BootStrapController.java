package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.mystudy.web.common.BaseController;
import com.mystudy.web.controller.util.DataTableConvertUtil;
import com.mystudy.web.model.EmployeeModel;
import com.mystudy.web.model.request.DataTableEditRequest;
import com.mystudy.web.model.request.DataTableRequest;
import com.mystudy.web.model.response.DataTableResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by 程祥 on 16/1/29.
 * Function：
 */
@Controller
@RequestMapping(value = "/bootstrap")
public class BootStrapController extends BaseController{

    @RequestMapping(value = "/fixedNav")
    public ModelAndView flxedNavBar(){
        ModelAndView mv = new ModelAndView("bootstrap/fixedNavbar");

        return mv;
    }

    @RequestMapping(value = "/carousel")
    public ModelAndView carousel(){
        ModelAndView mv = new ModelAndView("bootstrap/carousel");

        return mv;
    }

    @RequestMapping(value = "/t_{name}")
    public ModelAndView bootstrap(@PathVariable String name){
        System.out.println(name);
        ModelAndView mv = new ModelAndView("bootstrap/"+name);
        return mv;
    }

    private List<EmployeeModel> dataBase = new ArrayList<EmployeeModel>(){
        {
            add(new EmployeeModel("1","Thor Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("2","Tiger Nixon","System Architect","98540","20130811","New York","8327"));
            add(new EmployeeModel("3","Ashton Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("4","Cedric Walton","System","98540","20130811","New York","8327"));
            add(new EmployeeModel("5","Airi Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("6","Brielle Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("7","Rhona Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("8","Herrod Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("9","Chandler Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("10","Davidson Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("11","Colleen Walton","System","98540","20130811","New York","8327"));
            add(new EmployeeModel("12","Sonya Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("13","Jena Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("14","Gaines Walton","Senior Javascript Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("15","Quinn Walton","Senior Marketing Designer","98540","20130811","New York","8327"));
            add(new EmployeeModel("16","Flynn Walton","Regional Director","98540","20130811","New York","8327"));
            add(new EmployeeModel("17","Charde Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("18","Haley Walton","Accountant","98540","20130811","Edinburgh","8327"));
            add(new EmployeeModel("19","Tatyana Walton","Developer","98540","20130811","London","8327"));
            add(new EmployeeModel("20","Fitzpatrick Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("21","Michael Walton","Developer","98540","20130811","New York","8327"));
        }
    };



    /**
     * table ajax 提供数据
     */
    @RequestMapping(value = "/getTableData",method = RequestMethod.POST)
    @ResponseBody
    public Object getTableData(HttpServletRequest request,DataTableRequest params){
        params.setOrder_column(request.getParameter(DataTableRequest.ORDER_COLUMN));
        params.setOrder_dir(request.getParameter(DataTableRequest.ORDER_DIR));
        params.setSearch_regex(request.getParameter(DataTableRequest.SEARCH_REGEX));
        params.setSearch_value(request.getParameter(DataTableRequest.SEARCH_VALUE));
        params.setColumns_0_data(request.getParameter(DataTableRequest.COLUMNS_0_DATA));
        params.setColumns_0_name(request.getParameter(DataTableRequest.COLUMNS_0_NAME));
        params.setColumns_0_orderable(request.getParameter(DataTableRequest.COLUMNS_0_ORDERABLE));
        params.setColumns_0_searchable(request.getParameter(DataTableRequest.COLUMNS_0_SEARCHABLE));
        DataTableResponse<EmployeeModel> response = new DataTableResponse<EmployeeModel>();
        int draw = Integer.parseInt(request.getParameter("draw"));
        int start = params.getStart()==null?0:params.getStart();
        int end = start+params.getLength()>dataBase.size()?dataBase.size():start+params.getLength();
        Collections.sort(dataBase);
        response.setData(dataBase.subList(start, end));
        response.setDraw(draw++);
//        response.setError("error");
        response.setRecordsFiltered(dataBase.size());
        response.setRecordsTotal(dataBase.size());
        return response;
    }

    /**
     * table ajax 提供数据
     */
    @RequestMapping(value = "/updateTableData",method = RequestMethod.POST)
    @ResponseBody
    public Object updateTableData(HttpServletRequest request){
        DataTableResponse<EmployeeModel> response = new DataTableResponse<EmployeeModel>();
        try {
            DataTableEditRequest<EmployeeModel> paramObj = DataTableConvertUtil.converRest(
                    request,EmployeeModel.class);
            System.out.println(JSON.toJSONString(paramObj));
            if("edit".equals(paramObj.getAction())){
                updateData(paramObj.getDataList());
            }else if("create".equals(paramObj.getAction())){
                dataBase.add(paramObj.getDataList().get(0));
            }else {
                removeData(paramObj.getDataList());
            }

            response.setData(paramObj.getDataList());
        } catch (Exception e) {

            e.printStackTrace();
        }

        return response;
    }

    private void removeData(List<EmployeeModel> modelList){
        Map<String,EmployeeModel> map;
        try {
            map = listToMap(dataBase,EmployeeModel.class,"id");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if(map==null){
            return;
        }
        Iterator<EmployeeModel> it = modelList.iterator();
        while (it.hasNext()){
            EmployeeModel model = it.next();
            String key = model.getId();
            EmployeeModel oldModel = map.get(key);
            dataBase.remove(oldModel);
        }
    }

    private void updateData(List<EmployeeModel> modelList){
        Map<String,EmployeeModel> map;
        try {
             map = listToMap(dataBase,EmployeeModel.class,"id");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if(map==null){
            return;
        }
        Iterator<EmployeeModel> it = modelList.iterator();
        while (it.hasNext()){
            EmployeeModel model = it.next();
            String key = model.getId();
            EmployeeModel oldModel = map.get(key);
            oldModel.copyValue(model);
        }
    }

    private <T>Map<String,T> listToMap(List<T> list ,Class<T> c,String keyName) throws NoSuchFieldException, IllegalAccessException {
        Map<String,T> map = new HashMap<String, T>();
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()){
            T t = iterator.next();
            Field fieldObj=c.getDeclaredField(keyName);
            String key = String.valueOf(fieldObj.get(t));
            map.put(key,t);
        }
        return map;
    }


}
