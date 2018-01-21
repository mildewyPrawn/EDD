package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase para las entradas del diccionario. */
    private class Entrada {

        /* La llave. */
        public K llave;
        /* El valor. */
        public V valor;

        /* Construye una nueva entrada. */
        public Entrada(K llave, V valor) {
	    this.llave = llave;
	    this.valor = valor;
        }
    }

    /* Clase privada para iteradores de diccionarios. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        public Iterador() {
	    if(entradas != null && elementos > 0)
		getLista();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
	    return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        public Entrada siguiente() {
	    if(iterador == null)
		throw new NoSuchElementException("No hay siguiente en el diccionario");
	    Entrada res = iterador.next();
	    if(!iterador.hasNext()){
		indice++;
		getLista();
		if(indice == entradas.length)
		    iterador = null;
	    }
	    return res;
        }

	private void getLista(){
	    while(indice < entradas.length){
		if(entradas[indice] != null && !entradas[indice].esVacia()){
		    iterador = entradas[indice].iterator();
		    break;
		}
		indice++;
	    }
	}

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
	    if(!hasNext())
		throw new NoSuchElementException("No hay siguiente en el diccionario");
	    iterador.next();
	    if(iterador == null)
		mueveIterador();
        }
    }

    /* Clase privada para iteradores de llaves de diccionarios. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorLlaves() {
	    super();
        }

        /* Regresa el siguiente elemento. */
        @Override public K next() {
	    return super.siguiente().llave;
        }
    }

    /* Clase privada para iteradores de valores de diccionarios. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Construye un nuevo iterador de llaves del diccionario. */
        public IteradorValores() {
	    super();
        }

        /* Regresa el siguiente elemento. */
        @Override public V next() {
	    return super.siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores*/
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
	this.dispersor = dispersor;
	capacidad = capacidad < 64 ? 64 : capacidad;
	int c = 1;
	while(c < capacidad * 2)
	    c *= 2;
	entradas = nuevoArreglo(c);
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
	if(llave == null || valor == null)
	    throw new IllegalArgumentException("La llave o el valor son null");
	int i = (entradas.length - 1) & dispersor.dispersa(llave);
	if(entradas[i] != null){
	    for(Entrada e : entradas[i])
		if(e.llave.equals(llave)){
		    e.valor = valor;
		    return;
		}
	}else{
	    entradas[i] = new Lista<Entrada>();
	}
	entradas[i].agrega(new Entrada(llave, valor));
	elementos++;
	if(carga() >= 0.72)
	    creceArreglo();
    }

    private void creceArreglo(){
	Lista<Entrada>[] l = entradas;
	entradas = nuevoArreglo(entradas.length * 2);
	elementos = 0;
	for(Lista<Entrada> le : l)
	    if( le != null)
		for(Entrada e : le)
		    agrega(e.llave, e.valor);
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
	if(llave == null)
	    throw new IllegalArgumentException("La llave es null");
	int i = (entradas.length - 1) & dispersor.dispersa(llave);
	if(entradas[i] != null)
	    for(Entrada e : entradas[i])
		if(e.llave.equals(llave))
		    return e.valor;
	throw new NoSuchElementException("La llave no se encuentra");
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <tt>true</tt> si la llave está en el diccionario,
     *         <tt>false</tt> en otro caso.
     */
    public boolean contiene(K llave) {
	if(elementos == 0 || llave == null)
	    return false;
	int i = (entradas.length - 1) & dispersor.dispersa(llave);
	if(entradas[i] == null)
	    return false;
	for(Entrada e : entradas[i])
	    if(e.llave.equals(llave))
		return true;
	return false;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
	if(llave == null)
	    throw new IllegalArgumentException("La llave es null");
	int i = (entradas.length - 1) & dispersor.dispersa(llave);
	if(entradas[i] != null)
	    for(Entrada e : entradas[i])
		if(e.llave.equals(llave)){
		    entradas[i].elimina(e);
		    elementos--;
		    return;
		}
	throw new NoSuchElementException("La llave no se encuentra");
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
	int c = 0;
	if(entradas != null)
	    for(Lista<Entrada> l : entradas)
		if(l != null)
		    c += l.getElementos() - 1;
	return c;
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
	int c = 0;
	if(entradas != null)
	    for(Lista<Entrada> l : entradas)
		if(l != null)
		    if(c < l.getElementos() - 1)
			c = l.getElementos() - 1;
	return c;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
	return ((double)elementos)/entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
	return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
	return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
	entradas = nuevoArreglo(entradas.length);
	elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
	if(esVacia())
	    return "{}";
	String s = "{ ";
	for(Lista<Entrada> l : entradas)
	    if(l != null)
		for(Entrada e : l)
		    s += "'" + e.llave.toString() + "': '" + e.valor.toString() + "', ";
	return s + "}";
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
	if(elementos != d.elementos)
	    return false;
	for(Lista<Entrada> l : d.entradas)
	    if(l != null)
		for(Entrada e : l){
		    if(!contiene(e.llave))
			return false;
		    if(!e.valor.equals(get(e.llave)))
			return false;
		}
	return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
