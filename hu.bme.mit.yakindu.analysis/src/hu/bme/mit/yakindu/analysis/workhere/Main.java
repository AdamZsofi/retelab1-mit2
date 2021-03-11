package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.base.types.Event;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	static int nameCounter = 0;
	
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		ArrayList<Event> eventList = new ArrayList<Event>();
		ArrayList<VariableDefinition> varList = new ArrayList<VariableDefinition>();
		
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		
		// Iterate through model and save the events and variables
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof Event) {
				eventList.add((Event)content);
			} else if (content instanceof VariableDefinition) {
				varList.add((VariableDefinition)content);
			}
		}

		// Print out the code
		System.out.print("public class RunStatechart {\r\n" + 
			"	public static void main(String[] args) throws IOException {\r\n" + 
			"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
			"		s.setTimer(new TimerService());\r\n" + 
			"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
			"		s.init();\r\n" + 
			"		s.enter();\r\n" + 
			"		s.runCycle();\r\n" + 
			"\r\n" + 
			"		Scanner scanner = new Scanner(System.in);\r\n" + 
			"		String input;\r\n" + 
			"		boolean shouldExit = false;\r\n" + 
			"		\r\n" + 
			"		while(true) {\r\n" + 
			"			input = scanner.nextLine(); \r\n" + 
			"			switch(input) {\r\n");
		
		for(Event e : eventList) {
			String capitalizedEventName = e.getName().substring(0,1).toUpperCase() + e.getName().substring(1);
			System.out.print(
			"			case \""+e.getName()+"\":\r\n" + 
			"				s.raise"+capitalizedEventName+"();\r\n" + 
			"				break;\r\n"			
					);
		}
		
		System.out.print(
			"			case \"exit\":\r\n" + 
			"				shouldExit = true;\r\n" + 
			"				break;\r\n" + 
			"			default:\r\n" + 
			"				System.out.println(\"Unknown command\");\r\n" + 
			"				break;\r\n" + 
			"			}\r\n" + 
			"			\r\n" + 
			"			if(shouldExit) { \r\n" + 
			"				break;\r\n" + 
			"			}\r\n" + 
			"			s.runCycle();\r\n" + 
			"			print(s);\r\n" + 
			"		}\r\n" + 
			"			\r\n" + 
			"			System.exit(0);\r\n" + 
			"		}\r\n" + 
			"\r\n" + 
			"	public static void print(IExampleStatemachine s) {\r\n");
		
		for(VariableDefinition var : varList) {
			String shortName = var.getName().substring(0,1).toUpperCase();
			String capitalizedEventName = var.getName().substring(0,1).toUpperCase() + var.getName().substring(1);
			System.out.print(
			"		System.out.println(\""+shortName+" = \" + s.getSCInterface().get"+capitalizedEventName+"());\r\n");
		}
				
		System.out.print(
			"	}\r\n"+
			"}\r\n"
		);
		
		
		// Reading model
		/*// Exercise 2:
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getName().equals("")) {
					System.out.println("State is unnamed. Proposed name: State"+state.hashCode());
					nameCounter++;
				} else {
					System.out.println(state.getName());					
				}
			}
		}
		
		iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				System.out.println(transition.getSource().getName()+" -> "+transition.getTarget().getName());
			}
		}

		iterator = s.eAllContents();
		System.out.println("Traps:");
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if(state.getOutgoingTransitions().size()==0) {
					System.out.println(state.getName());					
				}
			}
		}
		*/
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
