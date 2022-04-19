import java.io.*;
import java.util.Date;
import java.text.*;
import java.net.*;

public class FTPWorker extends Thread {
    private static final int FILE_SIZE = 6022386;
    Socket s;
    String pwd=null, fakeShellCommand =null;
    String so0=null, so1=null, so2=null;

    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;

    int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;

    boolean userflag = false, passflag = false;

    String ip;

    String ftpGreeting= null;
    String goodbye = "221 Goodbye.";

    String ftpOK ="250 2.0.0 OK";
    int rcvbuf_size = 131072;
    int sndbuf_size = 16384;

    FTPWorker(Socket sck) throws IOException {
        s=sck;
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        ip = in.readLine(); //you get the IP as a String
//        System.out.println(ip);
        ftpGreeting = "Connected to " + ip + ".\n" + "220 ProFTPD 1.3.5 Server (ProFTPD Default Installation) [ ::ffff:"
                + ip + "]\n";
        // TODO: 4/18/2022  print greeting to client
//        System.out.println(ftpGreeting);


        System.out.printf("FTP thread created\n");
    }
    public void sendFile(String FILE_TO_SEND) throws IOException {
        File myFile = new File (FILE_TO_SEND);
        byte [] mybytearray  = new byte [(int)myFile.length()];
        fis = new FileInputStream(myFile);
        bis = new BufferedInputStream(fis);
        bis.read(mybytearray,0,mybytearray.length);
        os = sock.getOutputStream();
        System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
        os.write(mybytearray,0,mybytearray.length);
        os.flush();
        System.out.println("Done.");
    }

    public void recieveFile(String FILE_TO_RECEIVED) throws IOException {
        byte [] mybytearray  = new byte [FILE_SIZE];
        InputStream is = sock.getInputStream();
        fos = new FileOutputStream(FILE_TO_RECEIVED);
        bos = new BufferedOutputStream(fos);
        bytesRead = is.read(mybytearray,0,mybytearray.length);
        current = bytesRead;

        do {
            bytesRead =
                    is.read(mybytearray, current, (mybytearray.length-current));
            if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);

        bos.write(mybytearray, 0 , current);
        bos.flush();
        System.out.println("File " + FILE_TO_RECEIVED
                + " downloaded (" + current + " bytes read)");
    }

    public void run() {
        try {
            Date d = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            System.out.println("FTP thread starts at: " + formatter.format(d));



            //java stuff, take the tin cans out of the box
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

            bw.write(ftpGreeting, 0, ftpGreeting.length()); bw.newLine(); bw.flush();
            // read bytes into br
//            pwd=br.readLine();
//            System.out.println("pwd: " + pwd);

//            bw.write(ftpOK, 0, ftpOK.length()); bw.newLine(); bw.flush();

            String clientName = System.getProperty("user.name");
            bw.write("Name ("+ip+"):"+clientName+"):");
            // wait until the client closes the connection
            while(true) {
                fakeShellCommand = br.readLine();
                String ftpError ="500 "+fakeShellCommand+" not understood";
                System.out.println("response: " + fakeShellCommand);
                String filename = "";
                if (fakeShellCommand.equals("QUIT")) {
                    bw.write(goodbye, 0, goodbye.length());
                    bw.newLine();
                    bw.flush();
                    break;
                }
                if (fakeShellCommand.equals("disconnect")) {
                    bw.write(goodbye, 0, goodbye.length());
                    bw.newLine();
                    bw.flush();
                    break;
                }
                if (fakeShellCommand.equals("OPTS UTF8 ON")) {
//                    bw.write(ftpGreeting, 0, ftpGreeting.length());
                    bw.newLine();
                    bw.flush();
                }
                if (fakeShellCommand.startsWith("GET")) {
                    filename = fakeShellCommand.substring(4);
                    recieveFile(filename);
                    bw.newLine();
                    bw.flush();
                }
                if (fakeShellCommand.startsWith("PUT")) {
                    filename = fakeShellCommand.substring(4);
                    sendFile(filename);
                    bw.newLine();
                    bw.flush();
                }
                if (fakeShellCommand.startsWith("USER")) {
//                    bw.write(ftpGreeting, 0, ftpGreeting.length());
                    String user = fakeShellCommand.substring(5);
                    // TODO: 4/19/2022 use file writer to write " USER: " + user
                    // TODO: 4/18/2022 store this somewhere

                    bw.newLine();
                    bw.flush();
                }
                if (fakeShellCommand.startsWith("PASS")) {
//                    bw.write(ftpGreeting, 0, ftpGreeting.length());
                    String pass = fakeShellCommand.substring(5);
                    // TODO: 4/19/2022 use file writer to write "PASS: " + pass
                    // TODO: 4/18/2022 store this somewhere

                    bw.newLine();
                    bw.flush();
                }
                else {
                    bw.write(ftpError, 0, ftpError.length());
                    bw.newLine();
                    bw.flush();
                }
            }

            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
