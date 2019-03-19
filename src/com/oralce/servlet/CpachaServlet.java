package com.oralce.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oralce.util.CpachaUtil;

public class CpachaServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("loginCapcha".equals(method)) {
			generateLoginCpacha(req, resp);
			return;
		}
		resp.getWriter().write("error method");

	}

	private void generateLoginCpacha(HttpServletRequest request, HttpServletResponse reponse) throws IOException {
		CpachaUtil cpachaUtil = new CpachaUtil();
		String generatorVCode = cpachaUtil.generatorVCode();
		request.getSession().setAttribute("loginCapcha", generatorVCode);
		BufferedImage generatorRotateVCodeImage = cpachaUtil.generatorRotateVCodeImage(generatorVCode, true);
		ImageIO.write(generatorRotateVCodeImage, "gif", reponse.getOutputStream());
	}

}
