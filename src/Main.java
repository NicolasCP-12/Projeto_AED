import Modelo.Encomenda;
import Modelo.Intervalo;
import Modelo.Veiculo;
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
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final String FICHEIRO_ENCOMENDAS = "encomendas.txt";
    private static final String FICHEIRO_VEICULOS = "veiculos.txt";
    private static final String FICHEIRO_INTERVALOS = "intervalos.txt";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        // Os gestores partilham uma única instância de GestorHistorico (Secção 2 do Relatório)
        GestorHistorico gestorHistorico = new GestorHistorico();
        GestorEncomendas gestorEncomendas = new GestorEncomendas(gestorHistorico);
        GestorFrota gestorFrota = new GestorFrota(gestorHistorico);

        garantirFicheirosExemplo();
        carregarDados(gestorEncomendas, gestorFrota);

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
                System.out.println("0. Guardar dados e Sair");
                System.out.print("Escolha uma opção: ");

                try {
                    opcao = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    opcao = -1;
                }

                switch (opcao) {
                    case 1:
                        registarEncomendaMenu(sc, gestorEncomendas);
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
                        guardarDados(gestorEncomendas, gestorFrota);
                        System.out.println("Dados guardados. A sair...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        }
    }

    private static void registarEncomendaMenu(Scanner sc, GestorEncomendas gestorEncomendas) {
        try {
            System.out.print("ID da Encomenda: ");
            String id = sc.nextLine().trim();
            System.out.print("Peso (kg): ");
            double peso = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Volume (m3): ");
            double volume = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Prioridade (inteiro): ");
            int prioridade = Integer.parseInt(sc.nextLine().trim());

            Encomenda nova = new Encomenda(id, peso, volume, LocalDateTime.now(), prioridade);
            if (gestorEncomendas.registarEncomenda(nova)) {
                System.out.println("Encomenda registada com sucesso!");
            } else {
                System.out.println("Erro: Já existe uma encomenda com esse ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
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
        }
    }

    private static void adicionarIntervaloMenu(Scanner sc, GestorFrota gestorFrota) {
        try {
            System.out.print("Início (YYYY-MM-DDTHH:MM): ");
            LocalDateTime inicio = LocalDateTime.parse(sc.nextLine().trim());
            System.out.print("Fim (YYYY-MM-DDTHH:MM): ");
            LocalDateTime fim = LocalDateTime.parse(sc.nextLine().trim());
            gestorFrota.adicionarIntervalo(new Intervalo(inicio, fim));
            System.out.println("Intervalo registado!");
        } catch (Exception e) {
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

    private static void garantirFicheirosExemplo() {
        File fEnc = new File(FICHEIRO_ENCOMENDAS);
        if (!fEnc.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fEnc))) {
                pw.println("# id;peso;volume;tempoChegada;prioridade");
                pw.println("ENC01;12.5;0.40;2026-10-26T08:00;95");
                pw.println("ENC02;25.0;1.20;2026-10-26T08:15;120");
                pw.println("ENC03;8.0;0.25;2026-10-26T08:30;70");
                pw.println("ENC04;45.0;2.10;2026-10-26T09:00;110");
                pw.println("ENC05;15.0;0.60;2026-10-26T09:10;85");
                pw.println("ENC06;5.5;0.15;2026-10-26T09:20;130");
            } catch (IOException ignored) {}
        }

        File fVeic = new File(FICHEIRO_VEICULOS);
        if (!fVeic.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fVeic))) {
                pw.println("# id;pesoMaximo;volumeMaximo");
                pw.println("V01;60.0;2.50");
                pw.println("V02;120.0;5.00");
            } catch (IOException ignored) {}
        }

        File fInt = new File(FICHEIRO_INTERVALOS);
        if (!fInt.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fInt))) {
                pw.println("# inicio;fim");
                pw.println("2026-10-26T08:00;2026-10-26T10:00");
                pw.println("2026-10-26T09:00;2026-10-26T12:00");
                pw.println("2026-10-26T10:00;2026-10-26T11:30");
                pw.println("2026-10-26T11:00;2026-10-26T13:30");
                pw.println("2026-10-26T11:30;2026-10-26T13:00");
                pw.println("2026-10-26T13:00;2026-10-26T15:00");
            } catch (IOException ignored) {}
        }
    }

    private static void carregarDados(GestorEncomendas gestorEncomendas, GestorFrota gestorFrota) {
        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_ENCOMENDAS))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty() || l.startsWith("#")) continue;
                String[] p = l.split(";");
                gestorEncomendas.registarEncomenda(new Encomenda(
                        p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim()),
                        LocalDateTime.parse(p[3].trim()), Integer.parseInt(p[4].trim())));
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_VEICULOS))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty() || l.startsWith("#")) continue;
                String[] p = l.split(";");
                gestorFrota.adicionarVeiculo(new Veiculo(
                        p[0].trim(), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim())));
            }
        } catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(new FileReader(FICHEIRO_INTERVALOS))) {
            String l;
            while ((l = br.readLine()) != null) {
                if (l.trim().isEmpty() || l.startsWith("#")) continue;
                String[] p = l.split(";");
                gestorFrota.adicionarIntervalo(new Intervalo(
                        LocalDateTime.parse(p[0].trim()), LocalDateTime.parse(p[1].trim())));
            }
        } catch (Exception ignored) {}
    }

    private static void guardarDados(GestorEncomendas gestorEncomendas, GestorFrota gestorFrota) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_ENCOMENDAS))) {
            pw.println("# id;peso;volume;tempoChegada;prioridade");
            for (Encomenda e : gestorEncomendas.listarEncomendasPorPrioridade()) {
                pw.printf("%s;%.2f;%.2f;%s;%d%n",
                        e.getId(), e.getPeso(), e.getVolume(), e.getTempoChegada().toString(), (int) e.getPrioridade());
            }
        } catch (IOException ignored) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_VEICULOS))) {
            pw.println("# id;pesoMaximo;volumeMaximo");
            for (Veiculo v : gestorFrota.getVeiculos()) {
                pw.printf("%s;%.2f;%.2f%n", v.getId(), v.getPesoMaximo(), v.getVolumeMaximo());
            }
        } catch (IOException ignored) {}

        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHEIRO_INTERVALOS))) {
            pw.println("# inicio;fim");
            for (Intervalo i : gestorFrota.getJanelasEntrega()) {
                pw.printf("%s;%s%n", i.getInicio().toString(), i.getFim().toString());
            }
        } catch (IOException ignored) {}
    }
}