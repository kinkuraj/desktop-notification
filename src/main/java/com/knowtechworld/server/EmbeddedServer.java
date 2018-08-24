package com.knowtechworld.server;

import java.net.URI;
import java.net.URL;
import java.security.ProtectionDomain;

import javax.ws.rs.core.UriBuilder;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.knowtechworld.client.Notification;

public final class EmbeddedServer {

	private static final int SERVER_PORT = 8680;

	private EmbeddedServer() {
	}

	public static void main(String[] args) throws Exception {
		
		if(args.length == 0) {
			args = new String [1];
			args[0]="http://localhost";
		}
		
		if(!args[0].contains("http://localhost") && !args[0].matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}\r\n" + 
				" (?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
			System.out.println("Please enter a vallid IP address of the host.");
			return;
		}
		
		URI baseUri = UriBuilder.fromUri(args[0]).port(SERVER_PORT)
				.build();
		ResourceConfig config = new ResourceConfig(Notification.class);
		Server server = JettyHttpContainerFactory.createServer(baseUri, config,
				false);

		ContextHandler contextHandler = new ContextHandler("/rest");
		contextHandler.setHandler(server.getHandler());
		
		ProtectionDomain protectionDomain = EmbeddedServer.class
				.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase(location.toExternalForm());
		System.out.println(location.toExternalForm());
		HandlerCollection handlerCollection = new HandlerCollection();
		handlerCollection.setHandlers(new Handler[] { resourceHandler,
				contextHandler, new DefaultHandler() });
		server.setHandler(handlerCollection);
		server.start();
		server.join();
	}
}
