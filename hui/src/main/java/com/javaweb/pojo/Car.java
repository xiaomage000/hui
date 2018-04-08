package com.javaweb.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体类
 *
 * @author SONG
 */
public class Car implements Serializable {
  private static final long serialVersionUID = 7222532441028834595L;
  private Integer id;
  private String name;
  private Double price;
  private Date createDate;

  public Car() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}