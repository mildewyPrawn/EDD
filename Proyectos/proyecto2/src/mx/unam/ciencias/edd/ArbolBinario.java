package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        public T elemento;
        /** El padre del vértice. */
        public Vertice padre;
        /** El izquierdo del vértice. */
        public Vertice izquierdo;
        /** El derecho del vértice. */
        public Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public Vertice(T elemento) {
	    this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <tt>true</tt> si el vértice tiene padre,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayPadre() {
	    return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <tt>true</tt> si el vértice tiene izquierdo,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
	    return this.izquierdo != null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <tt>true</tt> si el vértice tiene derecho,
         *         <tt>false</tt> en otro caso.
         */
        @Override public boolean hayDerecho() {
	    return this.derecho != null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
	    if(!hayPadre())
		throw new NoSuchElementException("El vertice no tiene padre");
	    return this.padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
	    if(!hayIzquierdo())
		throw new NoSuchElementException("El vertice no tiene hijo izquierdo");
	    return this.izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
	    if(!hayDerecho())
		throw new NoSuchElementException("El vertice no tiene hijo derecho");
	    return this.derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
	    return altura(this);
        }

	private int altura(Vertice v){
	    if(v == null)
		return -1;
	    return 1 + Math.max(altura(v.izquierdo), altura(v.derecho));
	}

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
	    return profundidad(this);
        }

	private int profundidad(Vertice v){
	    if(v.padre == null)
		return 0;
	    return 1 + profundidad(v.padre);
	}

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
	    return elemento;
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param o el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)o;
	    if(vertice == null || !this.elemento.equals(vertice.elemento))
		return false;
	    if(this.elemento.equals(vertice.elemento)){
		if(this.derecho != null && this.izquierdo != null)
		    return this.derecho.equals(vertice.derecho) && this.izquierdo.equals(vertice.izquierdo);
		if(this.derecho != null && vertice.izquierdo == null)
		    return this.derecho.equals(vertice.derecho);
		if(this.izquierdo != null && vertice.derecho == null)
		    return this.izquierdo.equals(vertice.izquierdo);
	    }
	    return vertice.izquierdo == null && vertice.derecho == null;
	}

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        public String toString() {
	    if(this == null)
		return "";
	    return this.elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
	for(T c : coleccion)
	    this.agrega(c);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        return new Vertice(elemento);
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
	return raiz.altura();
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
	return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	return contiene(elemento, raiz);
    }

    private boolean contiene(T elemento, Vertice v){
	if(v == null)
	    return false;
	if(v.elemento.equals(elemento))
	    return true;
	return contiene(elemento, v.izquierdo) || contiene(elemento, v.derecho);
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <tt>null</tt>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <tt>null</tt> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
	return busca(raiz, elemento);
    }

    private VerticeArbolBinario<T> busca(Vertice v, T e){
	if(v == null)
	    return null;
	if(e == null)
	    return null;
	if(v.get().equals(e))
	    return v;
	Vertice i = vertice(busca(v.izquierdo, e));
	Vertice d = vertice(busca(v.derecho, e));
	if(i != null)
	    return i;
	return d;
    }
	

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
	if(esVacia())
	    throw new NoSuchElementException("El arbol es vacio");
	return this.raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
	return raiz == null;

    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
	raiz = null;
	elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param o el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") 
            ArbolBinario<T> arbol = (ArbolBinario<T>)o;
	if(arbol == null)
	    return false;
	if(!this.esVacia())
	    return raiz.equals(arbol.raiz);
	return this.esVacia() && arbol.esVacia();
    }

    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
	if(raiz == null)
	    return "";
	boolean[] a = new boolean[altura() + 1];
	for(int i = 0; i < altura()+1; i++)
	    a[i] = false;
	return toString(raiz, 0, a);
    }

    private String toString(VerticeArbolBinario<T> v, int l, boolean[] a){
	if(v == null)
	    return"";
	String s = v.toString() + "\n";
	a[l] = true;
	if(v.hayIzquierdo() && v.hayDerecho()){
	    s += dibujaEspacios(l,a) + "├─›" + toString(v.izquierdo(), l+1, a);
	    s += dibujaEspacios(l,a) + "└─»";
	    a[l] = false;
	    s += toString(v.derecho(),l+1,a);
	}else if(v.hayIzquierdo()){
	    s += dibujaEspacios(l,a) + "└─›";
	    a[l] = false;
	    s += toString(v.izquierdo(), l+1, a);
	}else if(v.hayDerecho()){
	    s += dibujaEspacios(l,a) + "└─»";
	    a[l] = false;
	    s += toString(v.derecho(), l+1, a);
	}
	return s;
    }

    private String dibujaEspacios(int l, boolean[] a){
	String cadena = "";
	for(int i = 0; i < l; i++)
	    if(a[i])
		cadena += "│  ";
	    else
		cadena += "   ";
	return cadena;
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
	return (Vertice)vertice;
    }
}
