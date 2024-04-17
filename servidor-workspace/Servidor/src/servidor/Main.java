package servidor;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		System.out.print("Servidor de chat en el puerto: 1234");
		
		System.out.print("Iniciando servidor...");
		
		FuncionServidor chat = new FuncionServidor();
		
		System.out.println("OK");
		chat.runServer();
	}
}	