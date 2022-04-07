import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.*;

class HWT extends Thread {
	Socket s;
	String pwd=null, hattempt=null;
	String so0=null, so1=null, so2=null;

	HWT (Socket sck) {
		s=sck;
		so0 = new String("root@68.183.19.99's password: ");
		so1 = new String("Permission denied, please try again.");
		so2 = new String("Welcome to Ubuntu 18.04.3 LTS (GNU/Linux 4.15.0-166-generic x86_64)\n * Documentation:  https://help.ubuntu.com\n * Management:     https://landscape.canonical.com\n * Support:        https://ubuntu.com/advantage\n\n * Canonical Livepatch is available for installation.\n - Reduce system reboots and improve kernel security. Activate at:\n https://ubuntu.com/livepatch\n\n 576 packages can be updated.\n 0 updates are security updates.\n\n\n\n\n\n Last login: Wed Apr  6 15:34:44 2022 from 137.45.192.100\n root@itecE:~# ");

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

			bw.write(so0, 0, so0.length()); bw.newLine(); bw.flush();
			pwd=br.readLine();
			System.out.println("pwd: " + pwd);

			bw.write(so2, 0, so2.length()); bw.newLine(); bw.flush();

			//while (true) {
			for (int i=0; i<1000; i++) {
				hattempt=br.readLine();
				System.out.println("> " + hattempt);
			}

			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
