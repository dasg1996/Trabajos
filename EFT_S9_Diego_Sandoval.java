package sandoval.diego.evaluaciónfinal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Sistema de venta de entradas para el Teatro Moro
 * 
 */
public class EFT_S9_Diego_Sandoval {
	// Aquí guardo los asientos disponibles de cada sección
    // Usé ArrayList por la utilidad dinámica que tienen
    static ArrayList<Integer> asientosVIP = new ArrayList<>();
    static ArrayList<Integer> asientosPalco = new ArrayList<>();
    static ArrayList<Integer> asientosPlateaBaja = new ArrayList<>();
    static ArrayList<Integer> asientosPlateaAlta = new ArrayList<>();
    static ArrayList<Integer> asientosGaleria = new ArrayList<>();

 // Precios en pesos chilenos y declaración e inicialización de constantes
    static final double PRECIO_VIP = 25000.0;
    static final double PRECIO_PALCO = 18000.0;
    static final double PRECIO_PLATEA_BAJA = 15000.0;
    static final double PRECIO_PLATEA_ALTA = 12000.0;
    static final double PRECIO_GALERIA = 8000.0;

    // Almacenamiento temporal de boletas
    //Usé list considerando que no sé cuántas entradas se comprarían
    static List<String[]> boletas = new ArrayList<>();

    public static void main(String[] args) {
    
    	inicializarAsientos();//
        Scanner consola = new Scanner(System.in);//Uso de clase Scanner para leer por consola lo introducido por el usuario

        System.out.println("Bienvenido al Teatro Moro");
        System.out.print("Ingrese la cantidad de entradas a comprar: ");
        int cantidad = obtenerEnteroValido(consola);//valido que lo que ingrese el usuario sea un número y añado un breakpoint

        for(int i = 0; i < cantidad; i++) {//ciclo for para el procesamiento de múltiples entradas
            System.out.println("\nProcesando entrada #" + (i+1));
            
            // Selección de sección
            String seccion = seleccionarSeccion(consola); //breakpoint
            if(seccion == null) continue;//Control de flujo para sección inválida

            // Punto de interrupción para depuración (selección de asiento)
            int asiento = seleccionarAsiento(consola, seccion);
            if(asiento == -1) continue;//manejo de falta de disponibilidad

            // Validación de edad
            System.out.print("Ingrese edad del cliente: ");
            int edad = obtenerEnteroValido(consola);

            // Aplicación de descuentos
            double descuento = aplicarDescuento(consola, edad);
            
            // Cálculo de precio
            double precioBase = obtenerPrecioBase(seccion);
            double precioFinal = precioBase * (1 - descuento);
            
            // Aquí almaceno los datos de la boleta
            String[] boleta = {
                seccion,
                String.valueOf(asiento),
                String.format("%.0f CLP", precioBase),
                String.format("%.0f%%", descuento * 100),
                String.format("%.0f CLP", precioFinal),
                String.valueOf(edad)
            };
            boletas.add(boleta);
            //Confirmación al usuario
            imprimirBoleta(boleta);
        }

        // Imprimo en consola el resumen de la compra
        System.out.println("\n--- RESUMEN DE COMPRA ---");
        boletas.forEach(EFT_S9_Diego_Sandoval::imprimirBoleta);
        consola.close();
    }

    /**
     * Inicializo los asientos disponibles para cada sección
     */
    private static void inicializarAsientos() {
        for(int i=1; i<=50; i++) asientosVIP.add(i);       // 50 asientos VIP, numeración del 1 al 50
        for(int i=1; i<=30; i++) asientosPalco.add(i);     // 30 asientos en el palco, numeración del 1 al 30
        for(int i=1; i<=100; i++) asientosPlateaBaja.add(i); // 100 asientos en platea baja, numeración del 1 al 100
        for(int i=1; i<=150; i++) asientosPlateaAlta.add(i); // 150 asientos en platea alta, numeración del 1 al 150
        for(int i=1; i<=200; i++) asientosGaleria.add(i);    // 200 asientos en galería, numeración del 1 al 200
    }

    /**
     * Gestiono de la selección de sección e incluyo validación con ciclo while
     */
    private static String seleccionarSeccion(Scanner consola) {
        while(true) { //iteración hasta encontrar una entrada válida
            System.out.println("\nSeleccione sección:");
            System.out.println("1. VIP");
            System.out.println("2. Palco");
            System.out.println("3. Platea Baja");
            System.out.println("4. Platea Alta");
            System.out.println("5. Galería");
            System.out.print("Opción: ");
            
            int opcion = obtenerEnteroValido(consola);
            switch(opcion) {//Control del flujo de ejecución por opción
                case 1: return "VIP";
                case 2: return "Palco";
                case 3: return "Platea Baja";
                case 4: return "Platea Alta";
                case 5: return "Galería";
                default: System.out.println("Opción inválida!");
            }
        }
    }

