package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase privada para iteradores de montículos. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
	    return indice < elementos;
	}

        /* Regresa el siguiente elemento. */
        @Override public T next() {
	    if(hasNext())
		return arbol[indice++];
	    throw new NoSuchElementException("No hay, no existe.");
        }
    }

    /* Clase estática privada para poder implementar HeapSort. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;
	
        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
	    this.elemento = elemento;
	    indice = -1;
        }
	
        /* Regresa el índice. */
        @Override public int getIndice() {
	    return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
	    this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
	    return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
	arbol = nuevoArreglo(100);
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
	this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
	elementos = n;
	arbol = nuevoArreglo(n);
	int i = 0;
	for(T e : iterable){
	    arbol[i] = e;
	    arbol[i].setIndice(i);
	    i++;
	}
	for(int j = (elementos - 1) / 2; j >= 0; j--)
	    monticuloMin(j);
    }

    private void monticuloMin(int i){
	int izq = i * 2 + 1;
	int der = i * 2 + 2;
	int min = i;

	if(elementos <= i)
	    return;
	if(izq < elementos && arbol[izq].compareTo(arbol[i]) < 0)
	    min = izq;
	if(der < elementos && arbol[der].compareTo(arbol[min]) < 0)
	    min = der;
	if(min == i)
	    return;
	else
	    intercambia(min, i);
	monticuloMin(min);
    }

    private void intercambia(int i, int j){
	arbol[i].setIndice(j);
	arbol[j].setIndice(i);
	T e = arbol[i];
	arbol[i] = arbol[j];
	arbol[j] = e;
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
	if(elementos == arbol.length)
	    duplicaTamaño();
	elemento.setIndice(elementos);
	arbol[elementos] = elemento;
	elementos++;
	recorreArriba(elementos-1);
    }

    private void duplicaTamaño(){
	T [] ar = nuevoArreglo(arbol.length * 2);
	elementos = 0;
	for(T e : arbol)
	    ar[elementos++] = e;
	this.arbol = ar;
    }

    private void recorreArriba(int i){
	int p = (i - 1) / 2;
	int m = i;

	if(p >= 0 &&arbol[p].compareTo(arbol[i]) > 0)
	    m = p;
	if(m != i){
	    T a = arbol[i];
	    arbol[i] = arbol[p];
	    arbol[i].setIndice(i);
	    arbol[p] = a;
	    arbol[p].setIndice(p);
	    recorreArriba(m);
	}
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
	if(elementos == 0)
	    throw new IllegalStateException("El monticulo es vacio");
	T e = arbol[0];
	elimina(e);
	return e;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
	if(elemento == null || esVacia())
	    return;
	if(!contiene(elemento))
	    return;
	int i = elemento.getIndice();
	if(i < 0 || elementos <= i)
	    return;
	intercambia(i, elementos - 1);
	arbol[elementos - 1] = null;
	elementos--;
	actualiza(i);
	monticuloMin(i);
	elemento.setIndice(-1);
    }

    private void actualiza(int i){
	if(i < 0)
	    return;
	int p = padre(i);
	while(0 < i && 0 <= p && arbol[i].compareTo(arbol[p]) < 0){
	    intercambia(p,i);
	    i = p;
	    p = padre(i);
	}
    }

    private int padre(int i){
	if(0 < i && i < elementos)
	    return (i - 1) / 2;
	return -1;
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	for(T e : arbol)
	    if(elemento.equals(e))
		return true;
	return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
	return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
	for(int i = 0; i < elementos; i++)
	    arbol[i] = null;
	elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
	if(elemento == null)
	    return;
	int i = elemento.getIndice();
	recorreArriba(i);
	monticuloMin(i);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
	return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
	if(i < 0 || i >= elementos)
	    throw new NoSuchElementException("Indice inválido");
	return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
	String s = "";
        for (int i = 0; i < elementos; i++) {
	    s += arbol[i].toString() + ", ";
        }
	return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param o el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)o;
	if(monticulo.getElementos() != this.getElementos())
	    return false;
	for(int i = 0; i < this.getElementos(); i++)
	    if(!arbol[i].equals(monticulo.get(i)))
		return false;
	return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
	Lista<Adaptador<T>> adaptadores = new Lista<Adaptador<T>>();
	for(T e : coleccion){
	    Adaptador<T> a = new Adaptador<T>(e);
	    adaptadores.agrega(a);
	}
	Lista<T> l = new Lista<T>();
	MonticuloMinimo<Adaptador<T>> m = new MonticuloMinimo<Adaptador<T>>(adaptadores);
	while(m.elementos != 0)
	    l.agrega(m.elimina().elemento);
	return l;
    }
}
