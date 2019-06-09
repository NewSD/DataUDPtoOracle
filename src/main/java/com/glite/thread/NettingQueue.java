package com.glite.thread;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NettingQueue {
	private static ConcurrentLinkedQueue<DatagramPacket> queue = new ConcurrentLinkedQueue<DatagramPacket>();

	public static List<DatagramPacket> getPacList() {
		List<DatagramPacket> l = new ArrayList<DatagramPacket>();
		int size = queue.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				l.add(queue.poll());
			}
		}
		return l;
	}

	public static void addPac(DatagramPacket pac) {
		queue.add(pac);
	}
}
