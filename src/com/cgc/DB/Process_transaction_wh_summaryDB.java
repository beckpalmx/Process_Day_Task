/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cgc.DB;

import com.cgc.bean.DataBean_Transaction_Process_wh_summary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Beckeck
 */
public class Process_transaction_wh_summaryDB {

    public void generater_transaction_process(String date_from, String date_to, String process_id, String table, String doc_type, String r, String username) throws Exception {
        ArrayList<DataBean_Transaction_Process_wh_summary> obj_AL_process_transaction = new ArrayList<>();
        DataBean_Transaction_Process_wh_summary bean = new DataBean_Transaction_Process_wh_summary();
        Connection con = new DBConnect().openConnection_ERP_Y();
        Connection Conn2 = new DBConnect().openConnection_ERP_Y();
        ResultSet rs, rec_periods;
        PreparedStatement p = null;
        //Random r = new Random();

        String SQL_MAIN,SQL_PERIOD, start_period = "";

        System.out.println("Start Process process_id : " + process_id);

        //System.out.println("Conn First : " + Conn);
        SQL_PERIOD = "SELECT * "
                + " FROM mperiod "
                + " where doc_type = 'S'";

        //System.out.println("date_to = " + date_to);

        int day = Integer.parseInt(date_to.substring(0, 2));

        //System.out.println("day = " + day);

        String sdate_to;
        //sdate_to = (date_to.substring(6, 10) + "-" + date_to.substring(3, 5) + "-");
        sdate_to = "-" + date_to.substring(3, 10);

        //System.out.println("sdate_to = " + sdate_to);

        rec_periods = Conn2.createStatement().executeQuery(SQL_PERIOD);
        if (rec_periods.next()) {
            start_period = rec_periods.getString("start_period");
            //System.out.println("Select DB start_period : " + rec_periods.getString("start_period"));
            //start_period = rec_periods.getString("start_period").substring(6, 10) + "-" + rec_periods.getString("start_period").substring(3, 5) + "-" + rec_periods.getString("start_period").substring(0, 2);
            //System.out.println("start_period : " + start_period);
        }

        String sqlDelete = "DELETE FROM tmp_stock_daily ;"
                + " ALTER SEQUENCE seq_tmp_stock_daily RESTART WITH 1; ";

        //System.out.println("sqlDelete = " + sqlDelete);
        delete(sqlDelete, con, p);

        int iday;

        for (iday = 1; iday <= day; iday++) {

            String sdate_to_cond = String.format("%02d", iday) + sdate_to;

            if (process_id.equals("PR_WH_STOCK_VALUE")) {
                SQL_MAIN = " select Sum(weight_total) as weight_total,Sum(weight_total * price_per_unit_num) as price_total from " + table
                        + " where to_date(format_date(doc_date),'YYYY-MM-DD') between to_date(format_date('" + start_period + "'),'YYYY-MM-DD') AND to_date(format_date('" + sdate_to_cond + "'),'YYYY-MM-DD')" ;
                        //+ " and pgroup_id <> 'RAW'";
            } else {
                SQL_MAIN = "";
            }

            System.out.println("SQL_MAIN = " + SQL_MAIN);

            //token = process_id + "_" + new SimpleDateFormat("ddMMyy_hhmmssS").format(new Date());
            //if (Record >= 1) {
            rs = con.createStatement().executeQuery(SQL_MAIN);

            while (rs.next()) {
                //sdate_to_db = sdate_to_cond.substring(8, 10) + "-" + sdate_to_cond.substring(5, 7) + "-" + sdate_to_cond.substring(0, 4);
                
                Timestamp ts1 = (new Timestamp(new java.util.Date().getTime()));
                //System.out.println("ts1 = " + ts1);               

                System.out.println("sdate_to_cond = " + sdate_to_cond + " | " + ts1);
                
                //Double weight_value = Double.parseDouble(rs.getString("weight_total"));
                //Double price_value = Double.parseDouble(rs.getString("price_total"));               
                

                bean.setStock_type("WH");
                bean.setDoc_date(sdate_to_cond);
                bean.setWeight_value(rs.getDouble("weight_total"));
                bean.setPrice_value(rs.getDouble("price_total"));

                //System.out.println(sdate_to_cond + " | " + weight_value + " | " + price_value);
                obj_AL_process_transaction.add(bean);

                insert(obj_AL_process_transaction, con, p);

            }

        }

        //}
        //System.out.println("P1 count_loop = " + count_loop);
        //SQL_TimeStamp = " Update t_process_log set condition = '" + SQL.replace("'", "#") + "', remark = '" + Record + " Record',complete_flag = 'Y' , end_time = '" + new Timestamp(new java.util.Date().getTime()) + "', update_by = '" + username + "' where log_id = '" + token + "'";
        //System.out.println("SQL_TimeStamp = " + SQL_TimeStamp);
        //InsTimeStamp(SQL_TimeStamp, con, p);
        System.out.println("End Process process_id : " + process_id);
    }

    private void delete(String SQL_DEL, Connection con, PreparedStatement p) throws Exception {
        try {
            //System.out.println("Function Delete : " + SQL_DEL);
            p = con.prepareStatement(SQL_DEL);
            p.executeUpdate();
            p.clearParameters();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            System.out.println("Delete Final");
        }
    }

    private void insert(ArrayList<DataBean_Transaction_Process_wh_summary> obj_AL, Connection con, PreparedStatement p) throws Exception {

        int i;

        try {
            p = con.prepareStatement("insert into tmp_stock_daily "
                    + "(stock_type,doc_date,weight_value,price_value,create_date) "
                    + "values"
                    + " (?,?,?,?,?)");

            //System.out.println("Insert DATA");

            for (DataBean_Transaction_Process_wh_summary bean : obj_AL) {
                i = 1;
                //System.out.println("i = " + i);
                p.setString(i++, bean.getStock_type());
                p.setString(i++, bean.getDoc_date());
                p.setDouble(i++, bean.getWeight_value());
                p.setDouble(i++, bean.getPrice_value());
                p.setTimestamp(i++, new Timestamp(new java.util.Date().getTime()));
                System.out.println("bean.getDoc_date() = " + bean.getDoc_date() + " | " + new Timestamp(new java.util.Date().getTime()));
                p.addBatch();
                //p.executeUpdate();
            }
            p.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace(System.out);
        } finally {
            if (p != null) {
                p.clearBatch();
                p.clearParameters();
            }
            obj_AL.clear();
        }
    }

}
