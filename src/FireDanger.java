
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
	public double fineFuelMoisture;
	public double fireLoadIndex;
	public double adjustedFuelMoisture;


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
	 * @param fineFuelMoisture 
	 * @param fireLoadIndex 
	 */
	public FireDanger(double dryTemp, double wetTemp, boolean snowBool, double snowPrecip, boolean rainBool, double rainPrecip, double windSpeedMph, double lastBui, int herbStage, double fineFuelMoisture, double fireLoadIndex, double adjustedFuelMoisture){
		FireDanger.dryTemp = dryTemp;
		FireDanger.wetTemp = wetTemp;
		FireDanger.snowBool = snowBool;
		FireDanger.snowPrecip = snowPrecip;
		FireDanger.rainBool = rainBool;
		FireDanger.rainPrecip = rainPrecip;
		FireDanger.windSpeedMph = windSpeedMph;
		FireDanger.lastBui = lastBui;		
		FireDanger.herbStage = herbStage;
		this.fineFuelMoisture = fineFuelMoisture;
		this.fireLoadIndex = fireLoadIndex;

		getSnow(snowBool, rainBool, rainPrecip, snowPrecip, lastBui, dryTemp, wetTemp, herbStage, windSpeedMph, fineFuelMoisture, fireLoadIndex, adjustedFuelMoisture);
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
	public void getSnow (boolean snowBool, boolean rainBool, double rainPrecip, double snowPrecip, double lastBui, double dryTemp, double wetTemp, int herbStage, double windSpeedMph, double fineFuelMoisture, double fireLoadIndex, double adjustedFuelMoisture){
		if (snowBool = true){
			double timberIndex = 0;
			double grassIndex = 0;
				if (snowPrecip > .1){
					double bui = calculateBui(snowPrecip, lastBui);
						if (bui < 0){
							bui = 0;
						}
				printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
				}
		} else {
			fineFuelMoisture = calculateFFM(dryTemp, wetTemp);
			double dryingFactor = calculateDryingFactor();
			fineFuelMoisture = adjustFineFuelForHerbStage(herbStage, fineFuelMoisture);
			getRain(snowBool, rainBool, rainPrecip, snowPrecip, lastBui, dryTemp, wetTemp, herbStage, dryingFactor, windSpeedMph, fineFuelMoisture);
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
	public void getRain(boolean snowBool, boolean rainBool, double rainPrecip, double snowPrecip, double lastBui, double dryTemp, double wetTemp, int herbStage, double dryingFactor, double windSpeedMph, double fineFuelMoisture){
		if (rainBool = true){
			if (rainPrecip > .1){
				double bui = increaseBuiByDryingFactor(lastBui, dryingFactor);
				double adjustedFuelMoisture = calculateAdjustedFuelMoisture(bui, fineFuelMoisture);
				fineFuelMoisture = calculuateNewFFM(fineFuelMoisture);
				double grassIndex = adjustGrassIndexForWind(windSpeedMph, fineFuelMoisture);
				double timberIndex = adjustTimberIndexForWind(windSpeedMph, adjustedFuelMoisture);

					if (timberIndex > 0 && bui > 0){
					double fireLoadIndex = calculateFireLoadIndex(bui, timberIndex);
					printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
					}
				printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
				
			} else{
				double grassIndex = adjustGrassIndexForWind(windSpeedMph, fineFuelMoisture);
				double timberIndex = adjustTimberIndexForWind(windSpeedMph, adjustedFuelMoisture);
					if (timberIndex > 0 && bui > 0){
					calculateFireLoadIndex(bui, timberIndex);
					printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
					}
				printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
			}
	} else {
			increaseBuiByDryingFactor(lastBui, dryingFactor);
			double adjustedFuelMoisture = calculateAdjustedFuelMoisture(bui, fineFuelMoisture);
			fineFuelMoisture = calculuateNewFFM(fineFuelMoisture);
			double grassIndex = adjustGrassIndexForWind(windSpeedMph, fineFuelMoisture);
			double timberIndex = adjustTimberIndexForWind(windSpeedMph, adjustedFuelMoisture);
			
				if (timberIndex > 0 && bui > 0){
				calculateFireLoadIndex(bui, timberIndex);
				printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
				}
			printResults(bui, fineFuelMoisture, adjustedFuelMoisture, timberIndex, grassIndex, fireLoadIndex);
			}
	}
	
	
	/**
	 * Calculate the fine fuel moisture.
	 * 
	 * @param dryTemp the dry-bulb reading
	 * @param wetTemp the wet-bulb reading
	 * @return 
	 * @return 
	 */
	public double calculateFFM(double dryTemp, double wetTemp){
		double dif = dryTemp - wetTemp;
		for(int i = 0; i < 3; i++){
			if (dif <= arrayC[i]){
				calculateFFM(dif, i);
				fineFuelMoisture = (arrayB[i])*Math.E*(arrayA[i]*dif);
			}
		}	return fineFuelMoisture;
	}
	


	/**
	 * Adjusts the fine fuel moisture based on the herb stage.
	 * 
	 * @param herbStage the herb stage integer where  1 = Cured, 2 = Transition, and 3 = Green
	 * @return 
	 */
	public double adjustFineFuelForHerbStage(int herbStage, double fineFuelMoisture){
		if (fineFuelMoisture <= 1){
			return fineFuelMoisture = 1;
		} if (herbStage > 1) {
			int i = 2;
			while (i <= herbStage){
				fineFuelMoisture = fineFuelMoisture + .05;
				i++;
			} return fineFuelMoisture;
		}
		return fineFuelMoisture;
	}

	/**
	 * Calculates the drying factor based on the fine fuel moisture.
	 * 
	 * @return the dryingFactor
	 */
	public double calculateDryingFactor(){
		int i = 0;
		while (i < 6){
			if (fineFuelMoisture >= this.arrayD[i]){
				dryingFactor = this.arrayD[i];
				return dryingFactor;
			} else if (fineFuelMoisture <= this.arrayD[i]){
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
	 * @return 
	 */
	public double calculateFireLoadIndex(double bui, double timberIndex){
		fireLoadIndex = Math.pow(10, 1.75*Math.log10(timberIndex) + .32*Math.log10(bui)-1.64);
			if (fireLoadIndex <= 0){
				return fireLoadIndex = 0;
			} else {
				return fireLoadIndex = Math.pow(10, fireLoadIndex);
			}
	}
	
	/**
	 * The grass index changes based on the wind speed.
	 * 
	 * @param windSpeedMph today's wind speed measured in MPH.
	 * @param fineFuelMoisture calculated FFM.
	 * @return new grass index calculation based on effecting winds.
	 */
	public double adjustGrassIndexForWind(double windSpeedMph, double fineFuelMoisture){
		if (windSpeedMph > 14){
			grassIndex = ((.009184*(windSpeedMph + 6)*Math.pow(33-fineFuelMoisture, 1.65) -3));
				if (grassIndex >= 99){
					return grassIndex = 99;
				} else {
					return grassIndex;
				}
		} else {
			grassIndex = ((.01312*(windSpeedMph + 6)*Math.pow(33-fineFuelMoisture, 1.65) -3));
				if (grassIndex <= 1){
					return grassIndex = 1;
				}else {
					return grassIndex;
				}
			}
	}
	
	/**
	 * The timber index changes with based on the wind speed.  
	 * 
	 * @param windSpeedMph today's wind speed measured in MPH.
	 * @param adjustedFuelMoisture calcualted FFM.
	 * @return new timber index calculation based on effecting winds.
	 */
	public double adjustTimberIndexForWind(double windSpeedMph, double adjustedFuelMoisture){
		if (windSpeedMph > 14){
			timberIndex = ((.009184*(windSpeedMph + 14.4)*Math.pow(33-adjustedFuelMoisture, 1.65) -3));
				if (timberIndex >= 99){
					return timberIndex = 99;
				} else {
					return timberIndex;
				}
		} else {
			timberIndex = ((.01312*(windSpeedMph + 6)*Math.pow(33-adjustedFuelMoisture, 1.65) -3));
				if (timberIndex <= 1){
					return timberIndex = 1;
				} else {
					return timberIndex;
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
	 * Moisture effects the fuel moisture which is taken into consideration and calculated.
	 * 
	 * @param bui yesterday's or today's bui depending on precipitation.
	 * @param fineFuelMoisture calculated FFM.
	 * @return adjusted fuel moisture is returned.
	 */
	public double calculateAdjustedFuelMoisture(double bui, double fineFuelMoisture){
		adjustedFuelMoisture = (.9*fineFuelMoisture + .5 + 9.5 *Math.E* (-bui/50));
			if (adjustedFuelMoisture > 30){
			return adjustedFuelMoisture = 1;
			} else {
			return adjustedFuelMoisture;
			}
	}
	
	public double calculuateNewFFM(double fineFuelMoisture){ 
		if (fineFuelMoisture > 30){
			fineFuelMoisture = 1;
			double timberIndex = 1;
			double grassIndex = 1;
			return fineFuelMoisture;
		} else {
			return fineFuelMoisture;
		}

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
		return bui = -50*(Math.log(1-(-Math.E*(-lastBui/50))*Math.E*(-1.175*(precipitation - .1))));
	}
	

	public void printResults(double bui, double fineFuelMoisture, double adjustedFuelMoisture, double timberIndex, double grassIndex, double fireLoadIndex){
		System.out.println("BUI: "+ bui);
		System.out.println("Fine Fuel Moisture: "+ fineFuelMoisture);
		System.out.println("Adjusted Fuel Moisture: "+ adjustedFuelMoisture);
		System.out.println("Timber Index: "+ timberIndex);
		System.out.println("Grass Index: "+ grassIndex);
		System.out.println("Fire Load Index: "+ fireLoadIndex);
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		double fineFuelMoisture = 99;
		double fireLoadIndex = 0;
		double adjustedFuelMoisture = 99;

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
		
		FireDanger calc = new FireDanger(dryTemp, wetTemp, snowBool, snowPrecip, rainBool, rainPrecip, windSpeedMph, lastBui, herbStage, fineFuelMoisture, fireLoadIndex, adjustedFuelMoisture);
	}
}

