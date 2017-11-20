package com.ef;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableAutoConfiguration
@SpringBootApplication
public class LogScanBoot {

	public static void main(String[] args) {
		SpringApplication.run(LogScanBoot.class, args);
		System.out.println(args[0]);
	}
}
