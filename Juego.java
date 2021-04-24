// Karen Garza Treviño                 611107
// Sofía Guadalupe Montoya Chavarría   566646

// JUEGO DE OBSTÁCULOS

/* --- Simbología ---
. - Lugar del tablero
# - Obstáculos
* - Objeto
M - Meta
X - Indica que el usuario ha colisionado con un objeto
O - Indica que el usuario ha llegado a la meta */

import java.io.*;
import java.util.*;

class Juego {   
   public static void main(String[] args) {
      Scanner s = new Scanner(System.in);
      char opc;
      char[][] tablero = new char[8][7];
      byte[] pO = new byte[2];
      byte[] pJ = new byte[2];
      byte[] pM = new byte[2];
      do { // Hacer mientras que el jugador quiera jugar
         boolean objetoRodeado;
         do { // Hacer mientras que el objeto o la meta estén rodeados por obstáculos
           objetoRodeado = false;
           byte[][] posiciones = new byte[8][2];
           byte lugar = 0; // Controla en qué lugar del arreglo posiciones nos encontramos
           byte choca = 0; // Indica por cuántos lados choca el objeto
            
           // Generar tablero
           for (byte i = 0; i < 8; i++)
               for (byte j = 0; j < 7; j++)
                  tablero[i][j] = '.';
                                 
           // GENERAR POSICIONES ALEATORIAS
           boolean repetido;
           
           // Obstáculos
           for (byte i = 0; i < 6; i++) {
               do {
                  pO = generarPosicion();
                  repetido = verificarNoRepeticion(posiciones, pO, lugar);
               } while (repetido);
               posiciones[lugar][0] = pO[0];
               posiciones[lugar][1] = pO[1];
               lugar++;
               tablero[pO[0]][pO[1]] = '#';
           }
           
           // Jugador
           do {
               pJ = generarPosicion();
               repetido = verificarNoRepeticion(posiciones, pJ, lugar);
           } while (repetido);
           posiciones[lugar][0] = pJ[0];
           posiciones[lugar][1] = pJ[1];
           lugar++;
           choca = desplazamientoPosible(pJ[0], pJ[1], tablero);
           if (choca >= 3)
               objetoRodeado = true;
           tablero[pJ[0]][pJ[1]] = '*';
           
           // Meta
           do {
               pM = generarPosicion();
               repetido = verificarNoRepeticion(posiciones, pM, lugar);
           } while (repetido);
           posiciones[lugar][0] = pM[0];
           posiciones[lugar][1] = pM[1];
           choca = desplazamientoPosible(pM[0], pM[1], tablero);
           if (choca >= 3)
               objetoRodeado = true;
           tablero[pM[0]][pM[1]] = 'M';
                      
         } while (objetoRodeado);
         
         // Imprimir el tablero
         imprimirTablero(tablero);
         
         // Inicializar estado de juego
         boolean estadoJuego = true;
         boolean ganar = false;
         
         // Movimiento
         while (estadoJuego) {
            tablero[pJ[0]][pJ[1]] = '.';
            pJ = movimiento(pJ);
            if (tablero[pJ[0]][pJ[1]] == '.') // Parte normal del tablero
               tablero[pJ[0]][pJ[1]] = '*';
            else if (tablero[pJ[0]][pJ[1]] == '#') { // Obstáculo
               tablero[pJ[0]][pJ[1]] = 'X';
               estadoJuego = false;
            }
            else if (tablero[pJ[0]][pJ[1]] == 'M') { // Meta
               tablero[pJ[0]][pJ[1]] = 'O';
               estadoJuego = false;
               ganar = true;
            }
            imprimirTablero(tablero);
         }
         
         // Verificar si el usuario ganó o perdió
         if (ganar)
            System.out.println("¡Has ganado!");
         else
            System.out.println("Has perdido");
         
         // Preguntar si el usuario quiere jugar de nuevo
         do {
            System.out.println("¿Quieres volver a jugar? (S/N)");
            opc = s.next().charAt(0);
         } while (opc != 'S' && opc != 's' && opc != 'N' && opc != 'n');
         
      } while (opc == 'S' || opc == 's');
   }
   
   // --- MÉTODOS ---
   
   // Generar una posición aleatoria
   public static byte [] generarPosicion() {
      byte pRenglon = (byte) (Math.random() * 8);
      byte pColumna = (byte) (Math.random() * 7);
      byte [] posicion = {pRenglon, pColumna};
      return posicion;
   }
   
   // Verificar que la posición generada no esté duplicada
   public static boolean verificarNoRepeticion(byte[][] posicionesGlobales, byte[] posicion, byte lugar) {
      boolean igual = false;
      for (byte j=0;j<lugar;j+=1)
         if((posicionesGlobales[j][0]==posicion[0])&&(posicionesGlobales[j][1]==posicion[1]))
            igual = true;
      return igual;
   }
   
   // Imprimir el tablero
   public static void imprimirTablero(char[][] tablero) {
      for (byte i = 0; i < 8; i++) {
         for (byte j = 0; j < 7; j++)
            System.out.print(tablero[i][j] + " ");
         System.out.print("\n");
      }
   }
   
   // Movimiento del jugador
   public static byte[] movimiento(byte[] posJugador) {
      Scanner s2 = new Scanner(System.in);
      byte renglonAntiguo = posJugador[0];
      byte columnaAntigua = posJugador[1];
      System.out.println("MOVIMIENTOS\n1. Izquierda\n2. Derecha\n3. Arriba\n4. Abajo");
      int renglonActual, columnaActual;
      byte mov;
      do {
         renglonActual = renglonAntiguo;
         columnaActual = columnaAntigua;
         do {
            mov = s2.nextByte();
         } while (mov < 1 || mov > 4);
         switch (mov) {
            case 1: columnaActual = columnaAntigua - 1; break;
            case 2: columnaActual = columnaAntigua + 1; break;
            case 3: renglonActual = renglonAntiguo - 1; break;
            case 4: renglonActual = renglonAntiguo + 1; break;
         }
      } while (renglonActual < 0 || renglonActual > 7 || columnaActual < 0 || columnaActual > 6);
      byte [] posNueva = {(byte) renglonActual, (byte) columnaActual};
      return posNueva;
   }
   
   // Verificar si el objeto o meta están rodeados de obstáculos
   public static byte desplazamientoPosible(byte renglon, byte columna, char[][] tablero) {
      byte choca = 0;
      byte r, c, contador;
      r = renglon;
      c = columna;
      // Movimiento hacia arriba
      contador = 0;
      while (r >= 0 && contador <= 3) {
         if (tablero[r][columna] == '#' || r == 0)
            choca++;
         r--;
         contador++;
      }
      // Movimiento hacia abajo
      contador = 0;
      r = renglon;
      while (r <= 7 && contador <= 3) {
         if (tablero[r][columna] == '#' || r == 7)
            choca++;
         r++;
         contador++;
      }
      // Movimiento hacia la izquierda
      contador = 0;
      while (c >= 0 && contador <= 3) {
         if (tablero[renglon][c] == '#' || c == 0)
            choca++;
         c--;
         contador++;
      }
      // Movimiento hacia la derecha
      contador = 0;
      c = columna;
      while (c <= 6 && contador <= 3) {
         if (tablero[renglon][c] == '#' || c == 6)
            choca++;
         c++;
         contador++;
      }
      return choca;
   }
}