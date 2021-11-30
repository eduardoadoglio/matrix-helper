package br.com.eduardoadoglio;

import java.util.Scanner;

public class EP1 {

    // metodo principal.

    public static void main(String [] args){

        Scanner in = new Scanner(System.in);	// Scanner para facilitar a leitura de dados a partir da entrada padrao.
        System.out.println("Qual operação será realizada?");
        System.out.println("| resolve | inverte | determinante |");
        String operation = in.next();		// le, usando o scanner, a string que determina qual operacao deve ser realizada.
        System.out.println("Qual é a dimensão da matriz?");
        int matrixDimension = in.nextInt();			// lê a dimensão da matriz a ser manipulada pela operacao escolhida.
        System.out.println("Insira os elementos da matriz:");
        if("resolve".equals(operation)){
            Matriz matrix = new Matriz(matrixDimension, matrixDimension);
            Matriz agg = new Matriz(matrixDimension, 1);
            for(int i = 0; i < matrixDimension; i++){
                for(int j = 0; j < matrixDimension + 1; j++) {
                    if (j == matrixDimension) {
                        agg.set(i, 0, in.nextDouble());
                    } else {
                        matrix.set(i, j, in.nextDouble());
                    }
                }
            }
            matrix.formaEscalonada(agg);
        }
        else if("inverte".equals(operation)){

        }
        else if("determinante".equals(operation)){

        }
        else {
            System.out.println("Operação desconhecida!");
            System.exit(1);
        }
    }
}
