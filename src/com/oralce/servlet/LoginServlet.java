package com.oralce.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oralce.dao.AdminDao;
import com.oralce.model.Admin;
import com.oralce.util.StringUtil;

public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=req.getParameter("method");
		if("logout".equals(method)) {
			logout(req, resp);
			return;
		}
		String vcode = req.getParameter("vcode");
		String name = req.getParameter("account");
		String password = req.getParameter("password");
		int type = Integer.parseInt(req.getParameter("type"));
		String loginCpacha = req.getSession().getAttribute("loginCapcha").toString();
		if(StringUtil.isEmpty(vcode)){
			resp.getWriter().write("vcodeError");
			return;
		}
		if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
			resp.getWriter().write("vcodeError");
			return;
		}

		// 验证码验证通过，对比用户名密码是否正确
		String loginStatus = "loginFaild";
		switch (type) {
		case 1: {
			AdminDao adminDao = new AdminDao();
			Admin admin = adminDao.login(name, password);
			adminDao.closeCon();
			if (admin == null) {
				resp.getWriter().write("loginError");
				return;
			}
			HttpSession session = req.getSession();
			session.setAttribute("user", admin);
			session.setAttribute("userType", type);
			loginStatus = "loginSuccess";

			break;
		}
		default:
			break;
		}
		resp.getWriter().write(loginStatus);

	}
	
	//注销登录的功能
	private void logout(HttpServletRequest req,HttpServletResponse resp) {
		req.getSession().removeAttribute("user");
		req.getSession().removeAttribute("userType");
		try {
			resp.sendRedirect("index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
