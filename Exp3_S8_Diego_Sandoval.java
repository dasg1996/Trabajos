import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Exp3_S8_Diego_Sandoval {
   
    static String[][] asientos;
    static String[][] clientes;
    static String[][] ventas;
    static List<String[]> promociones = new ArrayList<>();
    
    
    static int cantidadAsientos;
    static int cantidadClientes = 0;
    static int cantidadVentas = 0;
    static int asientosDisponibles;
    
    
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }

    static void inicializarSistema() {
        System.out.print("Ingrese cantidad total de asientos: ");
        cantidadAsientos = Integer.parseInt(scanner.nextLine());
        asientosDisponibles = cantidadAsientos;
        
        asientos = new String[cantidadAsientos][3]; 
        clientes = new String[100][4];
        ventas = new String[100][5];  
        
        
        for (int i = 0; i < cantidadAsientos; i++) {
            asientos[i][0] = String.valueOf(i); // ID
            asientos[i][1] = "F" + (i/10) + "-A" + (i%10); 
            asientos[i][2] = "D"; 
        }
        
        
        promociones.add(new String[]{"ESTUDIANTE", "0.10"});
        promociones.add(new String[]{"TERCERA EDAD", "0.15"});
    }

    static void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA TEATRO MORO ===");
            System.out.println("1. Vender entrada");
            System.out.println("2. Mostrar todas las ventas");
            System.out.println("3. Mostrar asientos disponibles");
            System.out.println("4. Buscar cliente");
            System.out.println("5. Salir");
            System.out.print("Seleccione opción: ");
            
            String opcion = scanner.nextLine();
            
            switch (opcion) {
                case "1":
                    venderEntrada();
                    break;
                case "2":
                    mostrarVentas();
                    break;
                case "3":
                    mostrarAsientosDisponibles();
                    break;
                case "4":
                    buscarCliente();
                    break;
                case "5":
                    System.out.println("Saliendo del sistema...");
                    return;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    static void venderEntrada() {
        if (asientosDisponibles == 0) {
            System.out.println("No hay asientos disponibles");
            return;
        }
        
        System.out.println("\n--- NUEVA VENTA ---");
        
        
        System.out.print("ID Cliente: ");
        String idCliente = scanner.nextLine();
        
        if (existeCliente(idCliente)) {
            System.out.println("Error: ID de cliente ya registrado");
            return;
        }
        
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Edad: ");
        String edad = scanner.nextLine();
        
        System.out.print("Categoría (General/Estudiante/Tercera Edad): ");
        String categoria = scanner.nextLine().toUpperCase();
        
        
        System.out.println("\nAsientos disponibles:");
        mostrarAsientosDisponibles();
        
        System.out.print("Seleccione ID de asiento: ");
        String idAsiento = scanner.nextLine();
        
        if (!validarAsiento(idAsiento)) {
            System.out.println("Asiento no válido");
            return;
        }
        
        System.out.print("Precio base: ");
        String precioStr = scanner.nextLine();
        double precio = Double.parseDouble(precioStr);
        
        
        double descuento = calcularDescuento(categoria);
        double precioFinal = precio * (1 - descuento);
        
       
        String[] cliente = {idCliente, nombre, edad, categoria};
        String[] venta = {
            String.valueOf(cantidadVentas + 1),
            idCliente,
            idAsiento,
            String.valueOf(precioFinal),
            "ACTIVA"
        };
        
        
        clientes[cantidadClientes++] = cliente;
        ventas[cantidadVentas++] = venta;
        actualizarAsiento(idAsiento, "O");
        asientosDisponibles--;
        
        System.out.printf("\nVenta registrada exitosamente!\n" +
                         "Asiento: %s\n" +
                         "Cliente: %s\n" +
                         "Precio final: $%.2f\n",
                         idAsiento, nombre, precioFinal);
    }

    static double calcularDescuento(String categoria) {
        for (String[] promo : promociones) {
            if (promo[0].equals(categoria)) {
                return Double.parseDouble(promo[1]);
            }
        }
        return 0.0;
    }

    static boolean existeCliente(String idCliente) {
        for (int i = 0; i < cantidadClientes; i++) {
            if (clientes[i][0].equals(idCliente)) {
                return true;
            }
        }
        return false;
    }

    static boolean validarAsiento(String idAsiento) {
        for (String[] asiento : asientos) {
            if (asiento[0].equals(idAsiento) && asiento[2].equals("D")) {
                return true;
            }
        }
        return false;
    }

    static void actualizarAsiento(String idAsiento, String estado) {
        for (String[] asiento : asientos) {
            if (asiento[0].equals(idAsiento)) {
                asiento[2] = estado;
                return;
            }
        }
    }

    static void mostrarAsientosDisponibles() {
        System.out.println("\n--- ASIENTOS DISPONIBLES ---");
        System.out.println("ID\tUbicación");
        for (String[] asiento : asientos) {
            if (asiento[2].equals("D")) {
                System.out.println(asiento[0] + "\t" + asiento[1]);
            }
        }
        System.out.println("Total disponibles: " + asientosDisponibles);
    }

    static void mostrarVentas() {
        System.out.println("\n--- HISTORIAL DE VENTAS ---");
        System.out.println("ID\tCliente\tAsiento\tPrecio\tEstado");
        for (int i = 0; i < cantidadVentas; i++) {
            String[] venta = ventas[i];
            String nombreCliente = obtenerNombreCliente(venta[1]);
            System.out.printf("%s\t%s\t%s\t$%s\t%s\n",
                venta[0], nombreCliente, venta[2], venta[3], venta[4]);
        }
    }

    static String obtenerNombreCliente(String idCliente) {
        for (int i = 0; i < cantidadClientes; i++) {
            if (clientes[i][0].equals(idCliente)) {
                return clientes[i][1];
            }
        }
        return "DESCONOCIDO";
    }

    static void buscarCliente() {
        System.out.print("\nIngrese ID del cliente: ");
        String id = scanner.nextLine();
        
        for (int i = 0; i < cantidadClientes; i++) {
            if (clientes[i][0].equals(id)) {
                System.out.println("\n--- DATOS DEL CLIENTE ---");
                System.out.println("ID: " + clientes[i][0]);
                System.out.println("Nombre: " + clientes[i][1]);
                System.out.println("Edad: " + clientes[i][2]);
                System.out.println("Categoría: " + clientes[i][3]);
                return;
            }
        }
        System.out.println("Cliente no encontrado");
    }
}

