package com.spring5.sample.jco.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.DirectFieldAccessor;

import com.sap.conn.jco.JCoFieldIterator;
import com.sap.conn.jco.JCoRecordField;
import com.sap.conn.jco.JCoRecordFieldIterator;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.spring5.sample.jco.exception.SapException;

public class SapUtil {

	public static <T> T extractDataFromSapStructure(JCoStructure structure, T t) {
		
		
		DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(t);

		Field [] fields =  t.getClass().getDeclaredFields();
		
		for(Field f : fields) {
		
			JCoFieldIterator iter = structure.getFieldIterator();
			while(iter.hasNextField()) {
				String name = iter.nextField().getName();
				if(StringUtils.equals(f.getName(), name)) {
					fieldAccessor.setPropertyValue(name, structure.getString(name));
					break;
				}
			}
			
		}
		
		return t;
	}
	
	public static <T> List<T> convertSapTableToObjectList(JCoTable table, Class<T> type) throws SapException {

		try {
			List<T> list = new ArrayList<T>();

			if(table.getNumRows() > 0) {
				table.firstRow();
				do {
					T t = type.newInstance();
					DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(t); 
					for (JCoRecordFieldIterator iter = table.getRecordFieldIterator(); iter
							.hasNextField();) {
						JCoRecordField recoredField = iter.nextRecordField();
						if(fieldAccessor.isReadableProperty(recoredField.getName())) {
							
							if(recoredField.getTypeAsString().equals("DATE")) {
								Date date = recoredField.getDate();
								if(date == null) continue;
								String dateAsString = DateFormatUtils.format(date, "yyyyMMdd");
								fieldAccessor.setPropertyValue(recoredField.getName(),
										dateAsString);
							} else if(recoredField.getTypeAsString().equals("TIME")) {
								Date date = recoredField.getTime();
								if(date == null) continue;
								String dateAsString = DateFormatUtils.format(date, "HHmmss");
								fieldAccessor.setPropertyValue(recoredField.getName(),
										dateAsString);
							} else if (recoredField.getTypeAsString().equals("BCD")) {
								fieldAccessor.setPropertyValue(recoredField.getName(),
										recoredField.getString());
							}
							else {
								fieldAccessor.setPropertyValue(recoredField.getName(),
										recoredField.getString());
							}
						}
					}
					list.add(t); 
				} while(table.nextRow());
			}
			
			return list;
		} catch (InstantiationException e) {
			throw new SapException("COVE0","", e);
		} catch (IllegalAccessException e) {
			throw new SapException("COVE0","", e);
		}

	}
}
