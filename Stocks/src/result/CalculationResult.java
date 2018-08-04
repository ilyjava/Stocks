package result;

import java.util.ArrayList;

public class CalculationResult {
	
	private int value;
	private ArrayList<Allocations> allocations;
	
	
	
	public CalculationResult(int value, ArrayList<Allocations> allocations) {
		super();
		this.value = value;
		this.allocations = allocations;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public ArrayList<Allocations> getAllocations() {
		return allocations;
	}
	public void setAllocations(ArrayList<Allocations> allocations) {
		this.allocations = allocations;
	}
}
