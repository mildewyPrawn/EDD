package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Conjunto;
import java.io.File;
import java.io.IOException;

public class Lector{
    
    /**Lista en donde guardamos los archivos recibidos*/
    Lista<String> l = new Lista<>();
    
    /**Ruta especificada por el usuario, después de la bandera -o*/
    String path;

    /**
     * Constructor que recibe los argumentos por la linea de comandos.
     * Llena la lista con archivos.
     * Crea los directorios necesarios para llegar al especificado.
     * Nos da la ruta para guardar los archivos.
     */
    public Lector(String[] args){
	File f = null;
	int j = 0;
	for(int i = 0; i < args.length; i++){
	    if(args[i].equals("-o")){
		if(j != 0){
		    System.out.println("Error: mas de una bandera '-o' detectada.");
		    System.exit(1);
		}
		f = new File(args[i+1]);
		path = args[i+1] + "/";
		j++;
		i++;
	    }else{
		l.agrega(args[i]);
	    }
	}
	f.mkdirs();
    }
    
    /**
     * Método general de la clase que crea un objeto organizador, con la lista
     * de archivos para abrir y la ruta en donde se quiere guardar los nuevos.
     */
    public void prepara(){
	Organizador o = new Organizador(l, path);
	o.organiza();
    }
    
}
