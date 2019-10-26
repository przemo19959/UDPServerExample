package app.client;

public class EchoClient {
	private static final int DEFAULT_SERVER_PORT=8765;
	
	public static void main(String[] args){
		if(args.length!=2)
			new EchoClientThread("127.0.0.1", DEFAULT_SERVER_PORT).start();
		else
			new EchoClientThread(args[0], Integer.valueOf(args[1])).start();
	}
}
