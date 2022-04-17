import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.*;

class SMTP extends Thread {
	Socket s;
	String pwd=null, hattempt=null;
	String clientInput=null;

	int connectionNumber;

	String smtpGreeting="220 ubuntu-s-1vcpu-2gb-amd-nyc1-01 ESMTP OpenSMTPD";
	String smtpError="500 5.5.1 Invalid command: Command unrecognized";
	String smtpOK="250 2.0.0 OK";
    String smtpServerReady = "220 2.0.0 SMTP server ready";

	SMTP (Socket sck, int con) {
		s=sck;
	
		connectionNumber=con;

		System.out.printf("honey thread created");
	}

    // SMTP handshake: https://www.afternerd.com/blog/smtp/
	// https://www.wormly.com/test-smtp-server/host/142.93.244.189/port/25/sendmail/0/id/smeNdVBq

	public void run() {
		try {
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			System.out.println("honey thread starts at: " + formatter.format(d));


			//java stuff, take the tin cans out of the box
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			bw.write(smtpGreeting, 0, smtpGreeting.length()); bw.newLine(); bw.flush();
			// // read bytes into br
			clientInput=br.readLine();
			System.out.println("first line: " + clientInput);

			bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();

            while(true)
			{
				clientInput=br.readLine();
				if(clientInput.toLowerCase().equals("elho"))
				{
					bw.write(smtpServerReady, 0, smtpServerReady.length()); bw.newLine(); bw.flush();
					System.out.println("hello " + clientInput.substring(3));
				}
				else if(clientInput.toUpperCase().substring(0, 10).matches("MAIL FROM.*"))
				{
					bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();
					System.out.println("mail from: " + clientInput.substring(10));
				}
				else if(clientInput.toUpperCase().substring(0,9).matches("RCPT TO.*"))
				{
					bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();
					System.out.println("rcpt to: " + clientInput.substring(9));
				}
				else if(clientInput.toUpperCase().substring(0,4).equals("DATA"))
				{
					bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();
					System.out.println("data");
				}
				else if(clientInput.toUpperCase().substring(0,4).equals("QUIT"))
				{
					bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();
					System.out.println("quit: " + connectionNumber);
					s.close();
					break;
				}
				else
				{
					// log the input
					System.out.println("invalid input: " + clientInput);
					bw.write(smtpError, 0, smtpError.length()); bw.newLine(); bw.flush();
				}


			}


			s.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
