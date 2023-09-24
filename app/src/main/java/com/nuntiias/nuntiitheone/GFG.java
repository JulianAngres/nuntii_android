package com.nuntiias.nuntiitheone;

// Java program to Convert ArrayList to
// String ArrayList using get() method

import java.util.ArrayList;

public class GFG {

    // Function to convert ArrayList<String> to String[]
    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // declaration and initialise String Array
        String str[] = new String[arr.size()];

        // ArrayList to Array Conversion
        for (int j = 0; j < arr.size(); j++) {

            // Assign each value to String array
            str[j] = arr.get(j);
        }

        return str;
    }

    /*// Driver code
    public static void main(String[] args)
    {

        // declaration and initialise ArrayList
        ArrayList<String>
                a1 = new ArrayList<String>();

        a1.add("Geeks");
        a1.add("for");
        a1.add("Geeks");

        // print ArrayList
        System.out.println("ArrayList: " + a1);

        // Get String Array
        String[] str = GetStringArray(a1);

        // Print Array elements
        System.out.print("String Array[]: "
                + Arrays.toString(str));
    }*/
}

