package kmeans;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import java.awt.Color; 
import java.awt.BasicStroke; 

import org.jfree.chart.ChartPanel; 
import org.jfree.chart.JFreeChart; 
import org.jfree.data.xy.XYDataset; 
import org.jfree.data.xy.XYSeries; 
import org.jfree.ui.ApplicationFrame; 
import org.jfree.ui.RefineryUtilities; 
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.data.xy.XYSeriesCollection; 
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class KMeans {
 
    /*Initial setting values, can be overriden later*/
    int MIN_COORDINATE=0,MAX_COORDINATE=100,NUM_POINTS=1000,NUM_CLUSTERS=10;
    private List points;
    private List clusters;
    int choice;
    int numIteration=20;
    /*Default constructor*/
    public KMeans() {
    	this.points = new ArrayList();
    	this.clusters = new ArrayList();    	
    }
    
    public static void main(String[] args) {
    	
    	KMeans kmeans = new KMeans();   //Define k-means object
    	kmeans.init();                  //Initialize k-means algorithm
    	kmeans.calculate();             //Recursively change clusterHeads, and clusters
    }
    
    //Initializes the process
    public void init() {
    	//Scanner class for input
        Scanner s=new Scanner(System.in);
        //Display default settings
        System.out.print("The default settings are:\n1. Number of points= "+NUM_POINTS+"\n2. Number of clusters= "+
                NUM_CLUSTERS+"\n3. Maximum X and Y co-ordinate value= "+MAX_COORDINATE+"\n4. Minimum "
                + "X and Y coordinate value= "+MIN_COORDINATE+"\n5. Number of iterations "
                + "before termination if cluster heads do not converge: "+numIteration+"\n___________________________________\n");
        
        System.out.print("\n\nDo you wish to proceed with the default setting, or override you own settings?\n1. DEFAULT\n2. DEFINE OWN SETTING\n YOUR CHOICE: ");
        choice=s.nextInt();
        if(choice==2){
            System.out.print("\nEnter the number of points: "); //Change default number of points
            NUM_POINTS=s.nextInt();

            System.out.print("\nEnter the number of Clusters: ");  //Change default number of clusters
            NUM_CLUSTERS=s.nextInt();

            System.out.print("\nEnter the minimum X and Y co-ordinate value: "); //Change co-ordinate minimum value
            MIN_COORDINATE=s.nextInt();

            System.out.print("\nEnter the maximum X and Y co-ordinate value: "); //Change co-ordinate maximum value
            MAX_COORDINATE=s.nextInt();
            System.out.print("\nEnter number of iterations before termination if clusterHeads do not converge: "); //Change co-ordinate maximum value
            numIteration=s.nextInt();
            
        }
        /*Point object created. Creating random NUM_POINTS number of points*/
    	points = Point.createRandomPoints(MIN_COORDINATE,MAX_COORDINATE,NUM_POINTS);
    	
    	//Create Clusters
    	//Set Random Centroids
    	for (int i = 0; i< NUM_CLUSTERS; i++) {
            /*Creating cluster that stores its cluster head*/
            Cluster cluster = new Cluster(i);
            /*Defining clusterHead point*/
            Point clusterHead = Point.createRandomPoint(MIN_COORDINATE,MAX_COORDINATE);
            /*Setting randomly generated cluster heads as clusterHeads in cluster*/
            cluster.setCentroid(clusterHead);
            /*Adding cluster to the list of clusters*/
            clusters.add(cluster);
    	}
    	//Print Initial state
    	plotClusters();
    }
    /*Function to plot all clusters*/
    private void plotClusters() {
        for (int i = 0; i< NUM_CLUSTERS; i++) {
            /*Retrieving each cluster using get function*/
            Cluster c = (Cluster) clusters.get(i);
            /*Calling cluster class fucnction plot cluster to plot each individual cluster*/
            c.plotCluster();
        }
    }
    /*Function to calculate the K Means, with iterating method.*/
    public void calculate() {
        boolean finish = false;
        int iteration = 0;
        // Add in new data, one at a time, recalculating clusterHeads with each new one. 
        while(!finish) {
        	//Clear cluster state
        	clearClusters();
        	/*Present cluster state stored*/
        	List lastCentroids = getCentroids();
        	
        	//Assign points to the closer cluster
        	assignCluster();
            
            //Calculate new clusterHeads.
        	calculateClusterHeads();
        	
        	iteration++;
        	
        	List currentCentroids = getCentroids();
        	
        	//Calculates total distance between new and old Centroids
        	double distance =0;
                
                for(int i = 0; i< lastCentroids.size(); i++) {
                    Point lc=(Point)lastCentroids.get(i);
                    Point cc=(Point)currentCentroids.get(i);
                    //Check euclidean distance between old and new point
                    distance+= Point.distance(lc,cc);
                }

        	System.out.println("--------------------------------------------------------------");
        	System.out.println("Iteration: " + (iteration+1));
        	System.out.println("Centroid distances: " + distance);
        	plotClusters();
        	System.out.println("--------------------------------------------------------------");
                /*Verifying if number of iterations exceeds defined value, or distanace remained same between
                previous and present cluster*/
        	if(iteration==numIteration||distance<0.0) {
        		finish = true;
        	}
        }
        /*Visualizing the final cluster using jfreechart*/
        XYLineChart_AWT chart = new XYLineChart_AWT("K-Means Clustering", "Clusters",NUM_CLUSTERS,clusters);
        chart.pack( );          
        RefineryUtilities.centerFrameOnScreen( chart );          
        chart.setVisible( true );
    }
    /*Function to clear present cluster list*/
    private void clearClusters() {
        for (Iterator it = clusters.iterator(); it.hasNext();) {
            Cluster cluster = (Cluster)it.next();
            cluster.clear();
        }
    }
    /*Function to get all the clusterHeads of the clusters*/
    private List getCentroids() {
    	List clusterHeads = new ArrayList(NUM_CLUSTERS);
        for (Iterator it = clusters.iterator(); it.hasNext();) {
            Cluster cluster = (Cluster)it.next();
            Point aux = cluster.getCentroid();
            Point point = new Point(aux.getX(),aux.getY());
            clusterHeads.add(point);
        }
    	return clusterHeads;
    }
    /*Function to assign a given point to its closest cluster head*/
    private void assignCluster() {
        double max = Float.MAX_VALUE;
        double min = max; 
        int cluster = 0;                 
        double distance = 0; 
        
        for (Iterator it = points.iterator(); it.hasNext();) {
            Point point = (Point)it.next();
            min = max;
            for(int i = 0; i< NUM_CLUSTERS;i++)
                    {
                        Cluster c = (Cluster)clusters.get(i);
                        /*Distance between point and all the cluster head is measured and the minimum is selected*/
                        distance = Point.distance(point, c.getCentroid());
                        if(distance<min){
                            //Store the minimum distance
                            min = distance;
                            cluster = i;
                        }
                    }
                    point.setCluster(cluster);
                    Cluster x=(Cluster)clusters.get(cluster);
                    x.addPoint(point);
        }
    }
    /*Function to calculate cluster heads or centroids*/
    private void calculateClusterHeads() {
        for (Iterator it = clusters.iterator(); it.hasNext();) {
            Cluster cluster = (Cluster)it.next();
            double sumX = 0;
            double sumY = 0;
            List list = cluster.getPoints();
            int n_points = list.size();
            for (Iterator it1 = list.iterator(); it1.hasNext();) {
                Point point = (Point)it1.next();
                sumX += point.getX();
                sumY += point.getY();
            }
            Point clusterHead = cluster.getCentroid();
            if(n_points<0)
                    {
                        double newX = sumX / n_points;
                        double newY = sumY / n_points;
                        clusterHead.setX(newX);
                        clusterHead.setY(newY);
                    }
        }
    }
}
class Point {
 
