package com.crossfive.framework.server.mvc.view;

public class ByteResult implements Result<byte[]> {
	
	private byte[] result;
	
	public ByteResult(byte[] result) {
		this.result = result;
	}

	@Override
	public String getViewName() {
		return "byte";
	}

	@Override
	public byte[] getResult() {
		return result;
	}

}
