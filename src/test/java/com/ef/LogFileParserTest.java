package com.ef;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.ef.entity.LogEntry;
import com.ef.parser.LogFileParser;

/**
 * @author Veeramani S
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LogFileParserTest {
	@Autowired
	private LogFileParser logFileParser;



	/**
	 * Test method for {@link com.ef.parser.LogFileParser#fetchEntries()}.
	 * @throws IOException 
	 */
	@Test
	public final void testFetchEntries() throws  IOException  {
		List<LogEntry> lgs= logFileParser.fetchEntries();
		assertThat(lgs).hasSize(500);
	}

}
