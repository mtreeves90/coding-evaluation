package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

public abstract class Organization {

    private final static int RANDOM_ID_NUM_RANGE = 1000000;
    
	private Position root;
    private Map<String, Stack<Position>> organizationMap;
	
    /*
     * Add each position into the map to allow the retrievel of each position in constant time
     * In this example, only one employee exists for each position, however, a stack is placed in the value for the map to accomodate for a position being open for multiple employees (Ex: multiple software engineers)
     */
	public Organization() {
		root = createOrganization();
        organizationMap = new HashMap<>();
        initiatePositions(root);
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
        Optional<Position> optionalPosition = Optional.empty();
        Stack<Position> positionStack = organizationMap.get(title);
        
        //Pop from the stack for position if position is open to hire employee
        if (positionStack.peek() != null) {
            optionalPosition = Optional.of(positionStack.pop());
        }
        
        if (optionalPosition.isPresent()) {
            Position position = optionalPosition.get();
            //Generate random ID number for new employee
            Random random = new Random();
            position.setEmployee(Optional.of(new Employee(random.nextInt(RANDOM_ID_NUM_RANGE), person)));
        }
        
		return optionalPosition;
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
    
    private void initiatePositions(Position position) {
        Stack<Position> currentStack;
        String title = position.getTitle();
        currentStack = organizationMap.getOrDefault(title, new Stack<>());
        
        currentStack.push(position);
        organizationMap.put(title, currentStack);
                                   
        Collection<Position> directReports = position.getDirectReports();
        
        //Depth first search to specify each position
        for (Position directReport : directReports) {
            initiatePositions(directReport);
        }
    }
}
