package app.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class EchoServerThread extends Thread {
	private static final int BUFFER_SIZE=1024;
	private DatagramSocket serverSocket;
	private byte[] buf=new byte[BUFFER_SIZE];
	
	public EchoServerThread(int serverPortNum){
		super("UDP Server");
		try {
			serverSocket = new DatagramSocket(serverPortNum,InetAddress.getByName("127.0.0.1")); //odpowiada bind + socket
			System.out.println("SERWER UDP: dzia³a na porcie: "+serverSocket.getLocalPort()+", adresie IP: "+serverSocket.getLocalAddress());
		} catch (SocketException e) {
			System.out.println("B³¹d przy tworzeniu gniazda!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("B³êdna nazwa hosta (adres IP)");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println(getName()+" started!");
		DatagramPacket packet=null;
		boolean run=true;
		while (run) {
			Arrays.fill(buf, (byte)'\0');
			System.out.println("SERWER UDP: oczekuje na dane...");
			
			packet = new DatagramPacket(buf, buf.length);
			try {
				serverSocket.receive(packet);
			} catch (IOException e1) {
				System.out.println("B³¹d w recieve!");
				run=false;
				e1.printStackTrace();
			}
			
			System.out.println("SERWER UDP: odebrano dane od "+packet.getPort()+"("+packet.getAddress()+")");
			System.out.println("SERWER UDP: odebrano: "+new String(packet.getData()));
			System.out.println("//=====================//");
			
			packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
			try {
				serverSocket.send(packet);
			} catch (IOException e1) {
				System.out.println("B³¹d w send!");
				run=false;
				e1.printStackTrace();
			}
		}
		serverSocket.close();
		System.out.println(getName()+" terminated!");
	}
}
