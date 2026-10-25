package Servico;

import Algoritmo.Empacotador;
import Algoritmo.SelecaoIntervalos;
import Estrutura.HeapBinario;
import Modelo.Encomenda;
import Modelo.Intervalo;
import Modelo.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class GestorFrota {
    private List<Veiculo> veiculos;
    private List<Intervalo> janelasEntrega;
    private Empacotador empacotador;
    private SelecaoIntervalos selecaoIntervalos;
    private GestorHistorico gestorHistorico;

    public GestorFrota(GestorHistorico gestorHistorico) {
        this.veiculos = new ArrayList<>();
        this.janelasEntrega = new ArrayList<>();
        this.empacotador = new Empacotador();
        this.selecaoIntervalos = new SelecaoIntervalos();
        this.gestorHistorico = gestorHistorico;
    }

    public boolean adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null || procurarVeiculo(veiculo.getId()) != null) {
            return false;
        }
        veiculos.add(veiculo);
        return true;
    }

    public Veiculo procurarVeiculo(String id) {
        for (Veiculo v : veiculos) {
            if (v.getId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }

    public List<Encomenda> carregarVeiculo(String idVeiculo, HeapBinario heap) {
        Veiculo veiculo = procurarVeiculo(idVeiculo);
        if (veiculo == null) {
            return new ArrayList<>();
        }

        List<Encomenda> carregadas = empacotador.carregarVeiculo(heap, veiculo);

        for (Encomenda enc : carregadas) {
            if (gestorHistorico != null) {
                gestorHistorico.registarOperacao(new OperacaoAtribuicao(enc, veiculo, heap));
            }
        }

        return carregadas;
    }

    public void adicionarIntervalo(Intervalo intervalo) {
        if (intervalo != null && intervalo.getInicio().isBefore(intervalo.getFim())) {
            janelasEntrega.add(intervalo);
        }
    }

    public List<Intervalo> planearEntregas() {
        return selecaoIntervalos.otimizarJanelas(janelasEntrega);
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public List<Intervalo> getJanelasEntrega() {
        return janelasEntrega;
    }
}