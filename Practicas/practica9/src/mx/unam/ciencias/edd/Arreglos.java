package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
	quickSort(arreglo, 0, arreglo.length-1, comparador);
    }

    private static <T> void
    quickSort(T[] arreglo, int i, int j, Comparator<T> comparador){
	if(i >= j)
	    return;
	int k = i+1;
	int l = j;
	while(k < l){
	    if(comparador.compare(arreglo[k], arreglo[i]) > 0 &&
	       comparador.compare(arreglo[l], arreglo[i]) <= 0)
		intercambia(arreglo, k++, l--);
	    else if(comparador.compare(arreglo[k], arreglo[i]) <= 0)
		k++;
	    else
		l--;
	}
	if(comparador.compare(arreglo[k], arreglo[i]) > 0)
	    k--;
	intercambia(arreglo, k, i);
	quickSort(arreglo, i, k-1, comparador);
	quickSort(arreglo, k+1, j, comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
	for(int i = 0; i < arreglo.length; i++)
	    for(int j = i+1; j < arreglo.length; j++)
		if(comparador.compare(arreglo[i], arreglo[j]) > 0)
		    intercambia(arreglo, i, j);
    }

    private static<T> void intercambia(T [] arreglo, int i, int j){
	T elemento = arreglo[i];
	arreglo[i] = arreglo[j];
	arreglo[j] = elemento;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
	int i = 0;
	int f = arreglo.length -1;
	int j;
	while(i <= f){
	    j = (i + f)/2;
	    if(comparador.compare(arreglo[j], elemento) > 0)
		f = j-1;
	    else if(comparador.compare(arreglo[j], elemento) < 0)
		i = j+1;
	    else
		return j;
	}
	return -1;
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
