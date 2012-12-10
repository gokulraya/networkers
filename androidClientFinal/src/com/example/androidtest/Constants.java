package com.example.androidtest;

public class Constants {
	//Ports
	public static final int SERVER_PORT=4445;
	public static final int SECURE_PORT=4444;
	
	//Error Codes
	public static final int NO_ERROR = 0;
	public static final int UNKNOWN_HOST_EXCEPTION = -3;
	public static final int IO_EXCEPTION = -2;
	public static final int ILLEGAL_ARGUMENT_EXCEPTION = -1;
	public static final int GENERAL_EXCEPTION = -9;
	
	//Error Messages
	public static final String INVALID_IP = "Invalid IP/Password entered";
	public static final String ERROR_DIALOG = "Error";
	
	//REGEX Patterns
	public static final String IP_REGEX="^((\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))\\.){3}(\\d|\\d{2}|([0-1]\\d{2})|(2[0-4][0-9])|(25[0-5]))$";
	public static final String WEBSITE_REGEX="^(((http|https)://)?)([A-Za-z0-9.]*)$";
	
	//Misc
	public static final String SUCCESS="SUCCESS";
	public static final String FAILED="FAILED";
	public static final String DELIMITER="^";
}
