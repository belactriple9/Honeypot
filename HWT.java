import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.*;

class HWT extends Thread {
	Socket s;
	String pwd=null, hattempt=null;
	String so0=null, so1=null, so2=null;

	String smtpGreeting="220 ubuntu-s-1vcpu-2gb-amd-nyc1-01 ESMTP OpenSMTPD";
	String smtpError="500 5.5.1 Invalid command: Command unrecognized";
	String smtpOK="250 2.0.0 OK";

	HWT (Socket sck) {
		s=sck;
	


		System.out.printf("honey thread created");
	}



	public void run() {
		try {
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			System.out.println("honey thread starts at: " + formatter.format(d));


			//java stuff, take the tin cans out of the box
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			bw.write(smtpGreeting, 0, smtpGreeting.length()); bw.newLine(); bw.flush();
			// read bytes into br
			pwd=br.readLine();
			System.out.println("pwd: " + pwd);

			bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();

			// wait until the client closes the connection
			while(true) {
				hattempt=br.readLine();
				System.out.println("hattempt: " + hattempt);
				if (hattempt.equals("QUIT")) {
					bw.write(smtpGreeting, 0, smtpGreeting.length()); bw.newLine(); bw.flush();
					break;
				}
				else {
					bw.write(smtpError, 0, smtpError.length()); bw.newLine(); bw.flush();
				}
			}

			s.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
