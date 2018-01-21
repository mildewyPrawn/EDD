package mx.unam.ciencias.edd.proyecto1;
import mx.unam.ciencias.edd.Lista;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Modificador{

    /**
     * Metodo que lee archivos y va agregando linea por linea a una lista.
     * @param arreglo de cadenas con las direcciones de los archivos a ordenar.
     * @return la lista con las lineas de los archivos.
     */
    public Lista<String> leerArchivos(String[] args){
	Lista<String> lista = new Lista<String>();
	try{
	    for(String files : args){
		BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(files)));
		String s;
		while((s = bfr.readLine()) != null)
		    lista.agrega(s);
		bfr.close();
	    }
	} catch (IOException ioe){
	    System.out.println("Error en la lectura de archivos.");
	    System.exit(1);
	}
	return lista;
    }

    /**
     * Metodo que es el que organiza el funcionamiento del programa.
     * @param el arreglo de cadenas con las direcciones de los archivos.
     */    
    public void ordenaArgs(String[] args){
	Lista<String> listaOrd = new Lista<String>();
	listaOrd = leerArchivos(args);
	ordenaList(listaOrd);
    }

    /**
     * Metodo que imprime los elementos de una lista.
     * @param la lista a imprimir.
     */
    public void imprime(Lista<String> l){
	for(String a : l)
	    System.out.println(a);
    }

    /**
     * Metodo que ordena una lista usando {@link mergeSort} y una lambda.
     * @param la lista a ordenar.
     */
    public void ordenaList(Lista<String> l){
	l = l.mergeSort((a,b) -> a.replaceAll("\\P{L}","").toLowerCase().compareTo(b.replaceAll("\\P{L}","")));
	imprime(l);
    }
}
