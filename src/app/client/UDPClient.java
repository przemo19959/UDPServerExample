package app.client;

import java.util.regex.Pattern;

public class UDPClient {
	private static final int DEFAULT_SERVER_PORT=8765;
	private static final String DEFAULT_SERVER_IPADDR="127.0.0.1";
	private static final Pattern PORT_PATTERN=Pattern.compile("[3-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-6][0-5][0-5][0-3][0-5]");
	private static final Pattern IP_ADDR_PATTERN=Pattern.compile("(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])");
	
	public static void main(String[] args){
		if(args.length==2 && IP_ADDR_PATTERN.matcher(args[0]).matches() && PORT_PATTERN.matcher(args[1]).matches())
			new UDPClientThread(args[0], Integer.valueOf(args[1])).start();
		else
			new UDPClientThread(DEFAULT_SERVER_IPADDR, DEFAULT_SERVER_PORT).start();
	}
}
