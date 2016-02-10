package com.munix

public enum PriceType {
	WHOLESALE("Wholesale"),
	RETAIL("Retail")
	
	String description
	
	PriceType(String description){
		this.description = description
	}
	
	@Override
	public String toString() {
		return description
	}
	
	public static PriceType getTypeByName(String name) {
		return PriceType.values().find { it.toString().equalsIgnoreCase name}
	}
}
