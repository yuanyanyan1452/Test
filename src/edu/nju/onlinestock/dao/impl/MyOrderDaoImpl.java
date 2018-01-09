package edu.nju.onlinestock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.nju.onlinestock.dao.DaoHelper;
import edu.nju.onlinestock.dao.MyOrderDao;
import edu.nju.onlinestock.model.Order;

public class MyOrderDaoImpl implements MyOrderDao{

	private static MyOrderDaoImpl myOrderDao=new MyOrderDaoImpl();
	private static DaoHelper daoHelper=DaoHelperImpl.getBaseDaoInstance();
	
	private MyOrderDaoImpl()
	{
		
	}
	
	public static MyOrderDaoImpl getInstance()
	{
		return myOrderDao;
	}
	
	@Override
	public List find(String username) {
		// TODO Auto-generated method stub
		Connection conn = daoHelper.getConnection();
		PreparedStatement stmt=null;
		ResultSet result=null;
		ArrayList list=new ArrayList();
		try 
		{
			stmt=conn.prepareStatement("select * from myorder where username = ?");
			stmt.setString(1,username);
			result=stmt.executeQuery();
			while(result.next())
			{
				Order order = new Order();
				order.setOrderid(result.getInt("orderid"));
				order.setUsername(result.getString("username"));
				order.setStockid(result.getInt("stockid"));
				order.setNumber(result.getInt("number"));
				order.setTotalPrice(result.getDouble("totalPrice"));
				list.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			daoHelper.closeConnection(conn);
			daoHelper.closePreparedStatement(stmt);
			daoHelper.closeResult(result);
		}
		return list;
	}

}
