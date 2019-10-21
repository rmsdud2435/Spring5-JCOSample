package com.spring5.sample.jco.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.DirectFieldAccessor;

import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.spring5.sample.jco.exception.SapException;

public abstract class AbstractSapMapper implements ISapMapper{
	
	public AbstractSapMapper() {}
  
	protected <T> T covertSapStructureToObject(JCoStructure structure, Class<T> type) throws SapException{
		try{
			T t = type.newInstance();
			DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(t);

			for (JCoFieldIterator iter = structure.getFieldIterator(); iter.hasNextField();) {
				JCoField field = iter.nextField();
				fieldAccessor.setPropertyValue(field.getName(), field.getValue());
			}
			
			return t;
		} catch (InstantiationException e) {
			throw new SapException("COV001", "", e);
		} catch (IllegalAccessException e) {
			throw new SapException("COV001", "", e);
		}
	}

	protected <T> List<T> convertSapTableToObjectList(JCoTable table, Class<T> type) throws SapException{
		try{
			List<T> list = new ArrayList();
      
			if (table.getNumRows() > 0) {
				table.firstRow();
				do {
					T t = type.newInstance();
					DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(t);
					JCoRecordFieldIterator iter = table.getRecordFieldIterator();
					while (iter.hasNextField()) {
						JCoRecordField recoredField = iter.nextRecordField();
						if (fieldAccessor.isReadableProperty(recoredField.getName())){
							if (recoredField.getTypeAsString().equals("DATE")) {
								Date date = recoredField.getDate();
								if (date != null) {
									String dateAsString = DateFormatUtils.format(date, "yyyyMMdd");
									fieldAccessor.setPropertyValue(recoredField.getName(), dateAsString);
								}
							} else if (recoredField.getTypeAsString().equals("TIME")) {
								Date date = recoredField.getTime();
								if (date != null) {
									String dateAsString = DateFormatUtils.format(date, "HHmmss");
									fieldAccessor.setPropertyValue(recoredField.getName(), dateAsString);
								}
							} else {
								fieldAccessor.setPropertyValue(recoredField.getName(), recoredField.getValue());
							}
						}
					}
          
					list.add(t);
				} while (table.nextRow());
			}

			return list;
    } catch (InstantiationException e) {
    	throw new SapException("COVE0", "", e);
    } catch (IllegalAccessException e) {
    	throw new SapException("COVE0", "", e);
    }
  }
  







  protected <T> JCoTable convertObjectListToSapTable(List<T> list, JCoTable table)
  {
/* 120 */     table.firstRow();
/* 121 */     for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
/* 122 */       T t = iter.next();
/* 123 */       table.appendRow();
      
/* 125 */       Class<?> clazz = t.getClass();
/* 126 */       Field[] fields = clazz.getDeclaredFields();
/* 127 */       DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(t);
/* 128 */       for (Field field : fields) {
/* 129 */         Object value = fieldAccessor.getPropertyValue(field.getName());
/* 130 */         table.setValue(field.getName(), value);
      }
    }
/* 133 */     return table;
  }
  









  protected JCoFunction setParametersToSapImportParam(Object parameters, JCoFunction function, String... excludes)
  {
	  Class<?> clazz = parameters.getClass();
/* 148 */     Field[] fields = clazz.getDeclaredFields();
/* 149 */     DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(parameters);
    
/* 151 */     boolean isExclude = false;
/* 152 */     for (Field field : fields) {
/* 153 */       isExclude = false;
/* 154 */       for (String exclude : excludes) {
/* 155 */         if (field.getName().equalsIgnoreCase(exclude)) {
/* 156 */           isExclude = true;
        }
      }
      
/* 160 */       if (!isExclude)
      {
/* 162 */         Object value = fieldAccessor.getPropertyValue(field.getName());
/* 163 */         function.getImportParameterList().setValue(field.getName(), value);
      }
    }
/* 166 */     return function;
  }
}