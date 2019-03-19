package com.oralce.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.oralce.model.Admin;
import com.oralce.model.Page;
import com.oralce.model.Student;
import com.oralce.util.StringUtil;

public class StudentDao extends BaseDao {
	// 添加学生
	public boolean addStudent(Student student) {
		String sql = "insert into s_student values(null,'" + student.getSn() + "','" + student.getName() + "'";
		sql += ",'" + student.getPassword() + "','" + student.getSex();
		sql += "'," + student.getClazzId() + ",'" + student.getMobile() + "'";
		sql += ",'" + student.getQq() + "',null)";
		return update(sql);
	}

	// 获取一个学生的方法
	public Student getStudent(int id) {
		String sql = "select * from s_student where student_id = " + id;
		Student student = null;
		ResultSet resultSet = query(sql);
		try {
			if (resultSet.next()) {
				student = new Student();
				student.setId(resultSet.getInt("student_id"));
				student.setClazzId(resultSet.getInt("clazz_id"));
				student.setMobile(resultSet.getString("mobile"));
				student.setName(resultSet.getString("student_name"));
				student.setPassword(resultSet.getString("student_password"));
				student.setPhoto(resultSet.getBinaryStream("photo"));
				student.setQq(resultSet.getString("qq"));
				student.setSex(resultSet.getString("sex"));
				student.setSn(resultSet.getString("sn"));
				return student;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return student;

	}

	// 查询列表的方法
	public List<Student> getStudentList(Student student, Page page) {
		List<Student> ret = new ArrayList<Student>();
		String sql = "select * from s_student ";
		if (!StringUtil.isEmpty(student.getName())) {
			sql += "and student_name like '%" + student.getName() + "%'";
		}
		if (student.getClazzId() != 0) {
			sql += " and clazz_id = " + student.getClazzId();
		}
		if (student.getId() != 0) {
			sql += " and student_id = " + student.getId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		// 把第一个and替换成where
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while (resultSet.next()) {
				Student s = new Student();
				s.setId(resultSet.getInt("student_id"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				s.setMobile(resultSet.getString("mobile"));
				s.setName(resultSet.getString("student_name"));
				s.setPassword(resultSet.getString("student_password"));
				s.setPhoto(resultSet.getBinaryStream("photo"));
				s.setQq(resultSet.getString("qq"));
				s.setSex(resultSet.getString("sex"));
				s.setSn(resultSet.getString("sn"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	// 查询学生的数量
	public int getStudentListTotal(Student student) {
		int total = 0;
		String sql = "select count(*)as total from s_student ";
		if (!StringUtil.isEmpty(student.getName())) {
			sql += "and student_name like '%" + student.getName() + "%'";
		}
		if (student.getClazzId() != 0) {
			sql += " and clazz_id = " + student.getClazzId();
		}
		if (student.getId() != 0) {
			sql += " and student_id = " + student.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
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

	// 修改学生信息的方法
	public boolean editStudent(Student student) {
		String sql = "update s_student set student_name = '" + student.getName() + "'";
		sql += ",sex = '" + student.getSex() + "'";
		sql += ",mobile = '" + student.getMobile() + "'";
		sql += ",qq = '" + student.getQq() + "'";
		sql += ",clazz_id = " + student.getClazzId();
		sql += " where student_id = " + student.getId();
		return update(sql);
	}
   //设置学生照片的功能
	public boolean setStudentPhoto(Student student) {
		// TODO Auto-generated method stub
		String sql = "update s_student set photo = ? where student_id = ?";
		Connection connection = (Connection) getConnection();
		try {
			PreparedStatement prepareStatement = (PreparedStatement) connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, student.getPhoto());
			prepareStatement.setInt(2, student.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update(sql);
	}
  //删除学生的功能
	public boolean deleteStudent(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_student where student_id in("+ids+")";
		return update(sql);
	}
  //修改学生密码的功能
	public boolean editPassword(Student student,String newPassword) {
		String sql = "update s_student set student_password = '"+newPassword+"' where student_id = " + student.getId();
		return update(sql);
	}

}
