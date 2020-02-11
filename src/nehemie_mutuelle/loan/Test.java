/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package  nehemie_mutuelle.loan;

import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.InputStream;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import nehemie_mutuelle.Loanopeninglist;
import nehemie_mutuelle.main;

/**
 *
 * @author ucao
 */
public class Test extends JFrame {
    private static String loanref;
    private static int type;
    private static Wizard wizard;
    private static Loanopeninglist lnopen;
    private static boolean simulation = false;
    
    public Wizard getWizard() {
        return this.wizard;
    }
    
    public Test() {
        this.simulation = true;
    }
    
    public Test(String loanref, int type) {
        this.loanref=loanref;
        this.type=type;
    }
    
    public Test(String loanref, int type,  Loanopeninglist lnopen) {
        this.loanref=loanref;
        this.type=type;
        this.lnopen=lnopen;
    }
    
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException, Exception {
//        Dialog dia;
//        JFrame owner= new JFrame("ffffff");
//        WizardPanelDescriptor jf1= new WizardPanelDescriptor();
//        WizardPanelDescriptor jf2= new WizardPanelDescriptor();
//        WizardPanelDescriptor jf3= new WizardPanelDescriptor();
//        dia = new Dialog(owner);
//        Wizard wiz= new Wizard(dia);
//        wiz.registerWizardPanel(new String("1"), jf1);
//        wiz.registerWizardPanel(new String("1"), jf2);
//        wiz.registerWizardPanel(new String("1"), jf3);
//                wiz.showModalDialog();
//    }
        
         InputStream themepack = main.class.getClass().getResourceAsStream("/nehemie_mutuelle/whistlerthemepack.zip");
       //     System.out.println("url:"+ "file://"+ main.class.getClass().getResourceAsStream("whistlerthemepack.zip"));
//           URL themepack = new URL("file://"+urlthe);
//           System.out.println("url:"+urlthe);
//           // URL themepack = new URL("file://"+new File("src/nehemie_mutuelle/whistlerthemepack.zip").getAbsolutePath() );
          Skin skin = SkinLookAndFeel.loadThemePack(themepack);
          SkinLookAndFeel.setSkin(skin);   
          UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel"); 
    
        if (simulation) wizard = new Wizard(true);
        else wizard = new Wizard();
        wizard.getDialog().setTitle("Données automatiques");
        
        WizardPanelDescriptor descriptor1;
        descriptor1 = new TestPanel1Descriptor(loanref);
        ((TestPanel1Descriptor) descriptor1).initset(type);
        wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);
        WizardPanelDescriptor automDesc;
        if (simulation == true) {
           automDesc = new CreditPanelAutom1Descriptor(true);
        } else  {
           automDesc = new CreditPanelAutom1Descriptor();
 
        }
         wizard.registerWizardPanel(CreditPanelAutom1Descriptor.IDENTIFIER, automDesc);
         
         WizardPanelDescriptor automDesc2 = new CreditPanelAutom2Descriptor();
         wizard.registerWizardPanel(CreditPanelAutom2Descriptor.IDENTIFIER, automDesc2);

        WizardPanelDescriptor descriptor2 = new TestPanel2Descriptor();
        wizard.registerWizardPanel(TestPanel2Descriptor.IDENTIFIER, descriptor2);

        WizardPanelDescriptor descriptor3 = new TestPanel3Descriptor();
        wizard.registerWizardPanel(TestPanel3Descriptor.IDENTIFIER, descriptor3);
        
        wizard.setCurrentPanel(TestPanel1Descriptor.IDENTIFIER);
//         UIManager.setLookAndFeel(
//            UIManager.getSystemLookAndFeelClassName());
        
        
        int ret = wizard.showModalDialog();
        if (!simulation) {
        nehemie_mutuelle.Loanopeninglist.retmod(ret);
        if(lnopen !=null) {
            if (ret == 0) {
                Test.lnopen.refreshopeninglist();
            }
        }
        }
        
        System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
 //       System.out.println("Second panel selection is: " + 
//            (((TestPanel2)descriptor2.getPanelComponent()).getRadioButtonSelected()));
        
       
        
    
}
}    
