//Munir Suleman

import java.nio.file.*;
import java.util.*;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import static java.nio.file.AccessMode.*;
import static java.nio.file.StandardOpenOption.*;

public class CandidatesToInterview {
	
	public CandidatesToInterview() {
		
	}
	
	public void findCandidates() {
		// Method reads from the 'clean' file and distinguishes which candidates are moving on to interview. These cadidates info
		// is written to the new 'to-interview' file. 
		
		// read and write relative paths
		Path readPath = Paths.get("cleancv.txt");
		Path writePath = Paths.get("to-interview.txt");
		
		InputStream input = null;
	    OutputStream output = null;
	    
	    //
	    try {
	    	// Buffer reader to read the lines coming from the input stream
	    	input = Files.newInputStream(readPath);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	        output = Files.newOutputStream(writePath);
	        
	        String line = reader.readLine();
	        
	        // Read line by line from the file until no more lines
	        while(line != null) {
	        	String candidateInfo = line;
	        	boolean candidateAcc = candidateAccepted(candidateInfo);
	        	// if the boolean is true then it needs to be written to the output file. if not candidate not selected
	        	if(candidateAcc == true) {
	        		String outputCandidate = line.replace(",", " ") + System.getProperty("line.separator");
	        		byte[] outputCandidateBytes = outputCandidate.getBytes();
	                output.write(outputCandidateBytes);
	                outputCandidate = "";
	                candidateAcc = false;
	        		line = reader.readLine();
	        	}
	        	else {
	        		line = reader.readLine();
	        	}
	        }
	        	// close the input and output streams
		        input.close();
		        output.close();
	    }
	    catch(IOException e) {
	    	System.out.println(e);
	    }
	}
	
