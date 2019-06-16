/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client4;

import RemoteBeans.OperationSelectRemote;
import java.awt.Color;
import java.awt.Container;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import javax.swing.ListSelectionModel;
import newpackage.Data;
import newpackage.DataInterface;
import newpackage.WarehouseLabel;
import newpackage.WarehouseLabelInterface;

/**
 *
 * @author Кирилл
 */
public class MainWindow extends JFrame implements LabelClientInterface {

    @EJB
    private OperationSelectRemote osr;

    public boolean ThreadStatus = LabelClientInterface.DEAD;

    // Common system elements
    private ArrayList<DataInterface> flowRackLabels;
    private ArrayList<DataInterface> highRackLabels;

    private DefaultListModel flowListModel;
    private DefaultListModel highListModel;

    private ArrayList<DataInterface> flowRackLabelsToPrint;
    private ArrayList<DataInterface> highRackLabelsToPrint;
    private DataInterface[] datas;
    //Main window elements 
    private Container cp;
    private JTabbedPane tp;
    private JPanel work;
    private Border border;
    private ButLis buttonListener;

    //Work panel elements
    private JLabel flowRackLabelLabel;
    private JLabel highRackLabelLabel;
    private JList<String> flowRackLabelsList;
    private JList<String> highRackLabelsList;
    private JScrollPane flowRackLabelsSCR;
    private JScrollPane highRackLabelsSCR;
    private JButton createPrintFile;
    private JButton updateList;
    private JButton delete;
    private JButton selectAll;

    public MainWindow() {
        flowRackLabels = new ArrayList();
        highRackLabels = new ArrayList();
        flowListModel = new DefaultListModel();
        highListModel = new DefaultListModel();

        setTitle("Label printing programm");
        setBounds(800, 200, 800, 800);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp = getContentPane();
        cp.setLayout(null);

        drawMainScreen();

        cp.add(tp);
        setVisible(true);
    }

