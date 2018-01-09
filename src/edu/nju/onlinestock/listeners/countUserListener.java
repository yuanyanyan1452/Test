package edu.nju.onlinestock.listeners;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class countUserListener implements ServletContextListener, ServletContextAttributeListener {
	int onlineNum;
	int visitorNum;
	int totalNum;
	String counterPath = "D:\\countUser.txt";

	/*
	 * default constructor
	 */
	public countUserListener() {

	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
		System.out.println("ServletContextattribute added");
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
		System.out.println("ServletContextattribute removed");
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		// TODO Auto-generated method stub
		System.out.println("ServletContextattribute replaced");
		writeCounter(scae);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("Application shut down");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Reading Start");
			BufferedReader reader = new BufferedReader(new FileReader(counterPath));
			onlineNum = Integer.parseInt(reader.readLine());
			visitorNum = Integer.parseInt(reader.readLine());
			totalNum = Integer.parseInt(reader.readLine());
			reader.close();
			System.out.println("Reading online user num " + onlineNum);
			System.out.println("Reading online user num " + visitorNum);
			System.out.println("Reading online user num " + totalNum);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("onlineUser", Integer.toString(onlineNum));
		servletContext.setAttribute("visitor", Integer.toString(visitorNum));
		servletContext.setAttribute("totalUser", Integer.toString(totalNum));
		System.out.println("Application initialized");
	}

	synchronized void writeCounter(ServletContextAttributeEvent scae) {
		ServletContext servletContext = scae.getServletContext();
		onlineNum = Integer.parseInt((String) servletContext.getAttribute("onlineUser"));
		visitorNum = Integer.parseInt((String) servletContext.getAttribute("visitor"));
		totalNum = Integer.parseInt((String)servletContext.getAttribute("totalUser"));

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(counterPath));
			writer.write(Integer.toString(onlineNum)+"\r\n"+Integer.toString(visitorNum)+"\r\n"+Integer.toString(totalNum));
			writer.close();
			System.out.println("Writing");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}
