package servidor;
import java.io.IOException;

public class main {
	
	public static void main(String[] args) throws IOException{
		
		System.out.print("Servidor de chat en el puerto: 1234");
		
		System.out.print("Iniciando servidor...");
		
		funcionServidor chat = new funcionServidor();
		
		System.out.println("OK");
		chat.runServer();
	}
}
