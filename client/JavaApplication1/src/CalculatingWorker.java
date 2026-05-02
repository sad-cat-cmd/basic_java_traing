
import java.util.LinkedList;
import javax.swing.SwingWorker;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin_
 */

public class CalculatingWorker {
    private TimeControler objTimeContoler;
    private int countThread = 4;
    private double minX;
    private double maxX;
    private double [] arrMinX;
    private double [] arrMaxX;
    private double step;
    private MathThread [] listThread;
    private double resultArea;
    private long timeInterval;
    private long timeIntervalPrevCalculating;
    private long timeIntervalCreatingThread;
    private long timeIntervalCalculating;
    
    public CalculatingWorker(double PminX, double PmaxX, double Pstep) {
        objTimeContoler = new TimeControler();
        minX = PminX;
        maxX = PmaxX;
        step = Pstep;
        listThread = new MathThread[countThread];
        arrMinX = new double[countThread];
        arrMaxX = new double[countThread];
        
        return;
    }
    public double operation() {
        TimeControler _objTimeContoler = new TimeControler();
        double difCalculating = (maxX - minX) / countThread;
        double localMinX = minX;
        double localMaxX = minX;
        for (int i = 0; i < countThread; i++) {
            localMaxX += difCalculating;
            arrMinX[i] = localMinX;
            arrMaxX[i] = localMaxX;
            localMinX = localMaxX;
        }
        timeIntervalPrevCalculating = _objTimeContoler.getTimeIntervalNs();
        _objTimeContoler = new TimeControler();
        for (int i = 0; i < countThread; i++) {
            MathThread newThread = new MathThread(arrMinX[i], arrMaxX[i], step);
            listThread[i] = newThread;
            listThread[i].start();
        }
        timeIntervalCreatingThread = _objTimeContoler.getTimeIntervalNs();
        try {
            _objTimeContoler = new TimeControler();
            for (int i = 0; i < countThread; i++){
                listThread[i].join();
                resultArea += listThread[i].get_result();
            }
            timeIntervalCalculating = _objTimeContoler.getTimeIntervalNs();
        }
        catch(InterruptedException exc) {
            System.out.println("Один из потоков был прерван. Перезупустите приложение");
        }
        finally {
            System.out.println("Первичные вычисления: " + timeIntervalPrevCalculating + " Создание нитей: " + timeIntervalCreatingThread + " Вычисления: " + timeIntervalCalculating);
            timeInterval = objTimeContoler.getTimeIntervalNs();
            return resultArea;
        }
    }
    public long getTimeCalculating () {return timeInterval;}
    public double getResult () {return resultArea;}
    
}
