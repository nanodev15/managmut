/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 *
 * @author elommarcarnold
 */
public class lnmangersum extends javax.swing.JPanel {
   //  private JButton buttons[] = new JButton[8];
    private JLabel buttons[] = new JLabel[8];
    private static final int GAP = 2;
    
    
    /** Creates new form lnmangersum */
    public lnmangersum(int category) {
        initComponents();
        int rows = 2;
        int cols = 4;
           if (category ==0) {
    //       int row = 2;
           // the layout will try to put the components in 3 rows 

           //  this.setLayout(new GridLayout(row, 4));
             
//             for(int i = 0; i < buttons.length; i++) {
//                 String text="";
//                 if (i==0) text="Num de Dossier LDFDFDFDFDFDDFDFDFDFD"; 
//                 if (i==1) text="Montant emprunté";   
//                 if (i==2) text="Durée du prêt";  
//                 if (i==3) text="Date clôture";
//                 if (i==4) text="Total intérêts"; 
//                 if (i==5) text="Bénéficiaires"; 
//                 if (i==6) text="Dernier paiement";
//                 if (i==7) text="Pourcentage payé";
//                
//             //    buttons[i] = new JButton(text);
//                  buttons[i] = new JLabel(text, SwingConstants.CENTER);
//                 this.add(buttons[i]);
//          }
      JPanel borderLayoutPanel = new JPanel(new BorderLayout());
      borderLayoutPanel.setPreferredSize(new Dimension(800, 652));
      borderLayoutPanel.setBorder(BorderFactory.createEmptyBorder());
      borderLayoutPanel.add(createGridPanel(), BorderLayout.CENTER); // add a Grid to it

//      // create a FlowLayout-using JPanel
//      JPanel flowLayoutPanel = new JPanel(new FlowLayout());
//      flowLayoutPanel.setBorder(BorderFactory.createTitledBorder("FlowLayout Panel"));
//      flowLayoutPanel.add(createGridPanel()); // add a grid to it

      // set up the main JPanel 
      setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
      setLayout(new GridLayout(1, 0, GAP, 0)); // grid with 1 row
      // and add the borderlayout and flowlayout using JPanels to it
      add(borderLayoutPanel);
 //     add(flowLayoutPanel);
        
      }    
      }
    
    public JLabel getButton (int i) {
        return buttons [i];
    }
    
    private JPanel createGridPanel() {
      int rows = 2;
      int cols = 4;
      JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
      gridPanel.setPreferredSize(new Dimension(400, 652));
      int k= 0;
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            // create the JLabel that simply shows the row and column number
//            JLabel label = new JLabel(String.format("[%d, %d]", i, j),
//                     SwingConstants.CENTER);
  
             JLabel label = new JLabel(String.format("", i, j),
                     SwingConstants.CENTER);
             System.out.println("k="+k);
            buttons[k]=label;
           
            // give the JLabel a border
            label.setBorder(BorderFactory.createEtchedBorder());
            gridPanel.add(label); // add to the GridLayout using JPanel
            
            k++;
           
         }
         
      }
      return gridPanel;
   }
      
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(838, 652));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
