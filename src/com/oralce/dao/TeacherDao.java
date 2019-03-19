package com.oralce.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.oralce.model.Admin;
import com.oralce.model.Page;
import com.oralce.model.Teacher;
import com.oralce.util.StringUtil;

public class TeacherDao extends BaseDao {
	// 添加教师的方法
	public boolean addTeacher(Teacher teacher) {
		String sql = "insert into s_teacher values(null,'" + teacher.getSn() + "','" + teacher.getName() + "'";
		sql += ",'" + teacher.getPassword() + "','" + teacher.getSex();
		sql += "'," + teacher.getClazzId() + ",'" + teacher.getMobile() + "'";
		sql += ",'" + teacher.getQq() + "',null)";
		return update(sql);
	}

	// 编辑修改老师信息的方法
	public boolean editTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		String sql = "update s_teacher set teacher_name = '" + teacher.getName() + "'";
		sql += ",teacher_sex = '" + teacher.getSex() + "'";
		sql += ",teacher_mobile = '" + teacher.getMobile() + "'";
		sql += ",teacher_qq = '" + teacher.getQq() + "'";
		sql += ",clazz_id = " + teacher.getClazzId();
		sql += " where teacher_id = " + teacher.getId();
		return update(sql);
	}

	// 设置老师照片的功能
	public boolean setTeacherPhoto(Teacher teacher) {
		// TODO Auto-generated method stub
		String sql = "update s_teacher set teacher_photo = ? where teacher_id = ?";
		Connection connection = (Connection) getConnection();
		try {
			PreparedStatement prepareStatement = (PreparedStatement) connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, teacher.getPhoto());
			prepareStatement.setInt(2, teacher.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update(sql);
	}

	// 删除老师信息的功能
	public boolean deleteTeacher(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_teacher where teacher_id in(" + ids + ")";
		return update(sql);
	}

	// 得到一条老师信息的功能
	public Teacher getTeacher(int id) {
		String sql = "select * from s_teacher where teacher_id = " + id;
		Teacher teacher = null;
		ResultSet resultSet = query(sql);
		try {
			if (resultSet.next()) {
				teacher = new Teacher();
				teacher.setId(resultSet.getInt("teacher_id"));
				teacher.setClazzId(resultSet.getInt("clazz_id"));
				teacher.setMobile(resultSet.getString("teacher_mobile"));
				teacher.setName(resultSet.getString("teacher_name"));
				teacher.setPassword(resultSet.getString("teacher_password"));
				teacher.setPhoto(resultSet.getBinaryStream("teacher_photo"));
				teacher.setQq(resultSet.getString("teacher_qq"));
				teacher.setSex(resultSet.getString("teacher_sex"));
				teacher.setSn(resultSet.getString("teacher_sn"));
				return teacher;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return teacher;
	}

	// 获取全部老师信息的功能
	public List<Teacher> getTeacherList(Teacher teacher, Page page) {
		List<Teacher> ret = new ArrayList<Teacher>();
		String sql = "select * from s_teacher ";
		if (!StringUtil.isEmpty(teacher.getName())) {
			sql += "and teacher_name like '%" + teacher.getName() + "%'";
		}
		if (teacher.getClazzId() != 0) {
			sql += " and clazz_id = " + teacher.getClazzId();
		}
		if (teacher.getId() != 0) {
			sql += " and teacher_id = " + teacher.getId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while (resultSet.next()) {
				Teacher t = new Teacher();
				t.setId(resultSet.getInt("teacher_id"));
				t.setClazzId(resultSet.getInt("clazz_id"));
				t.setMobile(resultSet.getString("teacher_mobile"));
				t.setName(resultSet.getString("teacher_name"));
				t.setPassword(resultSet.getString("teacher_password"));
				t.setPhoto(resultSet.getBinaryStream("teacher_photo"));
				t.setQq(resultSet.getString("teacher_qq"));
				t.setSex(resultSet.getString("teacher_sex"));
				t.setSn(resultSet.getString("teacher_sn"));
				ret.add(t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
   //获取全部老师的数量
	public int getTeacherListTotal(Teacher teacher) {
		int total = 0;
		String sql = "select count(*)as total from s_teacher ";
		if (!StringUtil.isEmpty(teacher.getName())) {
			sql += "and teacher_name like '%" + teacher.getName() + "%'";
		}
		if (teacher.getClazzId() != 0) {
			sql += " and clazz_id = " + teacher.getClazzId();
		}
		if (teacher.getId() != 0) {
			sql += " and teacher_id = " + teacher.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while (resultSet.next()) {
				total = resultSet.getInt("total");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	//修改密码
	public boolean editPassword(Teacher teacher,String newPassword) {
		String sql = "update s_teacher set teacher_password = '"+newPassword+"' where teacher_id = " + teacher.getId();
		return update(sql);
	}

}
