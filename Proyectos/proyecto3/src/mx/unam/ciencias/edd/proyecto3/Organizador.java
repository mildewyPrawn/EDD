package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Diccionario;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.Normalizer;

public class Organizador{

    /**Lista en donde guardamos los archivos recibidos.*/
    Lista<String> l;
    /**Ruta dada por el usuario.*/
    String path;

    /**
     * Constructor, recibe una lista con los nombres de los archivos
     * y una ruta.
     */
    public Organizador(Lista<String> l, String path){
	this.l = l;
	this.path = path;
    }

    /**
     * Método general de la clase. Manda a llamar a los métodos necesarios para
     * organizar los archivos.
     */
    public void organiza(){
	separa();
    }

    /**
     * Método que separa las cadenas de la lista y las convierte en un objeto
     * de tipo File, si no existe termina el programa, sino, crea un diccionario
     * y un objeto de tipo HtmlCreator.
     */
    public void separa(){
	File f = null;
	for(String s : l){
	    f = new File(s);
	    if(!f.exists()){
		System.out.println("No existe el archivo '" + f + "'");
		System.exit(1);
	    }else{//Para todos los archivos, uno por uno
		Diccionario<String,Integer> d = new Diccionario<>();
		d = otroOrganiza(s);
		HtmlCreator html = new HtmlCreator(d, path, s);
		html.cuenta();
	    }
	}
    }
        
    /**
     * Método que se encarga de manejar a los otros métodos para agregar palabras 
     * a un diccionario.
     * @param un archivo.
     * @return un diccionario con las palabras del archivo que recibimos.
     */
    public Diccionario<String,Integer> otroOrganiza(String ruta){
	Diccionario<String,Integer> d = new Diccionario<>();
	String arch = abridor(ruta);
	arch = mejorador(arch);
	Lista<String> palabras = listador(arch);
	d = llenador(palabras);
	return d;
    }

    /**
     * Método que llena un diccionario, contando.
     * @param lista de palabras de un archivo.
     * @return diccionario con las palabras y el número de veces que aparece.
     */
    public Diccionario<String, Integer> llenador(Lista<String> palabras){
	Diccionario<String,Integer> d = new Diccionario<>();
	for(String s : palabras)
	    if(d.contiene(s))
		d.agrega(s, d.get(s) + 1);
	    else
		d.agrega(s, 1);
	return d;
    }

    /**
     * Método que lista todas las palabras en un archivo. 
     * @param cadena con todo lo que contiene un archivo.
     * @return lista con todas las palabras en el archivo(cadena que recibe).
     */
    public Lista<String> listador(String s){
	String str = "";
	Lista<String> palabras = new Lista<>();
	for(int i = 0; i < s.length(); i++)
	    if(s.charAt(i) != ' '){
		str += s.charAt(i);
	    }else{
		palabras.agrega(str);
		str = "";
	    }
	return palabras;
    }

    /**
     * Método que arregla una cadena, para poder leer 'líquido', como 'Liquidó'.
     * @param una cadena para arreglar.
     * @return la cadena sin caracteres especiales.
     */
    public String mejorador(String s){
	s = Normalizer.normalize(s, Normalizer.Form.NFD);
	s = s.toLowerCase()
	    .replaceAll("[,|;|.|(|)|{|}]", " ")
	    .replaceAll(" +", " ")
	    .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	return s;
    }

    /**
     * Método que lee un archivo.
     * @param el archivo.
     * @return lo que contiene el archivo en una cadena.
     */
    public String abridor(String ruta){
	FileReader fr = null;
	BufferedReader br = null;
	String cadena = "";
	try{
	    fr = new FileReader(ruta);
	    br = new BufferedReader(fr);
	    String s;
	    while((s = br.readLine()) != null)
		cadena += s + " ";
	    br.close();
	}catch(IOException ioe){
	    System.out.println("Error en lectura.");
	    ioe.printStackTrace();
	    System.exit(1);
	}
	return cadena;
    }
}
