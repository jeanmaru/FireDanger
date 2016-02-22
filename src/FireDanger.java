
/**
 * @author Jess Rudolph
 * Project 1
 * COSC 603
 * 
 * This project utilizes the Computer Calculations of Fire Danger
 * Program available in Fortran with documentation and flow chart
 * to re-engineer the code to java.
 * 
 * The purpose is to learn about the basics of re-engineering code.
 *
 */

import java.util.*;
import java.lang.*;

public class FireDanger {

	public static boolean snowBool;
	public static boolean rainBool;
	public static int herbStage;
	public static double dryTemp;
	public static double wetTemp;
	public static double snowPrecip;
	public static double rainPrecip;
	public static double windSpeedMph;
	public static double lastBui;
	
	public double grassIndex;
	public double timberIndex;
	public double bui;
	public double dryingFactor;
	public double adjustedFuelMoisture;

	public double fineFuelMoisture = 99;
	public double fireLoadIndex = 0;

	double [] arrayA = {-0.185900, -0.85900, -0.59660, -0.077373};
	double [] arrayB = {30, 19.2, 13.8, 22.5};
	double [] arrayC = {4.5, 12.5, 27.5};
	double [] arrayD = {16, 10, 7, 5, 4, 3};
	
	/**
	 * This is the constructor for the input values that the user entered so that calculations can be made.
	 * 
	 * @param dryTemp the dry-bulb reading
	 * @param wetTemp the wet-bulb reading
	 * @param snowBool true/false value for weather or not it has snowed (see what I did there)
	 * @param snowPrecip the amount, in inches, for amount of snow precipitation in the past 24 hours
	 * @param rainBool the amount, in inches, for amount of rain precipitation in the past 24 hours
	 * @param rainPrecip true/false value for weather or not it has snowed (see what I did there)
	 * @param windSpeedMph the current wind speed in miles per hour
	 * @param lastBui yesterday's build-up index
	 * @param herbStage the herb stage integer where  1 = Cured, 2 = Transition, and 3 = Green
	 */
	public FireDanger(double dryTemp, double wetTemp, boolean snowBool, double snowPrecip, boolean rainBool, double rainPrecip, double windSpeedMph, double lastBui, int herbStage){
		FireDanger.dryTemp = dryTemp;
		FireDanger.wetTemp = wetTemp;
		FireDanger.snowBool = snowBool;
		FireDanger.snowPrecip = snowPrecip;
		FireDanger.rainBool = rainBool;
		FireDanger.rainPrecip = rainPrecip;
		FireDanger.windSpeedMph = windSpeedMph;
		FireDanger.lastBui = lastBui;		
		FireDanger.herbStage = herbStage;

		getSnow(snowBool, rainBool, rainPrecip, snowPrecip, lastBui, dryTemp, wetTemp, herbStage, windSpeedMph);
	}

	/**
	 * This determines how to calculate indexes based on whether or now it has snowed.
	 * 
	 * @param dryTemp the dry-bulb reading
	 * @param wetTemp the wet-bulb reading
	 * @param snowBool true/false value for weather or not it has snowed (see what I did there)
	 * @param snowPrecip the amount, in inches, for amount of snow precipitation in the past 24 hours
	 * @param rainBool the amount, in inches, for amount of rain precipitation in the past 24 hours
	 * @param rainPrecip true/false value for weather or not it has snowed (see what I did there)
	 * @param windSpeedMph the current wind speed in miles per hour
	 * @param lastBui yesterday's build-up index
	 * @param herbStage the herb stage integer where  1 = Cured, 2 = Transition, and 3 = Green
	 */
	public void getSnow (boolean snowBool, boolean rainBool, double rainPrecip, double snowPrecip, double lastBui, double dryTemp, double wetTemp, int herbStage, double windSpeedMph){
		if (snowBool = true){
			timberIndex = 0;
			grassIndex = 0;
				if (snowPrecip > .1){
					precipitationTrue(snowPrecip, lastBui);
			}
		} else {
			getFFM(dryTemp, wetTemp);
			dryingFactor = calculateDryingFactor();
			adjustFineFuelForHerbStage(herbStage);
			getRain(snowBool, rainBool, rainPrecip, snowPrecip, lastBui, dryTemp, wetTemp, herbStage, dryingFactor, windSpeedMph);
		}
	}
	
