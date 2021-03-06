package mx.unam.ciencias.edd.proyecto2;
import mx.unam.ciencias.edd.Lista;

public class FactoryDS{

    /**Lista de datos, se guardan los datos*/
    private Lista<Integer> datos;
    
    /**cadena que es la estructura con la que se quiere trabajar.*/
    private String ed;

    /**cadena con la que se inicializan todos las salidas de .svg*/
    private String s = "<?xml version = \"1.0\" encoding = \"utf-8\" ?>" + "\n";

    /**
     * Constructor para la fábrica de estructuras.
     * @param cadena con el nombre de la estructura.
     * @param lista con los elementos de la estructura.
     */
    public FactoryDS(String ed, Lista<Integer> datos){
        this.ed = ed;
        this.datos = datos;
    }

    /**
     * Metodo que verifica que la estructura sea válida y manda a llamar 
     * al constructor correspondiente dependiendo de con qué estructura 
     * estemos tratando.
     */
    public void fabrica(){
        switch(ed){
        case"Lista":
            FactoryLista fl = new FactoryLista(datos, s);
            fl.dibuja();
            break;
        case "Pila":
            FactoryPila fp = new FactoryPila(datos, s);
            fp.dibuja();
            break;
        case"Cola":
            FactoryCola fc = new FactoryCola(datos, s);
            fc.dibuja();
            break;
        case "ArbolBinarioCompleto":
            FactoryArbolCompleto fac = new FactoryArbolCompleto(datos, s);
            fac.dibuja();
            break;
        case "ArbolBinarioOrdenado":
            FactoryArbolOrdenado fao = new FactoryArbolOrdenado(datos, s);
            fao.dibuja();
            break;
        case "ArbolRojinegro":
            FactoryRojinegro fr = new FactoryRojinegro(datos, s);
            fr.dibuja();
            break;
        case "ArbolAVL":
            FactoryAVL favl = new FactoryAVL(datos, s);
            favl.dibuja();
            break;
        case "MonticuloMinimo":
            FactoryMonticulo fm = new FactoryMonticulo(datos, s);
            fm.dibuja();
            break;
        case "Grafica":
            FactoryGrafica fg = new FactoryGrafica(datos, s);
            fg.dibuja();
            //System.out.println("Graficas por el momento no están disponibles.");
            //System.exit(0);
            break;
        default:
            System.exit(1);
            break;
        }
    }
}
