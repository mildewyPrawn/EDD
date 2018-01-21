package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Construye un iterador con el vértice recibido. */
        public Iterador() {
	    pila = new Pila<Vertice>();
	    Vertice v = raiz;
	    while(v != null){
		pila.mete(v);
		v = v.izquierdo;
	    }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
	    return !pila.esVacia();
	}

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
	    Vertice v = pila.saca();
	    if(v.derecho != null){
		Vertice a = v.derecho;
		while(a != null){
		    pila.mete(a);
		    a = a.izquierdo;
		}
	    }
	    return v.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	Vertice v = nuevoVertice(elemento);
	elementos++;
	if(raiz == null){
	    raiz = v;
	    ultimoAgregado = v;
	}
	else
	    agrega(raiz, v);
    }

    private void agrega(Vertice v, Vertice a){
	if(a.elemento.compareTo(v.elemento) <= 0)
	    if(v.izquierdo == null){
		v.izquierdo = a;
		a.padre = v;
		ultimoAgregado = a;
	    }else
		agrega(v.izquierdo, a);
	else
	    if(v.derecho == null){
		v.derecho = a;
		a.padre = v;
		ultimoAgregado = a;
	    }else
		agrega(v.derecho, a);
    }
    
    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	elementos--;
	Vertice v = vertice(busca(elemento));
	if(v == null)
	    return;
	if(v.izquierdo == null || v.derecho == null)
	    eliminaVertice(v);
	else{
	    Vertice b = intercambiaEliminable(v);
	    eliminaVertice(b);
	}
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
	Vertice v = vertice(maximo(vertice.izquierdo));
	vertice.elemento = v.elemento;
	return v;
    }

    private Vertice maximo(Vertice v){
	if(vertice(v).derecho == null)
	    return v;
	return maximo(vertice(v).derecho);
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
	Vertice v = null;
	if(vertice.izquierdo != null)
	    v = vertice.izquierdo;
	if(vertice.derecho != null)
	    v = vertice.derecho;
	if(vertice.padre == null)
	    raiz = v;
	else
	    if(vertice.padre.derecho == vertice)
		vertice.padre.derecho = v;
	    else
		vertice.padre.izquierdo = v;
	if(v != null)
	    v.padre = vertice.padre;
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
	return busca(raiz, elemento);
    }
    
    private VerticeArbolBinario<T> busca(VerticeArbolBinario<T> v, T e){
	if(v == null)
	    return null;
	if(e.compareTo(v.get()) == 0)
	    return v;
	if(e.compareTo(v.get()) < 0)
	    return busca(vertice(v).izquierdo, e);
	return busca(vertice(v).derecho, e);
    }
    
    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
	if(vertice == null || vertice(vertice).izquierdo == null)
	    return;
	Vertice q = vertice(vertice);
	Vertice p = q.izquierdo;
	if(q.padre == null){
	    raiz = p;
	    p.padre = null;
	}else{
	    p.padre = q.padre;
	    if(q.padre.izquierdo == q)
		q.padre.izquierdo = p;
	    else
		q.padre.derecho = p;
	}
	q.izquierdo = p.derecho;
	if(q.izquierdo != null)
	    q.izquierdo.padre = q;
	q.padre = p;
	p.derecho = q;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(vertice == null || vertice(vertice).derecho == null)
	    return;
	Vertice q = vertice(vertice);
	Vertice p = q.derecho;
	if(q.padre == null){
	    raiz = p;
	    p.padre = null;
	}else{
	    p.padre = q.padre;
	    if(q.padre.derecho == q)
		q.padre.derecho = p;
	    else
		q.padre.izquierdo = p;
	}
	q.derecho = p.izquierdo;
	if(q.derecho != null)
	    q.derecho.padre = q;
	q.padre = p;
	p.izquierdo = q;
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
	dfsPreOrder(raiz(), accion);
    }

    private void dfsPreOrder(VerticeArbolBinario<T> v, AccionVerticeArbolBinario<T> a){
	if(v == null)
	    return;
	a.actua(v);
	dfsPreOrder(vertice(v).izquierdo, a);
	dfsPreOrder(vertice(v).derecho, a);
    }
	
    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
	dfsInOrder(raiz(), accion);
    }

    private void dfsInOrder(VerticeArbolBinario<T> v, AccionVerticeArbolBinario<T> a){
	if(v == null)
	    return;
	dfsInOrder(vertice(v).izquierdo, a);
	a.actua(v);
	dfsInOrder(vertice(v).derecho, a);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        dfsPostOrder(raiz(), accion);
    }

    private void dfsPostOrder(VerticeArbolBinario<T> v, AccionVerticeArbolBinario<T> a){
	if(v == null)
	    return;
	dfsPostOrder(vertice(v).izquierdo, a);
	dfsPostOrder(vertice(v).derecho, a);
	a.actua(v);
    }
    
    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
