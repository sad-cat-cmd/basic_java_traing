
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
public class ClientPingThread extends Thread{
    private PrintWriter out;
    private BufferedReader in;
    private boolean status = false;
    private String info = "";
    private String pingMsg = "check";
    private String strResultPing = "";
    private int idClient;
    
    public ClientPingThread(PrintWriter objPrintWriter, BufferedReader objBufferedReader, int IdClient) {
        out = objPrintWriter;
        in = objBufferedReader;
        idClient = IdClient;
    }
    @Override
    public void run() {
        out.println(pingMsg);
        out.flush();
        try {
            strResultPing = in.readLine();
            if (strResultPing == null) {
                info = "Соединение разорвано";
                return;
            }
            if (strResultPing.equals("stop")) {
                info = "Запрос на разрыв соединения";
                return;
            }
            if (strResultPing.equals("ready")) {
                status = true;
                info = "Готовность к вычислениям";
                return;
            }
        }
        catch (IOException IOexc) {
            info = "Соединение разорвано аварийно";
            return;
        }
    }
    public boolean getStatus () {
        return status;
    }
    public String getInfo () {
        return info;
    }
    public int getIdClient () {
        return idClient;
    }
    
}
