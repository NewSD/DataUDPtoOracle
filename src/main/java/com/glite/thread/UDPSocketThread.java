package com.glite.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * 监听端口，有数据报过来，就将数据报放入待处理队列
 * 
 * @author chenlj
 *
 */
public class UDPSocketThread extends Thread {
	private static Logger logger = Logger.getLogger(UDPSocketThread.class);
	private static int port = 50011;
	private byte[] h = Integer.toHexString(0x7b81).getBytes();

	private boolean startThread = true;
	private int counter = 0;

	public void restartThread() {
		startThread = true;
		this.start();
	}

	/**
	 * 停止线程
	 */
	public void stopThread() {
		startThread = false;
	}

	public void run() {
		System.out.println(".....start  .......UDPSocketThread.......");
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e1) {
			logger.error(e1);
		}
		while (this.startThread) {
			try {
				byte[] buf = new byte[1024];
				DatagramPacket pac = new DatagramPacket(buf, 1024);
				//System.out.println("...start.....recieve.........");
				socket.receive(pac);
				//System.out.println("...end.....recieved.........");
				counter ++;
				//System.out.println("received dp's counts: "+ counter);
				if (this.validatePac(pac)) {
					NettingQueue.addPac(pac);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
	}

	private boolean validatePac(DatagramPacket pac) {
		if (pac == null) {
			logger.error("pac is null");
			return false;
		}
		if (pac.getData() == null) {
			logger.error("received null pac data");
			return false;
		}
		if (pac.getData().length == 0) {
			logger.error("pac.getData().length=0");
			return false;
		}
		if (h.equals(Arrays.copyOfRange(pac.getData(), 0, 2))) {
			logger.error("received error packages " + pac.getData().length);
			return false;
		}
		return true;
	}

}
