package servidor;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class FuncionServidor {
    
    public Socket socket;
    public ServerSocket serverSocket;
    public final int PUERTO = 1235;
    public DataOutputStream dataOutputStream;
    public String mensaje;
    
    public FuncionServidor() throws IOException {
        this.serverSocket = new ServerSocket(PUERTO); //Inicializamos el servidor
        this.socket = new Socket();
    }
    
    public void runServer() throws IOException {
        System.out.print("Conexion del cliente...");
        
        this.socket = this.serverSocket.accept(); // Esperando a que algún cliente se conecte
        
        System.out.println("OK");
        
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream()); // Flujo donde se guarda lo que se envía al cliente
        this.dataOutputStream.writeUTF("Conexión aceptada");
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        
        // Hilo para leer la entrada de la consola y enviar al cliente
        Thread enviarMensajes = new Thread(() -> {
            try {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                String consoleInput;
                while ((consoleInput = consoleReader.readLine()) != null) {
                    // Solo envía el mensaje si la entrada proviene del servidor
                    if (!consoleInput.isEmpty()) {
                        dataOutputStream.writeUTF(consoleInput);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        enviarMensajes.start();

        String receivedMessage;
        while ((receivedMessage = bufferedReader.readLine()) != null) {
            // Imprime el mensaje recibido del cliente
            System.out.println("Recibido del Cliente: " + receivedMessage);
            System.out.println("pinguino"); // Imprime "pinguino" cada vez que recibe un mensaje del cliente
            
            // Leer la entrada de la consola del servidor y enviar al cliente
       
                }
            
       
        this.socket.close();
        this.serverSocket.close();
        
        System.out.println("La conexión con el cliente ha finalizado");
    }
}
