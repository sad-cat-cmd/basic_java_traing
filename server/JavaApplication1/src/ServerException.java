/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */
public class ServerException extends Exception{
    private String nameProgramUnit = "";
    private String msgExc = "";
    
    public ServerException(String NameProgramUnit, String MsgExc) {
        nameProgramUnit = NameProgramUnit;
        msgExc = MsgExc;
    }
    public String getNameProgramUnit(){return nameProgramUnit;}
    public String getMsgExc(){return msgExc;}
}
