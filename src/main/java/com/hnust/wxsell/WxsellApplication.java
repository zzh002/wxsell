package com.hnust.wxsell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WxsellApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxsellApplication.class, args);
	}
}
