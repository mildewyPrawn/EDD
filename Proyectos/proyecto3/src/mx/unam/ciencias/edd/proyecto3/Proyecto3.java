package mx.unam.ciencias.edd.proyecto3;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Proyecto3{
    
    /**
     * Método main.
     * @param array con los argumentos, archivos, una bandera y un directorio.
     */
    public static void main(String[] args){

	try{
	    Lector l = new Lector(args);
	    l.prepara();    
	}catch(Exception e){
	    System.out.println("Error al leer archivos");
	    System.exit(1);
	}
    }
}
