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
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
	    this.elemento = elemento;
	    color = Color.NINGUNO;
	    vecinos = new Lista<Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
	    return this.elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
	    return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
	    return this.color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
	    return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
	    this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
	    return this.indice;
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
    }
    
    /* Vecinos para gráficas; un vecino es un vértice y el peso de la arista que
     * los une. Extienden Vertice. */
    private class Vecino extends Vertice {
	
        /* El vértice del vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vecino con el vértice. */
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
	    return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
	    return vecino.vecinos.getLongitud();
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
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
	this.vertices = new Lista<Vertice>();
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
	if(elemento == null || this.contiene(elemento))
	    throw new IllegalArgumentException("No se puede agregar el elemento");
	Vertice v = new Vertice(elemento);
	vertices.agrega(v);
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
	conecta(a,b,1.0);
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
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("No son elementos de la gráfica");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	if(va == vb || sonVecinos(a,b) || peso < 0)
	    throw new IllegalArgumentException("No se puede conectar");
	Vecino vva = new Vecino(va, peso);
	Vecino vvb = new Vecino(vb, peso);
	va.vecinos.agrega(vvb);
	vb.vecinos.agrega(vva);
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
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("No estan en la grafica");
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException("No son vecinos");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	Vecino vva = new Vecino(va,1.0);
	Vecino vvb = new Vecino(vb,1.0);
	for(Vecino ve : vb.vecinos)
	    if(ve.vecino == va){
		vva = ve;
		break;
	    }
	for(Vecino ve : va.vecinos)
	    if(ve.vecino == vb){
		vvb = ve;
		break;
	    }
	va.vecinos.elimina(vvb);
	vb.vecinos.elimina(vva);
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
	    throw new NoSuchElementException("El elemento no esta en la gráfica");
	Vertice v = (Vertice)vertice(elemento);
	for(Vecino ve : v.vecinos){
	    Vertice a = ve.vecino;
	    desconecta(v.elemento, a.elemento);
	}
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
	for(Vecino v : va.vecinos)
	    if(v.vecino.equals(vb))
		return v.peso;
	return -1;
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
	    throw new IllegalArgumentException("El peso es cero");
	Vertice va = (Vertice)vertice(a);
	Vertice vb = (Vertice)vertice(b);
	Vecino vva = rVecino(vb, va);
	Vecino vvb = rVecino(va,vb);
	vva.peso = peso;
	vvb.peso = peso;
    }
    
    private Vecino rVecino(Vertice va, Vertice vb){
	T a = va.get();
	T b = vb.get();
	if(!this.contiene(a) || !this.contiene(b))
	    throw new NoSuchElementException("El elemento no esta");
	if(!sonVecinos(va.get(), vb.get()))
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
	for(Vertice v : vertices)
	    if(v.vecinos.getLongitud() == 0)
		return false;
	return true;
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
	for(Vertice v : vertices)
	    v.color = Color.ROJO;
	for(Vertice v : vertices){
	    for(Vecino ve : v.vecinos)
		if(v.color == Color.ROJO && ve.getColor() == Color.ROJO)
		    s += String.format("(%d, %d), ",v.elemento, ve.vecino.get());
	    v.color = Color.NEGRO;
	}
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
	if(this.getElementos() != vertices.getElementos() || this.getAristas() != aristas)
	    return false;
	for(Vertice v : vertices){
	    if(!grafica.contiene(v.elemento))
		return false;
	    for(Vecino ve : v.vecinos)
		if(!grafica.sonVecinos(v.elemento, ve.vecino.get()))
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
	if(!contiene(origen) || !contiene(destino))
	    throw new NoSuchElementException("No estan en la grafica");
	Lista<VerticeGrafica<T>> l = new Lista<VerticeGrafica<T>>();
	MonticuloMinimo<Vertice> mm;
	Vertice o = (Vertice)vertice(origen);
	Vertice d = (Vertice)vertice(destino);
	for(Vertice v : vertices)
	    v.distancia = Double.MAX_VALUE;
	o.distancia = 0;
	mm = new MonticuloMinimo<Vertice>(vertices);
	double aux;
	while(!mm.esVacia()){
	    Vertice v = mm.elimina();
	    for(Vecino ve : v.vecinos){
		aux = ve.peso;
		if(ve.vecino.distancia > (v.distancia+aux)){
		    ve.vecino.distancia = (v.distancia+aux);
		    mm.reordena(ve.vecino);
		}
	    }
	}
	if(d.distancia == Double.MAX_VALUE)
	    return l;
	Vertice v = d;
	l.agrega(v);
	while(!v.elemento.equals(origen))
	    for(Vecino ve : v.vecinos){
		aux = ve.peso;
		if(v.distancia == ve.vecino.distancia+aux){
		    l.agrega(ve.vecino);
		    v = ve.vecino;
		}
	    }
	return l.reversa();
    }
}
