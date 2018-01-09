package edu.nju.onlinestock.factory;

import edu.nju.onlinestock.service.OrderManageService;
import edu.nju.onlinestock.service.impl.OrderManageServiceImpl;

public class ServiceFactory {

	public static OrderManageService getStockManageService()
	{
		return OrderManageServiceImpl.getInstance();
	}
	
}
