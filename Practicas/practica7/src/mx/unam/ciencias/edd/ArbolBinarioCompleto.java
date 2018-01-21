package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios completos. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Constructor que recibe la raíz del árbol. */
        public Iterador() {
	    cola = new Cola<Vertice>();
	    if(raiz != null)
		cola.mete(raiz);
        }
	
        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
	    return !cola.esVacia();
        }
	
        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
	    Vertice c = cola.saca();
	    if(c.izquierdo != null)
		cola.mete(c.izquierdo);
	    if(c.derecho != null)
		cola.mete(c.derecho);
	    return c.elemento;
        }
    }
    
    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }
    
    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }
    
    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	Vertice v = nuevoVertice(elemento);
	elementos++;
	if(raiz == null)
	    raiz = v;
	else{
	    Cola<Vertice> c = new Cola<Vertice>();
	    c.mete(raiz);
	    while(c != null){
		v = c.saca();
		if(v.izquierdo != null)
		    c.mete(v.izquierdo);
		else{
		    Vertice n = nuevoVertice(elemento);
		    v.izquierdo = n;
		    n.padre = v;
		    return;
		}if(v.derecho != null)
		     c.mete(v.derecho);
		else{
		    Vertice n = nuevoVertice(elemento);
		    v.derecho = n;
		    n.padre = v;
		    return;
		}
	    }
	}
    }
    
    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {       
	Vertice v = vertice(busca(elemento));
	if(v == null)
	    return;
	elementos --;
	if(elementos == 0)
	    raiz = null;
	else{
	    Cola<Vertice> c = new Cola<Vertice>();
	    c.mete(raiz);
	    Vertice a = buscaVertice(c);
	    intercambia(v, a);
	    Vertice b = a.padre;
	    if(b.izquierdo.equals(a))
		b.izquierdo = null;
	    else if(b.derecho.equals(a))
		b.derecho = null;
	}
    }

    private Vertice buscaVertice(Cola<Vertice> c){
	if(c.esVacia())
	    return null;
	Vertice v = c.saca();
	if(v.izquierdo == null && v.derecho == null){
	    if(c.esVacia()){
		return v;
	    }
	    return buscaVertice(c);
	}else{
	    if(v.izquierdo != null)
		c.mete(v.izquierdo);
	    if(v.derecho != null)
		c.mete(v.derecho);
	}
	return buscaVertice(c);
    }

    private void intercambia(Vertice v, Vertice u){
	Vertice a = nuevoVertice(v.elemento);
	v.elemento = u.elemento;
	u.elemento = a.elemento;
    }
    
    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
	return (int)Math.floor(Math.log(elementos)/Math.log(2));
    }
    
    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
	if(raiz == null)
	    return;
	Cola<Vertice> c = new Cola<Vertice>();
	c.mete(raiz);
	while(!c.esVacia()){
	    Vertice v = c.saca();
	    accion.actua(v);
	    if(v.izquierdo != null)
		c.mete(v.izquierdo);
	    if(v.derecho != null)
		c.mete(v.derecho);
	}
    }
    
    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
