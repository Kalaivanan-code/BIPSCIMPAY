package com.bornfire.controller;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
	  private static Logger logger = LoggerFactory.getLogger(AsyncService.class);
	  
	  @Async("asyncExecutor")
		public CompletableFuture<Object> getEmployeeAddress() {
			logger.info("EmpAddress");
			return null;
		}
		@Async("asyncExecutor")
		public CompletableFuture<Object> getEmployeeName() {
			logger.info("EmpName");
			return null;
		}
		@Async("asyncExecutor")
		public CompletableFuture<Object> getEmployeePhone() {
			logger.info("EmpPhone");
			return null;
		}}
