package app.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class EchoClientThread extends Thread {
	private static final int BUFFER_SIZE=1024;
	private DatagramSocket clientSocket;
	private char[] buf=new char[BUFFER_SIZE];
	private BufferedReader input;
	private InetAddress serverAddr;
	private int serverPort;
	
	public EchoClientThread(String serverAddr,int serverPort){
		super("UDP Client");
		try {
			this.serverPort=serverPort;
			this.serverAddr=InetAddress.getByName(serverAddr);
			clientSocket = new DatagramSocket(); //odpowiada bind + socket
			System.out.println("KLIENT UDP: dzia³a na porcie: "+clientSocket.getLocalPort()+", adresie IP: "+clientSocket.getLocalAddress());
			input = new BufferedReader(new InputStreamReader(System.in));
		} catch (SocketException e) {
			System.out.println("B³¹d przy tworzeniu gniazda!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("B³¹d: z³y adres IP serwera!");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println(getName()+" started!");
		DatagramPacket packet=null;
		boolean run=true;
		while (run) {
			Arrays.fill(buf, '\0');
			System.out.print("KLIENT UDP: wprowadz dane:");
			try {
				input.read(buf);
			} catch (IOException e) {
				run=false;
				e.printStackTrace();
			}
			
			packet = new DatagramPacket(new String(buf).getBytes(), buf.length,serverAddr,serverPort);
			try {
				clientSocket.send(packet);
			} catch (IOException e1) {
				System.out.println("B³¹d w send!");
				run=false;
				e1.printStackTrace();
			}
			
			System.out.println("KLIENT UDP: wys³ano dane do "+packet.getPort()+"("+packet.getAddress()+")");
			
			packet = new DatagramPacket(new String(buf).getBytes(), buf.length);
			try {
				clientSocket.receive(packet);
			} catch (IOException e1) {
				System.out.println("B³¹d w receive!");
				run=false;
				e1.printStackTrace();
			}
			System.out.println("KLIENT UDP: odebrano echo: "+new String(packet.getData()));
			System.out.println("//=====================//");
		}
		clientSocket.close();
		System.out.println(getName()+" terminated!");
	}
}
