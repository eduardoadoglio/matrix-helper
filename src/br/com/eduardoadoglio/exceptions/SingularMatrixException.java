package br.com.eduardoadoglio.exceptions;

public class SingularMatrixException extends RuntimeException {
    private int zeroRow = 0;

    public SingularMatrixException(String errorMessage) {
        super(errorMessage);
    }

    public SingularMatrixException(String errorMessage, int zeroRow) {
        super(errorMessage);
        this.zeroRow = zeroRow;
    }

    public int getZeroRow() {
        return this.zeroRow;
    }
}
