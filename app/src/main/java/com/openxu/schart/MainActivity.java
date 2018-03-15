package com.openxu.schart;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.xm.bean.ChartData;
import com.xm.util.Constacts;
import com.xm.util.NumberFormatUtil;
import com.xm.view.stock.BarChart;
import com.xm.view.stock.LinesChart;
import com.xm.view.stock.LinesLableChart;
import com.xm.view.stock.bean.DataPoint;
import com.xm.view.stock.bean.FocusInfo;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ChartData data;
    BarChart bartChart;
    LinesLableChart trendLableChart, trendLableChartMonth, compareLableChart, zhabanLableChart, zhangfuLableChart, weightLableChart;
    LinesChart trendLinesChart, trendLinesChartMonth, compareLinesChart, zhabanLinesChart, zhangfuLinesChart, weightLinesChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bartChart = (BarChart)findViewById(R.id.bartChart);
        trendLableChart = (LinesLableChart)findViewById(R.id.trendLableChart);
        trendLableChartMonth = (LinesLableChart)findViewById(R.id.trendLableChartMonth);
        compareLableChart = (LinesLableChart)findViewById(R.id.compareLableChart);
        zhabanLableChart = (LinesLableChart)findViewById(R.id.zhabanLableChart);
        zhangfuLableChart = (LinesLableChart)findViewById(R.id.zhangfuLableChart);
        weightLableChart = (LinesLableChart)findViewById(R.id.weightLableChart);
        trendLinesChart = (LinesChart)findViewById(R.id.trendLinesChart);
        trendLinesChartMonth = (LinesChart)findViewById(R.id.trendLinesChartMonth);
        compareLinesChart = (LinesChart)findViewById(R.id.compareLinesChart);
        zhabanLinesChart = (LinesChart)findViewById(R.id.zhabanLinesChart);
        zhangfuLinesChart = (LinesChart)findViewById(R.id.zhangfuLinesChart);
        weightLinesChart = (LinesChart)findViewById(R.id.weightLinesChart);

        /**1、设置图表*/
        //涨跌分布
        //涨跌停走势（图表分为两部分）

        int[] trendLineColor = new int[]{Color.parseColor("#ffb26c"),
                Color.parseColor("#ce332f"),
                Color.parseColor("#2a8c39")};
        trendLableChart.setLineColor(trendLineColor);//设置折线颜色
        trendLinesChart.setLineColor(trendLineColor);
        trendLableChartMonth.setLineColor(trendLineColor);//设置折线颜色
        trendLinesChartMonth.setLineColor(trendLineColor);
        //涨跌对比
        int[] compareLineColor = new int[]{Color.parseColor("#cb3235"),
                Color.parseColor("#1f8f3b")};
        compareLableChart.setLineColor(compareLineColor);
        compareLinesChart.setLineColor(compareLineColor);
        //炸板
        int[] zhabanLineColor = new int[]{Color.parseColor("#647bc1")};
        zhabanLableChart.setLineColor(zhabanLineColor);
        zhabanLinesChart.setLineColor(zhabanLineColor);
        //涨幅
        int[] zhangfuLineColor = new int[]{Color.parseColor("#e21b20"),
                Color.parseColor("#2c5aa7")};
        zhangfuLableChart.setLineColor(zhangfuLineColor);
        //设置数据为百分比，如果数据为整数或者小数，不需要设置
        zhangfuLinesChart.setyMarkType(LinesChart.YMARK_TYPE.PERCENTAGE);
        zhangfuLinesChart.setLineColor(zhangfuLineColor);

        //权重
        int[] weightLineColor = new int[]{Color.parseColor("#dd1d12"),
                Color.parseColor("#fec52e"),
                Color.parseColor("#1e5bac"),
                Color.parseColor("#20b1dd")};
        weightLableChart.setLineColor(weightLineColor);
        //设置数据为百分比
        weightLinesChart.setyMarkType(LinesChart.YMARK_TYPE.PERCENTAGE);
        weightLinesChart.setLineColor(weightLineColor);


        getData();

    }
    /**2、模拟接口获取数据*/
    private void getData(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject(Constacts.data);
                    Gson gson = new Gson();
                    String dataStr = json.getString("data");
                    data = gson.fromJson(dataStr, ChartData.class);
                }catch (Exception e){
                    e.printStackTrace();
                }

                //绑定
                bindChartData();

            }
        }, 500);
    }


    /**3、绑定数据*/
    private void bindChartData(){
        bartChart.setLoading(false);
        bartChart.setData(data.getFenbu());

        //涨跌停走势   日
        trendLableChart.setLoading(false);                                       //不显示loading
        final String[] trendLables = new String[]{"非一字涨停","涨停", "跌停"};
        trendLableChart.setColumnNum(3);       //设置标签列数
        trendLableChart.setData(trendLables, "03-13 15：00");           //标签控件设置数据
        trendLinesChart.setLoading(false);                                       //不显示loading
        trendLinesChart.setAnimType(LinesChart.AnimType.LEFT_TO_RIGHT);              //设置动画类型（3种）
//        trendLinesChart.setAnimType(LinesChart.AnimType.BOTTOM_TO_TOP);
//        trendLinesChart.setAnimType(LinesChart.AnimType.SLOW_DRAW);
        //触摸焦点变化，改变标签数量
        trendLinesChart.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                String[] newTrendLables = new String[3];    //创建新的lable数组，数组元素数量是lable总数（必须和trendLables元素数量一致）
                List<DataPoint> datas = focusInfo.getFocusData();
                for(int i = 0; i<datas.size() ; i++){
                    //在标签上拼接数据
                    newTrendLables[i] = trendLables[i]+" "+ (int)datas.get(i).getValueY();
                }
                //重新设置标签数据
                trendLableChart.setData(newTrendLables, "03-13 15：00");
            }
        });
        trendLinesChart.setData(data.getTrend().getDay(), null);               //折线图设置数据

        //月
        trendLableChartMonth.setLoading(false);                                       //不显示loading
        trendLableChartMonth.setColumnNum(3);       //设置标签列数
        trendLableChartMonth.setData(trendLables, "03-13 15：00");           //标签控件设置数据
        trendLinesChartMonth.setLoading(false);                                       //不显示loading
        trendLinesChartMonth.setAnimType(LinesChart.AnimType.LEFT_TO_RIGHT);              //设置动画类型（3种）
