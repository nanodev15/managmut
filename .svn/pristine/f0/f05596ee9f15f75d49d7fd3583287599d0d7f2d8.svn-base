/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle.loan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author ucao
 */
//public class CreditPanelAutom1Descriptor  extends WizardPanelDescriptor {
//    
//    public static final String IDENTIFIER = "AUTOMATIC_DESCRIPTOR";
//    
//    public CreditPanelAutom1Descriptor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
//     //   super (IDENTIFIER, new TestPanel1());
//         super (IDENTIFIER, new CreditPanelAutom1());
//    }
//    
//    public Object getNextPanelDescriptor() {
//        return TestPanel3Descriptor.IDENTIFIER;
//    }
//    
//    public Object getBackPanelDescriptor() {
//        return null;
//    }  
//    
//}


public class CreditPanelAutom2Descriptor extends WizardPanelDescriptor {
    
    public static final String IDENTIFIER = "AUTOMATIC_DESCRIPTOR2";
    
    TestPanel2 panel2;
    CreditPanelAutom2 pan2;
    
    public CreditPanelAutom2Descriptor () throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
         pan2 = new CreditPanelAutom2();
        //panel2 = new TestPanel2();
      
      
      //  panel2.addCheckBoxActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(pan2);
        
    }
    
    public Object getNextPanelDescriptor() {
        
             return FINISH;
        
       
    }
    
   public Object getBackPanelDescriptor() {
        return CreditPanelAutom1Descriptor.IDENTIFIER;
   }
    
    
    public void aboutToDisplayPanel() {
        //setNextButtonAccordingToCheckBox();
        getWizard().setNextFinishButtonEnabled(false);
        getWizard().setBackButtonEnabled(false);
    }   
    
     public void displayingPanel() {

 //           Thread t = new Thread() {

//            public void run() {
//
//                try {
//                    Thread.sleep(2000);
//                    panel3.setProgressValue(25);
//                    panel3.setProgressText("Server Connection Established");
//                    Thread.sleep(500);
//                    panel3.setProgressValue(50);
//                    panel3.setProgressText("Transmitting Data...");
//                    Thread.sleep(3000);
//                    panel3.setProgressValue(75);
//                    panel3.setProgressText("Receiving Acknowledgement...");
//                    Thread.sleep(1000);
//                    panel3.setProgressValue(100);
//                    panel3.setProgressText("Data Successfully Transmitted");
//
//                    getWizard().setNextFinishButtonEnabled(true);
//                    getWizard().setBackButtonEnabled(true);
//
//                } catch (InterruptedException e) {
//                    
//                    panel3.setProgressValue(0);
//                    panel3.setProgressText("An Error Has Occurred");
//                    
//                    getWizard().setBackButtonEnabled(true);
//                }

//            }
//        };
//
//        t.start();
  }
    
    public void aboutToHidePanel() {
        //  Can do something here, but we've chosen not not.
    }    
//
//    public void actionPerformed(ActionEvent e) {
//        setNextButtonAccordingToCheckBox();
//    }
//            
//    
//    private void setNextButtonAccordingToCheckBox() {
//         if (panel2.isCheckBoxSelected())
//            getWizard().setNextFinishButtonEnabled(true);
//         else
//            getWizard().setNextFinishButtonEnabled(false);           
//    
//    }
}
