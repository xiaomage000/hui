package com.javaweb.servlet;

import com.javaweb.dao.CarDao;
import com.javaweb.pojo.Car;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * restful 风格的 Servlet
 */
public class CarServlet extends HttpServlet {
    /**
     * 查询所有:     http://127.0.0.1.8086/hui/CarServlet - get
     * 根据 id 查询: http://127.0.0.1.8086/hui/CarServlet?id=10 - get
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

        JSON json = null;
        CarDao dao = new CarDao();

        String id = request.getParameter("id");
        if (id == null || "".equals(id)) {
            // 查询所有
            List cars =  dao.find();
            json = JSONSerializer.toJSON(cars, jsonConfig);
        } else {
            // 根据 id 查询
            Car car = dao.find(Integer.parseInt(id));
            json = JSONSerializer.toJSON(car, jsonConfig);
        }
        out.println(json.toString());

        out.flush();
        out.close();
    }

    /**
     * 新增 http://127.0.0.1.8086/hui/CarServlet?id=10&name=xx - post
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        Double price = Double.parseDouble(request.getParameter("price"));
        String screateDate = request.getParameter("createDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date createDate = new Date();
        try {
            createDate = sdf.parse(screateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Car car = new Car();
        car.setName(name);
        car.setPrice(price);
        car.setCreateDate(createDate);

        int count = new CarDao().add(car);
        JSONObject jo = new JSONObject();
        jo.put("count", count);
        out.println(jo);

        out.flush();
        out.close();
    }

    /**
     * 修改 http://127.0.0.1.8086/hui/CarServlet?id=10&name=xx - put
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        Integer id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        Double price = Double.parseDouble(request.getParameter("price"));
        String screateDate = request.getParameter("createDate");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date createDate = new Date();
        try {
            createDate = sdf.parse(screateDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Car car = new Car();
        car.setId(id);
        car.setName(name);
        car.setPrice(price);
        car.setCreateDate(createDate);

        int count = new CarDao().modify(car);
        JSONObject jo = new JSONObject();
        jo.put("count", count);
        out.println(jo);

        out.flush();
        out.close();
    }

    /**
     * 删除 http://127.0.0.1.8086/hui/CarServlet?id=10 - delete
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();

        Integer id = Integer.parseInt(request.getParameter("id"));
        int count = new CarDao().remove(id);
        JSONObject jo = new JSONObject();
        jo.put("count", count);
        out.println(jo);

        out.flush();
        out.close();
    }
}