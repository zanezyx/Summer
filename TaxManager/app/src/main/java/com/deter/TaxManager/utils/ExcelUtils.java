package com.deter.TaxManager.utils;

import android.content.Context;
import android.util.Log;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.network.DtConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//import jxl.Cell;
//import jxl.Sheet;
//import jxl.Workbook;
//import jxl.WorkbookSettings;
//import jxl.write.Label;
//import jxl.write.WritableCell;
//import jxl.write.WritableCellFormat;
//import jxl.write.WritableFont;
//import jxl.write.WritableSheet;
//import jxl.write.WritableWorkbook;
//import jxl.write.WriteException;

public class ExcelUtils {
//	public static WritableFont arial14font = null;
//
//	public static WritableCellFormat arial14format = null;
//	public static WritableFont arial10font = null;
//	public static WritableCellFormat arial10format = null;
//	public static WritableFont arial12font = null;
//	public static WritableCellFormat arial12format = null;
//
//	public final static String UTF8_ENCODING = "UTF-8";
//	public final static String GBK_ENCODING = "GBK";
//
//	public static void format() {
//		try {
//			arial14font = new WritableFont(WritableFont.ARIAL, 14,
//					WritableFont.BOLD);
//			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
//			arial14format = new WritableCellFormat(arial14font);
//			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
//			arial14format.setBorder(jxl.format.Border.ALL,
//					jxl.format.BorderLineStyle.THIN);
//			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);
//			arial10font = new WritableFont(WritableFont.ARIAL, 10,
//					WritableFont.BOLD);
//			arial10format = new WritableCellFormat(arial10font);
//			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
//			arial10format.setBorder(jxl.format.Border.ALL,
//					jxl.format.BorderLineStyle.THIN);
//			arial10format.setBackground(jxl.format.Colour.LIGHT_BLUE);
//			arial12font = new WritableFont(WritableFont.ARIAL, 12);
//			arial12format = new WritableCellFormat(arial12font);
//			arial12format.setBorder(jxl.format.Border.ALL,
//					jxl.format.BorderLineStyle.THIN);
//		} catch (WriteException e) {
//
//			e.printStackTrace();
//		}
//	}
//
//	public static void initExcel(String fileName, String workbookName, String[] colName) {
//		format();
//		WritableWorkbook workbook = null;
//		try {
//			File file = new File(fileName);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			workbook = Workbook.createWorkbook(file);
//			WritableSheet sheet = workbook.createSheet(workbookName, 0);//家庭帐务表
//			sheet.addCell((WritableCell) new Label(0, 0, fileName,
//					arial14format));
//			for (int col = 0; col < colName.length; col++) {
//				sheet.addCell(new Label(col, 0, colName[col], arial10format));
//			}
//			workbook.write();
//		} catch (Exception e) {
//			Log.i("Log", "initExcel>>>"+e.toString());
//			e.printStackTrace();
//		} finally {
//			if (workbook != null) {
//				try {
//					workbook.close();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
//
//	@SuppressWarnings("unchecked")
//	public static <T> void writeObjListToExcel(List<T> objList,
//			String fileName, Context c) {
//		if (objList != null && objList.size() > 0) {
//			WritableWorkbook writebook = null;
//			InputStream in = null;
//			try {
//				WorkbookSettings setEncode = new WorkbookSettings();
//				setEncode.setEncoding(UTF8_ENCODING);
//				in = new FileInputStream(new File(fileName));
//				Workbook workbook = Workbook.getWorkbook(in);
//				writebook = Workbook.createWorkbook(new File(fileName),
//						workbook);
//				WritableSheet sheet = writebook.getSheet(0);
//				for (int j = 0; j < objList.size(); j++) {
//					ArrayList<String> list = (ArrayList<String>) objList.get(j);
//					for (int i = 0; i < list.size(); i++) {
//						sheet.addCell(new Label(i, j + 1, list.get(i),
//								arial12format));
//					}
//				}
//				writebook.write();
//				//Toast.makeText(c, "导出到手机存储中文件"+fileName+"成功", Toast.LENGTH_SHORT).show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				if (writebook != null) {
//					try {
//						writebook.close();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}
//				if (in != null) {
//					try {
//						in.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//		}
//	}
//
//
//	public static Object getValueByRef(Class cls, String fieldName) {
//		Object value = null;
//		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
//				.substring(0, 1).toUpperCase());
//		String getMethodName = "get" + fieldName;
//		try {
//			Method method = cls.getMethod(getMethodName);
//			value = method.invoke(cls);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return value;
//	}

//
//
//	public static  void readMacVendor2DB(File f, Context con) {
//		ArrayList<MacVendor> vendorArrayList = new ArrayList<MacVendor>();
//		try {
//			Workbook course = null;
//			course = Workbook.getWorkbook(f);
//			Sheet sheet = course.getSheet(0);
//
//			Cell cell = null;
//			for (int i = 1; i < sheet.getRows(); i++) {
//				MacVendor tc = new MacVendor();
//				cell = sheet.getCell(0, i);
//				tc.setMac(cell.getContents());
//				cell = sheet.getCell(1, i);
//				tc.setVendercn(cell.getContents());
//				cell = sheet.getCell(2, i);
//				tc.setVenderen(cell.getContents());
//
//				Log.i(DtConstant.TAG, "readMacVendor2DB>>>"+i+":"+tc.getMac() + tc.getVendercn()
//						+ tc.getVenderen());
//				vendorArrayList.add(tc);
//			}
//			Log.i(DtConstant.TAG, "readMacVendor2DB>>>vendorArrayList size:"+vendorArrayList.size());
//			DataManager.getInstance(con).addMacVendorListToDb(vendorArrayList);
//			Log.i(DtConstant.TAG, "readMacVendor2DB>>>success");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Log.i(DtConstant.TAG, "readMacVendor2DB>>>exception: "+e.toString());
//		}
//		return;
//	}
//

}
