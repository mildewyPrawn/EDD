package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.Diccionario;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class HtmlCreator{
    
    Diccionario<String, Integer> d;
    String path;
    String nomArch;
    String html = "<html>\n\t<head>\n\t\t<title>2018-1-EDD-Proyecto3</title>\n\t</head>";

    public HtmlCreator(Diccionario<String,Integer> d, String path, String nomArch){
	this.d = d;
	this.path = path;
	this.nomArch = nomArch;
    }

    public String title(String s){
	return String.format("\n\t<body>\n\t\t<h1>%s</h1>", s);
    }

    public String escribeConteo(){
	String s = format().trim();
	String pes = "";
	int count = 0;
	String p = "";
	for(int i = 0; i < s.length(); i++){
	    if(s.charAt(i) == ' ')
		count++;
	    if(count == 2){
		pes += p(p);
		count = 0;
		p = "";
	    }
	    p += s.charAt(i);
	}
	return pes;
    }

    public String p(String s){
	return String.format("\n\t\t<p>%s</p>",s);
    }

    public String termina(){
	return ("\n\t</body>\n<html>\n");
    }

    public String format(){
	String s = d.toString();
	s = s.replaceAll("'", "").replaceAll(",","\n");
	s = s.substring(2,s.length()-1);
	s = s.replaceAll("[\n]","").trim();
	return "\n " + s;
    }
    
    /**
     * Método que recorta una cadena hasta llegar a un '.'. Se hace para obtener 
     * nombres de archivos sin la extensión.
     * @param cadena con un punto y una extensión.
     * @return cadena sin el punto y la extensión.
     */
    public String nombrador(String s){
	String fich = "";
	for(int i=0; i < s.length(); i++)
	    if(s.charAt(i) == '.'){
		return fich;
	    }else{
		fich += s.charAt(i);
	    }
	return fich;
    }


    public void cuenta(){
	html += title(nomArch);
	html += escribeConteo();
	html += termina();
	escritor(path);
    }

    public void escritor(String path){
	String nombre = nombrador(nomArch) + ".html";
	File f = new File(path + nombre);
	if(f.exists()){
	    System.out.println("Hay otro archivo con el mismo nombre(" + nombre + ")");
	    System.exit(1);
	}else{
	    try{
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		pw.write(html);
		pw.close();
		bw.close();
	    }catch(IOException ioe){
		System.out.println("Hubo un error al crear el archivo " + nombre);
		System.exit(1);
	    }
	}
    }

}
