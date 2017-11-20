package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ef.LogApp.Duration;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LogParserTest {

	@Autowired
	private LogApp app;
	
	@Test
	public final void testParse() {
		//TODO
//		app.parse();
//		fail("Not yet implemented"); // TODO
//		assertThat(lgs).hasSize(500);
	}
	
	@Test 
	public final void testFindIpsReachedThreshold() throws ParseException{
		//TODO
		Date startTime= new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse("2017-01-01.23:58:00)");
		List<String> ips =app.findIpsReachedThreshold(startTime, Duration.hourly, 3);
		System.out.println(ips);
	}
	


}