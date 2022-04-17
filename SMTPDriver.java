import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;



class SMTPDriver {
	 public static void main(String[] args) {
			 ServerSocket ss=null;
			 Socket s=null;

			 int connection = 0;

			 if (args.length != 1) {
				 System.out.printf("usage: java SMTPDriver portNumber\n");
				 System.exit(1);
			 }

			 try {
			 	ss = new ServerSocket(Integer.parseInt(args[0]));
		 	} catch (Exception e) {
			 	e.printStackTrace();
		 	}

			 while (true) {
				 try {
					System.out.printf("wait for an incoming connection\n");
			 		s = ss.accept();
			 		System.out.printf("SMTPDriver: received an incoming connection\n");
			 		SMTP worker = new SMTP(s, connection);
					worker.start();
				 } catch (Exception e2) {
					 e2.printStackTrace();
					 try {Thread.sleep(5000);} catch (Exception en){}
				 }
			 }
	 }
}
