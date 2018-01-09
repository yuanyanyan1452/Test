package edu.nju.onlinestock.factory;

import edu.nju.onlinestock.dao.MyOrderDao;
import edu.nju.onlinestock.dao.impl.MyOrderDaoImpl;

public class DaoFactory {

	public static MyOrderDao getMyOrderDao(){
		return MyOrderDaoImpl.getInstance();
	}
}
