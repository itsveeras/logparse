package com.ef.entity;

import java.sql.Timestamp;

//import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.BatchSize;

@Entity
@NamedQueries({ @NamedQuery(name = "find_latest_log", query = "select l from LogEntry l order by l.id desc"),
				@NamedQuery(name = "find_ips_reached_threshold", query = "select l.ipAddress from LogEntry l "
						+ "where function('date_format', l.logEventTime, '%Y-%m-%d %H:%i:%s') >= :startTime "
						+ "and function('date_format', l.logEventTime, '%Y-%m-%d %H:%i:%s') <= :endTime "
						+ "group by l.ipAddress having count(l.ipAddress) >= :threshold  ") })
//	@NamedQuery(name = "find_ips_reached_threshold1", query = "select l.ipAddress from LogEntry l "
//			+ "where l.logEventTime  >= :startTime "
//			+ "and l.logEventTime <= :endTime "
//			+ "group by l.ipAddress having count(l.ipAddress) >= :threshold  ") })
@BatchSize(size = 100)
public class LogEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String ipAddress;
	private Timestamp logEventTime;
	private long filePointer;
//	private FileInfo fileInfo;

	public LogEntry() {

	}

	public LogEntry(int id, String ipAddress, String location, Timestamp logEventTime, long filePointer) {
		super();
		this.id = id;
		this.ipAddress = ipAddress;
		this.logEventTime = logEventTime;
		this.filePointer = filePointer;
	}

	public LogEntry(String ipAddress,Timestamp logEventTime, long filePointer) {
		super();
		this.ipAddress = ipAddress;
		this.logEventTime = logEventTime;
		this.filePointer = filePointer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Timestamp getLogEventTime() {
		return logEventTime;
	}

	public void setLogEventTime(Timestamp logEventTime) {
		this.logEventTime = logEventTime;
	}

	public long getFilePointer() {
		return filePointer;
	}

	public void setFilePointer(long filePointer) {
		this.filePointer = filePointer;
	}

}