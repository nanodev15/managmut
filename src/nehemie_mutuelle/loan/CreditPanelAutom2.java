/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle.loan;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author ucao
 */
public class CreditPanelAutom2 extends javax.swing.JPanel {

    /**
     * Creates new form CreditPanel0
     */
    public CreditPanelAutom2() {
        initComponents();
    }
    
    public class NumberCellRenderer extends DefaultTableCellRenderer {

    DecimalFormat numberFormat = new DecimalFormat("#.00");

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
        if (c instanceof JLabel && value instanceof Number) {
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(JLabel.LEFT);
            Number num = (Number) value;
            String text = numberFormat.format(num);
            label.setText(text);
        }
        return c;
    }
}
    
    public class DateCellRenderer extends DefaultTableCellRenderer {
        public DateCellRenderer() {
            super();
        }

        @Override
        public void setValue(final Object value) {
            final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String strValue = "";
            if(value != null && value instanceof Date){
               strValue = sdf.format(value);
            }
            super.setText(strValue);
        }
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(162, 161, 161));

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("                    Nouveau dossier de crédit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Tableau d'amortissement automatique");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Num", "Date", "Capital restant dû", "Intérêts", "Capital remboursé", "Echeance payée"
            }
        ));
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new DateCellRenderer());
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new NumberCellRenderer());
        jTable1.getColumnModel().getColumn(3).setCellRenderer(new NumberCellRenderer());
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new NumberCellRenderer());
        jTable1.getColumnModel().getColumn(5).setCellRenderer(new NumberCellRenderer());
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Recapitulatif");

        jLabel3.setText("Nombre d'échécances");

        jLabel4.setText("Dernière échéance");

        jLabel5.setText("Emprunt");

        jLabel6.setText("Total intérêts");

        jLabel7.setText("Capital remboursé");

        jLabel9.setText("Total echeances");

        jLabel10.setText("jLabel10");

        jLabel11.setText("jLabel11");

        jLabel12.setText("jLabel12");

        jLabel13.setText("jLabel13");

        jLabel14.setText("jLabel14");

        jLabel15.setText("jLabel15");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel13))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel14))))
                .addGap(136, 136, 136))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    
    public void fillTableAuto(Date firstdate, long nbEcheances, String freq, int nbjours, Double capdue, Double taux){
        
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
         dm.removeRow(i);
        }
         int nbPeriodeParAn=1; 
         Double tauxpe;
         Double echeance;
         Double interets;
         Double Tinterets;
         Double capr;
         Double Tcapr;
         Double capdu=capdue;
         Date mensdat= firstdate;
         if (freq.equalsIgnoreCase("mensuel")) {
             nbPeriodeParAn=12;
         } else if (freq.equalsIgnoreCase("bimestriel")){
             nbPeriodeParAn=6;
         }else if (freq.equalsIgnoreCase("trimestriel")){
             nbPeriodeParAn=4;
         }else if (freq.equalsIgnoreCase("quadrimestriel")){
            nbPeriodeParAn=3;
         } else if (freq.equalsIgnoreCase("semestriel")){
             nbPeriodeParAn=2;
         } else if ((freq.equalsIgnoreCase("annuel"))){
             nbPeriodeParAn=1;
         }
         
         if (!freq.equalsIgnoreCase("Périodique")) {
             tauxpe=taux/(nbPeriodeParAn*100);
             echeance=(double) capdu*tauxpe*Math.pow(1+tauxpe, nbEcheances)/(Math.pow(1+tauxpe, nbEcheances)-1);
             System.out.println("echeance"+echeance);
             interets=capdu*tauxpe; //initialisation intérêts
             //somme des intérêts
             Tinterets=interets;
             capr=echeance - interets;  // captital remboursé (principal) 
             System.out.println("capr"+capr);
             
             // Total du capital remboursé
             Tcapr= capr; 
             
             // Première initialisation, 
             ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1) ,mensdat,(double) Math.round(capdu*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/ 100 });
             
             // Autres initialisations 
             
             int i; 
             for (i=2; i<= nbEcheances; i++) {
                 capdu=capdu-capr;
                 interets= capdu*tauxpe;
                 Tinterets=Tinterets+interets;
                 capr= echeance - interets;
                 System.out.println("capr"+capr);
              
                 Tcapr=Tcapr+capr;
                 System.out.println("Tcapr partiel"+Tcapr);
                 
      
                 if (freq.equalsIgnoreCase("mensuel")) {
                     mensdat = DateUtils.addMonths(mensdat, 1);
                 } else if (freq.equalsIgnoreCase("bimestriel")) {
                     mensdat = DateUtils.addMonths(mensdat, 2);
                 } else if (freq.equalsIgnoreCase("trimestriel")) {
                     mensdat = DateUtils.addMonths(mensdat, 3);
                 } else if (freq.equalsIgnoreCase("quadrimestriel")) {
                     mensdat = DateUtils.addMonths(mensdat, 4);
                 }  else if (freq.equalsIgnoreCase("semestriel")) {
                     mensdat = DateUtils.addMonths(mensdat, 6);
                 }   else if (freq.equalsIgnoreCase("annuel")) {
                     mensdat = DateUtils.addMonths(mensdat, 12);
                 }
                 
                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capdu*100)/ 100,(double) Math.round(interets*100)/ 100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/100 });
             }
             
              System.out.println("Tcapr"+Tcapr);
             System.out.println("Tint"+Tinterets);
         
            System.out.println("Total echeances"+echeance*nbEcheances);
             jLabel10.setText(String.valueOf(nbEcheances));
             jLabel11.setText(String.valueOf(mensdat));
             jLabel12.setText(String.valueOf((double) Math.round(capdue*100)/100));
             jLabel13.setText(String.valueOf((double) Math.round(Tinterets*100)/ 100));
             jLabel14.setText(String.valueOf((double) Math.round(Tcapr*100)/100));
             jLabel15.setText(String.valueOf((double) Math.round(echeance*nbEcheances*100)/ 100));

             
             
         }
         
           
     }
    
     public void fillnbEcheances(Date firstdate, String freq, int nbjours, Double capdue, Double taux, Double echeance){
         int nbPeriodeParAn=1; 
         Double tauxpe;
         if (freq.equalsIgnoreCase("mensuel")) {
             nbPeriodeParAn=12;
         } else if (freq.equalsIgnoreCase("trimestriel")){
             nbPeriodeParAn=4;
         } else if (freq.equalsIgnoreCase("semestriel")){
             nbPeriodeParAn=2;
         } else if ((freq.equalsIgnoreCase("annuel"))){
             nbPeriodeParAn=1;
         }
         
           if (!freq.equalsIgnoreCase("Périodique")) {
             tauxpe=taux/(nbPeriodeParAn*100);
             long nbEcheances=Math.round((double) (Math.log(-1/(((capdue/echeance)*tauxpe)-1)) / (Math.log(1+tauxpe))));
               fillTableAuto(firstdate,nbEcheances,freq, nbjours, capdue, taux);
             
           }
         
         
         
//        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
//        int rowCount = dm.getRowCount();
//        //Remove rows one by one from the end of the table
//        for (int i = rowCount - 1; i >= 0; i--) {
//         dm.removeRow(i);
//        }
//         int nbPeriodeParAn=1; 
//         Double tauxpe;
//         Double interets;
//         Double Tinterets;
//         Double capr;
//         Double Tcapr;
//         Double capdu=capdue;
//         Date mensdat= firstdate;
//         if (freq.equalsIgnoreCase("mensuel")) {
//             nbPeriodeParAn=12;
//         } else if (freq.equalsIgnoreCase("trimestriel")){
//             nbPeriodeParAn=4;
//         } else if (freq.equalsIgnoreCase("semestriel")){
//             nbPeriodeParAn=2;
//         } else if ((freq.equalsIgnoreCase("annuel"))){
//             nbPeriodeParAn=1;
//         }
//     
//          
//         
//         if (!freq.equalsIgnoreCase("Périodique")) {
//             tauxpe=taux/(nbPeriodeParAn*100);
//             long nbEcheances=Math.round((double) (Math.log(-1/(((capdue/echeance)*tauxpe)-1)) / (Math.log(1+tauxpe))));
//             System.out.println("nbEcheances"+ nbEcheances); // Vérifier si N est un nombre
//          //   echeance=(double) capdu*tauxpe*Math.pow(1+tauxpe, nbEcheances)/(Math.pow(1+tauxpe, nbEcheances)-1);
//             interets=capdu*tauxpe; //initialisation intérêts
//             //somme des intérêts
//             Tinterets=interets;
//             capr=echeance - interets;  // captital remboursé (principal) 
//             
//             // Total du capital remboursé
//             Tcapr= capr; 
//             
//             // Première initialisation, 
//             ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1) ,mensdat,(double) Math.round(capdu*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/ 100 });
//             
//             // Autres initialisations 
//             
//             int i; 
//             for (i=2; i<= nbEcheances; i++) {
//                 capdu=capdu-capr;
//                 interets= capdu*tauxpe;
//                 Tinterets=Tinterets+interets;
//                 capr= echeance - interets;
//                 Tcapr=Tcapr+capr; 
//                 
//                  System.out.println("capr"+capr);
//                 System.out.println("capdu"+capdu);
//                 if (freq.equalsIgnoreCase("mensuel")) {
//                     mensdat = DateUtils.addMonths(mensdat, 1);
//                 } else if (freq.equalsIgnoreCase("trimestriel")) {
//                     mensdat = DateUtils.addMonths(mensdat, 3);
//                 } else if (freq.equalsIgnoreCase("semestriel")) {
//                     mensdat = DateUtils.addMonths(mensdat, 6);
//                 }  else if (freq.equalsIgnoreCase("annuel")) {
//                     mensdat = DateUtils.addMonths(mensdat, 12);
//                 }
//                 
//                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capdu*100)/ 100,(double) Math.round(interets*100)/ 100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/100 });
//             }
//             System.out.println("Tcapr"+Tcapr);
//             System.out.println("Tint"+Tinterets);
//             jLabel10.setText(String.valueOf(nbEcheances));
//            System.out.println("Total echeances"+echeance*nbEcheances);
//             jLabel11.setText(String.valueOf(mensdat));
//             jLabel12.setText(String.valueOf(Math.round(capdue)*100/(double)100));
//             jLabel13.setText(String.valueOf(Math.round(Tinterets)*100/(double) 100));
//             jLabel14.setText(String.valueOf(Math.round(Tcapr)*100/(double) 100));
//             jLabel15.setText(String.valueOf(Math.round(echeance*nbEcheances)*100/(double) 100));
//
//             
//             
//         }
         
         
     }
     
     public void fillCapital (Date firstdate, String freq, int nbjours, Double taux, Double echeance, long nbEcheances) {
         int nbPeriodeParAn=1; 
         Double tauxpe;
         if (freq.equalsIgnoreCase("mensuel")) {
             nbPeriodeParAn=12;
         } else if (freq.equalsIgnoreCase("trimestriel")){
             nbPeriodeParAn=4;
         } else if (freq.equalsIgnoreCase("semestriel")){
             nbPeriodeParAn=2;
         } else if ((freq.equalsIgnoreCase("annuel"))){
             nbPeriodeParAn=1;
         }
         
           if (!freq.equalsIgnoreCase("Périodique")) {
             tauxpe=taux/(nbPeriodeParAn*100);
             Double cap= (double) echeance* (1- Math.pow((1+tauxpe), -nbEcheances)) / (tauxpe);
             fillTableAuto(firstdate,nbEcheances,freq, nbjours, cap, taux);
             
           }
         
         
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}