import java.util.ArrayList;
import java.util.Scanner;

public class AnalizadorNivel3 {

    // =========================================
    // LISTAS DE TOKENS Y LEXEMAS
    // =========================================
    static ArrayList<String> tokens = new ArrayList<>();
    static ArrayList<String> lexemas = new ArrayList<>();

    // =========================================
    // TABLA DE SIMBOLOS
    // GUARDA VARIABLES DECLARADAS
    // =========================================
    static ArrayList<String> variables = new ArrayList<>();

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);

        // =========================================
        // ENTRADA MULTILINEA
        // =========================================
        System.out.println("Escribe el codigo (escribe 'fin' para terminar):");

        String codigo = "";

        while (leer.hasNextLine()) {

            String linea = leer.nextLine();

            if (linea.equals("fin")) {
                break;
            }

            codigo += linea + "\n";
        }

        // =========================================
        // ANALISIS LEXICO
        // =========================================
        analizadorLexico(codigo);

        System.out.println("\n--- TOKENS ENCONTRADOS ---");

        for (int i = 0; i < tokens.size(); i++) {

            System.out.println(tokens.get(i) + " -> " + lexemas.get(i));
        }

        // =========================================
        // ANALISIS SINTACTICO
        // =========================================
        System.out.println("\n--- ANALISIS SINTACTICO ---");

        analizadorSintactico();

        leer.close();
    }

    // =========================================
    // ANALIZADOR LEXICO
    // =========================================
    public static void analizadorLexico(String codigo) {

        int i = 0;

        while (i < codigo.length()) {

            char c = codigo.charAt(i);

            // IGNORAR ESPACIOS
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // =========================================
            // PALABRAS E IDENTIFICADORES
            // =========================================
            if (Character.isLetter(c)) {

                String palabra = "";

                while (i < codigo.length() &&
                        Character.isLetterOrDigit(codigo.charAt(i))) {

                    palabra += codigo.charAt(i);
                    i++;
                }

                // PALABRAS RESERVADAS
                if (palabra.equals("int") ||
                    palabra.equals("if") ||
                    palabra.equals("while")) {

                    tokens.add("PALABRA_CLAVE");

                } else {

                    tokens.add("ID");
                }

                lexemas.add(palabra);

                continue;
            }

            // =========================================
            // NUMEROS
            // =========================================
            if (Character.isDigit(c)) {

                String numero = "";

                while (i < codigo.length() &&
                        Character.isDigit(codigo.charAt(i))) {

                    numero += codigo.charAt(i);
                    i++;
                }

                tokens.add("NUMERO");
                lexemas.add(numero);

                continue;
            }

            // =========================================
            // OPERADORES
            // =========================================
            if ("+-=*/".indexOf(c) != -1) {

                tokens.add("OPERADOR");
                lexemas.add(String.valueOf(c));

                i++;
                continue;
            }

            // =========================================
            // SIMBOLOS
            // =========================================
            if ("();{}".indexOf(c) != -1) {

                tokens.add("SIMBOLO");
                lexemas.add(String.valueOf(c));

                i++;
                continue;
            }

            // =========================================
            // TOKEN DESCONOCIDO
            // =========================================
            tokens.add("DESCONOCIDO");
            lexemas.add(String.valueOf(c));

            i++;
        }
    }

    // =========================================
    // ANALIZADOR SINTACTICO
    // =========================================
    public static void analizadorSintactico() {

        int i = 0;

        while (i < tokens.size()) {

            // =========================================
            // DECLARACION DE VARIABLES
            // int x;
            // =========================================
            if (lexemas.get(i).equals("int")) {

                if (i + 2 < tokens.size()) {

                    // VALIDAR:
                    // int x ;
                    if (tokens.get(i + 1).equals("ID") &&
                        lexemas.get(i + 2).equals(";")) {

                        // GUARDAR VARIABLE
                        variables.add(lexemas.get(i + 1));

                        System.out.println("DECLARACION VALIDA");

                        i += 3;
                        continue;

                    } else {

                        System.out.println("ERROR EN DECLARACION");
                        return;
                    }
                }
            }

            // =========================================
            // IF
            // if ( x ) { }
            // =========================================
            if (lexemas.get(i).equals("if")) {

                if (i + 5 < tokens.size()) {

                    if (lexemas.get(i + 1).equals("(") &&

                        (tokens.get(i + 2).equals("ID")
                        || tokens.get(i + 2).equals("NUMERO")) &&

                        lexemas.get(i + 3).equals(")") &&
                        lexemas.get(i + 4).equals("{") &&
                        lexemas.get(i + 5).equals("}")) {

                        System.out.println("IF VALIDO");

                        i += 6;
                        continue;

                    } else {

                        System.out.println("ERROR EN IF");
                        return;
                    }
                }
            }

            // =========================================
            // WHILE
            // while ( x ) { }
            // =========================================
            if (lexemas.get(i).equals("while")) {

                if (i + 5 < tokens.size()) {

                    if (lexemas.get(i + 1).equals("(") &&

                        (tokens.get(i + 2).equals("ID")
                        || tokens.get(i + 2).equals("NUMERO")) &&

                        lexemas.get(i + 3).equals(")") &&
                        lexemas.get(i + 4).equals("{") &&
                        lexemas.get(i + 5).equals("}")) {

                        System.out.println("WHILE VALIDO");

                        i += 6;
                        continue;

                    } else {

                        System.out.println("ERROR EN WHILE");
                        return;
                    }
                }
            }

            // =========================================
            // ASIGNACIONES
            // =========================================
            if (tokens.get(i).equals("ID")) {

                // =========================================
                // VALIDAR VARIABLE DECLARADA
                // =========================================
                if (!variables.contains(lexemas.get(i))) {

                    System.out.println("ERROR: VARIABLE NO DECLARADA -> "
                            + lexemas.get(i));

                    return;
                }

                // =========================================
                // x = 5;
                // =========================================
                if (i + 3 < tokens.size()) {

                    if (lexemas.get(i + 1).equals("=") &&

                        (tokens.get(i + 2).equals("NUMERO")
                        || tokens.get(i + 2).equals("ID")) &&

                        lexemas.get(i + 3).equals(";")) {

                        System.out.println("ASIGNACION VALIDA");

                        i += 4;
                        continue;
                    }

                    // =========================================
                    // x = 5 + 2;
                    // =========================================
                    if (i + 5 < tokens.size()) {

                        if (lexemas.get(i + 1).equals("=") &&

                            (tokens.get(i + 2).equals("NUMERO")
                            || tokens.get(i + 2).equals("ID")) &&

                            tokens.get(i + 3).equals("OPERADOR") &&

                            (tokens.get(i + 4).equals("NUMERO")
                            || tokens.get(i + 4).equals("ID")) &&

                            lexemas.get(i + 5).equals(";")) {

                            System.out.println("OPERACION VALIDA");

                            i += 6;
                            continue;
                        }
                    }
                }
            }

            // =========================================
            // ERROR GENERAL
            // =========================================
            System.out.println("ERROR SINTACTICO CERCA DE: "
                    + lexemas.get(i));

            return;
        }

        // =========================================
        // FINALIZACION
        // =========================================
        System.out.println("ANALISIS TERMINADO SIN ERRORES");
    }
}
