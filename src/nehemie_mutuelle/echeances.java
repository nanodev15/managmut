/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import static nehemie_mutuelle.main.conn;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 *
 * @author arnowod
 */
public class echeances extends javax.swing.JFrame {
    Connection connect;
    
    MultiValueMap ech = new MultiValueMap();
  //  Map <Date, List<String>> ech = new HashMap<Date, List<String>> ();
    private Date beginDate; 
    private int periodicite;
    private int nbPeriod;
    private BigDecimal monthlypay;        
    private Double nominalrate;
    private BigDecimal montant;
    private String respdoss;
    private Date dateOr;
    private Vector <Vector<String>> data;
    private Color primarycolorbackground;
    private Color primarycolorforeground;  
    private loanmanager mn;

         

    /**
     * Creates new form echeances
     */
    public echeances() throws SQLException {
        populateechterm();
        populateechlibre();
        populatechautom();
        initComponents();
      
        

}
    
    
 public void setloanmanager(loanmanager ln) {
     this.mn = ln;
 }
    
    
class DateRenderer extends DefaultTableCellRenderer {


@Override
public void setValue(Object value) {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     setText((value == null) ? "" : sdf.format(value));
     setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);


}
}    
      public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    }
  

    
    
    public void populateechterm() throws SQLException {
           connect = Connect.ConnectDb2();
           String sql="SELECT temp.id, temp.statu,  temp.benef, temp.resp, temp.mont, temp.montantpaye,  temp.finloan, temp.resp, temp.dateremb, temp.term,  ras.typo, ras.nom, ras.prenoms FROM "
                   + "      ((SELECT a.idloan as id, Responsablegroupe as resp, a.montant as mont, a.statut as statu,  a.datefinloan as finloan , b.Responsable as respon, c.daterembours as dateremb, c.montantpay as 'montantpaye',  'terme' as term, b.Beneficiaires as benef FROM loanrecterme a LEFT JOIN Loan b" 
                   + " On a.idloan = b.Loanrefnum AND a.statut='En cours' LEFT JOIN rembours_terme c on a.idloan = c.idloan) temp  LEFT JOIN "
           + "          (SELECT nom, prenoms, tel, typo FROM (SELECT Noms as nom, Prenoms as prenoms, Telephone as tel, concat('adu', idProfil_adulte) as typo FROM Profil_adulte UNION "
           + "          (SELECT d.Nom as nom, Prenoms as prenoms, h.Telephone as tel, concat('enf', idProfil_enfant) as typo FROM Profil_enfant d LEFT JOIN representant_legal h on d.Id_representant_legal = h.id_representant_legal) UNION "
           + "          (SELECT Raison_sociale as nom, '' as prenoms, Telephone_ref as tel, concat('persm', idProfil_persmorale) as typo FROM Profil_persmorale)) ess) ras ON find_in_set(ras.typo, temp.benef)  <> 0) order by temp.finloan ASC";
            System.out.println("sql"+sql);
            PreparedStatement pre = connect.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            
            
                    
                    String previousid="";
                    String currentid="";
                    double topay =0;
                    double paid=0;
                    boolean flipped=true;
                    boolean lastflipped=true;
                    int firstenter=0;
                    String benef="";
                    String resp="";
                    Date finloan=new Date();
              
            
            while (rs.next()) {
                firstenter++;
                previousid = currentid; 
                currentid= rs.getString(1);
                
                  System.out.println("currentid"+currentid+"previoustid"+previousid);
                if(!currentid.equalsIgnoreCase(previousid)){
                        System.out.println("in loop");
                    
                    lastflipped=flipped;
                    
                    if (firstenter !=1) {
                        
                         List <String> datalist = new  ArrayList<String> ();
                         datalist.add(previousid);
                         datalist.add(benef);   // beneficiaires
                         datalist.add(resp); // responsables
                         datalist.add(String.valueOf(topay - paid));
                         if (paid ==0)  datalist.add("total");
                         else  datalist.add("partiel");
                         datalist.add("terme");
                         
                          ech.put(finloan, datalist);
                        
                    } 
                    
                    topay =rs.getDouble(5);
                    paid = rs.getDouble(6);
                    flipped = false;
                    benef= rs.getString(3);
                    resp= rs.getString(4);
                    finloan= rs.getDate(7);
                    
                    
                    
                    
                } else {
                    paid= paid+rs.getDouble(6);
                    flipped =true;
                    benef = rs.getString(3);
                    resp= rs.getString(4);
                    finloan= rs.getDate(7);
                } 
                
                
                
                
                
                
            }
            
            
          
                        
                         List <String> datalist = new  ArrayList<String> ();
                         datalist.add(currentid);
                         datalist.add(benef);   // beneficiaires
                         datalist.add(resp); // responsables
                         datalist.add(String.valueOf(topay - paid));
                         if (paid ==0)  datalist.add("total");
                         else  datalist.add("partiel");
                         datalist.add("terme");
                         
                         ech.put(finloan, datalist);
                        
         
            
            
        
        
        
        
        
    }
    
    
    public void populateechlibre() throws SQLException {
           connect = Connect.ConnectDb2();
           String sql="SELECT temp.id, temp.statu,  temp.benef, temp.resp, temp.mont, temp.montantpaye,  temp.finloan, temp.resp, temp.dateremb, temp.term,  ras.typo, ras.nom, ras.prenoms FROM "
                   + "      ((SELECT a.idloan as id, Responsablegroupe as resp, a.montant as mont, a.statut as statu,  a.datefinloan as finloan , b.Responsable as respon, c.DateEnr as dateremb, c.montant as 'montantpaye',  'libre' as term, b.Beneficiaires as benef FROM loanreclibre a LEFT JOIN Loan b" 
                   + " On a.idloan = b.Loanrefnum AND a.statut='En cours' LEFT JOIN rembours_libre c on a.idloan = c.idloan) temp  LEFT JOIN "
           + "          (SELECT nom, prenoms, tel, typo FROM (SELECT Noms as nom, Prenoms as prenoms, Telephone as tel, concat('adu', idProfil_adulte) as typo FROM Profil_adulte UNION "
           + "          (SELECT d.Nom as nom, Prenoms as prenoms, h.Telephone as tel, concat('enf', idProfil_enfant) as typo FROM Profil_enfant d LEFT JOIN representant_legal h on d.Id_representant_legal = h.id_representant_legal) UNION "
           + "          (SELECT Raison_sociale as nom, '' as prenoms, Telephone_ref as tel, concat('persm', idProfil_persmorale) as typo FROM Profil_persmorale)) ess) ras ON find_in_set(ras.typo, temp.benef)  <> 0) order by temp.finloan ASC";
            System.out.println("sql"+sql);
            PreparedStatement pre = connect.prepareStatement(sql);
            ResultSet rs = pre.executeQuery();
            
            
                    
                    String previousid="";
                    String currentid="";
                    double topay =0;
                    double paid=0;
                    boolean flipped=true;
                    boolean lastflipped=true;
                    int firstenter=0;
                    String benef="";
                    String resp="";
                    Date finloan=new Date();
              
            
            while (rs.next()) {
                firstenter++;
                previousid = currentid; 
                currentid= rs.getString(1);
                
                  System.out.println("currentid"+currentid+"previoustid"+previousid);
                if(!currentid.equalsIgnoreCase(previousid)){
                        System.out.println("in loop");
                    
                    lastflipped=flipped;
                    
                    if (firstenter !=1) {
                        
                         List <String> datalist = new  ArrayList<String> ();
                         datalist.add(previousid);
                         datalist.add(benef);   // beneficiaires
                         datalist.add(resp); // responsables
                         datalist.add(String.valueOf(topay - paid));
                         if (paid ==0)  datalist.add("total");
                         else  datalist.add("partiel");
                         datalist.add("libre");
                         
                          ech.put(finloan, datalist);
                        
                    } 
                    
                    topay =rs.getDouble(5);
                    paid = rs.getDouble(6);
                    flipped = false;
                    benef= rs.getString(3);
                    resp= rs.getString(4);
                    finloan= rs.getDate(7);
                    
                    
                    
                    
                } else {
                    paid= paid+rs.getDouble(6);
                    flipped =true;
                    benef = rs.getString(3);
                    resp= rs.getString(4);
                    finloan= rs.getDate(7);
                } 
                
                
                
                
                
                
            }
            
            
          
                        
                         List <String> datalist = new  ArrayList<String> ();
                         datalist.add(currentid);
                         datalist.add(benef);   // beneficiaires
                         datalist.add(resp); // responsables
                         datalist.add(String.valueOf(topay - paid));
                         if (paid ==0)  datalist.add("total");
                         else  datalist.add("partiel");
                         datalist.add("libre");
                         
                         ech.put(finloan, datalist);
                        
         
            
            
        
        
        
        
        
    }
    
    
    
    
    
    
    
    
    public void populatechautom() throws SQLException {
        // Populate first echeances automatiques
        
        // 1. Voir echeances en cours 
        connect = Connect.ConnectDb2();
        String sql = "SELECT loanrefnum, Beneficiaires, Responsable FROM Loan WHERE loanrefnum IN (SELECT loanref FROM loanrecautom WHERE statut='"+"En cours"+"')";
        PreparedStatement pre = connect.prepareStatement(sql);
        PreparedStatement pre2=null; 
        ResultSet rs2=null; 
        ResultSet rs = pre.executeQuery();
        Date today = new Date();
        while (rs.next()) {
            String loanref = rs.getString(1);
            
            
         String sql0="SELECT * FROM rembours_auto WHERE idloan='"+loanref+"' ";
         pre2 = connect.prepareStatement(sql0);
         rs2 = pre2.executeQuery();
         List<Map<String, Object>> list = new ArrayList<>();
         List<Integer> listids =new ArrayList<>();
         if (rs2.next()) {
            
             ResultSetMetaData md = rs2.getMetaData();
             int columns = md.getColumnCount();
             
             Map<String, Object> row = new HashMap<>(columns);
             for (int i = 1; i <= columns; ++i) {
            row.put(md.getColumnName(i), rs2.getObject(i));
            if(i==3) listids.add((Integer)rs2.getObject(i));
            }
        list.add(row);
        
        }
         
         
       while (rs2.next()) {
            ResultSetMetaData md = rs2.getMetaData();
            int columns = md.getColumnCount();    
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs2.getObject(i));
                if(i==3) listids.add((Integer)rs2.getObject(i));
             }
            list.add(row);
        }
       
         
         // setting params
        setloanparams(loanref);

           
         // Fill table
     
         int nbPeriodeParAn=1; 
         Double tauxpe;
         Double echeance;
         Double interets;
         Double Tinterets;
         Double capr;
         Double Tcapr;
         Double capdu=montant.doubleValue();
         Date mensdat= beginDate;
         if (periodicite==0) {
             nbPeriodeParAn=12;
         } else if (periodicite==1){
             nbPeriodeParAn=6;
         }else if (periodicite==2){
             nbPeriodeParAn=4;
         }else if (periodicite==3){
            nbPeriodeParAn=3;
         } else if (periodicite==4){
             nbPeriodeParAn=2;
         } else if (periodicite==5){
             nbPeriodeParAn=1;
         }
         
             tauxpe=nominalrate/(nbPeriodeParAn*100);
             echeance=(double) capdu*tauxpe*Math.pow(1+tauxpe, nbPeriod)/(Math.pow(1+tauxpe, nbPeriod)-1);
             interets=capdu*tauxpe; //initialisation intérêts
             //somme des intérêts
             Tinterets=interets;
             capr=echeance - interets;  // captital remboursé (principal)              
             // Total du capital remboursé
             Tcapr= capr; 
             
             // Première initialisation, 
             int pos;
            
           
             pos=listids.indexOf((Integer)1);
           
             if(pos !=-1) {
                 System.out.println("found");
                 Map <String, Object> rel = list.get(pos);
                   System.out.println("typeremb2"+rel.get("listmod"));
                   
               double sommedue = echeance + (Double) rel.get("penalite");
               double summremb = (Double) rel.get("sumrembours");
               
               if (summremb < sommedue && (mensdat.before(today) || DateUtils.isSameDay(today, mensdat))) {
//                   
//                String typerembours;
//                 
//                if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
//                else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
//                else 
//                   typerembours = "Virement";
                
                
                   List <String> datalist = new  ArrayList<String> ();
                   datalist.add(loanref); // Reference loan
                   datalist.add(rs.getString(2)); // Beneficiaires
                   datalist.add(rs.getString(3)); // Responsable
                   datalist.add(String.valueOf(Math.round((sommedue-summremb)*100)/100)); // Somme a rembourser
                   datalist.add("partiel"); // Remboursement partiel
                   datalist.add("autom"); // type automatique
                    
                   ech.put(mensdat, datalist);
                   
                 
                
               }
               
               
               
              } else {
                   
                 if (mensdat.before(today) || DateUtils.isSameDay(today, mensdat)) {

                   List <String> datalist = new  ArrayList<String> ();
                   datalist.add(loanref); // Reference loan
                   datalist.add(rs.getString(2)); // Beneficiaires
                   datalist.add(rs.getString(3)); // Responsable
                   datalist.add(String.valueOf(Math.round(echeance*100)/100)); // Somme a rembourser
                   datalist.add("Total"); // Remboursement total
                   datalist.add("autom"); // type automatique
                   
                    ech.put(mensdat, datalist);
                
              // Login.connectedUserfirstName+ " "+Login.connectedUserSurname
             }}                                                          // Numecheance, Date Echeance, Capital, Intérêts, CapitalEcheance l
             // Autres initialisations
             
             
             int i; 
             for (i=2; i<= nbPeriod; i++) {
                 capdu=capdu-capr;
                 interets= capdu*tauxpe;
                 Tinterets=Tinterets+interets;
                 capr= echeance - interets;
                 System.out.println("capr"+capr);
              
                 Tcapr=Tcapr+capr;
                 System.out.println("Tcapr partiel"+Tcapr);
                 
                 
      
                 if (periodicite==0) {
                     mensdat = DateUtils.addMonths(mensdat, 1);
                 } else if (periodicite==1) {
                     mensdat = DateUtils.addMonths(mensdat, 2);
                 } else if (periodicite==2) {
                     mensdat = DateUtils.addMonths(mensdat, 3);
                 } else if (periodicite==3) {
                     mensdat = DateUtils.addMonths(mensdat, 4);
                 }  else if (periodicite==4) {
                     mensdat = DateUtils.addMonths(mensdat, 6);
                 }   else if (periodicite==5) {
                     mensdat = DateUtils.addMonths(mensdat, 12);
                 }
                 pos=listids.indexOf((Integer)i);
                 if (pos != -1) {
                 Map <String, Object> rel = list.get(pos);
                 System.out.println("rel"+rel.get("listmod"));
                 
                double sommedue = echeance + (Double) rel.get("penalite");
                double summremb = (Double) rel.get("sumrembours");
               
               if (summremb < sommedue && (mensdat.before(today) || DateUtils.isSameDay(today, mensdat))) {
                   
               
                 
//                if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
//                else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
//                else 
//                   typerembours = "Virement";
                
                
                   List <String> datalist = new  ArrayList<String> ();
                   datalist.add(loanref); // Reference loan
                   datalist.add(rs.getString(2)); // Beneficiaires
                   datalist.add(rs.getString(3)); // Responsable
                   datalist.add(String.valueOf(Math.round((sommedue-summremb)*100)/100)); // Somme a rembourser
                   datalist.add("partiel"); // Remboursement partiel
                   datalist.add("autom"); // type automatique
                    
                    ech.put(mensdat, datalist);
                   
                 
                
               }
               
             
                } else {    
                        if (mensdat.before(today) || DateUtils.isSameDay(today, mensdat)) {

                   List <String> datalist = new  ArrayList<String> ();
                   datalist.add(loanref); // Reference loan
                   datalist.add(rs.getString(2)); // Beneficiaires
                   datalist.add(rs.getString(3)); // Responsable
                   datalist.add(String.valueOf(Math.round(echeance*100)/100)); // Somme a rembourser
                   datalist.add("Total"); // Remboursement total
                   datalist.add("autom"); // type automatique
                 ech.put(mensdat, datalist);
              // Login.connectedUserfirstName+ " "+Login.connectedUserSurname
             }
             }}
         
            
            
            
            
            
            
            
            
        }
        
        
        
        
     rs.close();
    
     pre.close();
     if(pre2 !=null) pre2.close();
     if (rs2!=null) rs2.close();
     
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row,
                int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (col == 3 || col ==5){
                    ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
                } else {

                    ((JLabel) comp).setHorizontalAlignment(JLabel.RIGHT);

                }

                Date rowdate = (Date) jTable1.getValueAt(row, 4);

                Calendar now = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();
                cal.setTime(rowdate);
                primarycolorbackground = jTable1.getBackground();
                primarycolorforeground = jTable1.getForeground();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                if (month == now.get(Calendar.MONTH) && year == now.get(Calendar.YEAR)) {
                    comp.setBackground(Color.LIGHT_GRAY);
                } else  {
                    comp.setBackground(primarycolorbackground);
                    comp.setForeground(primarycolorforeground);
                }

                return comp;

            }
        };
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        try {
            // TODO add your handling code here:
            data=populateEcheanceTable();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            to2DimArray(data),
            new String [] {
                "Num", "Numero dossier", "Nom & Prenoms ", "Montant dû", "Echeance", "Contact ", "Responable dossier", "Type dossier"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(35);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(230);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(80);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(130);
        //jTable1.getColumnModel().getColumn(7).setPreferredWidth(130);
        //jTable1.getColumnModel().getColumn(11).setWidth(0);
        //jTable1.getColumnModel().getColumn(11).setMinWidth(0);
        //jTable1.getColumnModel().getColumn(11).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new DateRenderer());
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalTextPosition(DefaultTableCellRenderer.CENTER);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(dtcr);
        jTable1.getColumnModel().getColumn(5).setCellRenderer(dtcr);
        jScrollPane1.setViewportView(jTable1);

        jToggleButton1.setText("Regulariser");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Cantarell", 0, 30)); // NOI18N
        jLabel1.setText("Echéances/ Sommes à rembourser");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(503, Short.MAX_VALUE)
                .addComponent(jToggleButton1)
                .addGap(474, 474, 474))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToggleButton1)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        
        if(((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7)).equalsIgnoreCase("autom")){ 
            try {
                this.mn.UpdateLoans((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
                this.dispose();
            } catch (Exception ex) {
                Logger.getLogger(echeances.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        } else if (((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7)).equalsIgnoreCase("libre")){ 
             try {
                this.mn.UpdateLoansfree((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
                this.dispose();
            } catch (Exception ex) {
                Logger.getLogger(echeances.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else if (((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7)).equalsIgnoreCase("terme")){ 
             try {
                this.mn.UpdateLoansterme((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)));
                this.dispose();
            } catch (Exception ex) {
                Logger.getLogger(echeances.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(echeances.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(echeances.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(echeances.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(echeances.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new echeances().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(echeances.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    
    public  Vector populateEcheanceTable() throws Exception {
        
    conn = Connect.ConnectDb();
    int i=1;
    Vector<Vector> membreVector = new Vector<Vector>();
    List<Date> keylist = new ArrayList<>(ech.keySet());
    Collections.sort(keylist, Collections.reverseOrder());
   
     
    Iterator<Date> mapIterator = keylist.iterator();
		
		// iterate over the map
    while (mapIterator.hasNext()) {
	Date key = mapIterator.next();
	//System.out.println("key:" + key + ", values=" + ech.get(key));
			
	Collection<ArrayList> values = ech.getCollection(key);
			
        // iterate over the entries for this key in the map
	for(Iterator<ArrayList> entryIterator = values.iterator(); entryIterator.hasNext();) {
            ArrayList value = entryIterator.next();
          
            Vector<Object> membre = new Vector<Object>();
            membre.add(i);
            membre.add(value.get(0));
            membre.add(value.get(1));
            membre.add(Double.valueOf((String) value.get(3)));
            membre.add(key);
            membre.add("90929392");
            membre.add(value.get(2));
            membre.add(value.get(5));
            membreVector.add(membre);
             
           i++;
            
	}
       
      
        
		
    }
    
  
        System.out.println("finished");
    return membreVector;
}
    
 private void setloanparams(String loanref) throws SQLException {
    
         connect = Connect.ConnectDb();
         String sql ="SELECT firstpaydate, frequency, nbfreq, monthlypayment, nominalrate, Montant, Responsable, DateOri FROM loanrecautom, Loan WHERE loanref = '"+loanref+"'";
         String sql2 ="SELECT SUM(sumrembours) FROM rembours_auto WHERE idloan='"+loanref+"'";
         
         PreparedStatement pst=connect.prepareStatement(sql);
         PreparedStatement pst2=connect.prepareStatement(sql2);
         ResultSet rst =pst.executeQuery();
         ResultSet rst2 =pst2.executeQuery();
    

         while (rst.next()) {
             beginDate=new Date(((java.sql.Date)rst.getDate(1)).getTime());
             periodicite = rst.getInt(2);
             nbPeriod = rst.getInt(3);
             monthlypay=rst.getBigDecimal(4);
             nominalrate = rst.getDouble(5);
             montant = rst.getBigDecimal(6);
             respdoss = rst.getString(7);
             dateOr = rst.getDate(8);
         }
         
    
          
}
         
    
// public void getEcheances() throws Exception {  // getLoan remboursements automatiques
//         // Updatefrontpage
//
//DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
//
//symbols.setGroupingSeparator(' ');
//formatter.setDecimalFormatSymbols(symbols);
//
//         connect= Connect.ConnectDb2();
//         String sql0="SELECT * FROM rembours_auto WHERE idloan='"+this.openloanref+"' ";
//         PreparedStatement pre = connect.prepareStatement(sql0);
//         ResultSet rs = pre.executeQuery();
//         List<Map<String, Object>> list = new ArrayList<>();
//         List<Integer> listids =new ArrayList<>();
//         if (rs.next()) {
//             existed = true;
//             ResultSetMetaData md = rs.getMetaData();
//             int columns = md.getColumnCount();
//             
//             Map<String, Object> row = new HashMap<>(columns);
//             for (int i = 1; i <= columns; ++i) {
//            row.put(md.getColumnName(i), rs.getObject(i));
//            if(i==3) listids.add((Integer)rs.getObject(i));
//            }
//        list.add(row);
//        }
//         
//         
//       while (rs.next()) {
//         ResultSetMetaData md = rs.getMetaData();
//         int columns = md.getColumnCount();    
//        Map<String, Object> row = new HashMap<>(columns);
//        for (int i = 1; i <= columns; ++i) {
//            row.put(md.getColumnName(i), rs.getObject(i));
//            if(i==3) listids.add((Integer)rs.getObject(i));
//        }
//        list.add(row);
//        }
//
//        
//         
//         
//         
//         
//         
//         // setting params
//        setloanparams2();
//        System.out.println("set loans params");
//         // Remove data from old table
//        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
//        int rowCount = dm.getRowCount();
//        //Remove rows one by one from the end of the table
//        for (int i = rowCount - 1; i >= 0; i--) {
//            dm.removeRow(i);
//        }
//        
//        
//        
//         // Fill table
//     
//         int nbPeriodeParAn=1; 
//         Double tauxpe;
//         Double echeance;
//         Double interets;
//         Double Tinterets;
//         Double capr;
//         Double Tcapr;
//         Double capdu=montant.doubleValue();
//         Date mensdat= beginDate;
//         if (periodicite==0) {
//             nbPeriodeParAn=12;
//         } else if (periodicite==1){
//             nbPeriodeParAn=6;
//         }else if (periodicite==2){
//             nbPeriodeParAn=4;
//         }else if (periodicite==3){
//            nbPeriodeParAn=3;
//         } else if (periodicite==4){
//             nbPeriodeParAn=2;
//         } else if (periodicite==5){
//             nbPeriodeParAn=1;
//         }
//         
//             tauxpe=nominalrate/(nbPeriodeParAn*100);
//             echeance=(double) capdu*tauxpe*Math.pow(1+tauxpe, nbPeriod)/(Math.pow(1+tauxpe, nbPeriod)-1);
//             interets=capdu*tauxpe; //initialisation intérêts
//             //somme des intérêts
//             Tinterets=interets;
//             capr=echeance - interets;  // captital remboursé (principal)              
//             // Total du capital remboursé
//             Tcapr= capr; 
//             
//             // Première initialisation, 
//             int pos;
//             String typerembours;
//           
//             pos=listids.indexOf((Integer)1);
//           
//             if(pos !=-1) {
//                 System.out.println("found");
//                 Map <String, Object> rel = list.get(pos);
//                   System.out.println("typeremb2"+rel.get("listmod"));
//                 
//              if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
//              else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
//              else 
//                 typerembours = "Virement";
//                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, (Double) rel.get("penalite"), (double) Math.round(echeance*100)/100+(double) rel.get("penalite"), (Double) rel.get("sumrembours"),typerembours, df.format((Date) rel.get("daterembours")) , (String) rel.get("listemod"), Boolean.TRUE});
//
//              } else {
//                 System.out.println("notfound");
//             ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, null, (double) Math.round(echeance*100)/100, null, null, df.format(new Date()) , null, false});
//              // Login.connectedUserfirstName+ " "+Login.connectedUserSurname
//             }                                                          // Numecheance, Date Echeance, Capital, Intérêts, CapitalEcheance l
//             // Autres initialisations
//             
//             int i; 
//             for (i=2; i<= nbPeriod; i++) {
//                 capdu=capdu-capr;
//                 interets= capdu*tauxpe;
//                 Tinterets=Tinterets+interets;
//                 capr= echeance - interets;
//                 System.out.println("capr"+capr);
//              
//                 Tcapr=Tcapr+capr;
//                 System.out.println("Tcapr partiel"+Tcapr);
//                 
//                 
//      
//                 if (periodicite==0) {
//                     mensdat = DateUtils.addMonths(mensdat, 1);
//                 } else if (periodicite==1) {
//                     mensdat = DateUtils.addMonths(mensdat, 2);
//                 } else if (periodicite==2) {
//                     mensdat = DateUtils.addMonths(mensdat, 3);
//                 } else if (periodicite==3) {
//                     mensdat = DateUtils.addMonths(mensdat, 4);
//                 }  else if (periodicite==4) {
//                     mensdat = DateUtils.addMonths(mensdat, 6);
//                 }   else if (periodicite==5) {
//                     mensdat = DateUtils.addMonths(mensdat, 12);
//                 }
//                 pos=listids.indexOf((Integer)i);
//                 if (pos != -1) {
//                 Map <String, Object> rel = list.get(pos);
//                 System.out.println("rel"+rel.get("listmod"));
//               
//               if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
//               else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
//               else 
//                typerembours = "Virement";
//               
//                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, (Double) rel.get("penalite"), (double) Math.round(echeance*100)/100+(double) rel.get("penalite"), (Double) rel.get("sumrembours"),typerembours, df.format((Date) rel.get("daterembours")) , (String) rel.get("listemod"), Boolean.TRUE});
//                } else {    
//               //  ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capdu*100)/ 100,(double) Math.round(interets*100)/ 100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/100 });
//               ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, null, (double) Math.round(echeance*100)/100, null, null, df.format(new Date()), null, false});
//               // 
//             }}
//         
//             
//        String sum="";
//        System.out.println("This is the value of montant"+montant);
//        if (montant != null) {sum= formatter.format((double) montant.doubleValue());} 
//    
//         DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//         String strDate = dateFormat.format(dateOr);
//         if(montant == null) System.out.println("montant null");
//         if(totalsummont == null) System.out.println("totalsumm null");
//         double perc = (totalsummont.doubleValue()/montant.doubleValue())*100;
//         String rounded = String.format("%.00f", perc);
//         Icon icon = new loanmanager.DynamicIcon(100/100);
//       // Grid updating
//         this.mangersumgrid.getButton(0).setText("<html>Numéro dossier: <br>"+"<font color='#ff00000'>"+openloanref+"</font></html>");
//         this.mangersumgrid.getButton(1).setText("<html>Montant emprunté: <br>"+"<font color='#ff0000'>"+sum+" XOF"+"</font></html>");
//         this.mangersumgrid.getButton(2).setText("<html>Pourcentage remboursé: <br>"+"<font color='#ff0000'>"+rounded+"</font></html>");
//    
//        
//        
//      // l.setBorder(border);
//      
//       
//       //  this.mangersumgrid.getButton(2).setVerticalAlignment(SwingConstants.CENTER);
//      //   this.mangersumgrid.getButton(2).setIcon(icon);
//         
//   
//         this.mangersumgrid.getButton(3).setText("<html>Responsable du dossier: <br>"+"<font color='#ff00000'>"+respdoss+"</font></html>"); 
//         this.mangersumgrid.getButton(4).setText("<html>Date d'origine du prêt: <br>"+"<font color='#ff0000'>"+strDate+"</font></html>");
//
//       //  return null;
//     }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
