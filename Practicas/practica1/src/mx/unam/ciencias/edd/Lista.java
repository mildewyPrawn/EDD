package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase Nodo privada para uso interno de la clase Lista. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
	    this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
	    siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
	    return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
	    if(siguiente == null)
		throw new NoSuchElementException("El elemento es null");
	    
	    anterior = siguiente;
	    siguiente = siguiente.siguiente;
	    return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
	    return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
	    if(anterior == null)
		throw new NoSuchElementException("El elemento es null");
	    
	    siguiente = anterior;
	    anterior = anterior.anterior;
	    return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
	    anterior = null;
	    siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
	    siguiente = null;
	    anterior = rabo;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
	return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
	return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
	//(cabeza == null) ? true: false;
	if(rabo == null)
	    return true;
	else
	    return false;		
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	Nodo n = new Nodo(elemento);
	longitud++;
	if(esVacia()){
	    rabo = n;
	    cabeza = n;
	}else{
	    rabo.siguiente = n;
	    n.anterior = rabo;
	    rabo = n;
	}
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
	agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	Nodo n = new Nodo(elemento);
	longitud++;
	if(esVacia()){
	    cabeza = n;
	    rabo = n;
	}else{
	    cabeza.anterior = n;
	    n.siguiente = cabeza;
	    cabeza = n;
	}
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor que cero, el elemento se agrega al inicio de la
     * lista. Si el índice es mayor o igual que el número de elementos en la
     * lista, el elemento se agrega al fina de la misma. En otro caso, después
     * de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	if(i < 1){
	    agregaInicio(elemento);
	    return;
	}
	if(i >= longitud){
	    agregaFinal(elemento);
	    return;
	}
        Nodo n = new Nodo(elemento);
	Nodo c = cabeza;
	while(i-- > 1)
	    c = c.siguiente;
	n.anterior = c;
	n.siguiente = c.siguiente;
	c.siguiente.anterior = n;
	c.siguiente = n;
	longitud++;
    }
    
    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
	if(elemento == null)
	    return;
	Nodo n = buscaAux(elemento);
	if(n == null)
	    return;
	if(cabeza == rabo){
	    cabeza = rabo = null;
	    longitud--;
	    return;
	}
	if(cabeza == n){
	    eliminaPrimero();
	    return;
	}
	if(rabo == n){
	    eliminaUltimo();
	    return;
	}

	n.anterior.siguiente = n.siguiente;
	n.siguiente.anterior = n.anterior;
	longitud--;
    }

    /**
     *Metodo auxiliar que busca un nodo en la lista que contenga el elemento 
     *que se busca.
     */
    private Nodo buscaAux(T elemento){
	Nodo n = cabeza;
	while(n != null){
	    if(n.elemento.equals(elemento))
		return n;
	    else
		n = n.siguiente;
	}
	return n;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
	if(esVacia())
	    throw new NoSuchElementException("La lista es vacia");
	
	T n = cabeza.elemento;
	if(longitud == 1){
	    cabeza = rabo = null;
	}
	if(longitud > 1){
	    cabeza = cabeza.siguiente;
	    cabeza.anterior = null;
	}
	longitud--;
	return n;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
	if(esVacia())
	    throw new NoSuchElementException("La lista es vacia");

	T n = rabo.elemento;
	if(longitud == 1){
	    cabeza = null;
	    rabo = null;
	}
	if(longitud > 1){
	    rabo = rabo.anterior;
	    rabo.siguiente = null;
	}
	
	longitud--;
	return n;
	
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <tt>true</tt> si <tt>elemento</tt> está en la lista,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	return buscaAux(elemento) != null;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
	Lista<T> l = new Lista<T>();
	Nodo n = cabeza;
	while(n != null){
	    l.agregaInicio(n.elemento);
	    n = n.siguiente;
	}
	return l;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
	Lista<T> l = new Lista<T>();
	Nodo n = cabeza;
	while(n != null){
	    l.agregaFinal(n.elemento);
	    n = n.siguiente;
	}
	return l;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
	longitud = 0;
	cabeza = rabo = null;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
	if(esVacia())
	    throw new NoSuchElementException("La lista es vacia");
	return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
	if(esVacia())
	    throw new NoSuchElementException("La lista es vacia");
	return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
	if(i < 0 || i >= longitud)
	    throw new ExcepcionIndiceInvalido("El indice es invalido");
	Nodo n = cabeza;
	int j = 0;
	while(j < i){
	    n = n.siguiente;
	    j++;
	}
	return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si
     *         el elemento no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
	Nodo n = cabeza;
	int i = 0;
	while(n != null){
	    if(n.elemento.equals(elemento))
		return i;
	    i++;
	    n = n.siguiente;
	}
	return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
	if(esVacia())
	    return "[]";
	Nodo n = cabeza;
	String r = "[" + n.elemento;
	n = n.siguiente;
	while(n != null){
	    r += ", " + n.elemento;
	    n = n.siguiente;
	}
	r += "]";
	return r;
    } 

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la lista es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
	if (lista.longitud == getLongitud()){
	    Nodo lu = lista.cabeza;
	    Nodo ld = this.cabeza;
	    while(lu != null && ld != null){
		if(!lu.elemento.equals(ld.elemento))
		    return false;
		lu = lu.siguiente;
		ld = ld.siguiente;
	    }
	    return true;
	}
	return false;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }
}
