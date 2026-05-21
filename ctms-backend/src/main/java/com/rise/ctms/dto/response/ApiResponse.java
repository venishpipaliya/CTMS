package com.rise.ctms.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
	private boolean success;
	private String message;
	private T data;
	private String error;
	private Instant timeStamp;
	
	public static <T> ApiResponse<T> success(T data){
		return ApiResponse.<T>builder()
				.success(true)
				.data(data)
				.timeStamp(Instant.now())
				.build();
		
		
	}
	
	public static <T> ApiResponse<T> success(T data, String message){
		return ApiResponse.<T>builder()
				.success(true)
				.data(data)
				.message(message)
				.timeStamp(Instant.now())
				.build();
		
		
	}
	
	public static <T> ApiResponse<T> error(String error){
		return ApiResponse.<T>builder()
				.success(true)
				.error(error)
				.timeStamp(Instant.now())
				.build();
		
		
	}
	
}
