package com.oralce.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oralce.dao.AdminDao;
import com.oralce.model.Admin;

public class SystemServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if("toPersonalView".equals(method)){
			personalView(req,resp);
			return;
		}else if("EditPasswod".equals(method)) {
			editPassword(req,resp);
			return;
		}
		try {
			req.getRequestDispatcher("view/system.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
	//修改密码的方法
    private void editPassword(HttpServletRequest req, HttpServletResponse resp) {
    	String password = req.getParameter("password");
		String newPassword = req.getParameter("newpassword");
		resp.setCharacterEncoding("UTF-8");
		int userType = Integer.parseInt(req.getSession().getAttribute("userType").toString());
		if(userType == 1){
			Admin admin = (Admin)req.getSession().getAttribute("user");
			if(!admin.getPassword().equals(password)){
				try {
					resp.getWriter().write("原密码错误！");
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			AdminDao adminDao = new AdminDao();
			if(adminDao.editPassword(admin, newPassword)){
				try {
					resp.getWriter().write("success");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					adminDao.closeCon();
				}
			}else{
				try {
					resp.getWriter().write("数据库修改错误");
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					adminDao.closeCon();
				}
			}
		}

	}

	//跳到个人页面的方法
	private void personalView(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			request.getRequestDispatcher("view/personalView.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

