package fr.sulivan.telekinesis;

import java.io.IOException;

public class Telekinesis {

	public static void main(String[] args) {

		try {
			Client client = new Client("127.0.0.1");
			client.send("mouse 500 500");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}
