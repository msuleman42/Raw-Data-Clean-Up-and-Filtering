package com.bham.pij.assignments.candidates;

// Munir Suleman 1348560

import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import static java.nio.file.AccessMode.*;
import static java.nio.file.StandardOpenOption.*;

public class CleaningUp {
	
	public CleaningUp() {
		
	}
	
	public void cleanUpFile() {
		// Method to 'clean up' the original file and output to another file with the relevant information
		
		// read and write relative paths
		Path readPath = Paths.get("dirtycv.txt");
		Path writePath = Paths.get("cleancv.txt");
		
		InputStream input = null;
	    OutputStream output = null;
	    
	    try {
	    	// Buffer reader to read the lines coming from the input stream
	    	input = Files.newInputStream(readPath);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        output = Files.newOutputStream(writePath);
	        
	        ArrayList<String> oneCV = new ArrayList<String>();
	        String outputCV;
	        String line = reader.readLine();
	        
	        // Read line by line from the file until no more lines
	        while(line != null) {
	        	// if the line reads 'End', its the end of that CV, and the string arraylist collected up to that point makes up
	        	// the persons CV. The array list is formated using the lineForOutput method, and output to the write file
	        	if(line.equals("End")) {
	        		outputCV = lineForOutput(oneCV);
	        		outputCV = outputCV + System.getProperty("line.separator");
	                byte[] outputCVBytes = outputCV.getBytes();
	                output.write(outputCVBytes);
	                outputCV = "";
	                oneCV.clear();
	        		line = reader.readLine();
	        	}
	        	else {
	        		//System.out.println(line);
		        	oneCV.add(line);
		        	line = reader.readLine();
	        	}
	         }
	         
	         // close the input and output streams
	         input.close();
	         output.close();
	         /*for(int i = 0; i < oneCV.size(); i++) {
	        	 System.out.print(oneCV.get(i) + ",");
	         }*/
	    }
	    catch (IOException e) {
	        System.out.println(e);
	    }
	}

	public String lineForOutput(ArrayList<String> oneCV) {
		//Takes the array containing one CV's information and outputs in the string form needed with commas
		
		// Extract the number from the CV header (i.e. 1 from CV 1) and remove the header from array
		String firstLine = oneCV.get(0);
		char num = '0';
		for(int j = 0; j < firstLine.length(); j++) {
			if(Character.isDigit(firstLine.charAt(j))) {
				num = firstLine.charAt(j);
			}
		}
		oneCV.remove(0);
		
		// Make a new array consisting of the required information that is after the colon
		ArrayList<String> editOneCV = new ArrayList<String>();
		for(int i = 0; i < oneCV.size(); i++) {
			// temp string to see what type of information is in that array (ie if surname, if first name etc)
			String temp = oneCV.get(i).split(":")[0];
			// make the surname into the identifier before copying into new arraylist.
			// if its the address or the first name dont copy it into the new arraylist
			if(temp.equalsIgnoreCase("surname")) {
				editOneCV.add(oneCV.get(i).split(":")[1] + "000" + num);
			}
			else if(temp.equalsIgnoreCase("first name") || temp.equalsIgnoreCase("firstname")) {
				
			}
			else if(temp.equalsIgnoreCase("address")) {
				
			}
			else {
				editOneCV.add(oneCV.get(i).split(":")[1]);
			}
			temp = "";
		}
		
		// Convert the string array into a string separated by commas and return it
		String outputCV = "";
		for(int k = 0; k < editOneCV.size(); k++) {
            outputCV = outputCV + editOneCV.get(k) + ",";
        }
		return outputCV;
	}
}
