package com.oralce.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.oralce.model.Clazz;
import com.oralce.model.Page;
import com.oralce.util.StringUtil;

public class ClazzDao extends BaseDao {
	public List<Clazz> getClazzList(Clazz clazz, Page page) {
		List<Clazz> ret = new ArrayList<Clazz>();
		String sql = "select * from s_clazz ";
		if (!StringUtil.isEmpty(clazz.getName())) {
			sql += "where clazz_name like '%" + clazz.getName() + "%'";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while (resultSet.next()) {
				Clazz cl = new Clazz();
				cl.setId(resultSet.getInt("clazz_id"));
				cl.setName(resultSet.getString("clazz_name"));
				cl.setInfo(resultSet.getString("info"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	// 获取一共有多少条信息
	public int getClazzListTotal(Clazz clazz) {
		int total = 0;
		String sql = "select count(*) as total from s_clazz ";
		if (!StringUtil.isEmpty(clazz.getName())) {
			sql += "where clazz_name like '%" + clazz.getName() + "%'";
		}
		ResultSet resultSet = query(sql);
		try {
			while (resultSet.next()) {
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	
	//添加班级列表功能
	public boolean addClazz(Clazz clazz){
		String sql = "insert into s_clazz values(null,'"+clazz.getName()+"','"+clazz.getInfo()+"') ";
		return update(sql);
	}
	//删除班级列表
	public boolean deleteClazz(int id){
		String sql = "delete from s_clazz where clazz_id = "+id;
		return update(sql);
	}
   //编辑班级信息
	public boolean editClazz(Clazz clazz) {
		// TODO Auto-generated method stub
		String sql = "update s_clazz set clazz_name = '"+clazz.getName()+"',info = '"+clazz.getInfo()+"' where clazz_id = " + clazz.getId();
		return update(sql);
	}
	


}
