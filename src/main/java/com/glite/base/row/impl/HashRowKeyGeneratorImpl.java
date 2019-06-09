package com.glite.base.row.impl;

import java.util.Random;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;

import com.glite.base.row.IRowKeyGenerator;

public class HashRowKeyGeneratorImpl implements IRowKeyGenerator {

	private long currentId = 1;
	private long currentTime = System.currentTimeMillis();
	private Random random = new Random();
	private int row_length = 8;
	@Override
	public String nextStrId() {
		try {
			currentTime = System.currentTimeMillis() + random.nextInt(1000);
			byte[] lowT = Bytes.copy(Bytes.toBytes(currentTime), 4, 4);
			byte[] lowU = Bytes.copy(Bytes.toBytes(currentId), 4, 4);
			return MD5Hash.getMD5AsHex(Bytes.add(lowU, lowT)).substring(0, row_length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			currentId++;
		}
	}
	@Override
	public byte[] nextId() {
		try {
			currentTime += random.nextInt(1000);
			byte[] lowT = Bytes.copy(Bytes.toBytes(currentTime), 4, 4);
			byte[] lowU = Bytes.copy(Bytes.toBytes(currentId), 4, 4);
			return Bytes.add(MD5Hash.getMD5AsHex(Bytes.add(lowU, lowT)).substring(0, row_length).getBytes(), Bytes.toBytes(currentId));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			currentId++;
		}
	}
}
