package com.ef;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ef.db.LogJpaRepository;
import com.ef.entity.LogEntry;
import com.ef.parser.LogFileParser;

@Component
public class LogApp {

	@Autowired
	private LogFileParser fileParser;
	@Autowired
	private LogJpaRepository logRepository;
	
	public enum Duration {
		hourly, daily;
	}
	
	private static final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public  void parse() {
		try {
			
			LogEntry lg= logRepository.getTheLatestEntry();
			
			if(lg!=null) {
			//check if the file is already processed
			if( fileParser.isFullyProcessed(lg.getFilePointer())){
				System.out.println("\n\n\n");
				System.out.println("Given log file is already processed");
				System.out.println("\n\n\n");
			} else {
				fileParser.setReadingPoint(lg.getFilePointer());
			}
			}
			
			//this provision is to break and resume large file process and process updated log file
			
			if(lg!=null && lg.getFilePointer()!= fileParser.getFileLength()){
				fileParser.setReadingPoint(lg.getFilePointer());
			}
			
			List<LogEntry> logEntries = null;
			do { 
				logEntries = fileParser.fetchEntries();
				logRepository.save(logEntries);
			} while (!logEntries.isEmpty());
			
			System.out.println("\n\n\n");
			System.out.println("Log file parsing and database load completed");
			System.out.println("\n\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<String> findIpsReachedThreshold(Date startDate, Duration inputDuration, long threshold){
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTime(startDate);
			if (Duration.hourly.equals(inputDuration)) {
				endDate.add(Calendar.MINUTE, 60);
			} else if (Duration.daily.equals(inputDuration)) {
				endDate.add(Calendar.DATE, 1);
			}
		String startTimeStr = dbDateFormat.format(startDate);
		String endTimeStr = dbDateFormat.format(endDate.getTime());
		List<String> result = logRepository.findIpsReachedThreshold(startTimeStr, endTimeStr, threshold);
		System.out.println("\n\n\n");
		System.out.println(String.format("IPs that made more than %d requests starting from %s to %s",threshold,startTimeStr, endTimeStr));
		System.out.println("\n");
		if (result == null || result.isEmpty()) {
			System.out.println(" NONE");
		} else {
			for (String ip : result) {
				System.out.println(ip);
			}
		}
		System.out.println("\n\n\n");
		return result;
		
	}
	

}
