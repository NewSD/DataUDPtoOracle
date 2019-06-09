package com.glite.thread;

import java.net.DatagramPacket;
import java.util.List;

public class UDPSaveThread extends Thread {
	private boolean isStartThread = true;

	public void restartThread() {
		isStartThread = true;
	}

	public void stopThread() {
		isStartThread = false;
	}

	public void run() {
		System.out.println("....start........UDPSaveThread.....");
		while (this.isStartThread) {
			try {
				List<DatagramPacket> pacList = NettingQueue.getPacList();
				if (pacList != null && pacList.size() > 0) {
					Thread thread = new ThreadSaveOracle(pacList);
					thread.start();
				}
			} catch (Throwable e) {
				System.out.println("thread created failed:" + e);
			}
		}
	}
}
