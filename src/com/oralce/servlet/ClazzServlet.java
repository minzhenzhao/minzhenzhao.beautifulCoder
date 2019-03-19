package com.oralce.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oralce.dao.ClazzDao;
import com.oralce.model.Clazz;
import com.oralce.model.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

//班级信息管理
public class ClazzServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("toClazzListView".equals(method)) {
			clazzList(req, resp);
		} else if ("getClazzList".equals(method)) {
			getClazzList(req, resp);
		} else if ("AddClazz".equals(method)) {
			addClazz(req, resp);
		} else if ("DeleteClazz".equals(method)) {
			deleteClazz(req, resp);
		} else if ("EditClazz".equals(method)) {
			editClazz(req, resp);
		}
	}

	// 编辑班级信息
	private void editClazz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("id"));
		String name = request.getParameter("name");
		String info = request.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		clazz.setId(id);
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.editClazz(clazz)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				clazzDao.closeCon();
			}
		}
	}

	// 刪除班级列表
	private void deleteClazz(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Integer id = Integer.parseInt(request.getParameter("clazzid"));
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.deleteClazz(id)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				clazzDao.closeCon();
			}
		}
	}

	// 添加功能的servlet
	private void addClazz(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter("name");
		String info = req.getParameter("info");
		Clazz clazz = new Clazz();
		clazz.setName(name);
		clazz.setInfo(info);
		ClazzDao clazzDao = new ClazzDao();
		if (clazzDao.addClazz(clazz)) {
			try {
				resp.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				clazzDao.closeCon();
			}
		}

	}

	private void clazzList(HttpServletRequest req, HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("/view/clazzList.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 展示班级的所有信息
	private void getClazzList(HttpServletRequest req, HttpServletResponse resp) {
		String name = req.getParameter("clazzName");
		Integer currentPage = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
		Integer pageSize = req.getParameter("rows") == null ? 999 : Integer.parseInt(req.getParameter("rows"));
		ClazzDao clazzDao = new ClazzDao();
		Clazz clazz = new Clazz();
		clazz.setName(name);
		List<Clazz> clazzList = clazzDao.getClazzList(clazz, new Page(currentPage, pageSize));
		int total = clazzDao.getClazzListTotal(clazz);
		clazzDao.closeCon();
		resp.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", clazzList);
		try {
			String from = req.getParameter("from");
			if ("combox".equals(from)) {
				resp.getWriter().write(JSONArray.fromObject(clazzList).toString());
			} else {
				//实现下拉列表展示班级信息的功能
				resp.getWriter().write(JSONObject.fromObject(ret).toString());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
