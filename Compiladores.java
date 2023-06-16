import static java.lang.Character.isDigit;
import java.util.*;

/* Grupo:
01463511 - Rodrigo da Costa Barros Araújo
01353493 - Carlos Augusto Felix Ferreira
01355556 - Pedro José Neris de Santana
*/
public class Compiladores {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
       // Parte 1
        String texto;
        do {
            System.out.print("Digite um texto: ");
            texto = scanner.nextLine();
        } while(validarParte3(texto));
        ArrayList<Character> tokens = analisadorLexico(texto);
        System.out.println("############## PARTE 1 ##############");
        System.out.println("Texto \"filtrado\":");
        tokens.forEach(System.out::print);

        // Parte 2
        boolean ehValida = true;

        while (ehValida) {
            int qtdCaracteres = tokens.size();
            
            if (qtdCaracteres > 10) {
                for (int i = qtdCaracteres; i > 10; i--) {
                    tokens.remove(i-1);
                }
                qtdCaracteres = 10;
            }
    
            // verifica se começa com uma consoante (válidada anteriormente)
            if (!ehConsoanteValida(tokens.get(0))) {
                ehValida = false;
                break;
            }
            for (int i = 1; i < qtdCaracteres; i++) {
                // verifica se há um algarismo numérico NO MEIO da cadeia
                if (isDigit(tokens.get(i)) && i != (qtdCaracteres - 1)) {
                    ehValida = false;
                    break;
                }
            }
            for (int i = 0; i < qtdCaracteres; i++) {
                int j = i + 1;
                if ((j) < qtdCaracteres) {
                    // verifica se é digrafo de consoantes e se é uma consoante seguida de vogal
                    if (ehDigrafoConsoante(tokens.get(i), tokens.get(j)) || 
                        (!ehVogalValida(tokens.get(j)))  && (!isDigit(tokens.get(j)) && j == (qtdCaracteres - 1)))
                    {
                        ehValida = false;
                        break;
                    }
                }
                i++;
            }
            
            System.out.println("\n############## PARTE 2 ##############");
            tokens.forEach(System.out::print);
            System.out.println(" é uma palavra " + (ehValida ? "é" : "não é") + " válida.");
            break;
        }
    }
    
    private static boolean validarParte3(String texto) {
        char c = texto.charAt(0);
        if (c == 'z' || c == 'x') {
            System.out.println("############## PARTE 3 ##############");
            System.out.println("O texto não pode começar com as consoantes Z ou X, pois são reservadas pelo sistema.");
            return true;
        }
        return false;
    }
    
    public static ArrayList<Character> analisadorLexico(String texto) {
        ArrayList<Character> tokens = new ArrayList<Character>();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (Character.isLetterOrDigit(c) && !ehExcecao(Character.toLowerCase(c))) {
                tokens.add(tokens.size(), c);
            }
        }
        return tokens;
    }

    public static boolean ehExcecao(char c) {
        char[] excecoes = { 'j', 'w', 'k', 'y', 'ç', 'h', 'q', '/', '(', ')', '&', '%', '$', '#', '@', '!', ' ' };
        for (int i = 0; i < excecoes.length; i++) {
            if (c == excecoes[i]) {
                return true;
            }
        }
        return false;
    }

    private static boolean ehVogalValida(char c) {
        return "aeiou".indexOf(c) >= 0;
    }

    private static boolean ehConsoanteValida(char c) {
        return !ehVogalValida(c);
    }

    private static boolean ehDigrafoConsoante(char c1, char c2) {
        return  (c1 == 's' && c2 == 's') || (c1 == 'r' && c2 == 'r') ||
                (c1 == 'g' && c2 == 'u') || (c1 == 's' && c2 == 'c');
    }
}
