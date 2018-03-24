package mx.unam.ciencias.edd.proyecto1;
import mx.unam.ciencias.edd.Lista;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Proyecto1{

    /**
     * Metodo que verifica la entrada estandar o por linea de comandos.
     * @param los argumentos de la entrada estandar o linea de comandos.
     * @param si recibimos o no argumentos de la linea de comandos.
     */
    private static void reconocedor(String[] args, boolean entrada){

        Modificador m = new Modificador();
        Lista<String> l = new Lista<String>();
        BufferedReader br;
        try{
            if(entrada){
                m.ordenaArgs(args);
            }
            else{
                br = new BufferedReader(new InputStreamReader(System.in));
                String s;
                while((s = br.readLine()) != null)
                    l.agrega(s);
                br.close();
                m.ordenaList(l);
            }
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
        }catch(Exception e){
            System.out.println("Error en la lectura del archivo");
            System.exit(1);
        }
    }
}
