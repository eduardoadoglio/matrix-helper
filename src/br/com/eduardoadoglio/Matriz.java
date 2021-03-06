package br.com.eduardoadoglio;

import br.com.eduardoadoglio.exceptions.SingularMatrixException;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public double[] solveMatrix(Matriz agregada) {
        double determinante = this.formaEscalonada(agregada);
        if(determinante == 0) {
            int zeroRow = this.getZeroRow();
            if(agregada.m[zeroRow][0] == 0) {
                System.out.println("sistema possui diversas solu????es");
            }else {
                System.out.println("sistema sem solu????o");
            }
            return new double[] {};
        } else {
            return this.backSubstitution(agregada);
        }
    }

    private int getZeroRow() {
        for (int i = 0; i < this.lin; i++) {
            int[] pivotInfo = encontraLinhaPivo(i);
            int pivotRow = pivotInfo[0];
            int pivotCol = pivotInfo[1];
            if(pivotRow == this.lin || this.m[pivotRow][pivotCol] == 0){
                return i;
            }
        }
        return -1;
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

        double determinant = 1.0;
        double sign = 1.0;
        
        int N = agregada.lin;
        // Loopando por cada linha da matriz, onde k ?? o numero da linha
        for (int k = 0; k < N; k++) {
            // Encontramos a linha e coluna do pr??ximo piv??
            int[] pivotInfo = encontraLinhaPivo(k);
            int pivotRow = pivotInfo[0];
            int pivotCol = pivotInfo[1];

            // Se o piv?? for 0 ou n??o encontrado
            // (nesse caso, assumindo o valor default do numero de linhas),
            // sabemos que a matriz ?? singular
            if(pivotRow == this.lin || this.m[pivotRow][pivotCol] == 0){
                // O determinante de uma matriz singular ?? zero
                return 0;
                // throw new SingularMatrixException("Matriz singular", pivotInfo[0] - 1);
            }

            // Se a linha do piv?? for diferente da linha atual, trocar as duas linhas
            // e inverter o sinal da determinante
            if(pivotRow != k) {
                trocaLinha(k, pivotRow);
                agregada.trocaLinha(k, pivotRow);
                sign *= -1;
            }


            // Fazendo todos os elementos abaixo do piv?? ser zero
            // i sendo a linha do elemento a ser zerado
            for (int i = k + 1; i < N; i++) {
                // Procuramos um n??mero que, ao ser multiplicado pelo elemento k, k e
                // subtraindo o resultado disso ao elemento i, k, obtemos zero
                double factor = this.m[i][k] / this.m[k][k];
                // Multiplicamos esse n??mero por todos os elementos da linha k e subtra??mos esse resultado
                // de todos os elementos da linha i, tanto da matriz original quanto da agregada
                for (int j = 0; j < agregada.col; j++) {
                    agregada.m[i][j] -= factor * agregada.m[k][j];
                }
                for (int j = k; j < N; j++) {
                    this.m[i][j] -= factor * this.m[k][j];
                }
            }
        }
        for (int i = 0; i < N; i++) {
            determinant *= this.m[i][i];
        }
        determinant *= sign;
        return determinant;
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
    // a matriz que invoca esta metodo eh uma matriz quadrada. N??o se pode assumir, contudo, que esta
    // matriz ja esteja na forma escalonada (mas voce pode usar o metodo acima para isso).

    public void formaEscalonadaReduzida(Matriz agregada){
        if(agregada == null){
            agregada = Matriz.identidade(this.lin);
        }
        // Garantir que a matriz est?? na forma escalonada
        double determinante = formaEscalonada(agregada);

        if(determinante == 0) {
            throw new SingularMatrixException("matriz singular");
        }

        for (int i = this.lin - 1; i > 0 ; i--) {
            if(this.m[i - 1][0] < this.m[i][0]) {
                trocaLinha(i - 1, i);
                agregada.trocaLinha(i - 1, i);
            }
        }

        for (int i = 0; i < this.lin; i++) {
            for (int j = 0; j < this.lin; j++) {
                if(i != j) {
                    double temp = this.m[j][i] / this.m[i][i];
                    for (int k = 0; k < this.lin; k++) {
                        this.m[j][k] -= this.m[i][k] * temp;
                        agregada.m[j][k] -= agregada.m[i][k] * temp;
                    }
                }
            }
        }
        for (int i = 0; i < this.lin; i++) {
            double temp = this.m[i][i];
            for (int j = 0; j < this.lin; j++) {
                this.m[i][j] = this.round(this.m[i][j] / temp, 7);
                agregada.m[i][j] = this.round(agregada.m[i][j] / temp, 7);;
            }
        }

    }

    private double round(double value, int places) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}