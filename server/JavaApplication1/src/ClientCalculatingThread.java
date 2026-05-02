
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class ClientCalculatingThread extends Thread{
    private PrintWriter out;
    private BufferedReader in;
    private String strStatusCalculating;
    private double minX;
    private double maxX;
    private double step;
    private double integral;
    private TimeInterval timeControler;
    
    private double getValueResult (String inStr) {
        return Double.parseDouble(inStr);
    }
    private String getStrForSend() {
        return Double.toString(minX) + "\\" + Double.toString(maxX) + "\\" + Double.toString(step);
    }
    public ClientCalculatingThread (double MinX, double MaxX, double Step,
                                    PrintWriter objPrintWriter, BufferedReader objBufferedReader){
        timeControler = new TimeInterval();
        minX = MinX;
        maxX = MaxX;
        step = Step;
        out = objPrintWriter;
        in = objBufferedReader;
    }
    @Override
    public void run() {
        String outStr = getStrForSend();
        out.println(outStr);
        out.flush();
        String resultStr = "";
        try {
            resultStr = in.readLine();
        }
        catch (IOException exc){
            strStatusCalculating = "\t\tОшибка для потока взаимодействия с клиентом.\n\t\t Сокет закрыт аварийно";
            integral = 0.0;
            return;
        }
        if (resultStr == null) {
            strStatusCalculating = "\t\tОшибка для потока взаимодействия с клиентом.\n\t\t Сокет был закрыт";
            integral = 0.0;
            return;
        }
        integral = getValueResult(resultStr);
        long timeCalc = timeControler.getTimeIntervalMs();
        strStatusCalculating = "\t\t Интеграл посчитан корректно. \n\t\t Время выполнения : " + String.valueOf(timeCalc) + " ms";
        return;
    }
    public double getIntegral() {
        return integral;
    }
    public String getStrStatusCalculating(){
        return strStatusCalculating;
    }
}
