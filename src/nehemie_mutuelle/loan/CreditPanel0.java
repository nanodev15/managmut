/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle.loan;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import nehemie_mutuelle.Connect;

/**
 *
 * @author ucao
 */
public class CreditPanel0 extends javax.swing.JPanel {
    private String loanref;
    private Connection conn;
    private Date OriDate;
    /**
     * Creates new form CreditPanel0
     */
    public CreditPanel0() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SQLException {
       UIManager.setLookAndFeel(
       UIManager.getSystemLookAndFeelClassName());
       initComponents();
       
        
    }
    
     public void iniset(int i) {
        if(i==0) {jRadioButton1.setEnabled(false); jRadioButton2.setSelected(true);} 
        if(i==1) {jRadioButton2.setEnabled(false); jRadioButton1.setSelected(true);}       
     } 
     public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    } 
     
    public void filldata() throws SQLException {
        conn = Connect.ConnectDb();
        String beneflist="";
        PreparedStatement prepr = conn.prepareStatement ("SELECT Beneficiaires, Fraissuivi, Fraisdossier, DateOri From Loan WHERE Loanrefnum ='"+this.loanref+"'");
        ResultSet rs = prepr.executeQuery();
        while(rs.next()) {
           jTextField2.setText(String.valueOf(rs.getDouble(2)));
           jTextField1.setText(String.valueOf(rs.getDouble(3)));
           OriDate=rs.getDate(4);
           beneflist=rs.getString(1);
        }
         int id=0;
         String[] strArray = beneflist.split(",");
         
         for (int j=0; j<strArray.length; j++) {
             System.out.println("strarry"+strArray[j]);
         }
         Vector<Vector> beneficiaires = new Vector<Vector>();
         
         for(int i=0; i<strArray.length; i++) {
             String [] splitted= strArray[i].split(" ");
             String lastOne= splitted[splitted.length-1];
             System.out.println("lastone"+lastOne);
             System.out.println("subs "+ lastOne.substring(lastOne.indexOf("f")+1, lastOne.length()));
             if (lastOne.startsWith("enf")) {
                   id= Integer.valueOf(lastOne.substring(lastOne.indexOf("f")+1, lastOne.length()));
             
              prepr = conn.prepareStatement ("SELECT Nom, Prenoms From Profil_enfant WHERE idProfil_enfant="+id+"");
              rs = prepr.executeQuery();
               Vector<Object> benef = new Vector<Object>();
               benef.add(i);
               while(rs.next()) {               
                  benef.add(rs.getString(1));
                  benef.add(rs.getString(2));
              }
               beneficiaires.add(benef);
             } else if(lastOne.startsWith("adu")) {
                   id= Integer.valueOf(lastOne.substring(lastOne.indexOf("u")+1, lastOne.length()));
                   prepr = conn.prepareStatement ("SELECT Noms, Prenoms From Profil_adulte WHERE idProfil_adulte ="+id+"");
                   rs = prepr.executeQuery();
               Vector<Object> benef = new Vector<Object>();
               benef.add(i);
               while(rs.next()) {               
                  benef.add(rs.getString(1));
                  benef.add(rs.getString(2));
               }
               beneficiaires.add(benef);
                 
             } else if(lastOne.startsWith("pers")) {   // verifier ce code
                   id= Integer.valueOf(lastOne.substring(lastOne.indexOf("m")+1, lastOne.length()));
                   prepr = conn.prepareStatement ("SELECT Raison_sociale From Profil_persmorale WHERE idProfil_persmorale ="+id+"");
                   rs = prepr.executeQuery();
                   Vector<Object> benef = new Vector<Object>();
                   benef.add(i);
                   while(rs.next()) {               
                  benef.add(rs.getString(1));
              }
              beneficiaires.add(benef);
                 
             }
         }
         
  jTable1.setModel(new javax.swing.table.DefaultTableModel(
          to2DimArray(beneficiaires),
    new String [] {
        "ID", "Noms", "Prénoms"
    }
));
      
     if(conn!=null)
        conn.close();
  
     if(rs != null)
         rs.close();
     
     if(prepr !=null)
         prepr.close();
        
        
     
    }
     
    public void setRef(String ref) throws SQLException{
        this.loanref=ref;
        filldata();
    }
    
     public String getRef(){
        return this.loanref;
    }
    
    private static void setWidthAsPercentages(JTable table,
        double... percentages) {
    final double factor = 10000;
 
    TableColumnModel model = table.getColumnModel();
    for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
        TableColumn column = model.getColumn(columnIndex);
        column.setPreferredWidth((int) (percentages[columnIndex] * factor));
    }
}
    
    public boolean isManuelSelected(){
        return jRadioButton1.isSelected();
    }
    
    public boolean noneisSelected () {
        if (jRadioButton1.isSelected() || jRadioButton2.isSelected()) return false;
        else return true;
    }
    
    public Date getOriDate(){
        return this.OriDate;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        jLabel2.setText("Bienvenue dans l'assistant de création d'un nouveau dossier de crédit");

        jLabel3.setText("Pour continuer, veuillez choisir votre type de configuration");

        jPanel2.setBackground(new java.awt.Color(162, 161, 161));

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("                    Nouveau dossier de crédit");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jRadioButton1.setText("Tableau d'amortissement manuel");

        jRadioButton2.setText("Tableau d'amortissement automatique");
        ButtonGroup bg= new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Bénéficiaires");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Noms", "Prénoms"
            }
        ));
        setWidthAsPercentages(jTable1, 0.10, 0.45, 0.45);
        jScrollPane1.setViewportView(jTable1);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Type de prêt:");

        jLabel7.setText("Prêt individuel");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("Frais de dossier:");

        jTextField1.setEnabled(false);

        jLabel8.setText("Frais de suivi:");

        jTextField2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jRadioButton2)
                            .addComponent(jRadioButton1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1)
                            .addComponent(jLabel8)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(jRadioButton1)
                            .addGap(10, 10, 10)
                            .addComponent(jRadioButton2)
                            .addGap(11, 11, 11)
                            .addComponent(jLabel4))
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(5, 5, 5)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(8, 8, 8)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
