package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;

public class FactoryCola{
    
    /**Lista de datos, se guardan los datos*/
    private Lista<Integer> datos;
    
    /**Cadena del codigo xml para las imágenes .svg*/
    private String s;

    /**
     * Constructor de la fabrica de colas.
     * @param Lista de enteros, son los datos a agregar.
     * @param inicio del codigo xml.
     */
    public FactoryCola(Lista<Integer> datos, String s){
	this.datos = datos;
	this.s = s;
    }
    
    /**
     * Metodo encargado de llevar a cabo todas las operaciones necesarias
     * para escribir el codigo xml.
     */
    public void dibuja(){
	int args = (datos.getLongitud());
	int width = (args * 40) + 50;
	s += tamaño(width);
	s += escritor(args);
	s += direccion(width - 48);
	s += termina();
	System.out.println(s);
    }

    public String direccion(int x){
	return String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"50\" x=\"%d\" y=\"65\">⇒</text>\n", x);
    }

    /**
     * Primer metodo a llamar, inicializa el tamaño de la imagen.
     * @param el tamaño en ancho de la imagen. NOTA. todas las listas tienen
     *                                               el mismo ancho.
     * @return cadena con el tamaño.
     */
    public String tamaño(int i){
	return String.format("<svg width=\"%d\" height=\"100\">\n\t<g>\n", i);
    }
    
    /**
     * Último metodo a llamar, termina el codigo xml.
     */
    public String termina(){
	return String.format("\t</g>\n</svg>");
    }

    /**
     * Metodo para dibujar los rectangulos. 
     * @param la componente en x en donde se va a dibuar el rectangulo. 
     * NOTA: todos los rectángulos son de las mismas dimensiones.
     * @return codigo xml de un rectángulo a dibujar.
     */
    public String dibujaRectangulo(int i){
	return String.format("\t\t<rect x=\"%d\" y=\"10\" width=\"40\" height=\"80\" style=\"fill:white;stroke:black;stroke.width:1\"/>\n", i);
    }

    /**
     * Método que itera sobre el numero de elementos y manda a llamar 
     * a los metodos que escriben el codigo xml.
     * @param el numero de datos que tendrá la cola.
     * @return cadena con el codigo xml de dibujar rectángulos, flechas y  
     * escribir el número correspondiente.
     */    
    public String escritor(int args){
	Lista<Integer> l = datos.reversa();
	String s = "";
	int j = 5;
	for(int i = 0; i < args; i++){
	    s += dibujaRectangulo(j);
	    s += escribeNumeros(j + 20 ,l.get(i));
	    j += 40;
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
	return String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=\"%d\" y=\"55\">%d</text>\n",x ,num);
    }
    
}
