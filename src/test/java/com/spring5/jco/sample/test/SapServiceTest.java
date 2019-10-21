package com.spring5.jco.sample.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.spring5.sample.jco.exception.SapException;
import com.spring5.sample.jco.mapper.AbstractSapMapper;
import com.spring5.sample.jco.service.SAPService;
import com.spring5.sample.jco.service.model1.InputModel;
import com.spring5.sample.jco.service.model1.OutputModel;
import com.spring5.sample.jco.util.SapUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
//@ContextConfiguration(classes = SAPService.class)
public class SapServiceTest {

	@Autowired
	private SAPService sapService;
	
	@Test
	public void testSapService() throws SapException{

		InputModel inputModel = new InputModel();
		inputModel.setI_S25CSQ("파라미터값");
		
		OutputModel outputModel = new OutputModel();
		outputModel = (OutputModel) sapService.execute("Fuction 이름", inputModel, new SapMapper());

	}
	
	class SapMapper extends AbstractSapMapper {

		@Override
		public JCoFunction mappingRequestObjectToSapData(JCoFunction function, Object param) throws SapException {
			
			//RFC의 Request가 일반 형식일때
			InputModel inputModel = new InputModel();
			setParametersToSapImportParam(inputModel, function);

			//RFC의 Request가 테이블 형식일때
			/*JCoTable table = function.getTableParameterList().getTable("테이블명");
			table = convertObjectListToSapTable(body.getT_DATA(), table);*/
			
			
			//RFC의 Request가 Stucture 형식일때
			/*JCoStructure structure = function.getImportParameterList().getStructure("구조체명");
			structure.setValue(arg0, arg1);*/
			
			return function; 
		}

		@Override
		public Object mappingResponseSapDataToObject(JCoFunction function) throws SapException {
			
			//RFC의 Response가 일반  형식일때
			/*OutputModel outputModel = new OutputModel();
			outputModel.setO_USER_ID(function.getExportParameterList().getString("O_USER_ID"));*/
			
			//RFC의  Response가 테이블 형식일때
			/*JCoTable table = function.getTableParameterList().getTable("테이블명");
			List<OutputModel> outputModelList = SapUtil.convertSapTableToObjectList(table, OutputModel.class);*/
			
			
			//RFC의  Response가 Stucture 형식일때
			/*JCoStructure structure = function.getExportParameterList().getStructure("구조체명");
			OutputModel outputModel = SapUtil.extractDataFromSapStructure(structure, new OutputModel());*/ 
			

			return new OutputModel();
		}

		@Override
		public void verifySapResult(JCoFunction function) throws SapException {
			/*
			 * 
			 * Sap통신 결과에 대해 에러메세지 및 확인하는 로직 추가
			 * 
			 */
		}
		
	}
}
