package edu.nju.onlinestock.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import edu.nju.onlinestock.model.Order;
import edu.nju.onlinestock.model.Stock;

/*
 此Servlet未使用DAO、MVC设计，要求只有已登录的客户才能查看自己购买的股票(数据源)，
 实现：已登录用户，才能查看；
            未登录用户，转去登录；
            从登录提交到此的用户，创建session，跟踪登录状态；如果是来自初次登录的用户，则创建cookie；并查看自己购买的股票；
            通过刷新页面/或已创建session的页面访问，则查看自己购买的股票。
 */

/**
 * Servlet implementation class StockListServlet
 */
@WebServlet("/ShowMyStockServlet")
public class ShowMyStockServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DataSource datasource = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ShowMyStockServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		InitialContext jndiContext = null;

		Properties properties = new Properties();
		properties.put(javax.naming.Context.PROVIDER_URL, "jnp:///");
		properties.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		try {
			jndiContext = new InitialContext(properties);
			datasource = (DataSource) jndiContext.lookup("java:comp/env/jdbc/onlinestock");
			System.out.println("got context");
			System.out.println("About to get ds---ShowMyStock");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html;charset=utf-8");
		req.setCharacterEncoding("UTF-8");
		
		HttpSession session = req.getSession(false);
		boolean cookieFound = false;
		System.out.println(req.getAttribute("login") + " req");
		Cookie cookie = null;
		Cookie[] cookies = req.getCookies();
		if (null != cookies) {
			// Look through all the cookies and see if the
			// cookie with the login info is there.
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookie.getName().equals("LoginCookie")) {
					cookieFound = true;
					break;
				}
			}
		}

		if (session == null) {
			String loginValue = req.getParameter("login");
			boolean isLoginAction = (null == loginValue) ? false : true;

			System.out.println(loginValue + " session null");
			if (isLoginAction) { // User is logging in
				if (cookieFound) { // If the cookie exists update the value only
					// if changed
					if (!loginValue.equals(cookie.getValue())) {
						cookie.setValue(loginValue);
						resp.addCookie(cookie);
					}
				} else {
					// If the cookie does not exist, create it and set value
					cookie = new Cookie("LoginCookie", loginValue);
					cookie.setMaxAge(Integer.MAX_VALUE);
					System.out.println("Add cookie");
					resp.addCookie(cookie);
				}

				// create a session to show that we are logged in
				session = req.getSession(true);
				session.setAttribute("login", loginValue);

				req.setAttribute("login", loginValue);
				
				System.out.println("1:"+req.getAttribute("login"));
				isUserValid(req,resp);

			} else {
				System.out.println(loginValue + " session null");
				// Display the login page. If the cookie exists, set login
				resp.sendRedirect(req.getContextPath() + "/Login");
			}
		} else {
			// 或未注销，重新加载该页面，session不为空
			String loginValue = (String) session.getAttribute("login");
			System.out.println(loginValue + " session");

			req.setAttribute("login", loginValue);
			getOrderList(req, resp);

		}

	}

	public void getStockList(HttpServletRequest req, HttpServletResponse res) {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		ArrayList list = new ArrayList();
		Statement sm = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			System.out.println(req.getAttribute("login"));
			stmt = connection.prepareStatement("select stockid from mystock where userid = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			while (result.next()) {
				Stock stock = new Stock();
				stock.setId(result.getInt("stockid"));
				/*
				 * stock.setCompanyName(result.getString(2));
				 * stock.setType(result.getString(3));
				 * stock.setPrice(result.getDouble(4));
				 * stock.setDate(result.getDate("date"));
				 */
				list.add(stock);
				System.out.println("stockid: "+result.getInt("stockid"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		req.setAttribute("list", list);

	}

	public void displayLogoutPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		// 注销Logout
		out.println("<form method='GET' action='" + res.encodeURL(req.getContextPath() + "/Login") + "'>");
		out.println("</p>");
		out.println("<input type='submit' name='Logout' value='Logout'>");
		out.println("</form>");
		out.println("<p>Servlet is version @version@</p>");
		out.println("</body></html>");

	}

	public void displayMyStocklistPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ArrayList list = (ArrayList) req.getAttribute("list"); // resp.sendRedirect(req.getContextPath()+"/MyStockList");

		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<table width='650' border='0' >");
		out.println("<tr>");
		out.println("<td width='650' height='80' background='" + req.getContextPath() + "/image/top.jpg'>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p>Welcome " + req.getAttribute("login") + "</p>");

		out.println("My Stock List:  ");

		for (int i = 0; i < list.size(); i++) {
			Stock stock = (Stock) list.get(i);
			out.println(stock.getId());
		}
		out.println("</p>");
		// 点击here，刷新该页面，会话有效
		out.println("Click <a href='" + res.encodeURL(req.getRequestURI()) + "'>here</a> to reload this page.<br>");
	}

	public void isUserValid(HttpServletRequest req,HttpServletResponse res) throws IOException {
		System.out.println("2:"+req.getAttribute("login"));
		String password = (String) req.getParameter("password");
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		ArrayList list = new ArrayList();
		Statement sm = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = connection.prepareStatement("select password from user where userid = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			if(!result.next()){
				PrintWriter out = res.getWriter();
				out.println("<html><body");
				out.println("<p>输入的用户id不存在</p>");
				out.println("</body></html>");
			}else {
				String passdb = result.getString("password");
				if(!password.equals(passdb)) {
					PrintWriter out = res.getWriter();
					out.println("<html><body");
					out.println("<p>用户密码错误</p>");
					out.println("</body></html>");
				}else {
					getOrderList(req,res);
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getOrderList(HttpServletRequest req, HttpServletResponse res) throws IOException {
		//存储订单缺货时信息的变量
		boolean isLack = false;
		int lackStockId = 0;
		
		System.out.println("3:"+req.getAttribute("login"));
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		ArrayList list = new ArrayList();
		Statement sm = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = connection.prepareStatement("select * from myorder where userid = ?");
			stmt.setString(1, (String) req.getAttribute("login"));
			result = stmt.executeQuery();
			while (result.next()) {
				Order order = new Order();
				order.setStockid(result.getInt("stockid"));
				order.setNumber(result.getInt("number"));
				int stockNum = getStockNum(result.getInt("stockid"));
				if(stockNum < result.getInt("number")) {
					isLack = true;
					lackStockId = result.getInt("stockid");
					break;
				}
				order.setTotalPrice(result.getDouble("totalPrice"));
				list.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		req.setAttribute("orderlist", list);
		
		if(isLack == true) {
			PrintWriter out = res.getWriter();
			out.println("<p>Welcome " + req.getAttribute("login") + "</p>");
			out.println("<p>您的订单中id为"+lackStockId+"的股票缺货</p>");
		}else {
			displayMyOrderlistPage(req, res);
			displayLogoutPage(req, res);
		}
	
	}
	
	public void displayMyOrderlistPage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		ArrayList list = (ArrayList) req.getAttribute("orderlist"); // resp.sendRedirect(req.getContextPath()+"/MyStockList");
		System.out.println("4:"+req.getAttribute("login"));
		PrintWriter out = res.getWriter();
		out.println("<html><body>");
		out.println("<table width='650' border='0' >");
		out.println("<tr>");
		out.println("<td width='650' height='80' background='" + req.getContextPath() + "/image/top.jpg'>&nbsp;</td>");
		out.println("</tr>");
		out.println("</table>");
		out.println("<p>Welcome " + req.getAttribute("login") + "</p>");

		out.println("My order List:  ");

		for (int i = 0; i < list.size(); i++) {
			Order order = (Order) list.get(i);
			out.println("<p>");
			out.println("<span>stockid:"+order.getStockid()+"</span>");
			out.println("<span>number:"+order.getNumber()+"</span>");
			out.println("<span>total price:"+order.getTotalPrice()+"</span>");
			out.println("</p>");
		}
		
		// 点击here，刷新该页面，会话有效
		out.println("Click <a href='" + res.encodeURL(req.getRequestURI()) + "'>here</a> to reload this page.<br>");
	}
	
	public int getStockNum(int stockid) {
		
		int stockNum = 0;
		
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet result = null;
		Statement sm = null;
		try {
			connection = datasource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt = connection.prepareStatement("select number from stock where stockid = ?");
			stmt.setInt(1, stockid);
			result = stmt.executeQuery();
			if (result.next()) {
				stockNum = result.getInt("number");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stockNum;
	}
}
