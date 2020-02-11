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


public class CreditPanelAutom1Descriptor extends WizardPanelDescriptor implements ActionListener {
    
    public static final String IDENTIFIER = "AUTOMATIC_DESCRIPTOR";
    
    TestPanel2 panel2;
    CreditPanelAutom1 pan2;
    
    public CreditPanelAutom1Descriptor () throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
         
        pan2 = new CreditPanelAutom1();
        //panel2 = new TestPanel2();
      
      
      //  panel2.addCheckBoxActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(pan2);
        
    }
    
       public CreditPanelAutom1Descriptor (Boolean freely) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
         
        pan2 = new CreditPanelAutom1(freely);
        //panel2 = new TestPanel2();
      
      
      //  panel2.addCheckBoxActionListener(this);
        
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(pan2);
        
    }
    
    public Object getNextPanelDescriptor() {
        
            // return TestPanel3Descriptor.IDENTIFIER;
             return CreditPanelAutom2Descriptor.IDENTIFIER;
        
       
    }
    
   public Object getBackPanelDescriptor() {
       return TestPanel1Descriptor.IDENTIFIER;
   }
    
    
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
