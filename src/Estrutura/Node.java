package Estrutura;

import Modelo.Operacao;

public class Node {
    private Operacao elemento;
    private Node seguinte;
    private Node anterior;

    public Node(Operacao elemento) {
        this.elemento = elemento;
        this.seguinte = null;
        this.anterior = null;
    }

    public Node(Operacao elemento, Node seguinte, Node anterior) {
        this.elemento = elemento;
        this.seguinte = seguinte;
        this.anterior = anterior;
    }

    public Operacao getElemento() {
        return elemento;
    }

    public void setElemento(Operacao elemento) {
        this.elemento = elemento;
    }

    public Node getAnterior() {
        return anterior;
    }

    public void setAnterior(Node anterior) {
        this.anterior = anterior;
    }

    public Node getSeguinte() {
        return seguinte;
    }

    public void setSeguinte(Node seguinte) {
        this.seguinte = seguinte;
    }
}