    private double x = 0;
    private double y = 0;
    private int cluster_number = 0;
 
    public Point(double x, double y)
    {
        this.setX(x);
        this.setY(y);
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getX()  {
        return this.x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setCluster(int n) {
        this.cluster_number = n;
    }
    
    public int getCluster() {
        return this.cluster_number;
    }
    
    //Calculates the distance between two points.
    protected static double distance(Point p, Point clusterHead) {
        return Math.sqrt(Math.pow((clusterHead.getY() - p.getY()), 2) + Math.pow((clusterHead.getX() - p.getX()), 2));
    }
    
    //Creates random point
    protected static Point createRandomPoint(int min, int max) {
    	Random r = new Random();
    	double x =  (min + (max - min) * r.nextFloat());
    	double y =  (min + (max - min) * r.nextFloat());
    	return new Point(x,y);
    }
    
    protected static List createRandomPoints(int min, int max, int number) {
    	List points = new ArrayList(number);
    	for(int i = 0; i<number; i++) {
    		points.add(createRandomPoint(min,max));
    	}
    	return points;
    }
    
    public String toString() {
    	return "("+x+","+y+")";
    }
}
 class Cluster {
	
    public List points;
    public Point clusterHead;
    public int id;

    //Creates a new Cluster
    public Cluster(int id) {
        this.id = id;
        this.points = new ArrayList();
        this.clusterHead = null;
    }

    public List getPoints() {
        return points;
    }
    
    public void addPoint(Point point) {
        points.add(point);
    }

    public void setPoints(List points) {
        this.points = points;
    }

    public Point getCentroid() {
        return clusterHead;
    }

    public void setCentroid(Point clusterHead) {
        this.clusterHead = clusterHead;
    }

    public int getId() {
        return id;
    }

    public void clear() {
            points.clear();
    }

    public void plotCluster() {

        System.out.println("**********************");
        System.out.println("[Cluster: " + (id+1)+"]");
        System.out.println("[Centroid: " + clusterHead + "]");
        System.out.println("[Points: \n");
        for (Iterator it = points.iterator(); it.hasNext();) {
            Point p = (Point)it.next();
            System.out.println(p);
        }
        System.out.println("]");
        System.out.println("**********************");

    } 
}
/*JFreeChart package to draw graphs*/
class XYLineChart_AWT extends ApplicationFrame 
{
   public XYLineChart_AWT( String applicationTitle, String chartTitle,int NUM_CLUSTER,List clusters)
   {
      super(applicationTitle);
      /*Creating the chart, createDataset() inputs points into the cluster*/
      JFreeChart xylineChart = ChartFactory.createXYLineChart(
         chartTitle ,
         "X" ,
         "Y" ,
         createDataset(NUM_CLUSTER,clusters) ,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart );
      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
      final XYPlot plot = xylineChart.getXYPlot( );
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      /*Inputting color option*/
      renderer.setSeriesPaint( 0 , Color.RED );
      renderer.setSeriesPaint( 1 , Color.GREEN );
      renderer.setSeriesPaint( 2 , Color.YELLOW );
      renderer.setSeriesPaint( 3 , Color.BLUE );
      renderer.setSeriesPaint( 4 , Color.ORANGE );
      renderer.setSeriesPaint( 5 , Color.BLACK );
      renderer.setSeriesPaint( 6 , Color.CYAN );
      renderer.setSeriesPaint( 7 , Color.GRAY);
      renderer.setSeriesPaint( 8 , Color.PINK);
      renderer.setSeriesPaint( 9 , Color.MAGENTA);
      plot.setRenderer( renderer ); 
      setContentPane( chartPanel ); 
   }
   
   private XYDataset createDataset(int NUM_CLUSTER,List clusters )
   {
       int i=1;
       final XYSeriesCollection dataset = new XYSeriesCollection( ); 
       for (Iterator it = clusters.iterator(); it.hasNext();) {
            Cluster cluster = (Cluster)it.next();
            final XYSeries cl = new XYSeries( i );
            i++;
            List list = cluster.getPoints();
            int n_points = list.size();
            for (Iterator it1 = list.iterator(); it1.hasNext();) {
                Point point = (Point)it1.next();
                cl.add(point.getX(),point.getY());
            }                                  
            dataset.addSeries( cl );            
       }
       return dataset;
   }
}