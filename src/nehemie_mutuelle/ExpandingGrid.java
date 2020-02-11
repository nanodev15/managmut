/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

/**
 *
 * @author elommarcarnold
 */

    import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class ExpandingGrid extends JPanel {
   private static final int GAP = 5;

   public ExpandingGrid() {

      // create a BorderLayout-using JPanel
      JPanel borderLayoutPanel = new JPanel(new BorderLayout());
      borderLayoutPanel.setBorder(BorderFactory.createTitledBorder("BorderLayout Panel"));
      borderLayoutPanel.add(createGridPanel(), BorderLayout.CENTER); // add a Grid to it

      // create a FlowLayout-using JPanel
      JPanel flowLayoutPanel = new JPanel(new FlowLayout());
      flowLayoutPanel.setBorder(BorderFactory.createTitledBorder("FlowLayout Panel"));
      flowLayoutPanel.add(createGridPanel()); // add a grid to it

      // set up the main JPanel 
      setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
      setLayout(new GridLayout(1, 0, GAP, 0)); // grid with 1 row
      // and add the borderlayout and flowlayout using JPanels to it
      add(borderLayoutPanel);
      add(flowLayoutPanel);
   }

   // create a JPanel that holds a bunch of JLabels in a GridLayout
   private JPanel createGridPanel() {
      int rows = 5;
      int cols = 5;
      JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < cols; j++) {
            // create the JLabel that simply shows the row and column number
            JLabel label = new JLabel(String.format("[%d, %d]", i, j),
                     SwingConstants.CENTER);
            // give the JLabel a border
            label.setBorder(BorderFactory.createEtchedBorder());
            gridPanel.add(label); // add to the GridLayout using JPanel
         }
      }
      return gridPanel;
   }

   private static void createAndShowUI() {
      JFrame frame = new JFrame("ExpandingGrid");
      frame.getContentPane().add(new ExpandingGrid());
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
   }

   public static void main(String[] args) {
      java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            createAndShowUI();
         }
      });
   }
}
    

