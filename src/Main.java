import Modelo.Encomenda;
import Modelo.Intervalo;
import Modelo.Veiculo;
import Servico.CalculadoraPrioridade;
import Servico.GestorEncomendas;
import Servico.GestorFrota;
import Servico.GestorHistorico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String FICHEIRO_ENCOMENDAS = "encomendas.txt";
    private static final String FICHEIRO_VEICULOS = "veiculos.txt";
    private static final String FICHEIRO_INTERVALOS = "intervalos.txt";

    // Pasta dos ficheiros de dados; pode ser indicada como argumento (por omissão, a pasta atual)
    private static File pastaDados = new File(".");

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        if (args.length > 0) {
            pastaDados = new File(args[0]);
        }

        // Os gestores partilham uma única instância de GestorHistorico (Secção 2 do Relatório)
        GestorHistorico gestorHistorico = new GestorHistorico();
        GestorEncomendas gestorEncomendas = new GestorEncomendas(gestorHistorico);
        GestorFrota gestorFrota = new GestorFrota(gestorHistorico);

        garantirFicheirosExemplo();
        carregarDados(gestorEncomendas, gestorFrota);
        System.out.println("Dados carregados de " + pastaDados.getPath() + ": "
                + gestorEncomendas.listarEncomendasPorPrioridade().size() + " encomendas pendentes, "
                + gestorFrota.getVeiculos().size() + " veículos, "
                + gestorFrota.getJanelasEntrega().size() + " janelas de entrega.");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try (Scanner sc = new Scanner(System.in)) {
            int opcao = -1;
            while (opcao != 0) {
                System.out.println("\n=======================================================");
                System.out.println("   SISTEMA DE GESTÃO DE ENCOMENDAS E FROTA - FASE 1    ");
                System.out.println("=======================================================");
                System.out.println("1. Registar encomenda");
                System.out.println("2. Consultar encomenda mais prioritária (Topo do Heap)");
                System.out.println("3. Listar todas as encomendas pendentes por prioridade");
                System.out.println("4. Alterar prioridade de uma encomenda");
                System.out.println("5. Cancelar encomenda por ID");
                System.out.println("6. Adicionar veículo à frota");
                System.out.println("7. Listar veículos e carga atribuída");
                System.out.println("8. Carregar veículo (Empacotador Guloso)");
                System.out.println("9. Planear entregas (Seleção de Intervalos / MergeSort)");
                System.out.println("10. Adicionar janela temporal de entrega");
                System.out.println("11. Listar e navegar no Histórico (Lista Duplamente Ligada)");
                System.out.println("12. Desfazer última operação (UNDO)");
                System.out.println("13. Refazer operação (REDO)");
                System.out.println("0. Sair (com opção de guardar os dados)");
                System.out.print("Escolha uma opção: ");

                try {
                    opcao = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        registarEncomendaMenu(sc, gestorEncomendas, gestorFrota);
                        break;
                    case 2:
                        Encomenda topo = gestorEncomendas.consultarMaisPrioritaria();
                        System.out.println(topo != null ? "Mais prioritária: " + topo : "A fila está vazia.");
                        break;
                    case 3:
                        List<Encomenda> fila = gestorEncomendas.listarEncomendasPorPrioridade();
                        if (fila.isEmpty()) {
                            System.out.println("Não existem encomendas pendentes.");
                        } else {
                            for (Encomenda e : fila) {
                                System.out.println(e);
                            }
                        }
                        break;
                    case 4:
                        System.out.print("ID da encomenda: ");
                        String idAlt = sc.nextLine().trim();
                        System.out.print("Nova prioridade (inteiro): ");
                        try {
                            int novaPrio = Integer.parseInt(sc.nextLine().trim());
                            if (gestorEncomendas.alterarPrioridade(idAlt, novaPrio)) {
                                System.out.println("Prioridade alterada com sucesso!");
                            } else {
                                System.out.println("Encomenda não encontrada na fila.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Prioridade inválida.");
                        }
                        break;
                    case 5:
                        System.out.print("ID da encomenda a cancelar: ");
                        String idCanc = sc.nextLine().trim();
                        Encomenda cancelada = gestorEncomendas.cancelarEncomenda(idCanc);
                        System.out.println(cancelada != null
                                ? "Encomenda cancelada: " + cancelada.getId()
                                : "Encomenda não encontrada.");
                        break;
                    case 6:
                        adicionarVeiculoMenu(sc, gestorFrota);
                        break;
                    case 7:
                        for (Veiculo v : gestorFrota.getVeiculos()) {
                            System.out.println(v);
                            for (Encomenda e : v.getEncomendasAtribuidas()) {
                                System.out.println("   -> " + e);
                            }
                        }
                        break;
                    case 8:
                        System.out.print("ID do veículo a carregar (ex: V01): ");
                        String idVeic = sc.nextLine().trim();
                        List<Encomenda> carregadas = gestorFrota.carregarVeiculo(idVeic, gestorEncomendas.getFilaPrioridade());
                        if (carregadas.isEmpty()) {
                            System.out.println("Nenhuma encomenda carregada.");
                        } else {
                            System.out.println("Encomendas carregadas no veículo " + idVeic + ":");
                            for (Encomenda e : carregadas) {
                                System.out.println("   -> " + e);
                            }
                        }
                        break;
                    case 9:
                        List<Intervalo> planeadas = gestorFrota.planearEntregas();
                        System.out.println("\n--- JANELAS SELECIONADAS SEM SOBREPOSIÇÃO (" + planeadas.size() + ") ---");
                        for (Intervalo i : planeadas) {
                            System.out.println("  [" + i.getInicio().format(fmt) + " -> " + i.getFim().format(fmt) + "]");
                        }
                        break;
                    case 10:
                        adicionarIntervaloMenu(sc, gestorFrota);
                        break;
                    case 11:
                        navegarHistoricoMenu(sc, gestorHistorico);
                        break;
                    case 12:
                        System.out.println(gestorHistorico.desfazer()
                                ? "Operação desfeita (UNDO) com sucesso!"
                                : "Não há operações para desfazer.");
                        break;
                    case 13:
                        System.out.println(gestorHistorico.refazer()
                                ? "Operação refeita (REDO) com sucesso!"
                                : "Não há operações para refazer.");
                        break;
                    case 0:
                        // Pergunta antes de gravar, para não alterar os ficheiros de exemplo sem intenção
                        System.out.print("Guardar os dados antes de sair? (S/N): ");
                        if (sc.hasNextLine() && sc.nextLine().trim().equalsIgnoreCase("S")) {
                            guardarDados(gestorEncomendas, gestorFrota);
                            System.out.println("Dados guardados.");
                        }
                        System.out.println("A sair...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
    }

    private static void registarEncomendaMenu(Scanner sc, GestorEncomendas gestorEncomendas, GestorFrota gestorFrota) {
        try {
            System.out.print("ID da Encomenda: ");
            String id = sc.nextLine().trim();
            if (gestorFrota.procurarEncomendaAtribuida(id) != null) {
                System.out.println("Erro: Já existe uma encomenda com esse ID.");
                return;
            }
            System.out.print("Peso (kg): ");
            double peso = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Volume (m3): ");
            double volume = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Urgência (1 a 5): ");
            int urgencia = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Taxa paga (€): ");
            double taxa = Double.parseDouble(sc.nextLine().trim());

            // A prioridade é calculada pelos critérios do grupo (ver CalculadoraPrioridade)
            LocalDateTime agora = LocalDateTime.now().withSecond(0).withNano(0);
            int prioridade = CalculadoraPrioridade.calcular(urgencia, taxa, agora, agora);
            Encomenda nova = new Encomenda(id, peso, volume, agora, prioridade);
            if (gestorEncomendas.registarEncomenda(nova)) {
                System.out.println("Encomenda registada com sucesso! Prioridade calculada: " + prioridade);
            } else {
                System.out.println("Erro: Já existe uma encomenda com esse ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void adicionarVeiculoMenu(Scanner sc, GestorFrota gestorFrota) {
        try {
            System.out.print("ID do Veículo: ");
            String id = sc.nextLine().trim();
            System.out.print("Peso Máximo (kg): ");
            double pesoMax = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Volume Máximo (m3): ");
            double volMax = Double.parseDouble(sc.nextLine().trim());

            if (gestorFrota.adicionarVeiculo(new Veiculo(id, pesoMax, volMax))) {
                System.out.println("Veículo adicionado com sucesso!");
            } else {
                System.out.println("Erro: ID de veículo já existente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void adicionarIntervaloMenu(Scanner sc, GestorFrota gestorFrota) {
        try {
            System.out.print("Início (YYYY-MM-DDTHH:MM): ");
            LocalDateTime inicio = LocalDateTime.parse(sc.nextLine().trim());
            System.out.print("Fim (YYYY-MM-DDTHH:MM): ");
            LocalDateTime fim = LocalDateTime.parse(sc.nextLine().trim());
            if (gestorFrota.adicionarIntervalo(new Intervalo(inicio, fim))) {
                System.out.println("Intervalo registado!");
            } else {
                System.out.println("Erro: o fim tem de ser posterior ao início.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Formato inválido. Exemplo: 2026-10-26T14:00");
        }
    }

    private static void navegarHistoricoMenu(Scanner sc, GestorHistorico gestorHistorico) {
        String cmd = "";
        while (!cmd.equalsIgnoreCase("V")) {
            List<String> linhas = gestorHistorico.listarHistoricoFormatado();
            if (linhas.isEmpty()) {
                System.out.println("O histórico está vazio.");
                return;
            }
            for (String l : linhas) {
                System.out.println(l);
            }
            System.out.print("[A] Anterior | [S] Seguinte | [I] Início | [F] Fim | [V] Voltar: ");
            cmd = sc.nextLine().trim().toUpperCase();
            if (cmd.equals("A")) gestorHistorico.navegarAnterior();
            else if (cmd.equals("S")) gestorHistorico.navegarSeguinte();
            else if (cmd.equals("I")) gestorHistorico.irParaInicio();
            else if (cmd.equals("F")) gestorHistorico.irParaFim();
        }
    }

    private static File ficheiro(String nome) {
        return new File(pastaDados, nome);
    }

    private static void garantirFicheirosExemplo() {
        if (!pastaDados.exists() && !pastaDados.mkdirs()) {
            System.out.println("Aviso: não foi possível criar a pasta " + pastaDados.getPath());
            return;
        }

        File fEnc = ficheiro(FICHEIRO_ENCOMENDAS);
        if (!fEnc.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fEnc))) {
                pw.println("# id;peso;volume;tempoChegada;prioridade;veiculo (vazio = pendente)");
                pw.println("ENC01;12.5;0.40;2026-10-26T08:00;95;");
                pw.println("ENC02;25.0;1.20;2026-10-26T08:15;120;");
                pw.println("ENC03;8.0;0.25;2026-10-26T08:30;70;");
                pw.println("ENC04;45.0;2.10;2026-10-26T09:00;110;");
                pw.println("ENC05;15.0;0.60;2026-10-26T09:10;85;");
                pw.println("ENC06;5.5;0.15;2026-10-26T09:20;130;");
            } catch (IOException e) {
                System.out.println("Aviso: não foi possível criar " + fEnc.getPath() + " (" + e.getMessage() + ")");
            }
        }

        File fVeic = ficheiro(FICHEIRO_VEICULOS);
        if (!fVeic.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fVeic))) {
                pw.println("# id;pesoMaximo;volumeMaximo");
                pw.println("V01;60.0;2.50");
                pw.println("V02;120.0;5.00");
            } catch (IOException e) {
                System.out.println("Aviso: não foi possível criar " + fVeic.getPath() + " (" + e.getMessage() + ")");
            }
        }

        File fInt = ficheiro(FICHEIRO_INTERVALOS);
        if (!fInt.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fInt))) {
                pw.println("# inicio;fim");
                pw.println("2026-10-26T08:00;2026-10-26T10:00");
                pw.println("2026-10-26T09:00;2026-10-26T12:00");
                pw.println("2026-10-26T10:00;2026-10-26T11:30");
                pw.println("2026-10-26T11:00;2026-10-26T13:30");
                pw.println("2026-10-26T11:30;2026-10-26T13:00");
                pw.println("2026-10-26T13:00;2026-10-26T15:00");
            } catch (IOException e) {
                System.out.println("Aviso: não foi possível criar " + fInt.getPath() + " (" + e.getMessage() + ")");
            }
        }
    }

    /**
     * Lê os ficheiros linha a linha. Uma linha inválida é ignorada e reportada,
     * sem interromper a leitura das restantes. Os veículos são lidos primeiro
     * para permitir repor as encomendas que lhes estavam atribuídas.
     */
    private static void carregarDados(GestorEncomendas gestorEncomendas, GestorFrota gestorFrota) {
        for (String[] p : lerLinhas(FICHEIRO_VEICULOS, 3)) {
            try {
                Veiculo v = new Veiculo(p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim()));
                if (!gestorFrota.adicionarVeiculo(v)) {
                    System.out.println("Aviso: " + FICHEIRO_VEICULOS + ": veículo repetido " + p[0].trim());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Aviso: " + FICHEIRO_VEICULOS + ": veículo " + p[0].trim() + " ignorado (" + e.getMessage() + ")");
            }
        }

        for (String[] p : lerLinhas(FICHEIRO_ENCOMENDAS, 5)) {
            try {
                Encomenda e = new Encomenda(p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim()),
                        LocalDateTime.parse(p[3].trim()), Integer.parseInt(p[4].trim()));
                String idVeiculo = p.length > 5 ? p[5].trim() : "";

                if (gestorFrota.procurarEncomendaAtribuida(e.getId()) != null) {
                    System.out.println("Aviso: " + FICHEIRO_ENCOMENDAS + ": encomenda repetida " + e.getId());
                } else if (idVeiculo.isEmpty()) {
                    if (!gestorEncomendas.carregarEncomenda(e)) {
                        System.out.println("Aviso: " + FICHEIRO_ENCOMENDAS + ": encomenda repetida " + e.getId());
                    }
                } else {
                    Veiculo v = gestorFrota.procurarVeiculo(idVeiculo);
                    if (v == null || !v.adicionarEncomenda(e)) {
                        System.out.println("Aviso: " + FICHEIRO_ENCOMENDAS + ": " + e.getId()
                                + " não pôde ser reposta no veículo " + idVeiculo);
                    }
                }
            } catch (IllegalArgumentException | DateTimeParseException ex) {
                System.out.println("Aviso: " + FICHEIRO_ENCOMENDAS + ": encomenda " + p[0].trim() + " ignorada (" + ex.getMessage() + ")");
            }
        }

        for (String[] p : lerLinhas(FICHEIRO_INTERVALOS, 2)) {
            try {
                if (!gestorFrota.adicionarIntervalo(new Intervalo(LocalDateTime.parse(p[0].trim()), LocalDateTime.parse(p[1].trim())))) {
                    System.out.println("Aviso: " + FICHEIRO_INTERVALOS + ": janela com fim anterior ou igual ao início ignorada");
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Aviso: " + FICHEIRO_INTERVALOS + ": data inválida (" + ex.getParsedString() + ")");
            }
        }
    }

    // Devolve os campos de cada linha de dados, ignorando linhas vazias, comentários e linhas incompletas
    private static List<String[]> lerLinhas(String nome, int camposMinimos) {
        List<String[]> linhas = new ArrayList<>();
        File f = ficheiro(nome);
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String l;
            int numero = 0;
            while ((l = br.readLine()) != null) {
                numero++;
                if (l.trim().isEmpty() || l.startsWith("#")) continue;
                String[] p = l.split(";", -1);
                if (p.length < camposMinimos) {
                    System.out.println("Aviso: " + nome + ", linha " + numero + ": campos em falta");
                    continue;
                }
                linhas.add(p);
            }
        } catch (IOException e) {
            System.out.println("Aviso: não foi possível ler " + f.getPath() + " (" + e.getMessage() + ")");
        }
        return linhas;
    }

    private static void guardarDados(GestorEncomendas gestorEncomendas, GestorFrota gestorFrota) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro(FICHEIRO_ENCOMENDAS)))) {
            pw.println("# id;peso;volume;tempoChegada;prioridade;veiculo (vazio = pendente)");
            for (Encomenda e : gestorEncomendas.listarEncomendasPorPrioridade()) {
                pw.printf("%s;%.2f;%.2f;%s;%d;%n",
                        e.getId(), e.getPeso(), e.getVolume(), e.getTempoChegada().toString(), (int) e.getPrioridade());
            }
            // As encomendas já atribuídas também são guardadas, com o veículo correspondente
            for (Veiculo v : gestorFrota.getVeiculos()) {
                for (Encomenda e : v.getEncomendasAtribuidas()) {
                    pw.printf("%s;%.2f;%.2f;%s;%d;%s%n",
                            e.getId(), e.getPeso(), e.getVolume(), e.getTempoChegada().toString(), (int) e.getPrioridade(), v.getId());
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao guardar " + FICHEIRO_ENCOMENDAS + ": " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro(FICHEIRO_VEICULOS)))) {
            pw.println("# id;pesoMaximo;volumeMaximo");
            for (Veiculo v : gestorFrota.getVeiculos()) {
                pw.printf("%s;%.2f;%.2f%n", v.getId(), v.getPesoMaximo(), v.getVolumeMaximo());
            }
        } catch (IOException e) {
            System.out.println("Erro ao guardar " + FICHEIRO_VEICULOS + ": " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro(FICHEIRO_INTERVALOS)))) {
            pw.println("# inicio;fim");
            for (Intervalo i : gestorFrota.getJanelasEntrega()) {
                pw.printf("%s;%s%n", i.getInicio().toString(), i.getFim().toString());
            }
        } catch (IOException e) {
            System.out.println("Erro ao guardar " + FICHEIRO_INTERVALOS + ": " + e.getMessage());
        }
    }
}
