/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client4;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import newpackage.Data;
import newpackage.WarehouseLabel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author kalex
 */
public class FileCreator {

    XSSFWorkbook book;
    FileOutputStream fileOut;
    XSSFRow[] row;
    // Read XSL file
    FileInputStream inputStream;

    private Data data;
    private WarehouseLabel label;
    private Data data2;
    private WarehouseLabel label2;

    // Get the workbook instance for XLS file
    XSSFWorkbook flowRackLabel;
    XSSFWorkbook highRackLabel;

    // Get first sheet from the workbook
    XSSFSheet flowRackLabelSheet;
    XSSFSheet highRackLabelSheet;

    public FileCreator() {
        try {

            inputStream = new FileInputStream(new File("FlowRackLabelDraft.xlsx"));
            flowRackLabel = new XSSFWorkbook(inputStream);
            flowRackLabelSheet = flowRackLabel.getSheetAt(0);

            inputStream = new FileInputStream(new File("highRackLabelDraft.xlsx"));
            highRackLabel = new XSSFWorkbook(inputStream);
            highRackLabelSheet = highRackLabel.getSheetAt(0);
            inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean createFlowrackLabels(ArrayList array) {
        if (!array.isEmpty()) {
     
        double labelQty;
        System.out.println("Creating flowRack file");
        try {
            labelQty = array.size();
            row = new XSSFRow[1000];
            for (int i = 0; i < 9; i++) {
                row[i] = flowRackLabelSheet.getRow(i);
            }
            CellCopyPolicy policy = new CellCopyPolicy.Builder().mergedRegions(true).rowHeight(true).cellValue(true).cellStyle(true).build();
            for (int i = 1; i < labelQty / 2 + 1; i++) {
                for (int j = 1; j < 10; j++) {
                    row[i * 9 + j] = flowRackLabelSheet.createRow(i * 9 + j);
                    row[i * 9 + j].copyRowFrom(row[j], policy);
                }
                if (i > 1) {
                    mergeRegion(i);

                }
                if (i == labelQty / 2) {
                    i++;
                    mergeRegion(i);
                }
            }
            int labelcount = 0;
            int stringCount = 0;
            data = (Data) array.get(labelcount);
            label = (WarehouseLabel) data.getLabel();
            if ((labelcount + 1) < array.size()) {
                data2 = (Data) array.get(labelcount + 1);
                label2 = (WarehouseLabel) data2.getLabel();
            }
            for (int i = 0; i < flowRackLabelSheet.getLastRowNum(); i++) {

                switch (i - stringCount * 9) {
                    case 3:
                        row[i].getCell(1).setCellValue(label.getLocation());
                        
                                                incertQR(1, label.getQRrcode(), i,6);
                        row[i].getCell(6).setCellValue(label.getQRrcode());
                        row[i].getCell(11).setCellValue(label.getPartNumber());
                        if (label2 != null) {
                            row[i].getCell(22).setCellValue(label2.getLocation());
                            
                                                    incertQR(1, label2.getQRrcode(), i,27);
                            row[i].getCell(27).setCellValue(label2.getQRrcode());
                            row[i].getCell(32).setCellValue(label2.getPartNumber());
                        }

                        break;
                    case 4:
                        Cell cell;
                        cell = row[i].createCell(11);

                        if (label2 != null) {
                            row[i].getCell(32).setCellValue(label2.getPartName());
                        }
                        break;
                    case 7:
                        row[i].getCell(1).setCellValue(label.getALC());
                        row[i].getCell(11).setCellValue(label.getMaxQty());
                        row[i].getCell(14).setCellValue(label.getMinQty());
                        row[i].getCell(17).setCellValue(label.getPackedQty());
                        if (label2 != null) {
                            row[i].getCell(22).setCellValue(label2.getALC());
                            row[i].getCell(32).setCellValue(label2.getMaxQty());
                            row[i].getCell(35).setCellValue(label2.getMinQty());
                            row[i].getCell(38).setCellValue(label2.getPackedQty());
                        }

                        break;
                }

                if ((i % 9 == 0) && (i != 0)) {
                    labelcount = labelcount + 2;
                    stringCount++;
                    if ((labelcount) < array.size()) {
                        data = (Data) array.get(labelcount);
                        label = (WarehouseLabel) data.getLabel();
                        label2 = null;
                    } else {
                        break;
                    }
                    if ((labelcount + 1) < array.size()) {
                        data2 = (Data) array.get(labelcount + 1);
                        label2 = (WarehouseLabel) data2.getLabel();
                    }
                }

            }

            File file = new File("FlowRackLabels.xlsx");
            System.out.println(file.createNewFile());
            fileOut = new FileOutputStream(file);
            flowRackLabel.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
        }
        return false;
    }

    public boolean createHighrackLabels(ArrayList array) {
    if (!array.isEmpty()) {
  

        System.out.println("Creating highRack file");
        try {

            row = new XSSFRow[array.size() * 10];
            for (int i = 0; i < 6; i++) {
                row[i] = highRackLabelSheet.getRow(i);
            }
            CellCopyPolicy policy = new CellCopyPolicy.Builder().mergedRegions(true).rowHeight(true).cellValue(true).cellStyle(true).build();
            for (int i = 1; i < array.size(); i++) {
                for (int j = 1; j < 7; j++) {
                    row[i * 6 + j] = highRackLabelSheet.createRow(i * 6 + j);
                    row[i * 6 + j].copyRowFrom(row[j], policy);
                }
            }
            int labelcount = 0;

            data = (Data) array.get(labelcount);
            label = (WarehouseLabel) data.getLabel();

            for (int i = 0; i < highRackLabelSheet.getLastRowNum(); i++) {
                System.out.println("Row index: " + (i - labelcount * 6));
                switch (i - labelcount * 6) {
                    case 2:
                        row[i].getCell(1).setCellValue(label.getLocation());

                        incertQR(2, label.getQRrcode(), i, 0);

                        row[i].getCell(12).setCellValue(label.getPartNumber());
                        System.out.println("printing " + labelcount);
                        break;
                    case 4:
                        row[i].getCell(1).setCellValue(label.getALC());
                        row[i].getCell(12).setCellValue(label.getPartName());
                        row[i].getCell(15).setCellValue(label.getMaxQty());
                        row[i].getCell(16).setCellValue(label.getMinQty());
                        row[i].getCell(17).setCellValue(label.getPackedQty());

                        break;
                }

                if ((i % 6 == 0) && (i != 0)) {
                    labelcount++;

                    if (labelcount < array.size()) {
                        data = (Data) array.get(labelcount);
                        label = (WarehouseLabel) data.getLabel();
                    } else {
                        break;
                    }
                }

            }

            File file = new File("HighRackLabels.xlsx");
            System.out.println(file.createNewFile());
            fileOut = new FileOutputStream(file);
            highRackLabel.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
        }
        return false;
    }

    void mergeRegion(int i) {
        flowRackLabelSheet.addMergedRegion((new CellRangeAddress(i * 9 - 6, i * 9 - 5, 1, 3)));
        flowRackLabelSheet.addMergedRegion((new CellRangeAddress(i * 9 - 6, i * 9 - 5, 6, 8)));
        flowRackLabelSheet.addMergedRegion((new CellRangeAddress(i * 9 - 6, i * 9 - 5, 22, 24)));
        flowRackLabelSheet.addMergedRegion((new CellRangeAddress(i * 9 - 6, i * 9 - 5, 27, 29)));
    }

    private void incertQR(int type, String QR, int row, int column) throws FileNotFoundException, IOException {
        if (type == WarehouseLabel.HIGH_RACK_LABEL) {

            CreationHelper helper = highRackLabel.getCreationHelper();
            Drawing drawing = highRackLabelSheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            int pictureIndex
                    = highRackLabel.addPicture(QRCodeGenerator.getQRByte(QR,95,95), highRackLabel.PICTURE_TYPE_PNG);
            anchor.setCol1(column);
            anchor.setRow1(row); // same row is okay
            anchor.setRow2(row + 1);
            anchor.setCol2(column+1);
            Picture pict = drawing.createPicture(anchor, pictureIndex);
            pict.resize();
        }else if (type == WarehouseLabel.FLOW_RACK_LABEL) {

            CreationHelper helper = flowRackLabel.getCreationHelper();
            Drawing drawing = flowRackLabelSheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
            int pictureIndex
                    = flowRackLabel.addPicture(QRCodeGenerator.getQRByte(QR,75,75), flowRackLabel.PICTURE_TYPE_PNG);
            anchor.setCol1(column);
            anchor.setRow1(row); // same row is okay
            anchor.setRow2(row + 3);
            anchor.setCol2(column + 3);
            Picture pict = drawing.createPicture(anchor, pictureIndex);
            pict.resize();
        }
    }
}
