package br.com.eduardoadoglio.exceptions;

public class SingularMatrixException extends RuntimeException {
    private int zeroRow;
    public SingularMatrixException(String errorMessage, int zeroRow) {
        super(errorMessage);
        this.zeroRow = zeroRow;
    }

    public int getZeroRow() {
        return this.zeroRow;
    }
}
