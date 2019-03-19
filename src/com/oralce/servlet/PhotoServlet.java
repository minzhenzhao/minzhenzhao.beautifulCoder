package com.oralce.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;
import com.oralce.dao.StudentDao;
import com.oralce.dao.TeacherDao;
import com.oralce.model.Student;
import com.oralce.model.Teacher;

//图片处理servlet
public class PhotoServlet extends HttpServlet {

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method = req.getParameter("method");
		if ("getPhoto".equals(method)) {
			getPhoto(req, resp);
		} else if ("SetPhoto".equals(method)) {
			uploadPhoto(req, resp);
		}
	}

	// 加载设置头像图片的功能
	private void uploadPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileSize(2048);
		response.setCharacterEncoding("UTF-8");
		try {
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			if (sid != 0) {
				Student student = new Student();
				student.setId(sid);
				student.setPhoto(uploadInputStream);
				StudentDao studentDao = new StudentDao();
				if (studentDao.setStudentPhoto(student)) {
					response.getWriter().write("<div id='message'>上传成功！</div>");
				} else {
					response.getWriter().write("<div id='message'>上传失败！</div>");
				}
			}
			if (tid != 0) {
				Teacher teacher = new Teacher();
				teacher.setId(tid);
				teacher.setPhoto(uploadInputStream);
				TeacherDao teacherDao = new TeacherDao();
				if (teacherDao.setTeacherPhoto(teacher)) {
					response.getWriter().write("<div id='message'>上传成功！</div>");
				} else {
					response.getWriter().write("<div id='message'>上传失败！</div>");
				}
			}

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			try {
				response.getWriter().write("<div id='message'>上传协议错误！</div>");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (NullFileException e1) {
			try {
				response.getWriter().write("<div id='message'>上传的文件为空!</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		} catch (SizeException e2) {
			try {
				response.getWriter().write("<div id='message'>上传文件大小不能超过" + fileUpload.getFileSize() + "！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e2.printStackTrace();
		} catch (IOException e3) {
			try {
				response.getWriter().write("<div id='message'>读取文件出错！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e3.printStackTrace();
		} catch (FileFormatException e4) {
			try {
				response.getWriter()
						.write("<div id='message'>上传文件格式不正确，请上传 " + fileUpload.getFileFormat() + " 格式的文件！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e4.printStackTrace();
		} catch (FileUploadException e5) {
			try {
				response.getWriter().write("<div id='message'>上传文件失败！</div>");
			} catch (IOException e) {
				e.printStackTrace();
			}
			e5.printStackTrace();
		}
	}

	// 获取图片的方法
	private void getPhoto(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		// File file = new File();
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));
		if (sid != 0) {
			// 学生
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.getStudent(sid);
			studentDao.closeCon();
			if (student != null) {
				InputStream photo = student.getPhoto();
				if (photo != null) {
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						response.getOutputStream().write(b, 0, b.length);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
		}
		if (tid != 0) {
			TeacherDao teacherDao = new TeacherDao();
			Teacher teacher = teacherDao.getTeacher(tid);
			teacherDao.closeCon();
			if (teacher != null) {
				InputStream photo = teacher.getPhoto();
				if (photo != null) {
					try {
						byte[] b = new byte[photo.available()];
						photo.read(b);
						response.getOutputStream().write(b, 0, b.length);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}

		String path = request.getSession().getServletContext().getRealPath("");
		File file = new File(path + "\\file\\zmz.jpg");
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] b = new byte[fis.available()];
			fis.read(b);
			response.getOutputStream().write(b, 0, b.length);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
