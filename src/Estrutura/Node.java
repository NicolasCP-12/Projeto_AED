package Estrutura;

public class Node {
    public int data;
    public Node seguinte;
    public Node anterior;

    public Node(int data, Node seguinte, Node anterior) {
        this.data = data;
        this.seguinte = seguinte;
        this.anterior = anterior;
    }
    public Node(int data, Node seguinte) {
        this.data = data;
        this.seguinte = seguinte;
        this.anterior = null;
    }

    public Node getAnterior() {
        return anterior;
    }

    public Node getSeguinte() {
        return seguinte;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setAnterior(Node anterior) {
        this.anterior = anterior;
    }

    public void setSeguinte(Node seguinte) {
        this.seguinte = seguinte;
    }
}
