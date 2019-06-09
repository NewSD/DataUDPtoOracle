package com.glite.domain;

/**
 * 位置信息
 */
public class PositionInfo {
	private String posId;//唯一标识
	private String userId;//用户账号
	// 定位0/1坐标数据
	private String x;
	private String y;
	private String z;
	private String positionTime;// 定位时间
	private String floorId;//所属楼层
	private String apmac;//AP设备MAC
	private String mapNumber;//地图编号
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getZ() {
		return z;
	}
	public void setZ(String z) {
		this.z = z;
	}
	public String getPositionTime() {
		return positionTime;
	}
	public void setPositionTime(String positionTime) {
		this.positionTime = positionTime;
	}
	public String getFloorId() {
		return floorId;
	}
	public void setFloorId(String floorId) {
		this.floorId = floorId;
	}
	public String getApmac() {
		return apmac;
	}
	public void setApmac(String apmac) {
		this.apmac = apmac;
	}
	public String getMapNumber() {
		return mapNumber;
	}
	public void setMapNumber(String mapNumber) {
		this.mapNumber = mapNumber;
	}
	@Override
	public String toString() {
		return "PositionInfo [posId=" + posId + ", userId=" + userId + ", x=" + x + ", y=" + y + ", z=" + z
				+ ", positionTime=" + positionTime + ", floorId=" + floorId + ", apmac=" + apmac + ", mapNumber="
				+ mapNumber + "]";
	}

}
