package com.greydev.messenger.exception;

public class AppException extends Exception {

	private static final long serialVersionUID = -8397370583597010563L;
	private int statusCode;
	private int errorCode;
	private String errorDescription;
	private String errorDocumentationLink;
	private String requestUri;
	private String requestMedhod;

	public AppException() {
	}

	public AppException(int statusCode, int errorCode, String errorDescription, String errorDocumentationLink,
			String requestMedhod, String requestUri) {
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.errorDescription = errorDescription;
		this.errorDocumentationLink = errorDocumentationLink;
		this.requestMedhod = requestMedhod;
		this.requestUri = requestUri;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public String getErrorDocumentationLink() {
		return errorDocumentationLink;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public String getRequestMedhod() {
		return requestMedhod;
	}

}
