/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
public class ClientException extends Exception {
    private String nameProgramUnit;
    private String msgException;
    
    public ClientException(String NameProgUnit, String msgExc) {
        nameProgramUnit = NameProgUnit;
        msgException = msgExc;
    }
    
    public String getNameProgramUnit(){return nameProgramUnit; }
    public String getMsgException() {return msgException; }
}
