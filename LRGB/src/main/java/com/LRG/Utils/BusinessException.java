package com.LRG.Utils;

@SuppressWarnings("serial")
public class BusinessException extends RuntimeException {
	
	public static final String INPUT_VALIDATION_ERROR 	= "INPUT_VALIDATION_ERROR";
	public static final String AUTH_ERROR 				= "AUTH_ERROR";
	public static final String DATA_UNAVAILABLE 		= "DATA_UNAVAILABLE";
	public static final String EXE_PROCESS_ERROR 		= "EXE_PROCESS_ERROR";
	public static final String SYSTEM_ERROR 			= "SYSTEM_ERROR";
	public static final String INITIALIZATION_ERROR 	= "INITIALIZATION_ERROR";
	public static final String UNKNOWN_ERROR_OCCURED 	= "UNKNOWN_ERROR_OCCURED";
	public static final String UNSUPPORTED_ERROR 		= "UNSUPPORTED_ERROR";
	public static final String EMAIL_VALIDATION_ERROR 	= "EMAIL_VALIDATION_ERROR";
	public static final String SUBSCRIPTION_EXPIRED_ERROR 	= "SUBSCRIPTION_EXPIRED_ERROR";
	public static final String DUP_LOGIN_ERROR 			= "DUP_LOGIN_ERROR";
	public static final String USER_INACTIVE_ERROR 		= "USER_INACTIVE_ERROR";
	public static final String USER_UNAVAILABLE_ERROR	= "USER_NOT_FOUND";
	public static final String CONNECTION_ERROR			= "CONNECTION_ERROR";
	public static final String DUPLICATE_ORIGINAL_TRANSACTION_ID_ERROR	= "DUPLICATE_ORIGINAL_TRANSACTION_ID_ERROR";
	public static final String DATA_DELETION_WARNING	= "DATA_DELETION_WARNING";
	public static final String TICKER_MAX_LIMIT_REACHED_ERROR	= "TICKER_MAX_LIMIT_REACHED_ERROR";
	public static final String PORTFOLIO_CREATED_DATE_UNAVAILABLE_ERROR	= "PORTFOLIO_CREATED_DATE_UNAVAILABLE_ERROR";
	public static final String PORTFOLIO_EXISTS_AS_SUBPORTFOLIO	= "PORTFOLIO_EXISTS_AS_SUBPORTFOLIO";
	public static final String PORTFOLIO_EXISTS_AS_COMPLEX_SECURITY	= "PORTFOLIO_EXISTS_AS_COMPLEX_SECURITY";
	public static final String PORTFOLIO_EXISTS_IN_HISTORY	= "PORTFOLIO_EXISTS_IN_HISTORY";
	public static final String ZSENIA_ERROR_CODE	= "ZSENIA_ERROR_CODE";
	public static final String ALERT_EXISTS_IN_COMPLEX_ALERT	= "ALERT_EXISTS_IN_COMPLEX_ALERT";
	public static final String PORTFOLIO_IS_COMPLEX	= "PORTFOLIO_IS_COMPLEX";
	public static final String SHARE_PORT_RESPONSE_RECEIVED	= "SHARE_PORT_RESPONSE_RECEIVED";
	public static final String SHARED_PORT_NOT_FOUND	= "SHARED_PORT_NOT_FOUND";
	public static final String PLAID_LOGIN_REQUIRED = "PLAID_LOGIN_REQUIRED";
	public static final String PLAID_DUPLICATE_LINKING = "PLAID_DUPLICATE_LINKING";
	public static final String USER_UPLOAD_PORTFOLIO_ERROR = "USER_UPLOAD_PORTFOLIO_ERROR";
	public static final String IB_LOGIN_ERROR = "IB_LOGIN_ERROR";
	public static final String ALPACA_ACCOUNT_SUBMISSION_FAILED = "ALPACA_ACCOUNT_SUBMISSION_FAILED";
	public static final String ALPACA_LIVE_ACCOUNT_DOES_NOT_EXIST = "ALPACA_LIVE_ACCOUNT_DOES_NOT_EXIST";
	
	private String description;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, String description) {
		super(message);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}