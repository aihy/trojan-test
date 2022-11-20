package com.hhwyz;

import com.alibaba.fastjson.JSON;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author erniu.wzh
 * @date 2022/11/20 00:11
 */
public class Face {
    static FaceTableModel ftm = new FaceTableModel();

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JTable table = new JTable(ftm);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
//        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(Face::createAndShowGUI);

        DirectTest ts = new DirectTest();

        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get("gui-config.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String s = new String(bytes, StandardCharsets.UTF_8);
        List<Object> configs = JSON.parseObject(s).getJSONArray("configs");
        ts.testAll(configs);
    }
}
