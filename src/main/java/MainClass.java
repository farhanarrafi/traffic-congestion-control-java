import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by bjit on 5/25/17.
 */
public class MainClass {
    public static final int SOCKET_SERVER_PORT = 7788;
    public static final int TIMEOUT = 180000;
    int backlog = 0;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    public static void main(String[] args) {



    }

    public void startServer() {
        MainClass.printLog("Server Started and listening on port: " + MainClass.SOCKET_SERVER_PORT + "\n");
        try {
            serverSocket = new ServerSocket(MainClass.SOCKET_SERVER_PORT, backlog);
            serverSocket.setSoTimeout(MainClass.TIMEOUT);
            while(true) {
                clientSocket = serverSocket.accept();
                new Thread(new ServerThread(clientSocket)).start();
            }
        } catch (SocketTimeoutException timeEx) {
            MainClass.printLog("Server timed out after "+ MainClass.TIMEOUT / 1000+" seconds. Port will be closed.\n");
        } catch (IOException ioEx) {
            MainClass.printLog("Socket cannot be initiated.");
            ioEx.printStackTrace();
        } /*finally {
			try {
				if(clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
        printLog("Server stopped and port: " + MainClass.SOCKET_SERVER_PORT + " closed.\n");

    }

    public static void printLog(String message) {
        System.out.println(message);
    }
}

class ServerThread implements Runnable {

    Socket clientSocket = null;
    BufferedReader reader = null;

    public ServerThread(Socket clientSocket) {
        super();
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));
            String inputString = null;
            while((inputString = reader.readLine()) != null) {
                inputString = reader.readLine();
                if(inputString != null)
                    System.out.println(inputString);
            }
        } catch (IOException ioEx) {
            MainClass.printLog("Socket cannot be initiated.");
            ioEx.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
