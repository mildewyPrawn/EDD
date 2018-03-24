package mx.unam.ciencias.edd.proyecto2;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.IOException;

public class Proyecto2{

    /**
     * Metodo que verifica la entrada estandar o por linea de comandos.
     * @param los argumentos de la entrada estandar o linea de comandos.
     * @param si recibimos o no argumentos de la linea de comandos.
     */
    private static void reconocedor(String[] args, boolean entrada){

        Lector l = new Lector();
        BufferedReader br;
        try{
            if(entrada)
                br = new BufferedReader(new FileReader(args[0]));
            else
                br = new BufferedReader(new InputStreamReader(System.in));
            l.manejaLector(br);
        }catch(Exception e){
            System.out.println("Error en reconocer la lectura del archivo");
            System.exit(1);
        }
    }

    /**
     * Clase principal, verificamos la lectura del archivo y mandamos a llamar un 
     * metodo para ver que tipo de entrada vamos a recibir.
     * @param entrada estandar o por la linea de comandos.
     */
    public static void main(String[] args){
        try{
            reconocedor(args, args.length != 0);
        } catch (Exception e){
            System.out.println("Error en la lectura del archivo");
            System.exit(1);
        }
    }
}
