package edu.nju.onlinestock.service.impl;

import java.util.ArrayList;
import java.util.List;

import edu.nju.onlinestock.factory.DaoFactory;
import edu.nju.onlinestock.model.Stock;
import edu.nju.onlinestock.service.OrderManageService;

public class OrderManageServiceImpl implements OrderManageService{
	
	private static OrderManageService orderService=new OrderManageServiceImpl();
	public static OrderManageService getInstance()
	{
		return orderService;
	}

	@Override
	public List getMyOrder(String username) {
		// TODO Auto-generated method stub
		ArrayList list=new ArrayList();
		list=(ArrayList) DaoFactory.getMyOrderDao().find(username);
	
		return list;
	}

}
