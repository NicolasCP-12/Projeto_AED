package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Veiculo {
    private String id;
    private double pesoMaximo;
    private double volumeMaximo;
    private List<Encomenda> encomendasAtribuidas;

    public Veiculo(String id, double pesoMaximo, double volumeMaximo) {
        this.id = id;
        this.pesoMaximo = pesoMaximo;
        this.volumeMaximo = volumeMaximo;
        this.encomendasAtribuidas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public double getPesoMaximo() {
        return pesoMaximo;
    }

    public double getVolumeMaximo() {
        return volumeMaximo;
    }

    public List<Encomenda> getEncomendasAtribuidas() {
        return encomendasAtribuidas;
    }

    public double getPesoAtual() {
        double pesoAtual = 0;
        for (Encomenda e : encomendasAtribuidas) {
            pesoAtual += e.getPeso();
        }
        return pesoAtual;
    }

    public double getVolumeAtual() {
        double volumeAtual = 0;
        for (Encomenda e : encomendasAtribuidas) {
            volumeAtual += e.getVolume();
        }
        return volumeAtual;
    }

    public boolean adicionarEncomenda(Encomenda encomenda) {
        if (getPesoAtual() + encomenda.getPeso() <= pesoMaximo &&
                getVolumeAtual() + encomenda.getVolume() <= volumeMaximo) {
            encomendasAtribuidas.add(encomenda);
            return true;
        }
        return false;
    }

    public boolean removerEncomenda(Encomenda encomenda) {
        return encomendasAtribuidas.remove(encomenda);
    }

    @Override
    public String toString() {
        return "Veiculo[" + id + " | Carga: " + getPesoAtual() + "/" + pesoMaximo + "kg | Vol: " + getVolumeAtual() + "/" + volumeMaximo + "m³]";
    }
}
