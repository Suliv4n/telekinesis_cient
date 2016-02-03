package fr.sulivan.telekinesis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
	
	private Socket server;
	private PrintWriter out;
	private boolean verbose = true;
	
	private Thread listen;
	
	private boolean connected = false;
	
	public static final int DEFAULT_PORT = 4242;
	
	public ArrayList<String> queue;
	
	public Client(String address) throws UnknownHostException, IOException{
		server = new Socket(address, DEFAULT_PORT);
		queue = new ArrayList<String>();
		
		
		BufferedReader input = new BufferedReader(new InputStreamReader(server.getInputStream()));
		connected = true;
		out = new PrintWriter(server.getOutputStream());
		
		listen = new Thread(
			() -> {
				while(true){
					try {
						String line = input.readLine();
						log("Server send : %s", line);
						if(line.equals("ok")){
							connected = true;
						}
						if(queue.size() > 0){
							send(queue.get(0));
							queue.remove(0);
						}
					} catch (Exception e) {
						log("Server disconnected");
						connected = false;
						listen.interrupt();
					}
				}
			}	
		);
		
		listen.start();
		
	}
	
	public void send(String message) throws IOException{
		if(connected){
			log("Send to server : " + message);
			PrintWriter out = new PrintWriter(server.getOutputStream());
	        out.println(message);
	        out.flush();
		}
		else{
			queue.add(message);
		}
	}
	
	
	
	//log
	
	public void log(String message, Object... args){
		if(verbose){
			System.out.println(String.format(message, args));
		}
	}
	
	public void log(String message){
		if(verbose){
			System.out.println(message);
		}
	}
}
