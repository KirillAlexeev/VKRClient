/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client4;



import java.io.File;
import newpackage.WarehouseLabelInterface;


/**
 *
 * @author Кирилл
 */
public interface LabelClientInterface {
    
    
   
    /**
       Label updating thread is alive.
     */
    public static final boolean ALIVE = true;
    /**
       Label updating thread is dead.
     */
    public static final boolean DEAD = false;        
    
    

    
    /*
        Method saves labels in file
    */
    public File printFileCreation();
    
    
    

    /*
        Method for not printed labels list updating
    */
    public void labelListUpdate ();
    
    
    
    /*
        Method for printed labels list deleting
    */
    public void labelListDelete ();
        
    
    
}