	/**
	 * This determines how to calculate indexes based on whether or now it has rained.
	 * 
	 * @param dryTemp the dry-bulb reading
	 * @param wetTemp the wet-bulb reading
	 * @param snowBool true/false value for weather or not it has snowed (see what I did there)
	 * @param snowPrecip the amount, in inches, for amount of snow precipitation in the past 24 hours
	 * @param rainPrecip the amount, in inches, for amount of rain precipitation in the past 24 hours
	 * @param rainBool true/false value for weather or not it has snowed (see what I did there)
	 * @param windSpeedMph the current wind speed in miles per hour
	 * @param lastBui yesterday's build-up index
	 * @param herbStage the herb stage integer where  1 = Cured, 2 = Transition, and 3 = Green
	 */
	public void getRain(boolean snowBool, boolean rainBool, double rainPrecip, double snowPrecip, double lastBui, double dryTemp, double wetTemp, int herbStage, double dryingFactor, double windSpeedMph){
		if (rainBool = true){
			if (rainPrecip > .1){
				increaseBuiByDryingFactor(lastBui, dryingFactor);
				adjustedFuelMoisture = calculateAdjustedFuelMoisture(bui, fineFuelMoisture);
				setFuelMoistureIndexes(adjustedFuelMoisture, fineFuelMoisture);
				adjustIndexesForWind(windSpeedMph, adjustedFuelMoisture, fineFuelMoisture);
				
				if (timberIndex > 0 && bui > 0){
				calculateFireLoadIndex(bui, timberIndex);
				}
				printResults();
				}
		} else {
			increaseBuiByDryingFactor(lastBui, dryingFactor);
			adjustedFuelMoisture = calculateAdjustedFuelMoisture(bui, fineFuelMoisture);
			setFuelMoistureIndexes(adjustedFuelMoisture, fineFuelMoisture);
			adjustIndexesForWind(windSpeedMph, adjustedFuelMoisture, fineFuelMoisture);
			
			if (timberIndex > 0 && bui > 0){
			calculateFireLoadIndex(bui, timberIndex);
			}
			printResults();
			}
	}
	
	/**
	 * This calculates the build-up index and is executed when there has been precipitation.
	 * 
	 * @param rainPrecip the amount, in inches, for amount of rain precipitation in the past 24 hours
	 * @param lastBui yesterday's build-up index
	 * @return
	 */
	public double precipitationTrue(double rainPrecip, double lastBui){
		calculateBui(rainPrecip, lastBui);
		adjustBui(bui);
		return bui;
	}
	
	/**
	 * Retrieve the necessary variables to calculate the fine fuel moisture.
	 * 
	 * @param dryTemp the dry-bulb reading
	 * @param wetTemp the wet-bulb reading
	 */
	public void getFFM(double dryTemp, double wetTemp){
		double dif = dryTemp - wetTemp;
		for(int i = 0; i < 3; i++){
			if (dif <= arrayC[i]){
				calculateFFM(dif, i);
			}
		}		
	}
	
	/**
	 * Calculate the fine fuel moisture.
	 * 
	 * @param dif the difference between dry temperature bulb reading and the wet temperature bulb reading.
	 * @param x a counter to iterate through the array.
	 */
	public void calculateFFM(double dif, int x){
		fineFuelMoisture = (arrayB[x])*Math.E*(arrayA[x]*dif);
	}

	/**
	 * Adjusts the fine fuel moisture based on the herb stage.
	 * 
	 * @param herbStage the herb stage integer where  1 = Cured, 2 = Transition, and 3 = Green
	 */
	public void adjustFineFuelForHerbStage(int herbStage){
		if (fineFuelMoisture <= 1){
			fineFuelMoisture = 1;
		} if (herbStage > 1) {
			int i = 2;
			while (i <= herbStage){
				fineFuelMoisture = fineFuelMoisture + .05;
				i++;
			}
		}
	}

	/**
	 * Calculates the drying factor based on the fine fuel moisture.
	 * 
	 * @return the dryingFactor
	 */
	public double calculateDryingFactor(){
		int i = 0;
		while (i < 6){
			if (fineFuelMoisture >= arrayD[i]){
				dryingFactor = arrayD[i];
				return dryingFactor;
			} else if (fineFuelMoisture <= arrayD[i]){
				i++;
				return dryingFactor;
			}
		}
		return dryingFactor;

	}


	/**
	 * Calculates the fire load index.
	 * 
	 * @param bui the build-up index
	 * @param timberIndex the timber spread index
	 */
	public void calculateFireLoadIndex(double bui, double timberIndex){
		fireLoadIndex = Math.pow(10, 1.75*Math.log10(timberIndex) + .32*Math.log10(bui)-1.64);
		if (fireLoadIndex <= 0){
			fireLoadIndex = 0;
		} else {
			fireLoadIndex = Math.pow(10, fireLoadIndex);
		}
	}
	
