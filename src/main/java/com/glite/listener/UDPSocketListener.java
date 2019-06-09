package com.glite.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.glite.thread.UDPSaveThread;
import com.glite.thread.UDPSocketThread;

public class UDPSocketListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(UDPSocketListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("-----------contextDestroyed---------");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("-----------contextInitialized---------");
		UDPSocketThread socket = new UDPSocketThread();
		UDPSaveThread save = new UDPSaveThread();
		socket.start();
		save.start();
	}

}
