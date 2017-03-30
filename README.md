# KMeans-Clustering-with-visualization
This program implements the K-means clustering algorithm which basically segments items based on similarity. In this unsupervised algorithm similarity is measured by minimum euclidean distance from a designated cluster head. Can be used for recommendation systems, market segmentation etc.  It also visualizes the result using the library jfreechart. Follow ReadMe to install jfreechart. 

Instruction on how to install jfreechart:https://www.tutorialspoint.com/jfreechart/jfreechart_xy_chart.htm

Instructions
-------------

For the program to work, the KMEans.java file, located in the package kmeans in the project folder KMeans should be excecuted using netbeans, or any other java compiler like BlueJ.

When the user runs the program, they will be presented with a contextual menu like the following:
_____________________________________________________________________________________________________________________________
The default settings are:
1. Number of points= 1000
2. Number of clusters= 10
3. Maximum X and Y co-ordinate value= 100
4. Minimum X and Y coordinate value= 0
5. Number of iterations before termination if cluster heads do not converge: 20
___________________________________


Do you wish to proceed with the default setting, or override you own settings?
1. DEFAULT
2. DEFINE OWN SETTING
 YOUR CHOICE: 
Enter the desired option:
_______________________________________________________________________________________________________________________________

If the user presses 1,  then the above mentioned default settings are excecuted.

If the user presses 2, the user gets to define his own parameters for the excecution of the Algorithm.
A sample run is as given below:
________________________________________________________________________________________________________________________________
Enter the number of points:
Enter the number of Clusters: 
Enter the minimum X and Y co-ordinate value: 
Enter the maximum X and Y co-ordinate value: 
Enter number of iterations before termination if clusterHeads do not converge: 
_________________________________________________________________________________________________________________________________

Upon complete excecution, a jfreegraph graph is opened which indicates all the clusters in different colors, and diffrent cluster points with different markups. 

