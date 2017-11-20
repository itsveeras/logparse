package com.ef.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ef.entity.LogEntry;

/**
 * @author Veeramani S
 * 
 * 
 *
 */
@PropertySource("classpath:application.properties")
@Scope(value = "singleton")
@Component
public class LogFileParser {

	/**
	 * Number of lines to be fetched and kept in memory at a time
	 */
	@Value("${log.file.entries.fetch.size}")
	private int fetchSize;
	@Value("${log.file.name}")
	private String fileName;
	@Value("${log.file.date.format}")
	private String logDateFormat;
	private SimpleDateFormat logDateFormater;
	@Value("${log.file.separator}")
	private String fileSeparator;
	private List<LogEntry> logEntries = new ArrayList<>();
	private RandomAccessFile br;
	private boolean eof=false;
	private long filePointer =0;

	public List<LogEntry> fetchEntries() throws IOException {
		String line = null;
		int i = 0;
		
		logEntries.clear();
		do {
			line = br.readLine();
			if (line != null) {
				logEntries.add(parseLogLine(line));
				filePointer = br.getFilePointer();
			} else {
				eof = true;
				return logEntries;
			}
		} while (++i < fetchSize && !eof);
		return logEntries;
	}

	private LogEntry parseLogLine(String line) {
		String[] lineContents= line.split(fileSeparator);
		try {
			// HTTP status & request device information is not in scope 
			// Now I assume the log file is consist in the format 
			Date logEntryDate = logDateFormater.parse(lineContents[0]);
			String ipAddress = lineContents[1];
			return new LogEntry(ipAddress,new Timestamp(logEntryDate.getTime()),filePointer);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @return
	 * @throws IOException 
	 */
	public void setReadingPoint(long filePoint) throws IOException {
			br.seek(filePoint);
			//move to the unread line
			br.readLine();
	}
	
	public long getFileLength() throws IOException{
		return br.length();
	}
	
	public boolean isFullyProcessed(long lastLineLocPoint) throws IOException{
		boolean processed =false;
		br.seek(lastLineLocPoint);
		br.readLine();
		if(br.length() == br.getFilePointer()) {
			processed = true;
		}
		//set the file pointer back to its original position
		br.seek(filePointer);
		return processed;
	}

	/**
	 * Reserved method
	 * @return
	 */
	public boolean isEof() {
		return eof;
	}

	@PostConstruct
	public void init() {
		try {
			br = new RandomAccessFile(new File(fileName), "r");
		} catch (FileNotFoundException e) {
			System.out.println("\n\n\n");
			System.out.println("File "+fileName+" is not available");
			System.out.println("\n\n\n");
		}
		logDateFormater = new SimpleDateFormat(logDateFormat);
	}

	@PreDestroy
	public void destroy() throws IOException {
		if (br != null) {
			br.close();
		}
	}

}

