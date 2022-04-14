import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.*;

class SMTP extends Thread {
	Socket s;
	String pwd=null, hattempt=null;
	String clientInput=null;

	String smtpGreeting="220 ubuntu-s-1vcpu-2gb-amd-nyc1-01 ESMTP OpenSMTPD";
	String smtpError="500 5.5.1 Invalid command: Command unrecognized";
	String smtpOK="250 2.0.0 OK";
    String smtpServerReady = "220 2.0.0 SMTP server ready";

	SMTP (Socket sck) {
		s=sck;
	


		System.out.printf("honey thread created");
	}

    // SMTP handshake: https://www.afternerd.com/blog/smtp/

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

            // read the client's reply, it should be STARTTLS
            clientInput=br.readLine();
            if(clientInput.equals("STARTTLS"))
            {
				System.out.println(clientInput);
                bw.write(smtpServerReady, 0, smtpServerReady.length()); bw.newLine(); bw.flush();
            }
            else
            {
                bw.write(smtpError, 0, smtpError.length()); bw.newLine(); bw.flush();
            }


            
			// loop forever
			for(int i = 0; i<100; i++)
			{
				clientInput=br.readLine();
				System.out.println(clientInput );
				bw.write(smtpOK, 0, smtpOK.length()); bw.newLine(); bw.flush();
			}
            


			s.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
