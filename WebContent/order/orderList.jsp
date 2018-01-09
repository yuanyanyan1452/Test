<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	ArrayList orderList = (ArrayList) request.getSession().getAttribute("orderList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Watch List</title>
</head>
<body>
	<table width="650" border="0">
		<tr>
			<td width="650" height="80"
				background="<%=request.getContextPath() + "/image/top.jpg"%>">&nbsp;</td>
		</tr>
		<tr>
			<td><a href="<%=request.getContextPath() + "/user/login.jsp"%>">Login</a>&nbsp;&nbsp;
				href="<%=request.getContextPath() + "/logout.do"%>">Log off</a>&nbsp;&nbsp;
			</td>
		</tr>
	</table>

	<H1>My Order.</H1>

	<H4>
		<BR>
		<TABLE width="100%" border="0" cellpadding="0" cellspacing="1">
			<TBODY>
				<TR>
					<TH width="20%">id</TH>
					<TH width="20%">companyName</TH>
					<TH width="20%">type</TH>
					<TH width="20%">price</TH>
					<TH width="20%">date</TH>
				</TR>

				<%
					for (int i = 0; i < orderList.size(); i++) {
						pageContext.setAttribute("item", orderList.get(i));
				%>
				<TR>
					<TD align="center"><jsp:getProperty name="item" property="id" /></TD>
					<TD align="center"><jsp:getProperty name="item"
							property="companyName" /></TD>
					<TD align="center"><jsp:getProperty name="item"
							property="type" /></TD>
					<TD align="center"><jsp:getProperty name="item"
							property="price" /></TD>
					<TD align="center"><jsp:getProperty name="item"
							property="date" /></TD>
				</TR>
				<%
					}
				%>

				<stock:stockInfo />
			</TBODY>
		</TABLE>
	</H4>

	<form method="GET"
		action="<%=response.encodeURL(request.getContextPath() + "/Login")%>">
		</p>
		<input type="submit" name="Logout" value="Logout">
	</form>

</body>
</html>