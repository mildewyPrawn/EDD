package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;

public class FactoryPila{

    /**Lista de datos, se guardan los datos*/
    private Lista<Integer> datos;

    /**Cadena del codigo xml para las imágenes .svg*/
    private String s;

    /**
     * Constructor de la fabrica de pilas.
     * @param Lista de enteros, son los datos a agregar.
     * @param inicio del codigo xml.
     */
    public FactoryPila(Lista<Integer> datos, String s){
	this.datos = datos;
	this.s = s;
    }

    /**
     * Metodo encargado de llevar a cabo todas las operaciones necesarias
     * para escribir el codigo xml.
     */
    public void dibuja(){
	int args = (datos.getLongitud());
	int height = (args * 30) + 10;
	s += tamaño(height);
	s += escritor(args);
	s += termina();
	System.out.println(s);
    }
    
    /**
     * Primer metodo a llamar, inicializa el tamaño de la imagen.
     * @param el tamaño en ancho de la imagen. NOTA. todas las listas tienen
     *                                               el mismo ancho.
     * @return cadena con el tamaño.
     */
    private String tamaño(int i){
	return String.format("<svg width=\"100\" height=\"%d\">\n\t<g>\n", i);
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
	    return String.format("\t\t<rect x=\"10\" y=\"%d\" width=\"80\" height=\"30\" style=\"fill:white;stroke:black;stroke.width:1\"/>\n", i);
    }

    /**
     * Método que itera sobre el numero de elementos y manda a llamar 
     * a los metodos que escriben el codigo xml.
     * @param el numero de datos que tendrá la pila.
     * @return cadena con el codigo xml de dibujar rectángulos, flechas y  
     * escribir el número correspondiente.
     */
    private String escritor(int args){
	Lista<Integer> l = datos.reversa();
	String s = "";
	int j = 5;
	for(int i = 0; i < args; i++){
	    s += dibujaRectangulo(j);
	    s += escribeNumeros(j + 20, l.get(i));
	    j += 30;
	}
	return s;
    }
    
    /**
     * Método que escribe el número de los elementos de la lista.
     * @param la componente en x en donde vamos a escribir.
     * @param el elemento a escribir.
     * @return el codigo xml de un número a escribir.
     */
    private String escribeNumeros(int y, int num){
	return String.format("\t\t<text fill=\"black\" font-family=\"sans-serif\" font-size=\"10\" x=\"45\" y=\"%d\">%d</text>\n",y ,num);
    }

}
