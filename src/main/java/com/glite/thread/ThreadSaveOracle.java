package com.glite.thread;

import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.glite.base.row.IRowKeyGenerator;
import com.glite.base.row.impl.HashRowKeyGeneratorImpl;
import com.glite.domain.PacDomain;
import com.glite.domain.PositionInfo;
import com.glite.utils.JDBCUtils;

public class ThreadSaveOracle extends Thread {
	private static Logger logger = Logger.getLogger(ThreadSaveOracle.class);
	private static IRowKeyGenerator rkGen = new HashRowKeyGeneratorImpl();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static int threadNum = 0;
	private static Object lock = new Object();
	private List<DatagramPacket> pacList;

	private static void threadAdd() {
		synchronized (lock) {
			threadNum = threadNum + 1;
		}
	}

	private static void threadDel() {
		synchronized (lock) {
			threadNum = threadNum - 1;
		}
	}

	public ThreadSaveOracle(List<DatagramPacket> pacList) {
		this.pacList = pacList;
		threadAdd();
	}

	/**
	 * 
	 * 211.67.150.26:12092
1024
...............header length....................
header:[B@380c4de8
length:59
211.67.150.26:12092
1024
	 */
	
	
	public void run() {
		long start = System.currentTimeMillis();
		try {
			for (DatagramPacket pac : pacList) {
				byte[] value = pac.getData();
//				System.out.println(pac.getAddress().getHostAddress() + ":" + pac.getPort());
//				System.out.println(pac.getLength());
				// logger.info("----------------saveRealPosToOracle------------");
				PacDomain domain = new PacDomain(value);
//				System.out.println("...............header length....................");
//				System.out.println("header:" + domain.getHeader());
//				System.out.println("length:" + domain.getLengthI());
//				System.out.println("...............body....................");
//				System.out.println(Arrays.toString(value));
//				System.out.println("x:" + domain.getXF());
//				System.out.println("y:" + domain.getYF());
//				System.out.println("z:" + domain.getZF());
//				System.out.println("mac:" + domain.getMacStr());
//				System.out.println("areaid:" + domain.getAreaIdI());
//				System.out.println("mapid:" + domain.getMapIdI());
//				System.out.println("poiid:" + domain.getPoilDI());
//				System.out.println("isAssica:" + domain.getIsAssociaB());
//				System.out.println("TimeStamp:" + domain.getTimeStampL());
				
				
				PositionInfo pos = new PositionInfo();
				pos.setPosId(rkGen.nextStrId());
				pos.setX(String.valueOf(domain.getXF()));
				pos.setY(String.valueOf(domain.getYF()));
				pos.setZ(String.valueOf(domain.getZF()));
				pos.setPositionTime(sdf.format(new Date(domain.getTimeStampL())));
				pos.setApmac(domain.getMacStr());
				pos.setMapNumber(String.valueOf(domain.getMapIdI()));

				String poi_id = String.valueOf(domain.getPoilDI());
				String user_mac = String.valueOf(domain.getMacStr());
				String userid = null;
				String floorId = null;

				floorId = new MyInnerClass().getFloorIdFromOracle(poi_id);
				userid = new MyInnerClass().getUserIdFromOracle(user_mac);

				pos.setUserId(userid);
				pos.setFloorId(floorId);
				// logger.info("query posinfo From Phoenix:" + pos);

				try {
					// logger.info("....save PositionInfo:" + pos);
					String tableName = new MyInnerClass().createTableEveryday();
					new MyInnerClass().savePostionInfo(tableName, pos);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (Throwable e) {
			logger.error(e);
		} finally {
			logger.info("   thread_avtive_nums:" + threadNum + "   receive_package_nums:" + pacList.size()
					+ "   thread_avtive_time:" + (System.currentTimeMillis() - start) + "    ");
			threadDel();
		}
	}

	class MyInnerClass {
		private String getFloorIdFromOracle(String poi_id) {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String floorId = null;
			Integer poiid = Integer.valueOf(poi_id);
			try {
				conn = JDBCUtils.getConnection();
				String sql = "SELECT * FROM POI WHERE POIID = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, poiid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					floorId = rs.getString("FLOORID");
					// System.out.println("FLOORID:" + floorId);
				}
			} catch (Throwable e) {
				logger.error(e);
			} finally {
				JDBCUtils.release(rs, stmt, conn);
			}
			return floorId;
		}

		private String getUserIdFromOracle(String user_mac) {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String userid = null;
			try {
				conn = JDBCUtils.getConnection();
				String sql2 = "SELECT * FROM ONLINEINFO WHERE USERMAC = ?";
				stmt = conn.prepareStatement(sql2);
				stmt.setString(1, user_mac);
				rs = stmt.executeQuery();
				// System.out.println("....PRINT SQL2....." + sql2);
				if (rs.next()) {
					userid = rs.getString("USERID");
					// System.out.println("USER_ID:" + userid);
				}
			} catch (Throwable e) {
				logger.error(e);
			} finally {
				JDBCUtils.release(rs, stmt, conn);
			}
			return userid;
		}

		private boolean hasTable(String tableName) {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtils.getConnection();
				stmt = conn.createStatement();
				String queryTable = "select count(*) from user_tables where table_name = '" + tableName + "'";
				rs = stmt.executeQuery(queryTable);
				if (rs.next()) {
					BigDecimal num = rs.getBigDecimal("count(*)");
					if (num.intValue() == 0) {
						return false;
					} else if (num.intValue() == 1) {
						return true;
					}
				}
			} catch (Throwable e) {
				logger.error(e);
			} finally {
				JDBCUtils.release(rs, stmt, conn);
			}
			return false;
		}

		public String createTableEveryday() {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			String tableName = "POSITION_" + df.format(new Date());
			if (!hasTable(tableName)) {
				Connection conn = null;
				Statement stmt = null;
				ResultSet rs = null;
				try {
					conn = JDBCUtils.getConnection();
					stmt = conn.createStatement();
					String createTable = "create table " + tableName
							+ "(pos_id varchar2(32) not null primary key, user_id varchar2(32),x varchar2(32),y varchar2(32),z varchar2(32),position_time varchar2(32),floor_id varchar2(32),ap_mac varchar2(32),map_number varchar2(32))";
					boolean execute = stmt.execute(createTable);
					System.out.println("create table:" + execute);
				} catch (Throwable e) {
					logger.error(e);
				} finally {
					JDBCUtils.release(rs, stmt, conn);
				}

				logger.info("Create Position Table " + tableName + " Success!");
			}
			return tableName;
		}

		public void savePostionInfo(String tableName, PositionInfo pos) {
			if (pos != null) {
				Connection conn = null;
				PreparedStatement stmt = null;
				try {
					conn = JDBCUtils.getConnection();
					String sql = "insert into " + tableName + " values(?,?,?,?,?,?,?,?,?)";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, pos.getPosId());
					stmt.setString(2, pos.getUserId());
					stmt.setString(3, pos.getX());
					stmt.setString(4, pos.getY());
					stmt.setString(5, pos.getZ());
					stmt.setString(6, pos.getPositionTime());
					stmt.setString(7, pos.getFloorId());
					stmt.setString(8, pos.getApmac());
					stmt.setString(9, pos.getMapNumber());
					stmt.execute();
				} catch (Throwable e) {
					logger.error(e);
				} finally {
					JDBCUtils.release(stmt, conn);
				}
				// logger.info("TO " + tableName + " insert position data :" +
				// pos + " success..");
			}
		}
	}

}