    /**
     * Gestión de selección de asientos con control de disponibilidad
     */
    private static int seleccionarAsiento(Scanner consola, String seccion) {
        ArrayList<Integer> asientosDisponibles = obtenerAsientosSeccion(seccion);
       //Verifico si hay asientos disponibles
        if(asientosDisponibles == null || asientosDisponibles.isEmpty()) {
            System.out.println("No hay asientos disponibles en esta sección");
            return -1;//devuelvo -1 para indicar error
        }

        while(true) {//Ciclo de iteración hasta que el usuario elija bien
            System.out.println("Asientos disponibles en " + seccion + ": " + asientosDisponibles.size());
            System.out.print("Ingrese número de asiento: ");
            int numero = obtenerEnteroValido(consola);
            
            if(asientosDisponibles.contains(numero)) {//Verifico que el asiento esté disponible
                asientosDisponibles.remove(Integer.valueOf(numero));//Lo quito de asientos disponibles. Añado un breakpoint
                return numero;//Devuelvo el número elegido
            }
            System.out.println("Asiento no disponible o inválido!");
        }
    }

    /**
     * Aplico lógica de descuentos según la categoría y valido las edades para niños y adulto mayor
     */
    private static double aplicarDescuento(Scanner consola, int edad) {
        while(true) {//ciclo while hasta que el usuario ingrrese una opción válida
            System.out.println("\nSeleccione categoría de descuento:");
            System.out.println("1. Niño (10%) - Menor de 18");
            System.out.println("2. Mujer (20%)");
            System.out.println("3. Estudiante (15%)");
            System.out.println("4. Adulto Mayor (25%) - 65 años o más");
            System.out.println("5. Ninguno");
            System.out.print("Opción: ");
            
            int opcion = obtenerEnteroValido(consola);
            switch(opcion) { //Uso de switch para validar descuentos
                case 1: 
                    if(edad < 18) return 0.10;
                    System.out.println("Edad no válida para categoría niño");
                    break;
                case 2: return 0.20;
                case 3: return 0.15;
                case 4: 
                    if(edad >= 65) return 0.25;
                    System.out.println("Edad no válida para adulto mayor");
                    break;
                case 5: return 0.0; //Sin descuento
                default: System.out.println("Opción inválida!");
            }
        }
    }

    /**
     * Obtengo lista de asientos según sección del teatro
     */
    private static ArrayList<Integer> obtenerAsientosSeccion(String seccion) {
        switch(seccion) {
            case "VIP": return asientosVIP;
            case "Palco": return asientosPalco;
            case "Platea Baja": return asientosPlateaBaja;
            case "Platea Alta": return asientosPlateaAlta;
            case "Galería": return asientosGaleria;
            default: return null;
        }
    }

    /**
     * Obtengo el precio base según sección del teatro
     */
    private static double obtenerPrecioBase(String seccion) {
        switch(seccion) {
            case "VIP": return PRECIO_VIP;
            case "Palco": return PRECIO_PALCO;
            case "Platea Baja": return PRECIO_PLATEA_BAJA;
            case "Platea Alta": return PRECIO_PLATEA_ALTA;
            case "Galería": return PRECIO_GALERIA;
            default: return 0;
        }
    }

    /**
     * Imprimo los detalles de la boleta
     */
    private static void imprimirBoleta(String[] boleta) {
        System.out.println("\n--- BOLETA ---");
        System.out.println("Sección: " + boleta[0]);
        System.out.println("Asiento: " + boleta[1]);
        System.out.println("Precio base: " + boleta[2]);
        System.out.println("Descuento aplicado: " + boleta[3]);
        System.out.println("Total a pagar: " + boleta[4]);
        System.out.println("Edad cliente: " + boleta[5]);
    }

    /**
     * Validación de entrada de números enteros
     * Prevengo excepciones con try catch y a la fuerza hago que ingresen entrada válida
     */
    private static int obtenerEnteroValido(Scanner consola) {
        while(true) {
            try {
                return Integer.parseInt(consola.nextLine());
            } catch(NumberFormatException e) {
                System.out.print("Entrada inválida! Ingrese un número: ");
            }
        }
    }
}