package com.ordermanger.online.ordermanager.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.ordermanger.online.ordermanager.R;
import com.ordermanger.online.ordermanager.model.OrderFilter;
import com.ordermanger.online.ordermanager.util.Des;
import com.ordermanger.online.ordermanager.util.LogUtil;
import com.ordermanger.online.ordermanager.util.StringUtil;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2017/6/8.
 */
@ContentView(R.layout.activity_order_filter)
public class OrderFilterActivity extends BaseActivity {

    @ViewInject(R.id.et_customerNo)
    EditText et_customerNo;
    @ViewInject(R.id.tv_start_at)
    TextView tv_start_at;
    @ViewInject(R.id.tv_end_at)
    TextView tv_end_at;
    @ViewInject(R.id.btn_search)
    Button btn_search;

    OrderFilter orderFilter;

    private Calendar calendar1 = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackBtn();
        displayTextAction("清空", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_start_at.setText("");
                calendar1 = Calendar.getInstance();
                calendar2 = Calendar.getInstance();
                tv_end_at.setText("");
                et_customerNo.setText("");
            }
        });
        orderFilter = (OrderFilter) getIntent().getSerializableExtra("orderFilter");
        if (StringUtil.isNotEmpty(orderFilter.getStart_at())) {
            tv_start_at.setText(orderFilter.getStart_at());
            calendar1.setTime(strToDate(orderFilter.getStart_at()));
        }
        if (StringUtil.isNotEmpty(orderFilter.getStart_at())) {
            tv_end_at.setText(orderFilter.getEnd_at());
            calendar2.setTime(strToDate(orderFilter.getEnd_at()));
        }
        et_customerNo.setText(orderFilter.getCUSTNO());
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isNotEmpty(tv_end_at.getText().toString()) && StringUtil.isEmpty(tv_start_at.getText().toString())){
                    ToastUtils.show(OrderFilterActivity.this, "请选择开始时间");
                    return;
                }
                if(StringUtil.isEmpty(tv_end_at.getText().toString()) && StringUtil.isNotEmpty(tv_start_at.getText().toString())){
                    ToastUtils.show(OrderFilterActivity.this, "请选择结束时间");
                    return;
                }
                orderFilter.setCUSTNO(et_customerNo.getText().toString());
                orderFilter.setStart_at(tv_start_at.getText().toString());
                orderFilter.setEnd_at(tv_end_at.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("orderFilter", orderFilter);
                String query = "";
                if (StringUtil.isNotEmpty(et_customerNo.getText().toString())) {
                    query = " ( CUSTNO like '%" + et_customerNo.getText().toString() + "%' Or CUSTNAME like '%" + et_customerNo.getText().toString() +"%' )";
                }
                if (StringUtil.isNotEmpty(tv_start_at.getText().toString()) && StringUtil.isNotEmpty(tv_end_at.getText().toString())) {
                    if (StringUtil.isNotEmpty(query)) {
                        query = query + " AND ";
                    }
                     query = query + " SHEETDATE >='" + tv_start_at.getText().toString() + "' AND SHEETDATE <= '" + tv_end_at.getText().toString() + "'";
                } else {
                    String date = tv_start_at.getText().toString();
                    if (StringUtil.isNotEmpty(tv_end_at.getText().toString())) {
                        date = tv_end_at.getText().toString();
                    }
                    if (StringUtil.isNotEmpty(date)) {
                        query = query + " AND SHEETDATE='" + date + "'";
                    }
                }
                LogUtil.print("orderFilter:" + query);
                if (StringUtil.isNotEmpty(query)) {
                    query = Des.encrypt(query);
                }
                intent.putExtra("QueryCondition", query);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tv_start_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(OrderFilterActivity.this, R.style.DateTheme,
                        listener1,
                        calendar1.get(Calendar.YEAR),
                        calendar1.get(Calendar.MONTH),
                        calendar1.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });


        tv_end_at.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(OrderFilterActivity.this, R.style.DateTheme,
                        listener2,
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)
                );
                dialog.show();
            }
        });

    }

    /**
     * 点击时间控件，更新UI
     */
    private DatePickerDialog.OnDateSetListener listener1 = new DatePickerDialog.OnDateSetListener() {  //
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            calendar1.set(Calendar.YEAR, arg1);
            calendar1.set(Calendar.MONTH, arg2);
            calendar1.set(Calendar.DAY_OF_MONTH, arg3);
            String value = dateToStrLong(calendar1.getTime());
            tv_start_at.setText(value);
            tv_end_at.setText(value);
        }
    };

    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    public static Date strToDate(String dateDateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 点击时间控件，更新UI
     */
    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {  //
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            calendar2.set(Calendar.YEAR, arg1);
            calendar2.set(Calendar.MONTH, arg2);
            calendar2.set(Calendar.DAY_OF_MONTH, arg3);
            String value = dateToStrLong(calendar2.getTime());
            tv_end_at.setText(value);
        }
    };

    @Override
    public void onSuccess(int reqcode, Object result) {

    }

    @Override
    public void onFailed(int reqcode, Object result) {

    }

    @Override
    public void onError(VolleyError volleyError) {

    }
}
