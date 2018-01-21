package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
	String s = "";
	if(!esVacia()){
	    Nodo n = cabeza;
	    while(n != null){
		s += n.elemento.toString() + "\n";
		n = n.siguiente;
	    }
	}
	return s;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
	if(elemento == null)
	    throw new IllegalArgumentException("El elemento es null");
	Nodo n = new Nodo(elemento);
	if(esVacia())
	    cabeza = rabo = n;
	else{
	    n.siguiente = cabeza;
	    cabeza = n;
	}
    }
}
