package com.spring5.sample.jco.util;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoRepository;
import com.spring5.sample.jco.exception.SapException;
import com.spring5.sample.jco.mapper.ISapMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Component;
 
@Component
public class SapConnector
{
	private String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";
	private JCoDestination jcoDestination;

	public SapConnector() {}

	public SapConnector(Properties connProp) throws SapException
	{
		String poolName = "ABAP_AS_WITH_POOL";
		FileOutputStream fos = null;
		try {
			if (connProp.getProperty("jco.pool.name") == null) {
				poolName = this.ABAP_AS_POOLED;
			} else {
				poolName = this.ABAP_AS_POOLED + "_" + connProp.getProperty("jco.pool.name", "default");
			}

			File conInfoFile = new File(poolName + ".jcoDestination");

			removeJcoDestinationFile(conInfoFile);

			if (!conInfoFile.isFile()) {
				fos = new FileOutputStream(conInfoFile, false);
				this.ABAP_AS_POOLED = poolName;
				connProp.store(fos, this.ABAP_AS_POOLED);
			}

			try{
				if (connProp != null)
					connProp.clear();
				if (fos != null) {
					fos.close();
				}
			}catch (IOException e) {}
				this.ABAP_AS_POOLED = poolName;
			}catch (IOException e){
				throw new SapException("JCO0000", "SAP Interface is non-initialized.", e);
			} finally {
				try {
					if (connProp != null)
						connProp.clear();
					if (fos != null) {
						fos.close();
					}
				}catch (IOException e) {}
			}
		
			initJcoConnectionManager(poolName);
	}

	private void initJcoConnectionManager(String poolName) throws SapException{
		try{
			this.jcoDestination = JCoDestinationManager.getDestination(poolName);
		} catch (JCoException e) {
			throw new SapException("JCO0001", poolName + " not found in JCO", e);
		}
	}

	public JCoDestination getJcoDestination() throws SapException {
		if (this.jcoDestination == null) {
			initJcoConnectionManager(this.ABAP_AS_POOLED);
		}
		
		return this.jcoDestination;
	}

	public Object executeSapInterface(String procedure, Object param, ISapMapper mapper) throws SapException{
		try{
			JCoDestination destination = getJcoDestination();

			JCoRepository repository = destination.getRepository();
			JCoFunctionTemplate template = repository.getFunctionTemplate(procedure);

			if (template == null) {
				throw new SapException("JCO0001", procedure + " not found in SAP.");
			}
 
			JCoFunction function = template.getFunction();

			if (function == null) {
				throw new SapException("JCO0001", procedure + " not found in SAP.");
			}

			function = mapper.mappingRequestObjectToSapData(function, param);
			function.execute(destination);

			mapper.verifySapResult(function);

			return mapper.mappingResponseSapDataToObject(function);
		}catch (JCoException e) {
			this.jcoDestination = null;
			throw new SapException("JCO0002", "JCo Function execute error.", e);
		} catch (SapException e) {
			this.jcoDestination = null;
			throw e;
		}
	}

	protected boolean removeJcoDestinationFile(File destinationFile) throws SapException{
		if (destinationFile.isFile()) {
			return destinationFile.delete();
		}
		
		return true;
	}

	protected boolean removeJcoDestinationFile(String jcoDestinationFullPath)throws SapException{
		return removeJcoDestinationFile(new File(jcoDestinationFullPath));
	}
}