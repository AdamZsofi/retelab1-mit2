package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;

// generált kód:
public class RunStatechart {
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();

		Scanner scanner = new Scanner(System.in);
		String input;
		boolean shouldExit = false;
		
		while(true) {
			input = scanner.nextLine(); 
			switch(input) {
			case "start":
				s.raiseStart();
				break;
			case "whiteStep":
				s.raiseWhiteStep();
				break;
			case "blackStep":
				s.raiseBlackStep();
				break;
			case "exit":
				shouldExit = true;
				break;
			default:
				System.out.println("Unknown command");
				break;
			}
			
			if(shouldExit) { 
				break;
			}
			s.runCycle();
			print(s);
		}
			
			System.exit(0);
		}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTimeLeft());
		System.out.println("B = " + s.getSCInterface().getBlackTimeLeft());
	}
}


/*
public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();

		Scanner scanner = new Scanner(System.in);
		String input;
		boolean shouldExit = false;
		
		while(true) {
			input = scanner.nextLine(); 
			switch(input) {
			case "start":
				s.raiseStart();
				break;
			case "black":
				s.raiseBlack();
				break;
			case "white":
				s.raiseWhite();
				break;
			case "exit":
				shouldExit = true;
				break;
			default:
				System.out.println("Unknown command");
				break;
			}
			
			if(shouldExit) { 
				break;
			}
			s.runCycle();
			print(s);
		}
		
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}*/
