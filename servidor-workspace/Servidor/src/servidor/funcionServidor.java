package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class funcionServidor {
    
    public Socket socket;
    public ServerSocket serverSocket;
    public final int PUERTO = 1234;
    public DataOutputStream dataOutputStream;
    public String mensaje;
    
    public funcionServidor() throws IOException {
        this.serverSocket = new ServerSocket(PUERTO); //Inicializamos el servidor
        this.socket = new Socket();
    }
    
    public void runServer() throws IOException {
        Scanner s1 = new Scanner(System.in);
        System.out.print("Conexion del cliente...");
        
        this.socket = this.serverSocket.accept(); // Esperando a que algún cliente se conecte
        
        System.out.println("OK");
        
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream()); // Flujo donde se guarda lo que se envía al cliente
        this.dataOutputStream.writeUTF("Conexión aceptada");
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        
        // Crear un hilo para leer la entrada de la consola mientras se espera la entrada del cliente
        Thread consoleInputThread = new Thread(() -> {
            try {
                String consoleInput;
                while ((consoleInput = consoleReader.readLine()) != null) {
                    // Envía el mensaje al cliente
                    dataOutputStream.writeUTF(consoleInput);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        consoleInputThread.start();
        
        String receivedMessage;
        while ((receivedMessage = bufferedReader.readLine()) != null) {
            System.out.println(receivedMessage);
        }
        
        // Cuando se cierra la conexión del cliente, se cierra el hilo de entrada de la consola
        consoleInputThread.interrupt();
        
        this.socket.close();
        this.serverSocket.close();
        
        System.out.println("La conexión con el cliente ha finalizado");
    }
}