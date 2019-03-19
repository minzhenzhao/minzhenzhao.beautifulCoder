package com.oralce.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oralce.dao.TeacherDao;
import com.oralce.model.Page;
import com.oralce.model.Teacher;
import com.oralce.util.SnGenerateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeacherServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("toTeacherListView".equals(method)) {
			StudentList(req, resp);
		} else if ("AddTeacher".equals(method)) {
			addTeacher(req, resp);
		} else if ("TeacherList".equals(method)) {
			getTeacherList(req, resp);
		} else if ("EditTeacher".equals(method)) {
			editTeacher(req, resp);
		} else if ("DeleteTeacher".equals(method)) {
			deleteTeacher(req, resp);
		}
	}

	// 删除老师信息的方法
	private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) {
		String[] ids = request.getParameterValues("ids[]");
		String idStr = "";
		for (String id : ids) {
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length() - 1);
		TeacherDao teacherDao = new TeacherDao();
		if (teacherDao.deleteTeacher(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				teacherDao.closeCon();
			}
		}
	}

	// 修改老师信息的功能
	private void editTeacher(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		int id = Integer.parseInt(request.getParameter("id"));
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setId(id);
		teacher.setQq(qq);
		teacher.setSex(sex);
		TeacherDao teacherDao = new TeacherDao();
		if (teacherDao.editTeacher(teacher)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				teacherDao.closeCon();
			}
		}
	}

	// 展示老师信息列表的方法
	private void getTeacherList(HttpServletRequest request, HttpServletResponse response) {

		String name = request.getParameter("teacherName");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Integer clazz = request.getParameter("clazzid") == null ? 0 : Integer.parseInt(request.getParameter("clazzid"));

		Teacher teacher = new Teacher();
		teacher.setName(name);
		teacher.setClazzId(clazz);
		TeacherDao teacherDao = new TeacherDao();
		List<Teacher> teacherList = teacherDao.getTeacherList(teacher, new Page(currentPage, pageSize));
		int total = teacherDao.getTeacherListTotal(teacher);
		teacherDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", teacherList);
		try {
			String from = request.getParameter("from");
			if ("combox".equals(from)) {
				response.getWriter().write(JSONArray.fromObject(teacherList).toString());
			} else {
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 添加老师的方法
	private void addTeacher(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		Teacher teacher = new Teacher();
		teacher.setClazzId(clazzId);
		teacher.setMobile(mobile);
		teacher.setName(name);
		teacher.setPassword(password);
		teacher.setQq(qq);
		teacher.setSex(sex);
		teacher.setSn(SnGenerateUtil.generateTeacherSn(clazzId));
		TeacherDao teacherDao = new TeacherDao();
		if(teacherDao.addTeacher(teacher)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				teacherDao.closeCon();
			}
		}
	}
// 展示老师界面的方法
	private void StudentList(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("/view/teacherList.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

	}

}
