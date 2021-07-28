package com.bham.pij.assignments.candidates;

//Munir Suleman 1348560

public class JobCandidatesMain {

	public static void main(String[] args) {
		// Question 1 ------------------------------------------------------------------------
		CleaningUp cleaningProcess = new CleaningUp();
		cleaningProcess.cleanUpFile();
		System.out.println("File 'cleancv.txt' has been created");
		
		
		// Question 2 ------------------------------------------------------------------------
		CandidatesToInterview interviewCandidates = new CandidatesToInterview();
		interviewCandidates.findCandidates();
		System.out.println("File 'to-interview.txt' has been created");
		
		// Question 3 ------------------------------------------------------------------------
		// CandidatesToInterview interviewCandidates = new CandidatesToInterview();
		interviewCandidates.candidatesWithExperience();
		System.out.println("File 'to-interview-experience.txt' has been created");
		
		// Question 4 ------------------------------------------------------------------------
		// CandidatesToInterview interviewCandidates = new CandidatesToInterview();
		interviewCandidates.createCSVFile();
		System.out.println("File 'to-interview-table-format.csv' has been created");
		interviewCandidates.createReport();
	}

}
