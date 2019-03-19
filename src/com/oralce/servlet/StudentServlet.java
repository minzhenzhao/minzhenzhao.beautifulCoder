package com.oralce.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oralce.dao.StudentDao;
import com.oralce.model.Page;
import com.oralce.model.Student;
import com.oralce.util.SnGenerateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class StudentServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("toStudentListView".equals(method)) {
			studentList(req, resp);
		} else if ("AddStudent".equals(method)) {
			addStudent(req, resp);
		} else if ("StudentList".equals(method)) {
			getStudentList(req, resp);
		}else if("EditStudent".equals(method)) {
			editStudent(req,resp);
		}else if("DeleteStudent".equals(method)) {
			deleteStudent(req,resp);
		}
	}
	//删除学生的功能(可以批量删除)
	private void deleteStudent(HttpServletRequest req,HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String[] ids = req.getParameterValues("ids[]");
		String idStr = "";
		for(String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		StudentDao studentDao = new StudentDao();
		if(studentDao.deleteStudent(idStr)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}


	//修改学生信息的功能
	private void editStudent(HttpServletRequest req,HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String name = req.getParameter("name");
		int id = Integer.parseInt(req.getParameter("id"));
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setId(id);
		student.setQq(qq);
		student.setSex(sex);
		StudentDao studentDao = new StudentDao();
		if(studentDao.editStudent(student)){
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}


	// 展示学生列表的方法
	private void getStudentList(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter("studentName");
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		Integer clazz = req.getParameter("clazzid") == null ? 0 : Integer.parseInt(req.getParameter("clazzid"));
		Student student = new Student();
		student.setName(name);
		student.setClazzId(clazz);
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = studentDao.getStudentList(student, new Page(currentPage, pageSize));
		int total = studentDao.getStudentListTotal(student);
		studentDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", studentList);
		try {
			String from = req.getParameter("from");
			if ("combox".equals(from)) {
				resp.getWriter().write(JSONArray.fromObject(studentList).toString());
			} else {
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			studentDao.closeCon();
		}

	}

	// 添加学生列表的方法
	private void addStudent(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
		}
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String sex = req.getParameter("sex");
		String mobile = req.getParameter("mobile");
		String qq = req.getParameter("qq");
		int clazzId = Integer.parseInt(req.getParameter("clazzid"));
		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setPassword(password);
		student.setQq(qq);
		student.setSex(sex);
		// 学号利用util自己封装的一个小工具类自己产生
		student.setSn(SnGenerateUtil.generateSn(clazzId));
		StudentDao studentDao = new StudentDao();
		if (studentDao.addStudent(student)) {
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				studentDao.closeCon();
			}
		}
	}

	
	private void studentList(HttpServletRequest req, HttpServletResponse resp) {

		try {
			req.getRequestDispatcher("/view/studentList.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
