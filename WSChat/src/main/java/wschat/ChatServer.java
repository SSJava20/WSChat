package wschat;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler ;

public class ChatServer extends Server {
	WebSocket websocket;
	SelectChannelConnector connector;
	WebSocketHandler wsHandler;
	ResourceHandler rsHandler;
	ArrayList<ChatSocket> ChatClients;

	public ChatServer(int port) {
		connector = new SelectChannelConnector();
		connector.setPort(port);
		this.addConnector(connector);
		wsHandler = new MyWebSocketHandler();
		this.setHandler(wsHandler);
		rsHandler = new ResourceHandler();
		rsHandler.setDirectoriesListed(true);
		rsHandler.setResourceBase("src/test/webapp" );
		wsHandler.setHandler(rsHandler);
		ChatClients = new ArrayList<ChatSocket>();
	}
	
	
	public ArrayList<ChatSocket> getChatClients( ) {
		return ChatClients;
	}

	public static void main(String[] args){
		ChatServer server = new ChatServer(8080);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

class MyWebSocketHandler extends WebSocketHandler {

	public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
		return new ChatSocket((ChatServer) this.getServer());
	}
	
}


