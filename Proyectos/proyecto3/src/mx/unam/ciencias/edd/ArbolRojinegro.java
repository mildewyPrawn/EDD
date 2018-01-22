package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles rojinegros. La única
     * diferencia con los vértices de árbol binario, es que tienen un campo para
     * el color del vértice.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
	    super(elemento);
	    this.color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
	    return (color == Color.ROJO ? "R{":"N{") + elemento.toString() + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)o;
	    return (color == vertice.color && super.equals(o));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() {
	super();
    }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
	super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
	return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
	return ((VerticeRojinegro)vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	VerticeRojinegro v = (VerticeRojinegro)ultimoAgregado;
	v.color = Color.ROJO;
	rebalanceaAgrega(v);
    }

    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice){
    	return (VerticeRojinegro)vertice;
    }
    
    private void rebalanceaAgrega(VerticeRojinegro v){
	VerticeRojinegro padre;
	//1
	if(v.padre == null){
	    v.color = Color.NEGRO;
	    return;
	}
	//2
	padre = verticeRojinegro(v.padre);
	if(getColor(padre) == Color.NEGRO)
	    return;
	//3
	VerticeRojinegro abuelo = verticeRojinegro(padre.padre);
	VerticeRojinegro tio = null;
	if(hijoIzquierdo(padre))
	    tio = verticeRojinegro(abuelo.derecho);
	else
	    tio = verticeRojinegro(abuelo.izquierdo);
	if(tio != null && tio.color == Color.ROJO){
	    tio.color = Color.NEGRO;
	    padre.color = Color.NEGRO;
	    abuelo.color = Color.ROJO;
	    rebalanceaAgrega(abuelo);
	    return;
	}
	//4
	VerticeRojinegro a;
	if(hijoIzquierdo(v) != hijoIzquierdo(padre)){
	    if(hijoIzquierdo(padre))
		super.giraIzquierda(padre);
	    else
		super.giraDerecha(padre);
	    a = padre;
	    padre = v;
	    v = a;
	}
	//5
	padre.color = Color.NEGRO;
	abuelo.color = Color.ROJO;
	if(hijoIzquierdo(v))
	    super.giraDerecha(abuelo);
	else
	    super.giraIzquierda(abuelo);
	
    }

    private boolean hijoIzquierdo(Vertice v){
	return (!v.hayPadre() ? false : v.padre.izquierdo == v);
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
	VerticeRojinegro v = verticeRojinegro(busca(elemento));
	if(v == null)
	    return;
	if(v.derecho != null && v.izquierdo != null)
	    v = verticeRojinegro(intercambiaEliminable(v));
	VerticeRojinegro h;
	if(v.derecho != null)
	    h = verticeRojinegro(v.derecho);
	else if(v.izquierdo != null)
	    h = verticeRojinegro(v.izquierdo);
	else{
	    h = new VerticeRojinegro(null);
	    v.izquierdo = h;
	    h.padre = v;
	    h.color = Color.NEGRO;
	}
	eliminaVertice(v);
	if(h.color == Color.ROJO)
	    h.color = Color.NEGRO;
	else if(v.color == Color.NEGRO && h.color == Color.NEGRO)
	    eliminaRojinegro(h);
	if(h.elemento == null)
	    eliminaVertice(h);
	elementos--;
    }

    private void eliminaRojinegro(VerticeRojinegro v){
	VerticeRojinegro padre = verticeRojinegro(v.padre);
	//1
	if(padre == null)
	    return;
	//2
	VerticeRojinegro bro = getBro(v);
	VerticeRojinegro abuelo = verticeRojinegro(padre.padre);
	if(esRojo(bro)){
	    padre.color = Color.ROJO;
	    bro.color = Color.NEGRO;
	    if(hijoIzquierdo(v))
		super.giraIzquierda(padre);
	    else
		super.giraDerecha(padre);
	    bro = getBro(v);
	}
	//3
	VerticeRojinegro sobIzq = verticeRojinegro(bro.izquierdo);
	VerticeRojinegro sobDer = verticeRojinegro(bro.derecho);
	if(!esRojo(padre) && !esRojo(bro) && !esRojo(sobIzq) && !esRojo(sobDer)){
	    bro.color = Color.ROJO;
	    eliminaRojinegro(padre);
	    return;
	}
	//4
	if(!esRojo(bro) && !esRojo(sobIzq) && !esRojo(sobDer) && esRojo(padre)){
	    bro.color = Color.ROJO;
	    padre.color = Color.NEGRO;
	    return;
	}
	//5
	if((hijoIzquierdo(v) && esRojo(sobIzq) && !esRojo(sobDer)) || (!hijoIzquierdo(v) && !esRojo(sobIzq) && esRojo(sobDer))){
	    bro.color = Color.ROJO;
	    if(esRojo(sobIzq))
		sobIzq.color = Color.NEGRO;
	    else
		sobDer.color = Color.NEGRO;
	    if(hijoIzquierdo(v))
		super.giraDerecha(bro);
	    else
		super.giraIzquierda(bro);
	    bro = getBro(v);
	    sobIzq = verticeRojinegro(bro.izquierdo);
	    sobDer = verticeRojinegro(bro.derecho);
	}
	//6
	bro.color = padre.color;
	padre.color = Color.NEGRO;
	(hijoIzquierdo(v) ? sobDer: sobIzq).color = Color.NEGRO;
	if(hijoIzquierdo(v))
	    super.giraIzquierda(padre);
	else
	    super.giraDerecha(padre);
	return;
    }

    private boolean esRojo(VerticeRojinegro v){
	return v != null && v.color == Color.ROJO;
    }

    private VerticeRojinegro getBro(VerticeRojinegro v){
	if(hijoIzquierdo(v))
	    return verticeRojinegro(v.padre.derecho);
	else
	    return verticeRojinegro(v.padre.izquierdo);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
