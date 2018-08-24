package com.knowtechworld.client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/notify")
public class Notification {

	@GET
	@Path("/status/{message}")
	public static String notifyMsg(@PathParam("message") String message) {
		if (SystemTray.isSupported()) {
			Notification td = new Notification();
			try {
				td.displayTray(message);
				return "Sucess";
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (AWTException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("System tray not supported!");
		}
		return "Failed";
	}

	public void displayTray(String message) throws AWTException, MalformedURLException {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
		TrayIcon trayIcon = new TrayIcon(image, "Andon Cord");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip(message);
		tray.add(trayIcon);
		trayIcon.displayMessage(message, "By Andon Cord Notification Service", MessageType.WARNING);
	}

}