	public void candidatesWithExperience() {
		// Filters out the candidates from the 'to-interview' file with 5+ years of experience in their current role and 
		// outputs them into another file 'to-interview-experience'
		
		// read and write relative paths
		Path readPath = Paths.get("to-interview.txt");
		Path writePath = Paths.get("to-interview-experience.txt");

		InputStream input = null;
		OutputStream output = null;

		//
		try {
			// Buffer reader to read the lines coming from the input stream
			input = Files.newInputStream(readPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			output = Files.newOutputStream(writePath);

			String line = reader.readLine();

			// Read line by line from the file until no more lines
			while(line != null) {
				String experienceInfo = line;
				String expOutput = enoughExperience(experienceInfo);
				
				// If the expOutput returns 0 then the candidate doesnt have enough experience so moves to next line
				// Otherwise write the expOutput string into the output file
				if(expOutput.equals("0")) {
					line = reader.readLine();
				}
				else {
					String outputExperience = expOutput + System.getProperty("line.separator");
					byte[] outputExperienceBytes = outputExperience.getBytes();
					output.write(outputExperienceBytes);
					outputExperience = "";
					line = reader.readLine();
				}
			}
			// close the input and output streams
			input.close();
			output.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void createCSVFile() {
		// Method that reads the to-interview file and outputs the information into a CSV file
		
		Path readPath = Paths.get("to-interview.txt");
		String csvFileName = "to-interview-table-format.csv";
		String lineSeparator = "\n";
		ArrayList<String[]> allCandidates = new ArrayList<String[]>();
		String[] header = {"Identifier","Qualification","Position1","Experience1","Position2","Experience2","eMail"};
		
		allCandidates.add(header);
		InputStream input = null;
		FileWriter fileWriter = null;

		// Reads the file line by line and writes them into the csv file
		try {
			// Buffer reader to read the lines coming from the input stream
			input = Files.newInputStream(readPath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			// Filewriter to write into the CSV file
			fileWriter = new FileWriter(csvFileName);
			// Write header into the file
			String headerCSV = arrayToString(header);
			fileWriter.append(headerCSV);
			fileWriter.append(lineSeparator);
			
			String line = reader.readLine();
			
			while(line != null) {
				String personInfo = line;
				personInfo = spacesToCommas(personInfo);
				String[] personInfoArray = stringToArray(personInfo);
				allCandidates.add(personInfoArray);
				String outputCSV = arrayToString(personInfoArray);
				fileWriter.append(outputCSV);
				fileWriter.append(lineSeparator);
				line = reader.readLine();
			}
			
			// close the input and output streams
			input.close();
			fileWriter.flush();
			fileWriter.close();
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}
	
	public void createReport() {
		// Method reads form the csv file and displays the specified data on the console in a table format
		
		BufferedReader fileReader = null;
		ArrayList<ArrayList<String>> fromCSV = new ArrayList<ArrayList<String>>();
		
		try {
			String line = "";
			fileReader = new BufferedReader(new FileReader("to-interview-table-format.csv"));
			line = fileReader.readLine();
			// Split the read line from the csv file and put into an arrayList. Then read next line
			while(line != null) {
				String[] arrayToken = line.split(",");
				ArrayList<String> listToken = new ArrayList<String>(Arrays.asList(arrayToken));
				fromCSV.add(listToken);
				line = fileReader.readLine();
			}
			fileReader.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		// Remove the Position2 and Experience2 columns 
		for(int m = 0; m < fromCSV.size(); m++) {
			fromCSV.get(m).remove(4);
			fromCSV.get(m).remove(4);
		}
		
		// Change Position1 and Experience1 headings to Position and Experience
		fromCSV.get(0).set(2, "Position");
		fromCSV.get(0).set(3, "Experience");
		// Display on console in the specific format
		for(int k = 0; k < fromCSV.size(); k++) {
			if(k == 0) {
				System.out.println(String.format("%-15s %-30s %-22s %-12s %-25s", fromCSV.get(k).get(0), fromCSV.get(k).get(1), 
						  														  fromCSV.get(k).get(2), fromCSV.get(k).get(3),
						  														  fromCSV.get(k).get(4)));
			}
			else {
				System.out.println(String.format("%-15s %-30s %-22s %-12s %-25s", fromCSV.get(k).get(0), fromCSV.get(k).get(1), 
																				  fromCSV.get(k).get(2), "    " + fromCSV.get(k).get(3),
																				  fromCSV.get(k).get(4)));
			}
		}
	}
	
	public boolean candidateAccepted(String candidateInfo) {
		// Method finds if the keywords are in the candidateInfo string and returns true if the candidate has both
		// a degree and some experience in the relevant field
		
		// The keywords words
		String[] keywordsDegree = {"Degree in Computer Science", "Masters in Computer Science"};
		String[] keywordsExperience = {"Data Analyst", "Programmer", "Computer programmer", "Operator"};
		
		// String made into an array then arraylist.
		String[] onePersonArray = candidateInfo.split(",");
		ArrayList<String> onePersonList = new ArrayList<String>(Arrays.asList(onePersonArray));
		boolean degreeCheck = false;
		boolean experienceCheck = false;
		
		// Check if one of the degree keywords is present in the candidates cv info
		for(int m = 0; m < keywordsDegree.length; m++) {
			if(onePersonList.toString().toLowerCase().contains(keywordsDegree[m].toLowerCase())) {
				degreeCheck = true;
			}
		}
		
		// Check if one of the experience keywords is present in the candidates cv info
		for(int n = 0; n < keywordsExperience.length; n++) {
			if(onePersonList.toString().toLowerCase().contains(keywordsExperience[n].toLowerCase())) {
				experienceCheck = true;
			}
		}
		
		// if both the degree boolean and experience boolean are true, then candidate meets the requirement for interview
		// and can thus return true. Else no interview, returns false
		if(degreeCheck == true && experienceCheck == true) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String enoughExperience(String experienceInfo) {
		// Method determines if the candidate has enough experience in their current role. If they do it returns the required 
		// info. If not it returns 0 which means the candidate doesn't have the necessary experience
		
		// Converting the string with spaces back into being separated by commas to put into an array
		String experience = spacesToCommas(experienceInfo);
		String[] experienceArray = experience.split(",");
		ArrayList<String> experienceList = new ArrayList<String>(Arrays.asList(experienceArray));
		
		// Most recent experience in the 4th slot (3rd in array)
		String currentExperience = experienceList.get(3);
		// String into integer
		int candidateExp = Integer.parseInt(currentExperience);
		// boolean is false unless the experience is of 5+ years
		boolean enoughExpCheck = false;
		if(candidateExp > 5) {
			enoughExpCheck = true;
		}
		
		// Return the string with the identifier and experience if they have enough experience in current role, else return 0
		String expOutput;
		if(enoughExpCheck == true) {
			expOutput = experienceList.get(0) + " " + experienceList.get(3);
			return expOutput;
		}
		else {
			return "0";
		}
	}
	
	public String spacesToCommas(String spacedInfo) {
		// Method reverts back the info with spaces to the info with commas
		
		String lineComma = spacedInfo.replace(" ", ",");
		String lineCommaEdited;
		// Makes the qualification with spaces instead of commas
		if(lineComma.toLowerCase().contains("degree,in,computer,science")) {
			lineCommaEdited = lineComma.replaceAll("(?i)degree,in,computer,science", "Degree in Computer Science");
		}
		else if(lineComma.toLowerCase().contains("masters,in,computer,science")) {
			lineCommaEdited = lineComma.replaceAll("(?i)masters,in,computer,science", "Masters in Computer Science");
		}
		else {
			lineCommaEdited = lineComma;
		}
		
		String[] lineArray = lineCommaEdited.split(",");
		ArrayList<String> lineArrayList = new ArrayList<String>(Arrays.asList(lineArray));
		
		// Join the experience/job title into 1
		int m = 3;
		boolean wordCheck = isWord(lineArrayList.get(m));
		
		while(wordCheck == true) {
			String i = lineArrayList.get(m-1);
			String j = lineArrayList.get(m);
			i = i.concat(" ").concat(j);
			lineArrayList.set(m-1, i);
			lineArrayList.remove(m);
			wordCheck = isWord(lineArrayList.get(m));
		}
		
		boolean emailCheck = lineArrayList.get(4).contains("@");
		
		if(emailCheck == false) {
			m = 5;
			wordCheck = isWord(lineArrayList.get(m));
			while(wordCheck == true) {
				String i = lineArrayList.get(m-1);
				String j = lineArrayList.get(m);
				i = i.concat(" ").concat(j);
				lineArrayList.set(m-1, i);
				lineArrayList.remove(m);
				wordCheck = isWord(lineArrayList.get(m));
			}
		}
		
		// Form the string from the arrayList
		StringBuilder commaSB = new StringBuilder();
		for(String s : lineArrayList) {
			commaSB.append(s);
			commaSB.append(",");
		}
		String commaInfo = commaSB.toString();
		
		return commaInfo;
	}
	
	public String[] stringToArray(String personInfoCommas) {
		// Method recieves the info as a string with commas and organises it into an array where the eMail is in the last slot
		String[] orderedForCSV = new String[7];
		String[] personInfoCommasArray = personInfoCommas.split(",");
		for(int i = 0; i < personInfoCommasArray.length; i++) {
			if(personInfoCommasArray[i].contains("@")) {
				String tempInfo = personInfoCommasArray[i];
				orderedForCSV[6] = tempInfo;
			}
			else {
				String tempInfo = personInfoCommasArray[i];
				orderedForCSV[i] = tempInfo;
			}
		}
		
		for(int j = 0; j < orderedForCSV.length; j++) {
			if(orderedForCSV[j] == null) {
				orderedForCSV[j] = "";
			}
		}
		
		return orderedForCSV;
	}
	
	public String arrayToString(String[] personInfoArray) {
		StringBuilder temp = new StringBuilder();
		temp.append(personInfoArray[0]);
		for(int i = 1; i < personInfoArray.length; i++) {
			temp.append("," + personInfoArray[i]);
		}
		String outputToCSV = temp.toString();
		return outputToCSV;
	}
	
	public boolean isWord(String element) {
		char[] chars = element.toCharArray();
		for(char c : chars) {
			if(!Character.isLetter(c)) {
				return false;
			}
		}
		return true;
	}

}
