
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
    private TimeController objTimeContoler;
    private int countThread = 4;
    private double minX;
    private double maxX;
    private double [] arrMinX;
    private double [] arrMaxX;
    private double step;
    //private LinkedList <double []> listXValue;
    //private LinkedList <double []> listStepValue;
    private MathThread [] listThread;
    private double resultArea;
    private long timeInterval;
    private long timeIntervalPrevCalculating;
    private long timeIntervalCreatingThread;
    private long timeIntervalCalculating;
    
    public CalculatingWorker(double PminX, double PmaxX, double Pstep) {
        objTimeContoler = new TimeController();
//        double approximateCountElement = (PmaxX - PminX) / Pstep / 4;
//        int countElement = (int) Math.ceil(approximateCountElement);
        minX = PminX;
        maxX = PmaxX;
        step = Pstep;
//        listXValue = new LinkedList<>();
//        listStepValue = new LinkedList<>();
        listThread = new MathThread[countThread];
        //results = new double[countThread];
//        for (int i = 0; i < countThread; i++) {
//            listXValue.add(new double[countElement]);
//            listStepValue.add(new double[countElement]);
//        }
        arrMinX = new double[countThread];
        arrMaxX = new double[countThread];
        
        return;
    }
    public double operation() {
        TimeController _objTimeContoler = new TimeController();
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
        _objTimeContoler = new TimeController();
        for (int i = 0; i < countThread; i++) {
            MathThread newThread = new MathThread(arrMinX[i], arrMaxX[i], step);
            listThread[i] = newThread;
            listThread[i].start();
        }
        timeIntervalCreatingThread = _objTimeContoler.getTimeIntervalNs();
        try {
            _objTimeContoler = new TimeController();
            for (int i = 0; i < countThread; i++){
                listThread[i].join();
                resultArea += listThread[i].get_result();
            }
            timeIntervalCalculating = _objTimeContoler.getTimeIntervalNs();
        }
        catch(InterruptedException exc) {
            System.out.println("Error joining thread");
        }
        finally {
            System.out.println("Первичные вычисления: " + timeIntervalPrevCalculating + " Создание нитей: " + timeIntervalCreatingThread + " Вычисления: " + timeIntervalCalculating);
            timeInterval = objTimeContoler.getTimeIntervalNs();
            return resultArea;
        }
    }
//    public double operation () {
//        int startIndex = 0;
//        int startIndexElement = 0 ;
//        double localStep = 0;
//        while (minX < maxX) {
//            double remaining = maxX - minX;
//            if (remaining >= step) {
//                localStep = step;
//            }
//            else {
//                if (remaining < 1e-10) {
//                    break;
//                }
//                localStep = remaining;
//            }
//            //System.out.println("minX:" + minX + " step: " + localStep);
//            listXValue.get(startIndex)[startIndexElement] = minX;
//            listStepValue.get(startIndex)[startIndexElement] = localStep;
//            if (startIndex == 3) { 
//                startIndex = 0;
//                startIndexElement++;
//            }
//            else {startIndex++;}
//            minX += localStep;
//        }
//        //System.out.println("end writing data");
//        for (int i = 0; i < countThread; i++) {
//            MathThread newThread = new MathThread(listXValue.get(i), listStepValue.get(i));
//            listThread[i] = newThread;
//            listThread[i].start();
//        }
//        try {
//            for (int i = 0; i < countThread; i++) {
//                listThread[i].join();
//                resultArea += listThread[i].getResultAreaValue();
//            }
//        } catch (InterruptedException e) {
//            //e.printStackTrace();
//        }
//        finally {
//            timeInterval = objTimeContoler.getTimeIntervalNs();
//            return resultArea;
//        }
//    }
    public long getTimeCalculating () {return timeInterval;}
    public double getResult () {return resultArea;}
    
}
