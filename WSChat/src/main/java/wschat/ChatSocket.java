package wschat;

import java.io.IOException;

import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.websocket.WebSocket;

public class ChatSocket implements WebSocket, WebSocket.OnFrame,
		WebSocket.OnBinaryMessage, WebSocket.OnTextMessage, WebSocket.OnControl {
	protected FrameConnection connection;
	private ChatServer server;
	
	public ChatSocket(ChatServer server) {
		this.server = server;
	}
	public FrameConnection getConnection() {
		return connection;
	}

	public void onOpen(Connection connection) {
		
		System.out.printf("%s#onOpen %s\n", this.getClass().getSimpleName(),
				connection);
	}

	public void onHandshake(FrameConnection connection) {
		System.out.printf("%s#onHandshake %s %s\n", this.getClass()
				.getSimpleName(), connection, connection.getClass()
				.getSimpleName());
		this.connection = connection;
		server.getChatClients().add(this);
	}

	public void onClose(int code, String message) {
		System.out.printf("%s#onDisonnect %d %s\n", this.getClass()
				.getSimpleName(), code, message);
		server.getChatClients().remove(this);
	}

	public boolean onFrame(byte flags, byte opcode, byte[] data, int offset,
			int length) {
		System.out.printf("%s#onFrame %s|%s %s\n", this.getClass()
				.getSimpleName(), TypeUtil.toHexString(flags), TypeUtil
				.toHexString(opcode), TypeUtil
				.toHexString(data, offset, length));
		return false;
	}

	public boolean onControl(byte controlCode, byte[] data, int offset,
			int length) {
		System.out.printf("%s#onControl  %s %s\n", this.getClass()
				.getSimpleName(), TypeUtil.toHexString(controlCode), TypeUtil
				.toHexString(data, offset, length));
		return false;
	}

	public void onMessage(String data) {
		System.out.printf("%s#onMessage     %s\n", this.getClass()
				.getSimpleName(), data);
		try {
			for (int i=0; i<server.getChatClients().size(); i++) {
				server.getChatClients().get(i).connection.sendMessage(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onMessage(byte[] data, int offset, int length) {
		System.out.printf("%s#onMessage     %s\n", this.getClass()
				.getSimpleName(), TypeUtil.toHexString(data, offset, length));
	}
}