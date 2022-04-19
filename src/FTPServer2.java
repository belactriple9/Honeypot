import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;


public class FTPServer2 {
    public static void main(String[] args) {
        ServerSocket ss=null;
        Socket s=null;

        if (args.length != 1) {
            System.out.printf("usage: java FTPServer portNumber\n");
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
                System.out.printf("FTPServer: received an incoming connection\n");
                FTPWorker worker = new FTPWorker(s);
                worker.start();
            } catch (Exception e2) {
                e2.printStackTrace();
                try {Thread.sleep(5000);} catch (Exception en){}
            }
        }
    }
}
