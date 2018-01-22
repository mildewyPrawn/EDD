package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Lector{

    /**
     * Metodo que maneja todo lo que tiene que hacer esta clase.
     * @param BufferedReader de la clase principal.
     */
    public void manejaLector(BufferedReader br){
	String s = lecturaArchivo(br);
	String[] parts = s.split(" ");
	String data = elejidor(parts[0]);
	int i = parts.length - 1;
	/**Sino tenemos datos terminamos*/
	if(i <= 0){
	    System.out.println("No hay datos en la entrada");
	    System.exit(1);
	}
	Lista<Integer> ai = new Lista<Integer>();
	for(int j = 0; j < i; j++)
	    ai.agrega(Integer.parseInt(parts[j+1]));
	FactoryDS fds = new FactoryDS(data, ai);
	fds.fabrica();
    }

    /**
     * Metodo que verifica que <b>ed</ed> sea una Estructura válida.
     * @param Cadena de la que se quiere saber si es una ED válida o no.
     * @return Cadena con la estructura válida. En caso de no existir, el
     *         programa termina.
     */
    private String elejidor(String ed){
	String s = "";
	switch(ed){
	case "Lista":
	    s = "Lista";
	    break;
	case "Pila":
	    s = "Pila";
	    break;
	case "Cola":
	    s = "Cola";
	    break;
	case "ArbolBinarioCompleto":
	    s = "ArbolBinarioCompleto";
	    break;
	case "ArbolBinarioOrdenado":
	    s = "ArbolBinarioOrdenado";
	    break;
	case "ArbolRojinegro":
	    s = "ArbolRojinegro";
	    break;
	case "ArbolAVL":
	    s = "ArbolAVL";
	    break;
	    //¿acento?
	case "Grafica":
	    s = "Grafica";
	    break;
	case "MonticuloMinimo":
	    s = "MonticuloMinimo";
	    break;
	default:
	    System.out.println("'" + ed + "'" + " No es una estructura válida.");
	    System.exit(1);
	    break;
	}
	return s;
    }

    /**
     * Metodo para leer un archivo.
     * @param BufferedReader, que nos pasa <link>manejaLector</link>.
     * @return cadena con el texto en un formato aceptable para trabajar con él.
     *         Quita saltos de linea.
     *         Omite las lineas a partir de <a>#</a>.
     *         Reemplaza n espacios con uno solo.
     */
    private String lecturaArchivo(BufferedReader br){
	String mensaje = "";
	try{
	    String cadena;
	    while((cadena = br.readLine()) != null)
		mensaje += buscador(cadena);
	} catch(IOException ioe){
	    System.out.println("Error en leer el archivo ");
	    System.exit(1);
	}
	mensaje = mensaje.replaceAll(" +", " ");
	return mensaje;
    }

    /**
     * Metodo auxiliar que da formato a la cadena final, toma linea por linea
     * para dar formato.
     * @param linea de la entrada, ya sea estandar o no.
     * @return cadena presentable.
     */
    private String buscador(String linea){
	String l = linea.replaceAll("\t","");
	String mensaje = "";
	char c = '#';
	for(int i = 0; i < l.length(); i++){
	    if(l.charAt(i) == c)
		return mensaje;
	    mensaje += l.charAt(i);
	}
	return mensaje + " ";
    }
}

