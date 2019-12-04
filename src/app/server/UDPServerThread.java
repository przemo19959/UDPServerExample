package app.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import app.Common;

public class UDPServerThread extends Thread {
	private DatagramSocket serverSocket;
	
	private List<Client> clients;
	private static final Pattern ADD_NUMBER_COMMAND_PATTERN = Pattern.compile("add(( | -)\\d+)+");

	public UDPServerThread(int serverPortNum) {
		super("UDP Server");
		try {
			serverSocket = new DatagramSocket(serverPortNum, InetAddress.getByName("127.0.0.1")); // odpowiada bind + socket
			clients = new ArrayList<>();
			System.out.println("SERWER UDP: dzia³a na porcie: " + serverSocket.getLocalPort() + ", adresie IP: " + serverSocket.getLocalAddress());
		} catch (SocketException e) {
			System.out.println("B³¹d przy tworzeniu gniazda!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("B³êdna nazwa hosta (adres IP)");
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println(getName() + " started!");
		boolean run = true;

		while (run) {
			System.out.println("SERWER UDP (aktualna liczba klientów: "+clients.size()+"): oczekuje na dane...");
			DatagramPacket receivedPacket = Common.receivePacketAtSocket(serverSocket);
			if(receivedPacket==null)
				break;
			
			Client client=addClientIfNew(receivedPacket);
		
			String val=processReceivedPacket(client, getReceivedData(receivedPacket.getData()));	
			if(!val.startsWith("kill")) {
				DatagramPacket toSendPacket = new DatagramPacket(val.getBytes(), val.length(),
					receivedPacket.getAddress(), receivedPacket.getPort());
				run = Common.sendPacketFromSocket(toSendPacket, serverSocket);
			}
		}

		// cleanUp
		serverSocket.close();
		System.out.println(getName() + " terminated!");
	}
	
	private String getReceivedData(byte[] buf) {
		int index=0;
		for(int i=0;i<buf.length;i++) {
			if(buf[i]==0) {
				index=i;
				break;
			}
		}
		return new String(Arrays.copyOfRange(buf, 0, index));
	}

	private Client addClientIfNew(DatagramPacket packet) {
		Client client=new Client(packet.getPort(), packet.getAddress());
		if(!clients.contains(client)) {
			clients.add(client);
			return client;
		}
		return clients.stream().filter(item->item.equals(client)).findFirst().get();
	}

	private String processReceivedPacket(Client client, String receivedData) {
		String response="Command not supported!\n\tadd <number>... - adds numbers to client list\n"
				+ "\tsum - prints sum of items\n\tsub - prints substraction of items\n\tsort asc|dsc - prints numbers"
				+ " sorted in ascending or descending order";
		receivedData=receivedData.replace("\r\n", ""); //wa¿ne
		if(ADD_NUMBER_COMMAND_PATTERN.matcher(receivedData).matches()) {
			response=client.addCommand(receivedData);
		} else {
			switch (receivedData) {//@formatter:off
				case "sort asc": response=client.sortItems(true);break;
				case "sort dsc": response=client.sortItems(false);break;
				case "sum": response=client.sumItems();break;
				case "sub": response=client.subItems();break;
				case "kill":{
					clients.remove(client);
					System.out.println("Client removed from server list!");
					return "kill";
				}
			}//@formatter:on
			if(!response.startsWith("Command not supported!"))
				client.clearList();
		}
		return response+"\n";
	}
}
