package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
	int m, n;
	int r = 0;
	int t = 0;
	for(m = llave.length, n = 0; m >= 4; m -= 4, n +=4)
	    r ^= (((llave[n] & 0xFF) << 24) | ((llave[n+1] & 0xFF) << 16)
		  |((llave[n+2] & 0xFF) << 8) | (llave[n+3] & 0xFF));
	while(m-- > 0)
	    t |= (llave[n+m] & 0xFF) << (8*(3-m));
	return r^t;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
	int x, y, z, l, k;
	k = 0;
	l = llave.length;
	x = y = 0x9e3779b9;
	z = 0xFFFFFFFF;
	while( l >= 12){
	    x += ((llave[k] & 0xFF) + ((llave[k+1] & 0xFF) << 8) +
		  ((llave[k+2] & 0xFF) << 16) + ((llave[k+3] & 0xFF) << 24));
	    y += ((llave[k+4] & 0xFF) + ((llave[k+5] & 0xFF) << 8) +
		  ((llave[k+6] & 0xFF) << 16) + ((llave[k+7] & 0xFF) << 24));
	    z += ((llave[k+8] & 0xFF) + ((llave[k+9] & 0xFF) << 8) +
		  ((llave[k+10] & 0xFF) << 16) + ((llave[k+11] & 0xFF) << 24));
	    int[] xyz = {x,y,z};
	    mezcla(xyz);
	    x = xyz[0];
	    y = xyz[1];
	    z = xyz[2];
	    l -= 12;
	    k += 12;
	}
	z += llave.length;
	switch(l){
	case 11: z += (llave[k+10] & 0xFF) << 24;
	case 10: z += (llave[k+9] & 0xFF) << 16;
	case 9: z += (llave[k+8] & 0xFF) << 8;
	case 8: y += (llave[k+7] & 0xFF) << 24;
	case 7: y += (llave[k+6] & 0xFF) << 16;
	case 6: y += (llave[k+5] & 0xFF) << 8;
	case 5: y += (llave[k+4] & 0xFF);
	case 4: x += (llave[k+3] & 0xFF) << 24;
	case 3: x += (llave[k+2] & 0xFF) << 16;
	case 2: x += (llave[k+1] & 0xFF) << 8;
	case 1: x += (llave[k] & 0xFF);
	    break;
	}
	int xxyyzz[] = {x,y,z};
	mezcla(xxyyzz);
	return(int)xxyyzz[2];	
    }

    private static void mezcla(int [] xyz){
	xyz[0] -= xyz[1];
	xyz[0] -= xyz[2];
	xyz[0] ^= (xyz[2] >>> 13);
	xyz[1] -= xyz[2];
	xyz[1] -= xyz[0];
	xyz[1] ^= (xyz[0] << 8);
	xyz[2] -= xyz[0];
	xyz[2] -= xyz[1];
	xyz[2] ^= (xyz[1] >>> 13);
	xyz[0] -= xyz[1];
	xyz[0] -= xyz[2];
	xyz[0] ^= (xyz[2] >>> 12);
	xyz[1] -= xyz[2];
	xyz[1] -= xyz[0];
	xyz[1] ^= (xyz[0] << 16);
	xyz[2] -= xyz[0];
	xyz[2] -= xyz[1];
	xyz[2] ^= (xyz[1] >>> 5);
	xyz[0] -= xyz[1];
	xyz[0] -= xyz[2];
	xyz[0] ^= (xyz[2] >>> 3);
	xyz[1] -= xyz[2];
	xyz[1] -= xyz[0];
	xyz[1] ^= (xyz[0] << 10);
	xyz[2] -= xyz[0];
	xyz[2] -= xyz[1];
	xyz[2] ^= (xyz[1] >>> 15);
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
	int k = 5381;
	for(int i = 0; i < llave.length; i++)
	    k += (k << 5) + (llave[i] & 0xFF);
	return k;
    }
}
