package com.hhwyz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author erniu.wzh
 * @date 2022/11/20 00:23
 */
public class FaceTableModel extends AbstractTableModel {
    static Set<String> serverList = new HashSet<>();
    static String[][] data;

    static {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get("gui-config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        List<Object> configs = JSON.parseObject(s).getJSONArray("configs");
        for (Object config : configs) {
            JSONObject c = (JSONObject) config;
            String server = c.getString("server");
            serverList.add(server);
        }
        data = new String[serverList.size()][6];
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "服务器";
            case 1:
                return "平均连接速度（越小越好）";
            case 2:
                return "连接速度方差（越小越好）";
            case 3:
                return "连通率（越大越好）";
            case 4:
                return "尝试次数";
            case 5:
                return "连接明细";
            default:
                return "";
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            return data[rowIndex][columnIndex];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            data[rowIndex][columnIndex] = String.valueOf(aValue);
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
