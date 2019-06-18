package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Graphs {
	static String interf;
	static String dirIP;
	static Scanner scanner = new Scanner(System.in);
	static String testFichero;
	static File testFichero2;


	public static void main(String[] args) {
		System.out.println("APP DEL TFG DE VÍCTOR");
		List<String> horas = new ArrayList<String>();
        XYSeries stats = new XYSeries("Caudal");
        int i=1;
        boolean data = true;
		//Leemos el archivo en el que están los datos de la interfaz correspondiente
        BufferedReader reader;
        try{
          reader = new BufferedReader(new FileReader(pedirDatos()));
          //Leemos la primera línea y no hacemos nada con ella ya que por el funcionamiento de la app en ONOS, esta línea será null
          String line = reader.readLine();
          line = reader.readLine();
          while (line != null) {
        	  //Para cada línea, añadimos el dato a la serie de datos que luego convertiremos en gráfica
        	  if (data) {
        		  Double caudal = Double.parseDouble(line);
        		  data = false;
                  stats.add(i, caudal);
                  i++;
        	  } else {
        		  horas.add(line);
        		  data = true;
        	  }
            System.out.println(line);

            line = reader.readLine();
          }
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        
        //Preparamos el display de la gráfica
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(stats);

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
          "Caudal cursado en la interfaz "+interf+" de "+dirIP,
          "Número de muestra",
          "Caudal cursado [kbps]",
          dataset,
          PlotOrientation.VERTICAL, true, true, false
        );

        XYPlot plot = xylineChart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        plot.setRenderer(renderer);

        ChartPanel panel = new ChartPanel(xylineChart);
        
        SymbolAxis sa = new SymbolAxis("Hora de la muestra [HH:MM:SS]", horas.toArray(new String[horas.size()]));
        plot.setDomainAxis(sa);
        JFrame ventana = new JFrame("Grafica");
        ventana.setVisible(true);
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ventana.add(panel);
	}
	
	//Pide al usuario los datos de IP e interfaz, y si no son correctos se los vuelve a pedir
	private static String pedirDatos() {
		System.out.print("Ingrese dirección IP del dispositivo: ");
		dirIP = scanner.nextLine();
		System.out.print("Ingrese la interfaz que desea consultar: ");
		interf = scanner.nextLine();
		testFichero = "/home/victor/Descargas/onos/archivosNETCONF/netconf:"+dirIP+":830"+interf+".txt";
		testFichero2 = new File(testFichero);
		if (testFichero2.exists()) {
				return testFichero;
			} else {
				System.out.println("Lo sentimos, la dirección IP o la interfaz no son correctas. Pruebe de nuevo");
				pedirDatos();
			}

		return testFichero;
		
	}

}
