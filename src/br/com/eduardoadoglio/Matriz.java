package br.com.eduardoadoglio;

import java.util.*;

// classe que representa uma matriz de valores do tipo double.

class Matriz {

    // constante para ser usada na comparacao de valores double.
    // Se a diferenca absoluta entre dois valores double for menor
    // do que o valor definido por esta constante, eles devem ser
    // considerados iguais.
    public static final double SMALL = 0.000001;

    private int lin, col;
    private double [][] m;

    // metodo estatico que cria uma matriz identidade de tamanho n x n.

    public static Matriz identidade(int n){

        Matriz mat = new Matriz(n, n);
        for(int i = 0; i < mat.lin; i++) mat.m[i][i] = 1;
        return mat;
    }

    // construtor que cria uma matriz de n linhas por m colunas com todas as entradas iguais a zero.

    public Matriz(int n, int m){

        this.lin = n;
        this.col = m;
        this.m = new double[lin][col];
    }

    public void set(int i, int j, double valor){
        m[i][j] = valor;
    }

    public double get(int i, int j){

        return m[i][j];
    }

    // metodo que imprime as entradas da matriz.

    public void imprime(){

        for(int i = 0; i < lin; i++){

            for(int j = 0; j < col; j++){

                System.out.printf("%7.2f ", m[i][j]);
            }

            System.out.println();
        }
    }

    // metodo que imprime a matriz expandida formada pela combinacao da matriz que
    // chama o metodo com a matriz "agregada" recebida como parametro. Ou seja, cada
    // linha da matriz impressa possui as entradas da linha correspondente da matriz
    // que chama o metodo, seguida das entradas da linha correspondente em "agregada".

    public void imprime(Matriz agregada){

        for(int i = 0; i < lin; i++){

            for(int j = 0; j < col; j++){

                System.out.printf("%7.2f ", m[i][j]);
            }

            System.out.print(" |");

            for(int j = 0; j < agregada.col; j++){

                System.out.printf("%7.2f ", agregada.m[i][j]);
            }

            System.out.println();
        }
    }

    // metodo que troca as linhas i1 e i2 de lugar.

    private void trocaLinha(int i1, int i2){
        double[] temp = this.m[i1];
        this.m[i1] = this.m[i2];
        this.m[i2] = temp;
    }

    // metodo que multiplica as entradas da linha i pelo escalar k

    private void multiplicaLinha(int i, double k){
        for (int j = 0; j < this.m[i].length; j++) {
            this.m[i][j] = this.m[i][j] * k;
        }
    }

    // metodo que faz a seguinte combinacao de duas linhas da matriz:
    //
    // 	(linha i1) = (linha i1) + (linha i2 * k)
    //

    private void combinaLinhas(int i1, int i2, double k){
        for (int i = 0; i < this.m[i1].length; i++) {
            this.m[i1][i] += (this.m[i2][i] * k);
        }
    }

    // metodo que procura, a partir da linha ini, a linha com uma entrada nao nula que
    // esteja o mais a esquerda possivel dentre todas as linhas. Os indices da linha e da
    // coluna referentes a entrada nao nula encontrada sao devolvidos como retorno do metodo.
    // Este metodo ja esta pronto para voces usarem na implementacao da eliminacao gaussiana
    // e eleminacao de Gauss-Jordan.

    private int [] encontraLinhaPivo(int ini){

        int pivo_col, pivo_lin;

        pivo_lin = lin;
        pivo_col = col;

        for(int i = ini; i < lin; i++){

            int j;

            for(j = 0; j < col; j++) if(Math.abs(m[i][j]) > 0) break;

            if(j < pivo_col) {

                pivo_lin = i;
                pivo_col = j;
            }
        }

        return new int [] { pivo_lin, pivo_col };
    }

    // metodo que implementa a eliminacao gaussiana, que coloca a matriz (que chama o metodo)
    // na forma escalonada. As operacoes realizadas para colocar a matriz na forma escalonada
    // tambem devem ser aplicadas na matriz "agregada" caso esta seja nao nula. Este metodo
    // tambem deve calcular e devolver o determinante da matriz que invoca o metodo. Assumimos
    // que a matriz que invoca este metodo eh uma matriz quadrada.

    public double formaEscalonada(Matriz agregada){
        if(agregada == null){
            agregada = Matriz.identidade(this.lin);
        }
        int N = agregada.lin;

        for (int k = 0; k < N; k++) {
            int[] pivotInfo = encontraLinhaPivo(k);

            trocaLinha(k, pivotInfo[0]);
            agregada.trocaLinha(k, pivotInfo[0]);

            for (int i = k + 1; i < N; i++) {
                double factor = this.m[i][k] / this.m[k][k];
                agregada.m[i][0] -= factor * agregada.m[k][0];
                for (int j = k; j < N; j++) {
                    //combinaLinhas(i, j, -factor);
                    this.m[i][j] -= factor * this.m[k][j];
                }
            }
        }
        double[] solutions = backSubstitution(agregada);
        for (double solution: solutions) {
            System.out.printf("%.1f\n", solution);
        }
        return 0.0;
    }

    private double[] backSubstitution(Matriz agregada){
        int numUnknowns = agregada.lin;
        double[] solution = new double[numUnknowns];
        for (int i = numUnknowns - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < numUnknowns; j++) {
                sum += this.m[i][j] * solution[j];
            }
            solution[i] = (agregada.m[i][0] - sum) / this.m[i][i];
        }
        return solution;
    }

    // metodo que implementa a eliminacao de Gauss-Jordan, que coloca a matriz (que chama o metodo)
    // na forma escalonada reduzida. As operacoes realizadas para colocar a matriz na forma escalonada
    // reduzida tambem devem ser aplicadas na matriz "agregada" caso esta seja nao nula. Assumimos que
    // a matriz que invoca esta metodo eh uma matriz quadrada. Não se pode assumir, contudo, que esta
    // matriz ja esteja na forma escalonada (mas voce pode usar o metodo acima para isso).

    public void formaEscalonadaReduzida(Matriz agregada){

        // TODO: implementar este metodo.
    }
}