	/**
	 * Adjusts the timber spread index and the grass index based on the wind speed.
	 * 
	 * @param windSpeedMph the wind speed on the current day, measured in miles per hour
	 * @param adjustedFuelMoisture adjusted fuel moisture
	 * @param fineFuelMoisture fine fuel moisture
	 */
	public void adjustIndexesForWind(double windSpeedMph, double adjustedFuelMoisture, double fineFuelMoisture){
		if (windSpeedMph > 14){
			timberIndex = ((.009184*(windSpeedMph + 14.4)*Math.pow(33-adjustedFuelMoisture, 1.65) -3));
			grassIndex = ((.009184*(windSpeedMph + 6)*Math.pow(33-fineFuelMoisture, 1.65) -3));
			if (grassIndex >= 99){
				grassIndex = 99;
			}
			if (timberIndex >= 99){
				timberIndex = 99;
			}
		} else {
			timberIndex = ((.01312*(windSpeedMph + 6)*Math.pow(33-adjustedFuelMoisture, 1.65) -3));
			grassIndex = ((.01312*(windSpeedMph + 6)*Math.pow(33-fineFuelMoisture, 1.65) -3));
			if (timberIndex <= 1){
				timberIndex =1;
			}
			if (grassIndex <= 1){
				grassIndex = 1;
			}
		}
	}
	
	/**
	 * Calculates the adjusted fuel moisture index and the fine fuel moisture index.
	 * 
	 * @param adjustedFuelMoisture
	 * @param fineFuelMoisture
	 */
	public void setFuelMoistureIndexes(double adjustedFuelMoisture, double fineFuelMoisture){
		if (adjustedFuelMoisture > 30){
			adjustedFuelMoisture = 1;
		} else{
			return;
		} if (fineFuelMoisture > 30){
			fineFuelMoisture = 1;
			timberIndex = 1;
			grassIndex = 1;
		} else {
			return;	
		}
	}
	
	/**
	 * Calculates the adjusted fuel moisture based on the build up index and fine fuel moisture. 
	 * 
	 * @param bui
	 * @param fineFuelMoisture
	 * @return
	 */
	public double calculateAdjustedFuelMoisture(double bui, double fineFuelMoisture){
		return adjustedFuelMoisture = (.9*fineFuelMoisture + .5 + 9.5 *Math.E* (-bui/50));
	}
	
	/**
	 * Increases the build up index by the drying factor.
	 * 
	 * @param bui
	 * @param dryingFactor
	 * @return
	 */
	public double increaseBuiByDryingFactor(double bui, double dryingFactor){
		return bui = bui + dryingFactor;
	}

	/**
	 * Calculates the build up index based on precipitation amount.
	 * 
	 * @param precipitation
	 * @param lastBui
	 * @return
	 */
	public double calculateBui (double precipitation, double lastBui) {
		bui = -50*(Math.log(1-(-Math.E*(-lastBui/50))*Math.E*(-1.175*(precipitation - .1))));
		return bui;
	}
	
	/**
	 * Sets the build up index to 0 if it is less than 0.
	 * 
	 * @param bui
	 */
	public void adjustBui(double bui){
		if (bui < 0){
			bui = 0;
		}
	}

	public void printResults(){
		System.out.println("BUI: "+ bui);
		System.out.println("Fine Fuel Moisture: "+ fineFuelMoisture);
		System.out.println("Adjusted Fuel Moisture: "+ adjustedFuelMoisture);
		System.out.println("Timber Index: "+ timberIndex);
		System.out.println("Grass Index: "+ grassIndex);
		System.out.println("Fire Load Index: "+ fireLoadIndex);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter the dry-bulb temperature reading: ");
		dryTemp = input.nextDouble();
		
		System.out.println("Enter the wet-bulb temperature reading: ");
		wetTemp = input.nextDouble();
		
		System.out.println("Is there snow on the ground? y/n ");
		String snowTF = input.next();
		  if (snowTF.equalsIgnoreCase("y")) {
		    snowBool = true;
			System.out.println("Enter the preceding 24-hour precipitation amount, measured in inches: ");
			snowPrecip = input.nextDouble(); 
		  } else if (snowTF.equalsIgnoreCase("n")) {
		    snowBool = false;
		  }
		  
		System.out.println("Has it rained in the last 24-hours? y/n ");
		String rainTF = input.next();
		  if (rainTF.equalsIgnoreCase("y")) {
			  rainBool = true;
				System.out.println("Enter the preceding 24-hour precipitation amount, measured in inches: ");
				rainPrecip = input.nextDouble(); 
				} else if (rainTF.equalsIgnoreCase("n")) {
		    rainBool = false;
		  }

		System.out.println("Enter the current windspeed, measured in MPH: ");
		windSpeedMph = input.nextDouble();
		
		System.out.println("Enter yesterday's buildup index: ");
		lastBui = input.nextDouble();
		
		System.out.println("Enter the herb state value where 1 = Cured, 2 = Transition, and 3 = Green: ");
		herbStage = input.nextInt();
		
		input.close();
		
		FireDanger calc = new FireDanger(dryTemp, wetTemp, snowBool, snowPrecip, rainBool, rainPrecip, windSpeedMph, lastBui, herbStage);
	}
}

