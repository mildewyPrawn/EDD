package mx.unam.ciencias.edd.proyecto1;
import mx.unam.ciencias.edd.Lista;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Proyecto1{
    public static void main(String[]args){
	
	Lista<String> lista = new Lista<String>();
	Modificador m = new Modificador();
	
	/**Por si no nos pasan argumenos*/
	if(args.length == 0){
	    try{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s;
		while((s = in.readLine()) != null)
		    lista.agrega(s);
		in.close();
	    } catch(IOException ioe){
		System.out.println("Error en la entrada");
		System.exit(1);
	    }
	    m.ordenaList(lista);
	}
	/**Si nos pasan al menos un archivo para ordenar*/
	else
	    m.ordenaArgs(args);
    }
    
}
