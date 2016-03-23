package com.mystudy.web.controller;

import com.mystudy.web.common.BaseController;
import com.mystudy.web.model.EmployeeModel;
import com.mystudy.web.model.response.DataTableResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
            add(new EmployeeModel("Thor Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Tiger Nixon","System Architect","98540","20130811","New York","8327"));
            add(new EmployeeModel("Ashton Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("Cedric Walton","System","98540","20130811","New York","8327"));
            add(new EmployeeModel("Airi Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Brielle Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("Rhona Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("Herrod Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Chandler Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("Davidson Walton","Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Colleen Walton","System","98540","20130811","New York","8327"));
            add(new EmployeeModel("Sonya Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("Jena Walton","Junior Technical Author","98540","20130811","New York","8327"));
            add(new EmployeeModel("Gaines Walton","Senior Javascript Developer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Quinn Walton","Senior Marketing Designer","98540","20130811","New York","8327"));
            add(new EmployeeModel("Flynn Walton","Regional Director","98540","20130811","New York","8327"));
            add(new EmployeeModel("Charde Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("Haley Walton","Accountant","98540","20130811","Edinburgh","8327"));
            add(new EmployeeModel("Tatyana Walton","Developer","98540","20130811","London","8327"));
            add(new EmployeeModel("Fitzpatrick Walton","Accountant","98540","20130811","New York","8327"));
            add(new EmployeeModel("Michael Walton","Developer","98540","20130811","New York","8327"));
        }
    };



    /**
     * table ajax 提供数据
     */
    @RequestMapping(value = "/getTableData",method = RequestMethod.POST)
    @ResponseBody
    public Object getTableData(HttpServletRequest request){
        System.out.println(request.getParameterNames());
        DataTableResponse response = new DataTableResponse();
        int draw = Integer.parseInt(request.getParameter("draw"));
        response.setData(dataBase.subList(0,10));
        response.setDraw(draw++);
//        response.setError("error");
        response.setRecordsFiltered(21);
        response.setRecordsTotal(21);
        return response;
    }

}
