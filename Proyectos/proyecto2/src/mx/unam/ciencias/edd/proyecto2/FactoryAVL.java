package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class FactoryAVL{
    
    /**Cadena del codigo xml para las imágenes .svg*/
    private String s;

    /**ArbolAVL en donde se guardaran los datos*/
    private ArbolAVL<Integer> datos;
    
    /**
     * Constructor de la fabrica de arboles AVL.
     * @param Lista de enteros, son los datos a agregar.
     * @param inicio del codigo xml.
     */
    public FactoryAVL(Lista<Integer> datos, String s){
	this.datos = new ArbolAVL<Integer>(datos);
	this.s = s;
    }

    public void dibuja(){
	int height = datos.altura() * 80;
	int width = datos.getElementos() * 80;
	s += tamaño(width + 10, height);
	VerticeArbolBinario raiz = datos.raiz();
	escritor(raiz, 0,0, width/2);
	s += ed;
	s += termina();
	System.out.println(s);
    }

    private String ed = "";
    public void escritor(VerticeArbolBinario raiz, int i, int y, int w){
	ed += dibujaVertice(raiz, w,y+20);
	if(raiz.hayIzquierdo()){
	    int wi = (w - i)/2;
	    ed += dibujaLineas(w-20,y+20,w-wi,y+50);
	    escritor(raiz.izquierdo(), i, y+40, w-wi);
	}
	if(raiz.hayDerecho()){
	    int wd = (w - i)/2;
	    ed += dibujaLineas(w+20,y+20,w+wd,y+50);
	    escritor(raiz.derecho(), w, y+40, w+wd);
	}
    }

    public String dibujaVertice(VerticeArbolBinario v, int x, int y){
	String s = String.format("\t\t<circle cx = \"%d\" cy=\"%d\" r =\"20\" stroke=\"black\" stroke-width=\"1\" fill=\"white\"/>\n",x,y);
	s += String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"12\" x=\"%d\" y=\"%d\">%s</text>\n",x,y+3,v.get().toString());
	s += String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=\"%d\" y=\"%d\">%s</text>\n",x+25,y,v.toString().substring(2,v.toString().length()));
	return s;
    }
    

    public String dibujaLineas(int x1, int y1, int x2, int y2){
	return String.format("\t\t<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"black\" stroke-width=\"1\"/>\n ",x1,y1,x2,y2);
    }

    public String tamaño(int width, int height){
	return String.format("<svg width=\"%d\" height=\"%d\">\n\t<g>\n", width, height);
    }

    public String termina(){
	return String.format("\t</g>\n</svg>");
    }
}
