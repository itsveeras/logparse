package com.ef;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ef.LogApp.Duration;

@Component
public class InputProcessor  implements CommandLineRunner{

	@Autowired
	private LogApp app;
	static final String START_DATE_PARAM = "--startDate";
	static final String DURATION_PARAM = "--duration";
	static final String THRESHOLD_PARAM = "--threshold";
	static final String PARSE = "--parse";
	private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
	
	@Override
	public void run(String... args) throws Exception {
		
		//input parameter capture and validation
		//parse is default operation when this application is executed without parameter

		if (args.length == 0) {
			return;
		} else if(args.length == 1){
			if (PARSE.equals(args[0])) {
				app.parse();
			}
			
		} else {
		int threshold = 0;
		Date inputDate = null;
		Duration duration = null;
				try {
					for (String input : args) {
						String[] token = input.split("=");
						String key = token[0];
						String value = token[1];
						switch (key) {
						case START_DATE_PARAM:
							inputDate = inputDateFormat.parse(value);
							break;
						case DURATION_PARAM:
							if (Duration.daily.toString().equals(value)) {
								duration = Duration.daily;
							} else if (Duration.hourly.toString().equals(value)) {
								duration = Duration.hourly;
							}
							break;
						case THRESHOLD_PARAM:
							threshold = Integer.parseInt(value);
							break;
						default:
							throw new RuntimeException("Invalid input");
						}
					}
					if (threshold < 1 || inputDate == null || duration == null) {
						throw new RuntimeException("Invalid input");
					}

				} catch (Exception e) {
					System.out.println("Invalid Input. Parameters exepcted in the format --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100");
					System.exit(-1);
				}
		app.findIpsReachedThreshold(inputDate, duration, threshold);
	}
		
	}

}
