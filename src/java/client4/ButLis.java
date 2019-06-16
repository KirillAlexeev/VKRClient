/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Кирилл
 */
class ButLis implements ActionListener {

    private MainWindow win;

    public ButLis(MainWindow win) {
        this.win = win;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Update list":
                System.out.println("Update");
                win.labelListUpdate();
                break;
            case "Create print file":
                win.printFileCreation();
                break;
            case "Delete record":
                win.labelListDelete();
                break;
            case "Select all":
                win.selectAll();
        }

    }

}
