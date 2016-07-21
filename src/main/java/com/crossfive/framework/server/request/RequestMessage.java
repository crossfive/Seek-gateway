package com.crossfive.framework.server.request;

/**
 * 消息封装格式
 * @author kira
 *
 */
public class RequestMessage {

	private int requestId;
	
	private byte[] content;
	
	private String command;
	
	private String sessionId;
	
	public RequestMessage() {}
	
	public RequestMessage(int requestId, byte[] content, String command, String sessionId) {
		this.requestId = requestId;
		this.content = content;
		this.command = command;
		this.sessionId = sessionId;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	
	
}
