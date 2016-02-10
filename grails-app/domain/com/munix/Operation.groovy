package com.munix

public enum Operation {
    ADD("Add"),
	SUBTRACT("Subtract")

	String description

	Operation(String description){
		this.description = description
	}

	@Override
	public String toString() {
		return description
	}

	public static Operation getTypeByName(String name) {
		return Operation.values().find { it.toString().equalsIgnoreCase name}
	}
}
