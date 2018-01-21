package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices de árboles AVL. La única diferencia
     * con los vértices de árbol binario, es que tienen una variable de clase
     * para la altura del vértice.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
	    super(elemento);
	    altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
	    return this.altura;
        }
	
	private void cambiaAltura(){
	    int i = this.hayIzquierdo() ? this.izquierdo.altura() : -1;
	    int d = this.hayDerecho() ? this.derecho.altura() : -1;
	    this.altura = Math.max(i, d) +1;
	}

	public int balance(){
	    int i = this.hayIzquierdo() ? this.izquierdo.altura() : -1;
	    int d = this.hayDerecho() ? this.derecho.altura() : -1;
	    return (i - d);
	}
	
        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
	    return String.format("%s %d/%d", elemento, altura, balance());
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)o;
	    return altura == vertice.altura && super.equals(o);
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
	super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	super.agrega(elemento);
	rebalancea((VerticeAVL)ultimoAgregado.padre);
    }

    private void rebalancea(VerticeAVL v){
	if(v == null)
	    return;
	v.cambiaAltura();
	int balance = v.balance();
	VerticeAVL i = (VerticeAVL)v.izquierdo;
	VerticeAVL d = (VerticeAVL)v.derecho;
	VerticeAVL a;
	VerticeAVL b;
	if(balance == -2){
	    a = (VerticeAVL)d.izquierdo;
	    b = (VerticeAVL)d.derecho;
	    if(d.balance() == 1){
		super.giraDerecha(d);
		d.cambiaAltura();
		a.cambiaAltura();
		d = (VerticeAVL)v.derecho;
		a = (VerticeAVL)d.izquierdo;
		b = (VerticeAVL)d.derecho;
	    }
	    super.giraIzquierda(v);
	    v.cambiaAltura();
	    d.cambiaAltura();
	}else{
	    if(balance == 2){
		b = (VerticeAVL)i.derecho;
		if(i.balance() == -1){
		    super.giraIzquierda(i);
		    i.cambiaAltura();
		    b.cambiaAltura();
		    i = (VerticeAVL)v.izquierdo;
		    b = (VerticeAVL)i.derecho;
		}
		super.giraDerecha(v);
		v.cambiaAltura();
		i.cambiaAltura();
	    }
	}
	rebalancea((VerticeAVL)v.padre);
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
	VerticeAVL e = (VerticeAVL)busca(elemento);
	if(e == null)
	    return;
	if(e.hayIzquierdo())
	    e = (VerticeAVL)intercambiaEliminable(e);
	VerticeAVL padre = (VerticeAVL)e.padre;
	eliminaVertice(e);
	elementos--;
	rebalancea(padre);
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
