package com.spring5.sample.jco.service;

import org.springframework.stereotype.Component;

import com.spring5.sample.jco.exception.SapException;
import com.spring5.sample.jco.mapper.ISapMapper;
import com.spring5.sample.jco.util.SapConnector;

@Component
public class SAPServiceImpl implements SAPService{
	private SapConnector sapConnector;

	public SAPServiceImpl(SapConnector sapConnector){
		this.sapConnector = sapConnector;
	}
   
	public Object execute(String functionName, Object parameter, ISapMapper mapper) throws SapException {
		return this.sapConnector.executeSapInterface(functionName, parameter, mapper);
	}
}