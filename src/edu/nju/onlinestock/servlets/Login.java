package edu.nju.onlinestock.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext Context= getServletContext();
		int onlineNum = Integer.parseInt((String)Context.getAttribute("onlineUser"));
		int totalNum = Integer.parseInt((String)Context.getAttribute("totalUser"));
		if (null == request.getParameter("Logout")) {
			System.out.println("totalUser++\n");
			totalNum++;
			Context.setAttribute("totalUser", Integer.toString(totalNum));
		}else {
			System.out.println("onlineUser--\n");
			onlineNum--;
			Context.setAttribute("onlineUser", Integer.toString(onlineNum));
		}

		String login="";
		HttpSession session = request.getSession(false);
		Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
  
        Integer ival = new Integer(1);
        		
        if (null != cookies) {
            // Look through all the cookies and see if the
            // cookie with the login info is there.
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals("LoginCookie")) {
                    login=cookie.getValue();
                    break;
                }
            }
        }
    
        // Logout action removes session, but the cookie remains
        if (null != request.getParameter("Logout")) {
            if (null != session) {
            	session.invalidate();
                session = null;
            }
        }
       
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");

        out.println(
                "<form method='POST' action='"
                    + response.encodeURL(request.getContextPath()+"/ShowMyOrderServlet")
                    + "'>");
        out.println(
            "login: <input type='text' name='login' value='" + login + "'>");
        out.println(
            "password: <input type='password' name='password' value=''>");
        out.println("<input type='submit' name='Submit' value='Submit'>");
   
        out.println("<p>Servlet is version @version@</p>");
//    out.println("</p>You are visitor number " + webCounter);
       
    
    out.println("</form></body></html>");
     
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