//        trendLinesChart.setAnimType(LinesChart.AnimType.BOTTOM_TO_TOP);
//        trendLinesChart.setAnimType(LinesChart.AnimType.SLOW_DRAW);
        //触摸焦点变化，改变标签数量
        trendLinesChartMonth.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                String[] newTrendLablesMonth = new String[3];
                List<DataPoint> datas = focusInfo.getFocusData();
                for(int i = 0; i<datas.size() ; i++){
                    //在标签上拼接数据
                    newTrendLablesMonth[i] = trendLables[i]+" "+ (int)datas.get(i).getValueY();
                }
                //重新设置标签数据
                trendLableChartMonth.setData(newTrendLablesMonth, "03-13 15：00");
            }
        });
        //重组X轴刻度
        List<List<String>> monthData = data.getTrend().getMonth();
        String[] xStrArr = new String[monthData.size()];
        for(int i = 0; i<monthData.size(); i++){
            xStrArr[i] = monthData.get(i).get(0);
        }
        trendLinesChartMonth.setData(data.getTrend().getMonth(), xStrArr);               //折线图设置数据

        //涨跌对比
        compareLableChart.setLoading(false);
        final String[] compareLables = new String[]{"涨家数", "跌家数"};
        compareLableChart.setColumnNum(2);
        compareLableChart.setData(compareLables, "03-13 15：00");
        compareLinesChart.setLoading(false);
        compareLinesChart.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                String[] newCompareLables = new String[2];
                List<DataPoint> datas = focusInfo.getFocusData();
                for(int i = 0; i<datas.size() ; i++){
                    newCompareLables[i] = compareLables[i] +" "+ (int)datas.get(i).getValueY();
                }
                compareLableChart.setData(newCompareLables, "03-13 15：00");
            }
        });
        //全部
        compareLinesChart.setData(data.getCompare().getCompare_line().getStock_updown_all(), null);

        //炸板
        zhabanLableChart.setLoading(false);
        final String[] zhabanLables = new String[]{"炸板家数"};
        zhabanLableChart.setColumnNum(1);
        zhabanLableChart.setData(zhabanLables, "03-13 15：00");
        zhabanLinesChart.setLoading(false);
        zhabanLinesChart.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                String[] newZhabanLables = new String[1];
                List<DataPoint> datas = focusInfo.getFocusData();
                for(int i = 0; i<datas.size() ; i++){
                    newZhabanLables[i] = zhabanLables[i] + (" "+(int)datas.get(i).getValueY()+"家"+"   炸板数");
                }
                zhabanLableChart.setData(newZhabanLables, "03-13 15：00");
            }
        });
        zhabanLinesChart.setData(data.getZhaban().getZhaban_line(), null);

        //涨幅
        zhangfuLableChart.setLoading(false);
        final String[] zhangfuLables = new String[]{"上证指数涨幅","昨日涨停股今日涨幅"};
        zhangfuLableChart.setColumnNum(1);
        zhangfuLableChart.setData(zhangfuLables, "03-13 15：00");
        zhangfuLinesChart.setLoading(false);
        zhangfuLinesChart.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                String[] newZhangfuLables = new String[2];
                List<DataPoint> datas = focusInfo.getFocusData();
                for(int i = 0; i<datas.size() ; i++){
                    newZhangfuLables[i] = zhangfuLables[i]+  " "+
                            NumberFormatUtil.formattedDecimalToPercentage(datas.get(i).getValueY());
                }
                zhangfuLableChart.setData(newZhangfuLables, "03-13 15：00");
            }
        });
        zhangfuLinesChart.setData(data.getZhangfu().getZhangfu_line(), null);

        //权重
        weightLableChart.setLoading(false);
        final String[] weightLables = new String[]{"沪深300","中小板" , "上证指数", "深证指数"};
        weightLableChart.setColumnNum(2);
        weightLableChart.setData(weightLables, "03-13 15：00");
        weightLinesChart.setLoading(false);
        weightLinesChart.setOnFocusChangeListener(new LinesChart.OnFocusChangeListener() {
            @Override
            public void onfocus(FocusInfo focusInfo) {
                List<DataPoint> datas = focusInfo.getFocusData();
                String[] newWeightLables = new String[4];
                for(int i = 0; i<weightLables.length ; i++){
                    newWeightLables[i] = weightLables[i]+ " "+
                            (i>datas.size()-1?"0":NumberFormatUtil.formattedDecimalToPercentage(datas.get(i).getValueY()));
                }
                weightLableChart.setData(newWeightLables, "03-13 15：00");
            }
        });
        weightLinesChart.setData(data.getWeight().getWeight_line(), null);

    }
}
