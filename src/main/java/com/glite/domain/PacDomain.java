package com.glite.domain;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.glite.base.row.IRowKeyGenerator;
import com.glite.base.row.impl.HashRowKeyGeneratorImpl;

public class PacDomain {

	static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(PacDomain.class);
	private IRowKeyGenerator rkGen = new HashRowKeyGeneratorImpl();

	private byte[] datas;

	public PacDomain(byte[] datas) {
		this.datas = datas;
	}

	
	public byte[] getHeader() {
		return Arrays.copyOfRange(this.datas, 0, 2);
	}

	public Short getLengthI() {
		return Bytes.toShort(this.getLength());
	}

	public byte[] getLength() {
		return Arrays.copyOfRange(this.datas, 2, 4);
	}

	public Integer getMapIdI() {
		return Bytes.toInt(this.getMapId());
	}

	public byte[] getMapId() {
		return Arrays.copyOfRange(this.datas, 4, 8);
	}

	public Integer getAreaIdI() {
		return Bytes.toInt(this.getAreaId());
	}

	public byte[] getAreaId() {
		return Arrays.copyOfRange(this.datas, 8, 12);
	}

	public Integer getPoilDI() {
		return Bytes.toInt(this.getPoilD());
	}

	public byte[] getPoilD() {
		return Arrays.copyOfRange(this.datas, 12, 16);
	}

	public String getMacStr() {
		byte[] macs = this.getMac();
		return this.getMacStr(macs);
	}

	public String getMacStr(byte[] macs) {
		StringBuilder sbd = new StringBuilder();
		for (int i = 0; i < macs.length; i++) {
			byte mac = macs[i];
			String sub = "";
			if ((0xff & mac) < 16) {
				sub = "0" + Bytes.toHex(Arrays.copyOfRange(macs, i, i + 1));
			} else {
				sub = Bytes.toHex(Arrays.copyOfRange(macs, i, i + 1));
			}
			sbd.append(sub);
		}
		return sbd.toString();
	}

	public byte[] getMac() {
		return Arrays.copyOfRange(this.datas, 16, 22);
	}

	public Long getTimeStampL() {
		return Bytes.toLong(this.getTimeStamp());
	}

	public byte[] getTimeStamp() {
		return Arrays.copyOfRange(this.datas, 22, 30);
	}

	public long getTs() {
		return Bytes.toLong(this.getTimeStamp());
	}

	public float getXF() {
		return Bytes.toFloat(this.getX());
	}

	public byte[] getX() {
		return Arrays.copyOfRange(this.datas, 30, 34);
	}

	public float getYF() {
		return Bytes.toFloat(this.getY());
	}

	public byte[] getY() {
		return Arrays.copyOfRange(this.datas, 34, 38);
	}

	public float getZF() {
		return Bytes.toFloat(this.getZ());
	}

	public byte[] getZ() {
		return Arrays.copyOfRange(this.datas, 38, 42);
	}

	public Boolean getIsAssociaB() {
		return Bytes.toBoolean(getIsAssocia());
	}

	public byte[] getIsAssocia() {
		return Arrays.copyOfRange(this.datas, 42, 43);
	}

	public byte[] getOptions() {
		if (Bytes.toShort(this.getLength()) > 43) {
			return Arrays.copyOfRange(this.datas, 43, Bytes.toShort(this.getLength()));
		}
		return null;
	}
}
