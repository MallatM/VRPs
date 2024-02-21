/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.graphhopper.jsprit.examples;

import com.graphhopper.jsprit.analysis.toolbox.AlgorithmSearchProgressChartListener;
import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.analysis.toolbox.StopWatch;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.algorithm.listener.VehicleRoutingAlgorithmListeners.Priority;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem.Builder;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter.Print;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.jsprit.instance.reader.CordeauReader;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;
import com.graphhopper.jsprit.util.Examples;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;


public class SCCVRP{
	
	


	public static void main(String[] args) throws FileNotFoundException {
        /*
         * some preparation - create output folder
		 */
        Examples.createOutputFolder();
        
        
        

        VehicleRoutingProblem.Builder vrpBuilderSCC = VehicleRoutingProblem.Builder.newInstance();
        VehicleRoutingProblem.Builder vrpBuilderSCCSep1 = VehicleRoutingProblem.Builder.newInstance();
        VehicleRoutingProblem.Builder vrpBuilderSCCSep2 = VehicleRoutingProblem.Builder.newInstance();
     
        //new VrpXMLReader(vrpBuilder).read("eclipse-workspace\\tutotial\\src\\main\\java\\com\\graphhopper\\jsprit\\examples\\cordeau01.xml");
  
        List<String> stngFile = new ArrayList<String>();
        String Id = "8100";
        Scanner scnr = new Scanner(new FileReader(
                 //"C:\\Users\\malla\\Documents\\AAA_Importantes\\Livros\\PIBITI_2023\\SCCinst\\SCCVRP-dat\\vrps_"+Id+"L.dat"));
        		"C:\\Users\\malla\\Documents\\AAA_Importantes\\Livros\\PIBITI_2023\\SCCinst\\SCCVRP-dat\\vrps_"+Id+".dat"));
        String str;
        while (scnr.hasNext()) {
                    str = scnr.next();
                    stngFile.add(str);
                }
        
        System.out.println(stngFile.size());
        //System.out.println(stngFile.indexOf("Id=" + Id +";"));
        
        String[] res = stngFile.get(stngFile.indexOf("Id="+Id+";")+2).split("=|\\;", 0);
        int ns = Integer.parseInt(res[1]);
        System.out.println("Number of services: " + ns);
        
        String[] res2 = stngFile.get(stngFile.indexOf("Id="+Id+";")+1).split("=|\\;", 0);
        int nd = Integer.parseInt(res2[1]);
        System.out.println("Number of deposits : " + nd);
        
        
        int n = nd + ns;
        
        
        String[] res3 = stngFile.get(stngFile.indexOf("Id="+Id+";")+9).split("=|\\;|\\[|\\]|\\,", 0);
        int nv1 = Integer.parseInt(res3[2]);
        int nv2 = Integer.parseInt(res3[3]);
        System.out.println(nv1);
        System.out.println(nv2);
  
        String[] resQ = stngFile.get(stngFile.indexOf("Id="+Id+";")+5).split("=|\\;", 0);
        int Q = Integer.parseInt(resQ[1]);
        System.out.println("Q = " +  Q);
      
        
       
        
        
        VehicleType vehicleType = VehicleTypeImpl.Builder.newInstance("1_type")
        		.addCapacityDimension(0,Q)
        		.addCapacityDimension(1,0)
        		.setCostPerDistance(1.0)
        		.build();
        VehicleType vehicleType2 = VehicleTypeImpl.Builder.newInstance("2_type")
        		.addCapacityDimension(0,0)
        		.addCapacityDimension(1,Q)
        		.setCostPerDistance(1.0)
        		.build();
        VehicleType vehicleType3 = VehicleTypeImpl.Builder.newInstance("3_type")
        		.addCapacityDimension(2,Q)
        		.setCostPerDistance(1.0)
        		.build();

       com.graphhopper.jsprit.core.problem.Location.Builder loc = new Location.Builder();
       
       loc.setId("P"+(ns+1));
       loc.setCoordinate(new Coordinate(0,0));
       
       Location Pn1 = loc.build();
       
       loc.setId("P"+(ns+2));
       loc.setCoordinate(new Coordinate((ns+10),0));
       
       Location Pn2 = loc.build();
 
        for (int i = 0; i<nv1;i++) {
            VehicleImpl.Builder vehicleBuilderSCCSep = VehicleImpl.Builder.newInstance("Sep1_" + (i+1) + "_vehicle");
            vehicleBuilderSCCSep.setStartLocation(Pn1);
            vehicleBuilderSCCSep.setType(vehicleType);
            VehicleImpl vehicle = vehicleBuilderSCCSep.build();
            vrpBuilderSCCSep1.addVehicle(vehicle);
            
            VehicleImpl.Builder vehicleBuilderSCC = VehicleImpl.Builder.newInstance("1_" + (i+1) + "_vehicle");
            vehicleBuilderSCC.setStartLocation(Pn1);
            vehicleBuilderSCC.setType(vehicleType3);
            vehicleBuilderSCC.addSkill("driver1");
            VehicleImpl vehicle2 = vehicleBuilderSCC.build();
            vrpBuilderSCC.addVehicle(vehicle2);
        }
        for (int i = 0; i<nv2;i++) {
            VehicleImpl.Builder vehicleBuilderSCCSep = VehicleImpl.Builder.newInstance("Sep2_" + (i+1) + "_vehicle");
            vehicleBuilderSCCSep.setStartLocation(Pn2);
            vehicleBuilderSCCSep.setType(vehicleType2);
            VehicleImpl vehicle = vehicleBuilderSCCSep.build();
            vrpBuilderSCCSep2.addVehicle(vehicle);
            
            VehicleImpl.Builder vehicleBuilderSCC = VehicleImpl.Builder.newInstance("2_" + (i+1) + "_vehicle");
            vehicleBuilderSCC.setStartLocation(Pn2);
            vehicleBuilderSCC.setType(vehicleType3);
            vehicleBuilderSCC.addSkill("driver2");
            VehicleImpl vehicle2 = vehicleBuilderSCC.build();
            vrpBuilderSCC.addVehicle(vehicle2);
        }
        
        
        String[] coordsP = stngFile.get(stngFile.indexOf("Id="+Id+";")+8).split("=|\\;|\\[|\\]|\\,", 0);
 
      
        
        for (int i = 1; i<=ns; i++) {
        	loc.setId("P"+i);
            loc.setCoordinate(new Coordinate(i, i));
            Location P = loc.build();
        	
            if (Integer.parseInt(coordsP[4*i-2]) == 0) {
            	Service service = Service.Builder.newInstance("P" + i)		
            			.setServiceTime(0)
                 		.addSizeDimension(2, Integer.parseInt(coordsP[4*i-3]))
                 		.setTimeWindow(TimeWindow.newInstance(0, 72000))
                 		.setLocation(P)
                 		.addRequiredSkill("driver1")
                 		.build();
            	
            	vrpBuilderSCC.addJob(service);
            } 
                else if (Integer.parseInt(coordsP[4*i-3]) == 0) {
                	Service service = Service.Builder.newInstance("P" + i)		
                			.setServiceTime(0)
                     		.addSizeDimension(2, Integer.parseInt(coordsP[4*i-2]))
                     		.setTimeWindow(TimeWindow.newInstance(0, 72000))
                     		.setLocation(P)
                     		.addRequiredSkill("driver2")
                     		.build();
                	
                	vrpBuilderSCC.addJob(service);
            } else {
            	Service servicep1 = Service.Builder.newInstance("P(1)" + i)		
            			.setServiceTime(0)
                 		.addSizeDimension(2, Integer.parseInt(coordsP[4*i-3]))
                 		.setTimeWindow(TimeWindow.newInstance(0, 72000))
                 		.setLocation(P)
                 		.build();
            	
            	vrpBuilderSCC.addJob(servicep1);
            	
            	Service servicep2 = Service.Builder.newInstance("P(2)" + i)		
            			.setServiceTime(0)
                 		.addSizeDimension(2, Integer.parseInt(coordsP[4*i-2]))
                 		.setTimeWindow(TimeWindow.newInstance(0, 72000))
                 		.setLocation(P)
                 		.build();
            	
            	vrpBuilderSCC.addJob(servicep2);
            }
        	 
        	 
        	 
        	 Service service1 = Service.Builder.newInstance("1P" + i)		
         			.setServiceTime(0)
              		.addSizeDimension(0, Integer.parseInt(coordsP[4*i-3]))
              		.addSizeDimension(1, 0)
              		.setTimeWindow(TimeWindow.newInstance(0, 72000))
              		.setLocation(P)
              		.build();
        	 
        	 if( Integer.parseInt(coordsP[4*i-3]) != 0) {
        		 vrpBuilderSCCSep1.addJob(service1);
        	 }
        	 
        	 
        	 Service service2 = Service.Builder.newInstance("2P" + i)		
         			.setServiceTime(0)
         			.addSizeDimension(0, 0)
              		.addSizeDimension(1, Integer.parseInt(coordsP[4*i-2]))
              		.setTimeWindow(TimeWindow.newInstance(0, 72000))
              		.setLocation(P)
              		.build();
        	 //System.out.println("Demand" + Integer.parseInt(coordsP[4*i-2]));
        	 
        	 if( Integer.parseInt(coordsP[4*i-2]) != 0) {
        		 vrpBuilderSCCSep2.addJob(service2);
        	 }
        	
        	 
             
        } 
        	
        
        // setting cost matrix
        
        
        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

        for (int i = 1; i<=n;i++) {
        	for(int j = i; j<=n;j++) {
 
        		int indice = 0;
        		if (i == j) {
        			
        			costMatrixBuilder.addTransportDistance("P" + i,"P" + j, 0);
            		costMatrixBuilder.addTransportTime("P" + i,"P" + j, 0);
            		
        		} else {
        			
        			String[] res4 = stngFile.get(stngFile.indexOf("Id="+Id+";")+16+(i-1)*(n-1)+(j-2)).split(":|\\,", 0);   
                    double cost = Double.parseDouble(res4[2]);
                    
        			costMatrixBuilder.addTransportDistance("P" + i,"P" + j, cost);
            		costMatrixBuilder.addTransportTime("P" + i,"P" + j, cost);
            		//System.out.println(i + "," + j +  "," + indice +  ","+ cost );
        		}
      
        		
        		
        
        	}
        }


		/*
         * define problem with finite fleet
		 */
        vrpBuilderSCCSep1.setFleetSize(FleetSize.FINITE);
        vrpBuilderSCCSep2.setFleetSize(FleetSize.FINITE);
        vrpBuilderSCC.setFleetSize(FleetSize.FINITE);

		/*
         * build the problem
		 */
        vrpBuilderSCC.setRoutingCost(costMatrixBuilder.build());
        VehicleRoutingProblem vrpSCC = vrpBuilderSCC.build();
        
        vrpBuilderSCCSep1.setRoutingCost(costMatrixBuilder.build());
        VehicleRoutingProblem vrpSCCSep1 = vrpBuilderSCCSep1.build();
        
        
        vrpBuilderSCCSep2.setRoutingCost(costMatrixBuilder.build());
        VehicleRoutingProblem vrpSCCSep2 = vrpBuilderSCCSep2.build();
    

		/*
         * plot to see how the problem looks like
		 */
//		SolutionPlotter.plotVrpAsPNG(vrp, "output/problem01.png", "p01");

		/*
         * solve the problem
		 */
        VehicleRoutingAlgorithm vra = Jsprit.Builder.newInstance(vrpSCC).setProperty(Jsprit.Parameter.THREADS, "5").buildAlgorithm();
        vra.setMaxIterations(2000);
        vra.getAlgorithmListeners().addListener(new StopWatch(), Priority.HIGH);
        vra.getAlgorithmListeners().addListener(new AlgorithmSearchProgressChartListener("output/progress.png"));
        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        SolutionPrinter.print(Solutions.bestOf(solutions));

        new Plotter(vrpSCC, Solutions.bestOf(solutions)).setLabel(Plotter.Label.ID).plot("output/p01_solution.png", "p01");

        new GraphStreamViewer(vrpSCC, Solutions.bestOf(solutions)).setRenderDelay(100).display();
        SolutionPrinter.print(vrpSCC,Solutions.bestOf(solutions),Print.VERBOSE);

        
        VehicleRoutingAlgorithm vraSep1 = Jsprit.Builder.newInstance(vrpSCCSep1).setProperty(Jsprit.Parameter.THREADS, "5").buildAlgorithm();
        vraSep1.setMaxIterations(2000);
        vraSep1.getAlgorithmListeners().addListener(new StopWatch(), Priority.HIGH);
        vraSep1.getAlgorithmListeners().addListener(new AlgorithmSearchProgressChartListener("output/progress.png"));
        Collection<VehicleRoutingProblemSolution> solutions2 = vraSep1.searchSolutions();

        SolutionPrinter.print(Solutions.bestOf(solutions2));

        new Plotter(vrpSCCSep1, Solutions.bestOf(solutions2)).setLabel(Plotter.Label.ID).plot("output/p02_solution.png", "p02");

        new GraphStreamViewer(vrpSCCSep1, Solutions.bestOf(solutions2)).setRenderDelay(100).display();
        SolutionPrinter.print(vrpSCCSep1,Solutions.bestOf(solutions2),Print.VERBOSE);
        
        
        VehicleRoutingAlgorithm vraSep2 = Jsprit.Builder.newInstance(vrpSCCSep2).setProperty(Jsprit.Parameter.THREADS, "5").buildAlgorithm();
        vraSep2.setMaxIterations(2000);
        vraSep2.getAlgorithmListeners().addListener(new StopWatch(), Priority.HIGH);
        vraSep2.getAlgorithmListeners().addListener(new AlgorithmSearchProgressChartListener("output/progress.png"));
        Collection<VehicleRoutingProblemSolution> solutions3 = vraSep2.searchSolutions();

        SolutionPrinter.print(Solutions.bestOf(solutions3));

        new Plotter(vrpSCCSep2, Solutions.bestOf(solutions3)).setLabel(Plotter.Label.ID).plot("output/p03_solution.png", "p03");

        new GraphStreamViewer(vrpSCCSep2, Solutions.bestOf(solutions3)).setRenderDelay(100).display();
        SolutionPrinter.print(vrpSCCSep2,Solutions.bestOf(solutions3),Print.VERBOSE);
    }

}
