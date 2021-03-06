package ServerClient;

import java.net.*;

import core.BetColor;

import java.io.*;

public class ServerThread extends Thread
{
	private Server server = null;
	private Socket socket = null;
	private int ID = -1;
	private DataInputStream streamIn = null;
	private DataOutputStream streamOut = null;

	public ServerThread(Server _server, Socket _socket)
	{
		super();
		server = _server;
		socket = _socket;
		ID = socket.getPort();
	}

	public void sendMessage(String msg)
	{
		try
		{
			streamOut.writeUTF(msg);
			streamOut.flush();
		} catch (IOException ioe)
		{
			System.out.println(ioe.getMessage());
			server.remove(ID);
			stop();
		}
	}
	

	public int getID()
	{
		return ID;
	}

	public void run()
	{
		System.out.println("Server Thread " + ID + " running.");
		while (true)
		{
			try
			{
				server.handle(streamIn.readUTF());
			} catch (IOException ioe)
			{
				System.out.println(ID + " ERROR reading: " + ioe.getMessage());
				server.remove(ID);
				stop();
			}
		}
	}

	public void open() throws IOException
	{
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}

	public void close() throws IOException
	{
		if (socket != null)
			socket.close();
		if (streamIn != null)
			streamIn.close();
		if (streamOut != null)
			streamOut.close();
	}
}