    void drawMainScreen() {
        tp = new JTabbedPane();
        work = new JPanel();

        if (osr == null) {

            System.out.println("Null!!!!!!!!!!!!!!!");
            try {
                InitialContext ic = new InitialContext();
                osr = (OperationSelectRemote) ic.lookup("java:global/VKR3/VKR3-ejb/OperationSelect!RemoteBeans.OperationSelectRemote");
            } catch (NamingException ex) {
                Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (osr == null) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@");
        }

        // Labels
        border = BorderFactory.createLineBorder(Color.BLUE);
        flowRackLabelLabel = new JLabel("Flow racks:");
        flowRackLabelLabel.setBounds(20, 20, 100, 30);
        flowRackLabelLabel.setBorder(border);
        flowRackLabelLabel.setBackground(Color.WHITE);
        flowRackLabelLabel.setOpaque(true);
        flowRackLabelLabel.setHorizontalAlignment(JLabel.CENTER);
        work.add(flowRackLabelLabel);

        highRackLabelLabel = new JLabel("High racks:");
        highRackLabelLabel.setBounds(flowRackLabelLabel.getX(), flowRackLabelLabel.getY() + 330, 100, 30);
        highRackLabelLabel.setBorder(border);
        highRackLabelLabel.setBackground(Color.WHITE);
        highRackLabelLabel.setOpaque(true);
        highRackLabelLabel.setHorizontalAlignment(JLabel.CENTER);
        work.add(highRackLabelLabel);

        // Lists
        flowRackLabelsList = new JList<String>(flowListModel);
        flowRackLabelsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        flowRackLabelsSCR = new JScrollPane(flowRackLabelsList);
        flowRackLabelsSCR.setBounds(flowRackLabelLabel.getX(), flowRackLabelLabel.getY() + 37, 580, 280);
        work.add(flowRackLabelsSCR);

        highRackLabelsList = new JList<String>(highListModel);
        highRackLabelsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        highRackLabelsSCR = new JScrollPane(highRackLabelsList);
        highRackLabelsSCR.setBounds(flowRackLabelLabel.getX(), flowRackLabelLabel.getY() + 37 + 330, 580, 280);
        work.add(highRackLabelsSCR);

        // Buttons
        buttonListener = new ButLis(this);

        createPrintFile = new JButton("Create print file");
        createPrintFile.setBounds(flowRackLabelLabel.getX() + 585, flowRackLabelLabel.getY() + 37, 140, 30);
        createPrintFile.setBackground(Color.cyan);
        createPrintFile.setOpaque(true);
        createPrintFile.setHorizontalAlignment(JLabel.CENTER);
        createPrintFile.setActionCommand("Create print file");
        createPrintFile.addActionListener(buttonListener);
        work.add(createPrintFile);

        updateList = new JButton("Update list");
        updateList.setBounds(flowRackLabelLabel.getX() + 585, flowRackLabelLabel.getY() + 77, 140, 30);
        updateList.setBackground(Color.cyan);
        updateList.setOpaque(true);
        updateList.setHorizontalAlignment(JLabel.CENTER);
        updateList.setActionCommand("Update list");
        updateList.addActionListener(buttonListener);
        work.add(updateList);

        delete = new JButton("Delete record");
        delete.setBounds(flowRackLabelLabel.getX() + 585, flowRackLabelLabel.getY() + 117, 140, 30);
        delete.setBackground(Color.cyan);
        delete.setOpaque(true);
        delete.setHorizontalAlignment(JLabel.CENTER);
        delete.setActionCommand("Delete record");
        delete.addActionListener(buttonListener);
        work.add(delete);

        selectAll = new JButton("Select all");
        selectAll.setBounds(flowRackLabelLabel.getX() + 585, flowRackLabelLabel.getY() + 157, 140, 30);
        selectAll.setBackground(Color.cyan);
        selectAll.setOpaque(true);
        selectAll.setHorizontalAlignment(JLabel.CENTER);
        selectAll.setActionCommand("Select all");
        selectAll.addActionListener(buttonListener);
        work.add(selectAll);

        work.setLayout(null);
        tp.addTab("Main screen", work);
        tp.setBounds(20, 10, 750, 730);

    }

    @Override
    public File printFileCreation() {
        int[] flowSelected;
        int[] highSelected;

        DataInterface tmpData1;

        flowSelected = flowRackLabelsList.getSelectedIndices();
        highSelected = highRackLabelsList.getSelectedIndices();
        flowRackLabelsToPrint = new ArrayList();
        highRackLabelsToPrint = new ArrayList();
        try {
            for (int i = 0; i < flowSelected.length; i++) {
                tmpData1 = clone(flowRackLabels.get(i));
                tmpData1.getLabel().setPrintQty(0);
                while (flowRackLabels.get(i).getLabel().getPrintQty() > 0) {
                    flowRackLabels.get(i).getLabel().setPrintQty(flowRackLabels.get(i).getLabel().getPrintQty() - 1);
                    flowRackLabelsToPrint.add(tmpData1);
                }

                osr.dbSelect(new Data(DataInterface.UPDATE, flowRackLabels.get(i).getLabel(), "Printed"));

            }
            for (int i = 0; i < highSelected.length; i++) {

                tmpData1 = clone(highRackLabels.get(i));
                tmpData1.getLabel().setPrintQty(0);
                while (highRackLabels.get(i).getLabel().getPrintQty() > 0) {
                    highRackLabels.get(i).getLabel().setPrintQty(highRackLabels.get(i).getLabel().getPrintQty() - 1);
                    highRackLabelsToPrint.add(tmpData1);
                }

                osr.dbSelect(new Data(DataInterface.UPDATE, highRackLabels.get(i).getLabel(), "Printed"));
            }
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }

        FileCreator fc = new FileCreator();
        if (flowRackLabelsToPrint != null) {
            fc.createFlowrackLabels(flowRackLabelsToPrint);
        }
        if (highRackLabelsToPrint != null) {
            fc.createHighrackLabels(highRackLabelsToPrint);
        }

        flowRackLabelsToPrint = null;
        highRackLabelsToPrint = null;
        flowListModel.clear();
        highListModel.clear();

        labelListUpdate();
        return null;
    }

    @Override
    public void labelListUpdate() {
        System.out.println("We are in List update");
        flowRackLabels.clear();
        highRackLabels.clear();
        if (osr != null) {

            datas = osr.dbSelect(new Data(DataInterface.SELECT, null, "UpdateList"));
        } else {
            System.out.println("Bean is zero");

        }

//        ArrayList<Data> array1 = new ArrayList();
//
//        for (int i = 0; i < array1.size(); i++) {
//            datas[i] = array1.get(i);
//
//        }

        if (datas[0].getMessage().equals("No record")) {

        } else {
            for (int i = 0; i < datas.length; i++) {
                if (datas[i].getLabel().getType() == WarehouseLabelInterface.FLOW_RACK_LABEL) {
                    flowRackLabels.add(datas[i]);
                } else if (datas[i].getLabel().getType() == WarehouseLabelInterface.HIGH_RACK_LABEL) {
                    highRackLabels.add(datas[i]);
                }

            }
        }
        flowListModel.clear();
        highListModel.clear();
        for (int i = 0; i < flowRackLabels.size(); i++) {

            flowListModel.add(i, flowRackLabels.get(i));

        }
        for (int i = 0; i < highRackLabels.size(); i++) {

            highListModel.add(i, highRackLabels.get(i));

        }

    }

    @Override
    public void labelListDelete() {
        int[] flowSelected;
        int[] highSelected;
        flowSelected = flowRackLabelsList.getSelectedIndices();
        highSelected = highRackLabelsList.getSelectedIndices();
        for (int i = 0; i < flowSelected.length; i++) {
            osr.dbSelect(new Data(DataInterface.UPDATE, flowRackLabels.get(i).getLabel(), "Printed"));
        }
        for (int i = 0; i < highSelected.length; i++) {
            osr.dbSelect(new Data(DataInterface.UPDATE, highRackLabels.get(i).getLabel(), "Printed"));
        }
        flowListModel.clear();
        highListModel.clear();

        labelListUpdate();
    }

    private DataInterface clone(DataInterface tmpData) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous;
        ous = new ObjectOutputStream(baos);
        ous.writeObject(tmpData);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        DataInterface tmpData1 = (DataInterface) ois.readObject();
        
        ous.close();
        bais.close();
        baos.close();
        ois.close();
        return tmpData1;
    }

    public void selectAll() {
        int indices[] = new int[flowListModel.size()];
        for (int i = 0; i < flowListModel.size(); i++) {
            indices[i] = i;
        }
        flowRackLabelsList.setSelectedIndices(indices);
        indices = new int[highListModel.size()];
        for (int i = 0; i < highListModel.size(); i++) {
            indices[i] = i;
        }
        highRackLabelsList.setSelectedIndices(indices);


    }
}
