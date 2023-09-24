package com.nuntiias.nuntiitheone;

import java.util.Random;

public class ConnectionDistanceComparator {

    public static boolean LessThanKm (boolean beginner, String oldLat, String oldLon, String newLat, String newLon) {

        boolean J = true;

        try{
            if (beginner == true) {
                J = false;
            } else {

                float floatOldLat = Float.parseFloat(oldLat);
                float floatOldLon = Float.parseFloat(oldLon);
                float floatNewLat = Float.parseFloat(newLat);
                float floatNewLon = Float.parseFloat(newLon);

                if (ConnectionDistanceComparator.L_orth(floatOldLat, floatNewLat, floatOldLon, floatNewLon) < 130) {
                    J = false;
                } else {
                    J = true;
                }
            }
        } catch (Exception e) {
            J = false;
        }

        return J;

    }

    public static double L_orth(float phi_1, float phi_2, float lambda_1, float lambda_2) {
        phi_1 = (float) ((Math.PI / 180) * phi_1);
        phi_2 = (float) ((Math.PI / 180) * phi_2);
        lambda_1 = (float) ((Math.PI / 180) * lambda_1);
        lambda_2 = (float) ((Math.PI / 180) * lambda_2);
        double v = 6373.3 * Math.acos(
                Math.cos(phi_1) * Math.cos(phi_2) * Math.cos(lambda_1 - lambda_2) + Math.sin(phi_1) * Math.sin(phi_2)
        );

        // Create a new Random object
        Random rand = new Random();

        // Generate a random integer between 1 and 100 (inclusive)
        int randomNumber = rand.nextInt(1000000);
        double tilfeldig = (double)randomNumber*1e-6;

        return v + tilfeldig;
    }

}
