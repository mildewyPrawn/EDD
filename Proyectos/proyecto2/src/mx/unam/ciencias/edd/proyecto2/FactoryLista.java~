package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;

public class FactoryLista{

    /**Lista de datos, se guardan los datos*/
    private Lista<Integer> datos;

    /**Cadena del codigo xml para las imágenes .svg*/
    private String s;

    /**
     * Constructor de la fabrica de listas.
     * @param Lista de enteros, son los datos a agregar.
     * @param inicio del codigo xml.
     */
    public FactoryLista(Lista<Integer> datos, String s){
	this.datos = datos;
	this.s = s;
    }

    /**
     * Metodo encargado de llevar a cabo todas las operaciones necesarias
     * para escribir el codigo xml.
     */
    public void dibuja(){
	int args = (datos.getLongitud());
	int width = (args * 35) + (args * 7) - 2;
	s += tamaño(width);
	s += escritor(args);
	s += termina();
	System.out.println(s);
    }

    /**
     * Primer metodo a llamar, inicializa el tamaño de la imagen.
     * @param el tamaño en ancho de la imagen. NOTA. todas las listas tienen
     *                                               la misma altura.
     * @return cadena con el tamaño.
     */
    private String tamaño(int i){
	return String.format("<svg width=\"%d\" height=\"105\">\n\t<g>\n", i);
    }

    /**
     * Último metodo a llamar, termina el codigo xml.
     */
    private String termina(){
	return String.format("\t</g>\n</svg>");
    }

    /**
     * Metodo para dibujar los rectangulos. 
     * @param la componente en x en donde se va a dibuar el rectangulo. 
     * NOTA: todos los rectángulos son de las mismas dimensiones.
     * @return codigo xml de un rectángulo a dibujar.
     */
    private String dibujaRectangulo(int i){
	return String.format("\t\t<rect x=\"%d\" y=\"40\" width=\"35\" height=\"25\" style=\"fill:white;stroke:black;stroke.width:1\"/>\n", i);
    }

    /**
     * Método que itera sobre el numero de elementos y manda a llamar 
     * a los metodos que escriben el codigo xml.
     * @param el numero de datos que tendrá la pila.
     * @return cadena con el codigo xml de dibujar rectángulos, flechas y  
     * escribir el número correspondiente.
     */
    private String escritor(int args){
	String s = "";
	int j = 5;
	int h;
	for(int i = 0; i < args; i++){
	    s += dibujaRectangulo(j);
	    s += escribeNumeros(j + 15, datos.get(i));
	    j += 35 + 7;
	    h = j - 8;
	    s += dibujaFlechas(h);
	}
	return s;
    }

    /**
     * Método que escribe el número de los elementos de la lista.
     * @param la componente en x en donde vamos a escribir.
     * @param el elemento a escribir.
     * @return el codigo xml de un número a escribir.
     */
    private String escribeNumeros(int x, int num){
	return String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=\"%d\" y=\"55\">%d</text>\n",x,num);
    }

    /**
     * Método que dibuja las flechas por ser listas doblemente ligadas.
     * NOTA: no "dibuja", es un caracter el que se escribe.
     * @param la componente en x en donde vamos a escribir '↔'.
     * @return el codigo xml de la cadena con la flecha a escribir.
     */
    private String dibujaFlechas(int x){
	return String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=\"%d\" y=\"55\">↔</text>\n", x);
    }
    
}
