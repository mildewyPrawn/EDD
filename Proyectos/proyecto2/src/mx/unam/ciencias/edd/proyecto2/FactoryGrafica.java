package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.Grafica;

public class FactoryGrafica{

    /**Cadena del codigo xml para las imágenes .svg*/
    private String s;

    public FactoryGrafica(Lista<Integer> datos, String s){
        this.s = s;
        if (verifica(datos)){
            //si se puede
        }else{
            System.out.println("No se puede realizar la gráfica con un número " +
"impar de datos.");
            System.exit(1);
        }

    }

    private boolean verifica(Lista<Integer> datos){
        return datos.getLongitud() % 2 == 0;
    }

    public void dibuja(){

    }
}
