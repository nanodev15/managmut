/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle.loan;

/**
 *
 * @author ucao
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;


//public class TestPanel1Descriptor extends WizardPanelDescriptor {
//    
//    public static final String IDENTIFIER = "INTRODUCTION_PANEL";
//  
//    private static final CreditPanel0 pan = new CreditPanel0(); 
//    
//    public TestPanel1Descriptor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
//     //   super (IDENTIFIER, new TestPanel1());
//         super (IDENTIFIER, pan );
//    }
//    
//    public Object getNextPanelDescriptor() {
//        return TestPanel2Descriptor.IDENTIFIER;
//    }
//    
//    public Object getBackPanelDescriptor() {
//        return null;
//    }  
//    
//}

public class TestPanel1Descriptor extends WizardPanelDescriptor implements ActionListener {
    
    public static final String IDENTIFIER = "INTRODUCTION_PANEL";
    
    TestPanel2 panel2;
    CreditPanel0 pan2;
    private String loanref;
    
    public TestPanel1Descriptor(String loanref) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
         pan2 = new CreditPanel0();
         pan2.setRef(loanref);
         this.loanref= loanref;
        //panel2 = new TestPanel2();
      
      
      //  panel2.addCheckBoxActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(pan2);
        
    }
    
   public String getLoanRef() {
       return this.loanref;
   } 
   
   public void initset(int i){
       pan2.iniset(i);
   }
    
    public Object getNextPanelDescriptor() {
        if (pan2.isManuelSelected()) {
             return TestPanel2Descriptor.IDENTIFIER;
        } else {
            return CreditPanelAutom1Descriptor.IDENTIFIER;
        }
       
    }
    
//    public Object getBackPanelDescriptor() {
//        return TestPanel1Descriptor.IDENTIFIER;
//    }
    
    
    public void aboutToDisplayPanel() {
        setNextButtonAccordingToCheckBox();
    }    

    public void actionPerformed(ActionEvent e) {
        setNextButtonAccordingToCheckBox();
    }
            
    
    private void setNextButtonAccordingToCheckBox() {
         if (panel2.isCheckBoxSelected())
            getWizard().setNextFinishButtonEnabled(true);
         else
            getWizard().setNextFinishButtonEnabled(false);           
    
    }
}