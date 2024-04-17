package servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class FuncionServidor {

    private ServerSocket serverSocket;
    private final int PUERTO = 4321; // Puerto del servidor
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public FuncionServidor() throws IOException {
        this.serverSocket = new ServerSocket(PUERTO); // Inicializamos el servidor
    }

    public void runServer() throws IOException {
        System.out.println("Servidor en espera. Buscando conexiones...");

        Socket clientSocket = serverSocket.accept(); // Esperando a que algún cliente se conecte

        System.out.println("Cliente conectado desde " + clientSocket.getInetAddress()); // Información del cliente

        this.dataInputStream = new DataInputStream(clientSocket.getInputStream()); // Flujo de entrada para recibir datos del cliente
        this.dataOutputStream = new DataOutputStream(clientSocket.getOutputStream()); // Flujo de salida para enviar datos al cliente

        // Hilo para recibir mensajes del cliente
        Thread recibirMensajes = new Thread(() -> {
            try {
                while (true) {
                    String mensaje = dataInputStream.readUTF(); // Lee un mensaje del cliente
                    System.out.println("Cliente: " + mensaje); // Imprime el mensaje recibido del cliente
                    if (mensaje.equalsIgnoreCase("manolo")) { // Si el cliente envía "Adios", cierra la conexión
                        System.out.println("Conexion finalizada");
                        dataOutputStream.writeUTF("Conexion finalizada"); // Envía un mensaje de confirmación al cliente
                        cerrarConexion(serverSocket, clientSocket, dataOutputStream, dataInputStream);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error al recibir mensajes del cliente: " + e.getMessage());
            }
        });
        recibirMensajes.start(); // Inicia el hilo para recibir mensajes del cliente

        // Hilo para enviar mensajes al cliente desde la consola del servidor
        Thread enviarMensajes = new Thread(() -> {
            try {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                String consoleInput;
                while ((consoleInput = consoleReader.readLine()) != null) {
                    dataOutputStream.writeUTF(consoleInput); // Envía el mensaje al cliente
                }
            } catch (IOException e) {
                System.out.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        });
        enviarMensajes.start(); // Inicia el hilo para enviar mensajes al cliente

        // Espera a que ambos hilos terminen
        try {
            recibirMensajes.join();
            enviarMensajes.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            FuncionServidor servidor = new FuncionServidor();
            servidor.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cerrarConexion(ServerSocket serverSocket, Socket clientSocket, DataOutputStream dataOutputStream, DataInputStream dataInputStream) {
        try {
            dataOutputStream.close();
            dataInputStream.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

