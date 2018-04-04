package com.javaweb.dao;

import com.javaweb.pojo.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * dao: data access object 数据访问对象: 封装对表 CAR 的所有访问 CRUD
 */
public class CarDao {
  private Connection conn;
  private PreparedStatement ps;
  private ResultSet rs;

  /**
   * 新增小汽车
   *
   * @param car
   * @return
   */
  public int add(Car car) {
    int count = 0;
    try {
      conn = DBUtil.getConnection();
      String sql = "insert into car(name,price,create_date) values(?,?,?)";
      ps = conn.prepareStatement(sql);
      ps.setString(1, car.getName());
      ps.setDouble(2, car.getPrice());
      ps.setDate(3, new java.sql.Date(car.getCreateDate().getTime()));

      count = ps.executeUpdate(); // 返回值 1 成功; 0 失败
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return count;
  }

  /**
   * 修改小汽车
   *
   * @param car
   * @return
   */
  public int modify(Car car) {
    int count = 0;
    try {
      conn = DBUtil.getConnection();
      String sql = "update car set name=?,price=?,create_date=? where id=?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, car.getName());
      ps.setDouble(2, car.getPrice());
      ps.setDate(3, new java.sql.Date(car.getCreateDate().getTime()));
      ps.setInt(4, car.getId());

      count = ps.executeUpdate(); // 返回值 1 成功; 0 失败
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return count;
  }

  /**
   * 根据 id 删除小汽车
   *
   * @param id
   * @return
   */
  public int remove(Integer id) {
    int count = 0;
    try {
      conn = DBUtil.getConnection();
      String sql = "DELETE  from car where id=? ";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, id);

      count = ps.executeUpdate(); // 返回值 1 成功; 0 失败
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return count;
  }

  /**
   * 根据 id 查询小汽车
   *
   * @param id
   * @return
   */
  public Car find(Integer id) {
    Car car = null;
    try {
      conn = DBUtil.getConnection();
      String sql = "select id,name,price,create_date from car where id=?";
      ps = conn.prepareStatement(sql);
      ps.setInt(1, id);

      rs = ps.executeQuery();
      if (rs.next()) {
        car = new Car();
        car.setId(rs.getInt(1)); // 根据字段索引获取值
        car.setName(rs.getString("name")); // 根据字段名获取值
        car.setPrice(rs.getDouble("price"));
        car.setCreateDate(rs.getDate("create_date"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return car;
  }

  /**
   * 登录
   *
   * @param username
   * @param password
   * @return
   */
  public Car login(String username, String password) {
    Car car = null;
    try {
      conn = DBUtil.getConnection();
      String sql = "select id,name,price,create_date from car where name=? and id=?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, username);
      ps.setInt(2, Integer.parseInt(password));

      rs = ps.executeQuery();
      if (rs.next()) {
        car = new Car();
        car.setId(rs.getInt(1)); // 根据字段索引获取值
        car.setName(rs.getString("name")); // 根据字段名获取值
        car.setPrice(rs.getDouble("price"));
        car.setCreateDate(rs.getDate("create_date"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return car;
  }

  /**
   * 查询所有小汽车
   *
   * @return
   */
  public List<Car> find() {
    List<Car> cars = new ArrayList<Car>();

    try {
      conn = DBUtil.getConnection();
      String sql = "select id,name,price,create_date from car";
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        Car car = new Car();
        car.setId(rs.getInt(1)); // 根据字段索引获取值
        car.setName(rs.getString("name")); // 根据字段名获取值
        car.setPrice(rs.getDouble("price"));
        car.setCreateDate(rs.getDate("create_date"));

        cars.add(car);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return cars;
  }

  /**
   * 查询所有小汽车 | 条件查询
   *
   * @param page     当前第 n 页
   * @param pagesize 一页显示 n 行数据
   * @param sort     排序字段
   * @param order    排序方式 (asc 升序 desc 降序)
   * @param name     条件参数
   * @param price
   * @return
   */
  public List<Car> find(
      Integer page, Integer pagesize, String sort, String order,
      String name, Double price) {
    List<Car> cars = new ArrayList<Car>();

    try {
      conn = DBUtil.getConnection();
      // String sql = "select id,name,price,create_date from car where 1=1 ";

      // 分页SQL
      String sql = "select id,name,price,create_date from car ";
      sql += "INNER JOIN (select id from car where 1=1 ";

      if (name != null && name.trim().length() > 0) {
        sql += " and name like ? ";
      }
      if (price != null && price > 0) {
        sql += " and price = ? ";
      }
      sql += "ORDER BY " + sort + " " + order + " LIMIT " + page + ", " +
          pagesize + ") AS lim USING(id)";

      System.out.println("分页SQL: " + sql);

      // select * from car where 1=1
      // select * from car where 1=1 and name like ?
      // select * from car where 1=1 and price = ?
      // select * from car where 1=1 and name like ? and price = ?

      ps = conn.prepareStatement(sql);
      int index = 1;
      if (name != null && name.trim().length() > 0) {
        ps.setString(index, "%" + name + "%");
        index++;
      }
      if (price != null && price > 0) {
        ps.setDouble(index, price);
      }

      rs = ps.executeQuery();
      while (rs.next()) {
        Car car = new Car();
        car.setId(rs.getInt(1)); // 根据字段索引获取值
        car.setName(rs.getString("name")); // 根据字段名获取值
        car.setPrice(rs.getDouble("price"));
        car.setCreateDate(rs.getDate("create_date"));

        cars.add(car);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    return cars;
  }

  /**
   * 最大页数
   *
   * @param pagesize
   * @param name
   * @param price
   * @return
   */
  public Integer getMaxPage(Integer pagesize, String name, Double price) {
    Integer count = 0;
    try {
      conn = DBUtil.getConnection();
      String sql = "select count(id) from car where 1=1 ";
      if (name != null && name.trim().length() > 0) {
        sql += " and name like ? ";
      }
      if (price != null && price > 0) {
        sql += " and price = ? ";
      }

      ps = conn.prepareStatement(sql);
      int index = 1;
      if (name != null && name.trim().length() > 0) {
        ps.setString(index, "%" + name + "%");
        index++;
      }
      if (price != null && price > 0) {
        ps.setDouble(index, price);
      }

      rs = ps.executeQuery();
      rs.next();
      count = rs.getInt(1); // 获取总记录条数
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBUtil.close(conn, ps, rs);
    }
    // 总页数
    return (count % pagesize == 0) ?
        count / pagesize : count / pagesize + 1;
  }
}
