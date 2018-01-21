package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase privada para iteradores de gráficas. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
	    iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
	    return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
	    return iterador.next().elemento;
        }
    }

    /* Vértices para gráficas; implementan la interfaz VerticeGrafica */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
	    this.elemento = elemento;
	    this.color = color.NINGUNO;
	    vecinos = new Lista<Vertice>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
	    return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
	    return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
	    return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
	    return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
	this.vertices = new Lista<>();
	this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
	return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
	return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
	if(this.contiene(elemento))
	    throw new IllegalArgumentException("El elemento ya esta en la grafica");
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	vertices.agrega(new Vertice(elemento));
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
	Vertice v = (Vertice) vertice(a);
	Vertice u = (Vertice) vertice(b);
	if(v == u || v.vecinos.contiene(u) && v.vecinos.contiene(u))
	    throw new IllegalArgumentException("Los elementos no se pueden conectar");
	v.vecinos.agrega(u);
	u.vecinos.agrega(v);
	aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
	Vertice v = (Vertice) vertice(a);
	Vertice u = (Vertice) vertice(b);
	if(!v.vecinos.contiene(u) || !u.vecinos.contiene(v))
	    throw new IllegalArgumentException("Los vertices no estan conectados");
	u.vecinos.elimina(v);
	v.vecinos.elimina(u);
	aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
	for(Vertice v : vertices)
	    if(v.elemento.equals(elemento))
		return true;
	return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
	if(!this.contiene(elemento))
	    throw new NoSuchElementException("El elemento no esta en la grafica");
	Vertice v = (Vertice)vertice(elemento);
	for(Vertice u : v.vecinos)
	    desconecta(v.elemento, u.elemento);
	vertices.elimina(v);
    }
    
    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("No son elementos de la grafica");
	Vertice v = (Vertice)vertice(a);
	Vertice u = (Vertice)vertice(b);
	return(v.vecinos.contiene(u) && u.vecinos.contiene(v));
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
	for(Vertice v : vertices)
	    if(v.elemento.equals(elemento))
		return v;
	throw new NoSuchElementException("El elemento no es elemento de la grafica");
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
	if(vertice == null)
	    throw new IllegalArgumentException();
	Vertice v = (Vertice)vertice(vertice.get());
	if(v == null)
	    return;
	if(!(color.equals(color.ROJO) || color.equals(color.NEGRO)))
	    throw new IllegalArgumentException();
	Vertice u = vertices.getPrimero();
	if(v.getClass() != u.getClass())
	    throw new IllegalArgumentException();
	v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
	if(aristas < vertices.getElementos()-1)
	    return false;
	int[] recorre = new int[1];
	Cola<Vertice> cola = new Cola<Vertice>();
	Vertice v = vertices.getPrimero();
	v.color = Color.ROJO;
	cola.mete(v);
	recorrido((vertice) -> recorre[0]++, cola);
	return recorre[0] == vertices.getElementos();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
	for(Vertice v : vertices)
	    accion.actua(v); 
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
	if(vertices.esVacia())
	    return;
	Vertice v = (Vertice)(vertice(elemento));
	for(Vertice u : vertices)
	    u.color = Color.NINGUNO;
	Cola<Vertice> cola = new Cola<Vertice>();
	cola.mete(v);
	while(!cola.esVacia()){
	    v = cola.saca();
	    v.color = Color.ROJO;
	    accion.actua(v);
	    for(Vertice u : v.vecinos){
		if(u.color == Color.ROJO)
		    continue;
		u.color = Color.ROJO;
		cola.mete(u);
	    }
	}
	for(Vertice u : vertices)
	    u.color = Color.NINGUNO;
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
	if(vertices.esVacia())
	    return;
	Vertice v = (Vertice)(vertice(elemento));
	for(Vertice u : vertices)
	    u.color = Color.NINGUNO;
	Pila<Vertice> pila = new Pila<Vertice>();
	pila.mete(v);
	while(!pila.esVacia()){
	    v = pila.saca();
	    v.color = Color.ROJO;
	    accion.actua(v);
	    for(Vertice u : v.vecinos){
		if(u.color == Color.ROJO)
		    continue;
		u.color = Color.ROJO;
		pila.mete(u);
	    }
	}
	for(Vertice u : vertices)
	    u.color = Color.NINGUNO;
    }

    private void recorrido(AccionVerticeGrafica<T> a, MeteSaca<Vertice> m){
	while(!m.esVacia()){
	    Vertice v = m.saca();
	    a.actua(v);
	    for(Vertice u : v.vecinos)
		if(u.color == Color.NINGUNO){
		    u.color = Color.ROJO;
		    m.mete(u);
		}
	}
	for(Vertice v : vertices)
	    v.color = Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
	return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
	vertices.limpia();
	aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
	String s = "";
	for(Vertice v : vertices){
	    v.color = Color.ROJO;
	    for(Vertice vecino : v.vecinos)
		if(vecino.color != Color.ROJO)
		    s += "(" + v.elemento.toString() + ", " +
			vecino.elemento.toString() + "), ";
	}
	for(Vertice v : vertices)
	    v.color = Color.NINGUNO;
	return s;
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param o el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)o;
	if(this.getElementos() != vertices.getElementos() ||
	   this.getAristas() != aristas)
	    return false;
	for(Vertice v : vertices){
	    if(!grafica.contiene(v.elemento))
		return false;
	    for(Vertice u : v.vecinos)
		if(!grafica.sonVecinos(v.elemento, u.elemento))
		    return false;
	}
	return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
