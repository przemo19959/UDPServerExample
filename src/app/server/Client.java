package app.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Client {
	private final int clientPort;
	private final InetAddress clientAddress;
	private final List<Integer> clientList;

	public Client(int clientPort, InetAddress clientAddress) {
		this.clientPort = clientPort;
		this.clientAddress = clientAddress;
		clientList = new ArrayList<>();
	}

	public void clearList() {
		clientList.clear();
	}

	// komenda: add val1 val2 ...
	public String addCommand(String command) {
		command = command.replace("add ", "");
		clientList.addAll(
			Arrays.stream(command.split(" "))//
				.map(val -> Integer.valueOf(val))//
				.collect(Collectors.toList()));
		return "Current list (size: " + clientList.size() + "): " + clientList;
	}

	public String sortItems(boolean isAscending) {
		if(clientList.size() > 0) {
			if(isAscending) {
				return "Sorted ascend: " + clientList.stream().sorted()//
					.collect(Collectors.toList());
			}
			return "Sorted desc: " + clientList.stream().sorted(Comparator.reverseOrder())//
				.collect(Collectors.toList());
		}
		return "List is empty, nothing to sort!";
	}

	public String sumItems() {
		return "Sum: " + clientList.stream().collect(Collectors.summingInt(item -> item)).intValue();
	}

	public String subItems() {
		int result = clientList.get(0);
		for(int i = 1;i < clientList.size();i++) {
			result -= clientList.get(i);
		}
		return "Sub: " + result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientAddress == null) ? 0 : clientAddress.hashCode());
		result = prime * result + clientPort;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(!(obj instanceof Client))
			return false;
		Client other = (Client) obj;
		if(clientAddress == null) {
			if(other.clientAddress != null)
				return false;
		} else if(!clientAddress.equals(other.clientAddress))
			return false;
		if(clientPort != other.clientPort)
			return false;
		return true;
	}
}
