package br.com.meli.projetointegrador.exception;

public class SectionIsAlreadyInUse extends RuntimeException{
    public SectionIsAlreadyInUse(String message) {
        super(message);
    }
}
