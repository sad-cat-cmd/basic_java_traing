
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class ClientStopJoinDataThread extends Thread {
    private PrintWriter out;
    private BufferedReader in;
    private String strAddedInfoStatusStoping = "";
    
    public ClientStopJoinDataThread(PrintWriter objPrintWriter, BufferedReader objBufferedReader) {
        out = objPrintWriter;
        in = objBufferedReader;
    }
    
    @Override
    public void run() {
        String strMsg = "stopJoin";
        String inStr = "";
        out.println(strMsg);
        out.flush();
        try {
            inStr = in.readLine();
        }
        catch (IOException excI) {
            strAddedInfoStatusStoping += "Сообщение об окончании ожидания даных отправлено. Сокет был закрыт со стороны клиента (аварийно)";
            return;
        }
        if (inStr == null) {
            strAddedInfoStatusStoping += "Сообщение об окончании ожидания даных отправлено. Сокет был закрыт со стороны клиента (нормально)";
        }
        strAddedInfoStatusStoping = "Сообщение об окончании ожидания даных отправлено. Сокет открыт";
        return;
    }
    
    public String getStrAddedInfoStatusStoping () {
        return strAddedInfoStatusStoping;
    } 
}
