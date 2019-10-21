package com.spring5.sample.jco.mapper;

import com.sap.conn.jco.JCoFunction;
import com.spring5.sample.jco.exception.SapException;

public interface ISapMapper {
	public abstract JCoFunction mappingRequestObjectToSapData(JCoFunction paramJCoFunction, Object paramObject) throws SapException;
			  
	public abstract Object mappingResponseSapDataToObject(JCoFunction paramJCoFunction)	throws SapException;
			  
	public abstract void verifySapResult(JCoFunction paramJCoFunction) throws SapException;
}
