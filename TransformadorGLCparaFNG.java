import java.util.*;

/*
Grupo:
01463511 - Rodrigo da Costa Barros Araújo
01353493 - Carlos Augusto Felix Ferreira
01355556 - Pedro José Neris de Santana
 */
public class TransformadorGLCparaFNG {
    private static int nonTerminalCount = 0;

    public static void main(String[] args) {
        // Lista de produções da GLC original
        List<String> productions = new ArrayList<>();
        productions.add("S -> AB | CSB");
        productions.add("A -> aB | C");
        productions.add("B -> bbB | b");

        // Transforma a GLC em FNG
        List<String> fngProductions = transformToFNG(productions);

        // Imprime as produções da GLC original
        System.out.println("Produções da GLC original:");
        productions.forEach(System.out::println);

        // Imprime as produções da FNG resultante
        System.out.println("\nProduções da FNG resultante:");
        fngProductions.forEach(System.out::println);
    }

    public static List<String> transformToFNG(List<String> productions) {
        // Lista de produções da FNG resultante
        List<String> fngProductions = new ArrayList<>();

        // Mapeamento de não terminais para suas produções únicas
        Map<String, Set<String>> nonTerminalProductions = new HashMap<>();

        // Processa cada produção da GLC
        for (String production : productions) {
            String[] parts = production.split("->");
            String nonTerminal = parts[0].trim();
            String[] rhs = parts[1].split("\\|");

            Set<String> uniqueProductions = new HashSet<>();

            // Processa cada lado direito da produção
            for (String productionRHS : rhs) {
                List<String> transformedProductions = transformRHS(productionRHS.trim(), nonTerminalProductions);
                uniqueProductions.addAll(transformedProductions);
            }

            // Armazena as produções únicas para o não terminal
            nonTerminalProductions.put(nonTerminal, uniqueProductions);
        }

        // Constrói as produções da FNG resultante
        StringBuilder fngProduction = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : nonTerminalProductions.entrySet()) {
            String nonTerminal = entry.getKey();
            Set<String> uniqueProductions = entry.getValue();

            fngProduction.append(nonTerminal).append(" -> ");
            int i = 1;
            for (String uniqueProduction : uniqueProductions) {
                fngProduction.append(uniqueProduction);
                if (i < uniqueProductions.size()) {
                    fngProduction.append(" | ");
                }
                i++;
            }

            // Adiciona a produção à lista de produções da FNG
            fngProductions.add(fngProduction.toString());
            fngProduction.setLength(0);
        }

        return fngProductions;
    }

    private static List<String> transformRHS(String rhs, Map<String, Set<String>> nonTerminalProductions) {
        // Lista de produções transformadas
        List<String> transformedProductions = new ArrayList<>();

        // Verifica se o lado direito tem no máximo 2 símbolos
        if (rhs.length() <= 2) {
            transformedProductions.add(rhs);
        } else {
            StringBuilder fngProduction = new StringBuilder();
            String[] symbols = rhs.split("");

            // Processa cada símbolo do lado direito
            for (int i = 0; i < symbols.length; i++) {
                String symbol = symbols[i];
                if (!symbol.equals(" ")) {
                    if (i == 0) {
                        // Primeiro símbolo
                        fngProduction.append(symbol).append(" ");
                    } else if (i == symbols.length - 1) {
                        // Último símbolo
                        Set<String> productions = nonTerminalProductions.get(symbol);
                        if (productions != null) {
                            // Se for um não terminal, substitui pela primeira produção
                            fngProduction.append(productions.iterator().next());
                        } else {
                            // Se for um terminal, mantém o símbolo
                            fngProduction.append(symbol);
                        }
                    } else {
                        // Símbolo intermediário
                        String nonTerminal = generateNonTerminal();
                        fngProduction.append(nonTerminal).append(" ");

                        // Adiciona o mapeamento do símbolo intermediário para o símbolo atual
                        nonTerminalProductions.put(nonTerminal, Collections.singleton(symbol));
                    }
                }
            }

            // Adiciona a produção transformada à lista
            transformedProductions.add(fngProduction.toString());
        }

        return transformedProductions;
    }

    private static String generateNonTerminal() {
        // Gera um novo símbolo não terminal
        return "N" + nonTerminalCount++;
    }
}
