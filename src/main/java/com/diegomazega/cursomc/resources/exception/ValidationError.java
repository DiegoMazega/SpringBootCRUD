package com.diegomazega.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {
	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<>();
	
	public ValidationError(Integer id, String msg, Long timeStamp) {
		super(id, msg, timeStamp);
	}
	
	public List<FieldMessage> getErrors(){
		return this.errors;
	}
	
	public void addError(String fieldName, String message) {
		this.errors.add(new FieldMessage(fieldName, message)); 
	}
}
