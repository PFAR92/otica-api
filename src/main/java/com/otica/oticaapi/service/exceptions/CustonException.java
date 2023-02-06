package com.otica.oticaapi.service.exceptions;

public class CustonException extends RuntimeException{
    
    /* O controle é necessário porque um .class pode ter sofrido alterações e 
    ainda assim se manter compatível com sua versão anterior. */
    private static final long serialVersionUID = 1L;

    public CustonException(String message){
        super(message);
    }
}
