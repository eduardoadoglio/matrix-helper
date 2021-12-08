package br.com.eduardoadoglio;

import br.com.eduardoadoglio.exceptions.SingularMatrixException;

import java.util.Locale;
import java.util.Scanner;

public class EP1 {

    // metodo principal.

    public static void main(String [] args){
        Locale.setDefault(Locale.US);
        Scanner in = new Scanner(System.in);	// Scanner para facilitar a leitura de dados a partir da entrada padrao.
        String operation = in.next();		// le, usando o scanner, a string que determina qual operacao deve ser realizada.
        int matrixDimension = in.nextInt();			// lê a dimensão da matriz a ser manipulada pela operacao escolhida.
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
            double[] solution = matrix.solveMatrix(agg);
            for(double value: solution){
                System.out.printf("%.2f\n", value);
            }
        }
        else if("inverte".equals(operation)){
            Matriz matrix = new Matriz(matrixDimension, matrixDimension);
            for(int i = 0; i < matrixDimension; i++){
                for(int j = 0; j < matrixDimension; j++) {
                    matrix.set(i, j, in.nextDouble());
                }
            }
            Matriz identidade = Matriz.identidade(matrixDimension);
            try {
                matrix.formaEscalonadaReduzida(identidade);
                identidade.imprime();
            } catch(SingularMatrixException e) {
                System.out.println("matriz singular");
            }
        }
        else if("determinante".equals(operation)){
            Matriz matrix = new Matriz(matrixDimension, matrixDimension);
            for(int i = 0; i < matrixDimension; i++){
                for(int j = 0; j < matrixDimension; j++) {
                    matrix.set(i, j, in.nextDouble());
                }
            }
            double determinant = matrix.formaEscalonada(null);
            System.out.printf("%.2f", determinant);
        }
        else {
            System.out.println("Operação desconhecida!");
            System.exit(1);
        }
    }
}
