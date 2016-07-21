package com.crossfive.framework.server.servlet.tcp;

import io.netty.channel.Channel;

import com.crossfive.framework.common.session.Push;
import com.crossfive.framework.common.session.ServerProtocol;
import com.crossfive.framework.common.session.Session;
import com.crossfive.framework.util.WrapperUtil;
/**
 * TCP推送管道
 * @author kira
 *
 */
public class TcpPush implements Push {
	
	private Channel channel;
	
	public TcpPush(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void push(Session session, String command, byte[] body) {
		if (isPushable()) {
			channel.write(WrapperUtil.wrapper(command, 0, body));
		}
	}

	@Override
	public boolean isPushable() {
		return channel != null && channel.isWritable();
	}

	@Override
	public void clear() {
		if (channel != null) {
			channel.close();
		}
	}

	@Override
	public void discard() {
//		channel.close();
	}

	@Override
	public void heartbeat() {
		String command = "heart@heartBeat";
		channel.write(WrapperUtil.wrapper(command, 0, command.getBytes()));
	}

	@Override
	public ServerProtocol getPushProtocol() {
		// TODO Auto-generated method stub
		return ServerProtocol.TCP;
	}

}
