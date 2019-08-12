package com.ciba.wholefinancial.util;


import android.util.Log;

import com.android.volley.misc.AsyncTask;
import com.ciba.wholefinancial.R;
import com.ciba.wholefinancial.bean.CityCodeBean;
import com.ciba.wholefinancial.callback.OnCodeListsCallBack;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.ciba.wholefinancial.util.UIUtils.getResources;

public class ReadCityCodeAsyncTask extends AsyncTask<Integer, Integer, Integer> {
    List<CityCodeBean> cityCodeBeanList=new ArrayList<>();
    OnCodeListsCallBack onCodeListsCallBack;
    public ReadCityCodeAsyncTask() {
        super();
    }

    public void setOnCodeListsCallBack(OnCodeListsCallBack onCodeListsCallBack) {
        this.onCodeListsCallBack = onCodeListsCallBack;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {

        InputStream stream = getResources().openRawResource(R.raw.test1);
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                CityCodeBean cityCodeBean = new CityCodeBean();
                for (int c = 0; c < cellsCount; c++) {
                    String value = getCellAsString(row, c, formulaEvaluator);
                    String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                    if (c == 0) {
                        cityCodeBean.setCityCode(value);
                    } else if (c == 1) {
                        cityCodeBean.setCityName(value);
                    }
                }
                cityCodeBeanList.add(cityCodeBean);

            }
        } catch (Exception e) {
            /* proper exception handling to be here */
        }

        return 1;
    }

    protected void onPreExecute() {
        Log.d("tag", "开始执行");
    }

    protected void onProgressUpdate(Integer... values) {

    }

    protected void onPostExecute(Integer result) {

        onCodeListsCallBack.CallBackCityCodeList(cityCodeBeanList);
    }
    protected String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("dd/MM/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {
            /* proper error handling should be here */
        }
        return value;
    }
}
