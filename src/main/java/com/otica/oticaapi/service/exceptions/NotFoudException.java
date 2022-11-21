package com.otica.oticaapi.service.exceptions;

public class NotFoudException extends RuntimeException{
    
    /* O controle é necessário porque um .class pode ter sofrido alterações e 
    ainda assim se manter compatível com sua versão anterior. */
    private static final long serialVersionUID = 1L;

    public NotFoudException(String message){
        super(message);
    }
}
