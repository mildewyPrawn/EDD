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

    /* Vertices para gráficas; implementan la interfaz ComparableIndexable y
     * VerticeGrafica */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* El diccionario de vecinos del vértice. */
        public Diccionario<T, Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
	    this.elemento = elemento;
	    color = Color.NINGUNO;
	    indice = -1;
	    vecinos = new Diccionario<T, Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
	    return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
	    return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
	    return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
	    return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
	    this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
	    return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
	    if(distancia == -1 && vertice.distancia == -1)
		return 0;
	    if(distancia == -1)
		return 1;
	    if(vertice.distancia == -1)
		return -1;
	    double d = distancia - vertice.distancia;
	    if(d > 0)
		return 1;
	    if(d < 0)
		return -1;
	    return 0;
        }

        /* Define el color del vértice. */
        public void setColor(Color color) {
	    this.color = color;
        }

	private Vecino getVecino(T elemento){
	    for(Vecino v : vecinos)
		if(v.vecino.elemento.equals(elemento))
		    return v;
	    return null;
	}
    }

    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Extienden Vertice. */
    private class Vecino extends Vertice {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
	    super(vecino.get());
	    this.vecino = vecino;
	    this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
	    return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
	    return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
	    return vecino.color;
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
	    return vecino.vecinos;
        }

        /* Define el color del vecino. */
        @Override public void setColor(Color color) {
	    vecino.setColor(color);
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Diccionario<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
	vertices = new Diccionario<T, Vertice>();
	this.aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
	return vertices.getElementos();
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
	if(elemento == null || contiene(elemento))
	    throw new IllegalArgumentException("El elemento es null");
	vertices.agrega(elemento, new Vertice(elemento));
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
	conecta(a, b, 1.0);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
	if(peso <= 0 || a.equals(b) || sonVecinos(a,b))
	    throw new IllegalArgumentException("No se puede conectar");
	Vertice va = getVertice(a);
	Vertice vb = getVertice(b);
	va.vecinos.agrega(vb.elemento, new Vecino(vb, peso));
	vb.vecinos.agrega(va.elemento, new Vecino(va, peso));
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

	if(a.equals(b))
	    throw new IllegalArgumentException("Los elementos son iguales");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	if(!va.vecinos.contiene(b))
	    throw new IllegalArgumentException("Los elementos no estan conectados");
	va.vecinos.elimina(vb.elemento);
	vb.vecinos.elimina(va.elemento);
	aristas--;
    }

    private Vertice getVertice(T elemento){
	if(!vertices.contiene(elemento))
	    throw new NoSuchElementException("El elemento no está");
	return vertices.get(elemento);
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
	Vertice v = getVertice(elemento);
	for(Vecino ve : v.vecinos){
	    ve.vecino.vecinos.elimina(elemento);
	    aristas--;
	}
	vertices.elimina(elemento);
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
	if(a.equals(b))
	    return false;
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	for(Vecino v : va.vecinos)
	    if(v.vecino == vb)
		return true;
	return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("No estan en la grafica");
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException("No son vecinos");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	return va.vecinos.get(b).peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("No estan en la grafica");
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException("No son vecinos");
	if(peso <= 0)
	    throw new IllegalArgumentException("EL peso es cero");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	Vecino vva = rVecino(vb, va);
	Vecino vvb = rVecino(va, vb);
	vva.peso = peso;
	vvb.peso = peso;
    }

    private Vecino rVecino(Vertice va, Vertice vb){
	T a = va.get();
	T b = vb.get();
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("El elemento no esta");
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException("No son vecinos");
	Vecino v = new Vecino(vb, 1.0);
	for(Vecino vv : va.vecinos)
	    if(vv.vecino == vb)
		v = vv;
	return v;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
	if(!this.contiene(elemento))
	    throw new NoSuchElementException("No esta el elemento");
	for(Vertice v : this.vertices)
	    if(v.elemento.equals(elemento))
		return v;
	return null;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || !Vertice.class.isInstance(vertice))
            throw new IllegalArgumentException("El vértice no es válido");
	Vertice v = new Vertice(null);
	if(v.getClass() == vertice.getClass()){
	    Vertice ve = (Vertice)vertice;
	    ve.setColor(color);
	}else{
	    Vecino ve = (Vecino)vertice;
	    ve.setColor(color);
	}
    }
    
    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
	if(vertices.getElementos() <= 1)
	    return true;
	if(aristas < vertices.getElementos()-1)
	    return false;
	int[] rec = new int[1];
	Cola<Vertice> c = new Cola<Vertice>();
	Vertice v = vertices.iterator().next();
	v.color = Color.ROJO;
	c.mete(v);
	recorrido((vertice) -> rec[0]++, c);
	return rec[0] == vertices.getElementos();
    }

    private void recorrido(AccionVerticeGrafica<T> accion, MeteSaca<Vertice> m){
	while(!m.esVacia()){
	    Vertice v = m.saca();
	    accion.actua(v);
	    for(Vecino ve : v.vecinos)
		if(ve.vecino.color == Color.NINGUNO){
		    ve.vecino.color = Color.ROJO;
		    m.mete(ve.vecino);
		}
	}
	for(Vertice v : vertices)
	    v.color = Color.NINGUNO;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
	for(Vertice v : this.vertices)
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
	if(!this.contiene(elemento))
	    throw new NoSuchElementException("El elemento no esta");
	if(vertices.esVacia())
	    return;
	Vertice v = (Vertice)vertice(elemento);
	for(Vertice ve : vertices)
	    v.color = Color.NINGUNO;
	Cola<Vertice> c = new Cola<Vertice>();
	c.mete(v);
	while(!c.esVacia()){
	    v = c.saca();
	    v.color = Color.ROJO;
	    accion.actua(v);
	    for(Vecino ve : v.vecinos){
		if(ve.getColor() == Color.ROJO)
		    continue;
		ve.vecino.color = Color.ROJO;
		c.mete(ve.vecino);
	    }
	}
	for(Vertice rve : vertices)
	    rve.color = Color.NINGUNO;
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
	if(!this.contiene(elemento))
	    throw new NoSuchElementException("El elemento no esta");
	if(vertices.esVacia())
	    return;
	Vertice v = (Vertice)vertice(elemento);
	for(Vertice ve : vertices)
	    v.color = Color.NINGUNO;
	Pila<Vertice> p = new Pila<Vertice>();
	p.mete(v);
	while(!p.esVacia()){
	    v = p.saca();
	    v.color = Color.ROJO;
	    accion.actua(v);
	    for(Vecino ve : v.vecinos){
		if(ve.getColor() == Color.ROJO)
		    continue;
		ve.vecino.color = Color.ROJO;
		p.mete(ve.vecino);
	    }
	}
	for(Vertice rve : vertices)
	    rve.color = Color.NINGUNO;	
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
	    for(Vecino ve : v.vecinos)
		if(ve.vecino.color != Color.ROJO)
		    s += "(" + v.elemento.toString() + ", " + ve.vecino.elemento.toString() + "), ";
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
	if(grafica.getElementos() != vertices.getElementos() || grafica.getAristas() != aristas)
	    return false;
	for(Vertice v : vertices){
	    if(!grafica.contiene(v.elemento))
		return false;
	    for(Vecino ve : v.vecinos)
		if(!grafica.sonVecinos(v.elemento, ve.vecino.elemento))
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

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
	if(!contiene(origen) || !contiene(destino))
	    throw new NoSuchElementException("No estan en la grafica");
	Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();
	Vertice o = (Vertice)vertice(origen);
	Vertice d = (Vertice)vertice(destino);
	if(origen.equals(destino)){
	    l.agrega(o);
	    return l;
	}
	for(Vertice v : vertices)
	    v.distancia = -1;
	o.distancia = 0;
	Vertice v = o;
	Cola<Vertice> c = new Cola<Vertice>();
	c.mete(v);
	while(!c.esVacia()){
	    v = c.saca();
	    for(Vecino ve : v.vecinos)
		if(ve.vecino.distancia == -1){
		    ve.vecino.distancia = v.distancia+1;
		    c.mete(ve.vecino);
		}
	}
	if(d.distancia == -1)
	    return l;
	v = d;
	l.agrega(v);
	while(!v.elemento.equals(origen))
	    for(Vecino ve : v.vecinos)
		if(v.distancia == ve.vecino.distancia+1){
		    l.agrega(ve.vecino);
		    v = ve.vecino;
		}
	return l.reversa();	
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y
     *         el vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
	Vertice o = (Vertice)vertice(origen);
	Vertice d = (Vertice)vertice(destino);
	Lista<VerticeGrafica<T>> l = new Lista<>();
	l.agrega(d);
	if(origen.equals(destino))
	    return l;
	for(Vertice v : vertices)
	    v.distancia = -1;
	o.distancia = 0;
	MonticuloMinimo<Vertice> mm = new MonticuloMinimo<Vertice>(vertices,vertices.getElementos());
	while(!mm.esVacia()){
	    Vertice ve = mm.elimina();
	    for(Vecino v : ve.vecinos)
		if(v.vecino.distancia == -1 || v.vecino.distancia > ve.distancia + v.peso){
		    v.vecino.distancia = ve.distancia + v.peso;
		    mm.reordena(v.vecino);
		}
	}
	if(d.distancia == -1)
	    return new Lista<VerticeGrafica<T>>();
	while(!d.elemento.equals(origen)){
	    for(Vecino v : d.vecinos){
		if(v.vecino.distancia == d.distancia - v.peso){
		    l.agregaInicio(v.vecino);
		    d = v.vecino;
		    break;
		}
	    }
	}
	return l;
    }
}
