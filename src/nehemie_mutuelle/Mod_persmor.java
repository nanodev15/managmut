/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author elommarcarnold
 */
public class Mod_persmor extends javax.swing.JFrame {
    
    Connection connect=null;
     String adulte_photourl="";
     PreparedStatement pst=null;
     PreparedStatement pst2=null;
     private int id;
     private boolean res1=false;
     private boolean res2=false;
     private String typeAdhesion="";  // Type d'adhesion à modifier
     private Date dateEpargne;    // Date d'adhesion epargne
     private Date dateTontine;    // Date d'adhesion Tontine
     private String nomrais=""; //same
    // private String prenom=""; //same
     private String numcarnep="";
     private String numcartont="";
     private main mn;
     private String previousitemselection="";
  
     
     private boolean issavedcarn;
//     private String savednumcarnep="";
//     private String savednumcarntont="";
     private int saveepargne=0; // may we save epargne carnet number
     private int savetontine=0; // may we save tontine carnet number
     private String carnetEnrEpargne="";
     private String carnetEnrTontine="";
     private boolean mustdeleteep=false; // boolean to check we are in the case to delete epargne recording
     private boolean mustdeletetont=false; // boolean to check we are in the case to delete tontine recording

    /** Creates new form Mod_adulte */
    public Mod_persmor() {
        initComponents();
    }
    
     
    public Mod_persmor(String nom, main mn) throws SQLException {
         this.id=getId(nom);
         this.mn=mn;
         initComponents();
         fillform();
    }
    
      // Uppercase filter
    class UppercaseDocumentFilter extends DocumentFilter {
    public void insertString(DocumentFilter.FilterBypass fb, int offset,
            String text, AttributeSet attr) throws BadLocationException {

        fb.insertString(offset, text.toUpperCase(), attr);
    }

    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException {

        fb.replace(offset, length, text.toUpperCase(), attrs);
    }
  }
    
    public void savecarn(int i) throws SQLException{
    System.out.println("called"); 
   // exit(0);
    String sql="";
     if (i==0) { // epargne
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Epargne"+"', '"+numcarnep+"', '"+ this.getId(this.nomrais)+"', '"+"Adulte"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";   
     } else {
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Tontine"+"', '"+numcarnep+"', '"+ this.getId(this.nomrais)+"', '"+"Adulte"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";    
     }
     
    pst2=connect.prepareStatement(sql);
    pst2.execute();
    pst2.close();
}
  
    public boolean check_tontine_before_date(int id, Date date) throws SQLException {
       java.sql.Date sqldate=new java.sql.Date(date.getTime());
       String sql0="SELECT idTontine as id FROM Tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte' AND MONTH(DateTontine) < MONTH('"+sqldate+"') AND YEAR(DateTontine) <= YEAR('"+sqldate+"') UNION SELECT idretraits_tontine as id FROM retraits_tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte' AND DATE(DateRet) < '"+sqldate+"'";
       connect=Connect.ConnectDb();
       Statement stmt = null;
       ResultSet rs = null;
       try {
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            if (rs.next()) {
                     return true;
              }
                
                  
                  //    JOptionPane.showMessageDialog( this, "hostel name already exists","Error", JOptionPane.ERROR_MESSAGE);
                  //      y3.setText("");
                  //      y3.requestDefaultFocus();
                  //     return;
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
              }
              
 //             connect.close();
              stmt.close();
              rs.close();
             return false;
}         
    
    private static class Emailvalidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN = 
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public Emailvalidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }
    
   
   
    /**
     * Validate hex with regular expression
     * 
     * @param hex
     *            hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex) {

        matcher = pattern.matcher(hex);
        return matcher.matches();

    }
    }
    
     public void deleteSavedcarn() throws SQLException {
        String sql= "DELETE FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Adulte'";
        connect=Connect.ConnectDb();
        Statement stmt = null;
   
        
        try
        {
            stmt= connect.createStatement();
            stmt.executeUpdate(sql);
          }
        catch(Exception e)
        {
           Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, e);

        }
        // Close connection
        if(connect !=null) connect.close();
        if(stmt!=null) stmt.close();

        
        
  }
    
     private int getId(String raisonSociale) throws SQLException {
        connect = Connect.ConnectDb();
        
            String sql01 = "SELECT idProfil_persmorale FROM Profil_persmorale WHERE Raison_sociale= '" +raisonSociale+"'";
            Statement stmt = null;
            System.out.println("sql01"+sql01);
            connect = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = connect.createStatement();
                rs1 = stmt.executeQuery(sql01);
                //result= rs1.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs1.next()) {

                    result = rs1.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        //    if(connect!=null) connect.close();
            if (stmt!=null) stmt.close();
            if (rs1!=null) rs1.close();
            return result;
       
    }
     
    
    public void fillform() throws SQLException {
         String sql0="SELECT * From Profil_persmorale WHERE idProfil_persmorale='"+this.id+"'";
         
               // Vérifier numero carnet 
            connect=Connect.ConnectDb();
            // check carnets enregistrés
            
         //   issavedcarn=check_saved_carn();
            Statement stmt = null;
            ResultSet rs = null;
           
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            while(rs.next()){
                  jTextField3.setText(rs.getString(2)); // Raison sociale
                  nomrais =rs.getString(2);   
                  nomrais=jTextField3.getText();
                  demoDateField3.setDate(rs.getDate(3)); // Date de création
                  jTextField5.setText((rs.getString(4))); // Lieu de creation
                  jTextField6.setText((rs.getString(5))); // Activité principale
                  jTextField7.setText((rs.getString(6))); // Adresse 
                  jTextField18.setText((rs.getString(7))); // Numero COE
                  jTextField19.setText(rs.getString(8).split(" ")[0]); // Nom personne de référence
                  jTextField19.setText(rs.getString(8).split(" ")[1]); // Prénom personne de référence
                  // Téléphone 
                 
                  if (rs.getString(9)!=null || ! rs.getString(13).isEmpty())  jComboBox4.setSelectedIndex(0);
                  else if (rs.getString(9).startsWith("+")) jComboBox4.setSelectedIndex(1);  // Type de téléphone nat ou international
                  
                  jFormattedTextField3.setText((rs.getString(9)));  // Téléphone 
                  
                  // formattage téléphone 
                  if (jComboBox4.getSelectedIndex()==1){  //international
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber phoneNumber;
                  try {
                  phoneNumber = phoneUtil.parse(jFormattedTextField3.getText(), "");
                  if (!phoneUtil.isValidNumber(phoneNumber)){
                   jLabel9.setText("Numéro non valide");
                                             
                    }
            
                    String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                    jFormattedTextField3.setDocument(new javax.swing.text.PlainDocument());
                    jFormattedTextField3.setText(formatted);
                  } catch (NumberParseException ex) {
                    jLabel9.setText("Numéro non valide");
                    Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                 }

       // partie adhésion
       if(rs.getString(10).equalsIgnoreCase("Epargne")) {
           jComboBox3.setSelectedIndex(0);// Type d'adhésion
           jFormattedTextField2.setEnabled(false);
           demoDateField2.setEnabled(false);
           if(rs.getString(11) !=null)    jFormattedTextField1.setText(rs.getString(11));  // Numéro carnet epargne
           demoDateField1.setDate(rs.getDate(14)); // Date d'adhésion épargne
           demoDateField2.setDate(rs.getDate(15)); 
           demoDateField2.setEnabled(false);
           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
           numcarnep=rs.getString(11);
           
       } else if(rs.getString(10).equalsIgnoreCase("Tontine")) {
           jComboBox3.setSelectedIndex(1);// Type d'adhésion
           jFormattedTextField1.setEnabled(false);
           demoDateField2.setEnabled(true);
          if(rs.getString(11) !=null)   jFormattedTextField2.setText(rs.getString(11));  // Numéro carnet tontine
            demoDateField2.setDate(rs.getDate(14)); // Date d'adhésion tontine
            demoDateField1.setDate(rs.getDate(15)); // Date d'adhésion épargne
            demoDateField1.setEnabled(false);
           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
           numcartont=rs.getString(11);
       } else if(rs.getString(10).equalsIgnoreCase("Epargne & Tontine")) {
           jComboBox3.setSelectedIndex(2);// Type d'adhésion
           if(rs.getString(11) !=null) jFormattedTextField1.setText(rs.getString(11).substring(0, 4));  // Numéro carnet Epargne
           if(rs.getString(11) !=null) jFormattedTextField2.setText(rs.getString(11).substring(5, rs.getString(11).length())); // Numéro carnet Tontine
           demoDateField1.setDate(rs.getDate(14)); // Date d'adhésion Epargne
           demoDateField2.setDate(rs.getDate(15)); // Date d'adhésion tontine
           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
           numcarnep=rs.getString(11).substring(0, 4);
           numcartont=rs.getString(11).substring(5, rs.getString(11).length());

       }
       
       // Droit d'adhésion et part sociale 
       
       jTextField16.setText(String.valueOf(rs.getInt(12)));   // Droits d'adhésion
       jTextField17.setText(String.valueOf(rs.getInt(13)));   // Part Sociale
}
       typeAdhesion=(String) jComboBox3.getSelectedItem();    // Type d'adhesion recorded

      
                 
    }
            
      if (connect!=null) connect.close();
      if (stmt!=null) stmt.close();
      if(rs!=null)  rs.close();
    }  
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        demoDateField3 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel35 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jPanel36 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel25 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel26 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jPanel30 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/mod_pers_morale.png"))); // NOI18N

        jPanel4.setLayout(new java.awt.GridLayout(11, 1, 0, 5));

        jPanel6.setLayout(new java.awt.GridLayout(1, 2));

        jLabel4.setText("Raison sociale");
        jPanel6.add(jLabel4);

        DocumentFilter filter = new UppercaseDocumentFilter();
        ((AbstractDocument) jTextField3.getDocument()).setDocumentFilter(filter);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jPanel6.add(jTextField3);

        jPanel4.add(jPanel6);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jLabel5.setText("Date de création");
        jPanel5.add(jLabel5);

        demoDateField3.setYearDigitsAmount(4);
        jPanel5.add(demoDateField3);

        jPanel4.add(jPanel5);

        jPanel7.setLayout(new java.awt.GridLayout(1, 2));

        jLabel6.setText("Lieu de création");
        jPanel7.add(jLabel6);

        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jPanel7.add(jTextField5);

        jPanel4.add(jPanel7);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        jLabel7.setText("Activité principale");
        jPanel8.add(jLabel7);

        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jPanel8.add(jTextField6);

        jPanel4.add(jPanel8);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel31.setLayout(new java.awt.GridLayout(1, 2));

        jLabel23.setText("Adresse");
        jPanel31.add(jLabel23);

        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });
        jPanel31.add(jTextField7);

        jPanel4.add(jPanel31);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel32.setLayout(new java.awt.GridLayout(1, 2));

        jLabel26.setText("Numéro COE");
        jPanel32.add(jLabel26);

        jTextField18.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField18FocusLost(evt);
            }
        });
        jPanel32.add(jTextField18);

        jPanel4.add(jPanel32);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel33.setLayout(new java.awt.GridLayout(1, 2));

        jLabel27.setText("Nom pers de ref");
        jPanel33.add(jLabel27);

        ((AbstractDocument) jTextField19.getDocument()).setDocumentFilter(filter);
        jTextField19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField19FocusLost(evt);
            }
        });
        jPanel33.add(jTextField19);

        jPanel4.add(jPanel33);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel34.setLayout(new java.awt.GridLayout(1, 2));

        jLabel28.setText("Prénoms pers de ref");
        jPanel34.add(jLabel28);

        jTextField20.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField20FocusLost(evt);
            }
        });
        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField20KeyTyped(evt);
            }
        });
        jPanel34.add(jTextField20);

        jPanel4.add(jPanel34);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel11.setLayout(new java.awt.GridLayout(1, 2));

        jLabel11.setText("Téléphone");
        jPanel11.add(jLabel11);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "National", "International" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });
        jPanel11.add(jComboBox4);

        jPanel4.add(jPanel11);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel35.setLayout(new java.awt.GridLayout(1, 2));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jPanel35.add(jLabel2);

        try{
            jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField3ActionPerformed(evt);
            }
        });
        jFormattedTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField3FocusLost(evt);
            }
        });
        jPanel35.add(jFormattedTextField3);

        jPanel4.add(jPanel35);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel36.setLayout(new java.awt.GridLayout(1, 2));

        jLabel8.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jPanel36.add(jLabel8);

        jLabel9.setForeground(new java.awt.Color(255, 47, 0));
        jPanel36.add(jLabel9);

        jPanel4.add(jPanel36);

        jPanel10.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 0, 5));
        jPanel9.add(jLabel3);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 379, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(20, 20, 20)
                            .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(0, 40, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 374, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22))
        );

        jTabbedPane1.addTab("Identification", jPanel1);

        jPanel22.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(1, 2));

        jLabel17.setText("Type d'adhésion");
        jPanel23.add(jLabel17);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Epargne", "Tontine", "Epargne & Tontine" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jPanel23.add(jComboBox3);

        jPanel22.add(jPanel23);

        jPanel24.setLayout(new java.awt.GridLayout(1, 2));

        jLabel18.setText("Numéro carnet epargne");
        jPanel24.add(jLabel18);

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jPanel24.add(jFormattedTextField1);

        jPanel22.add(jPanel24);

        jPanel25.setLayout(new java.awt.GridLayout(1, 2));

        jLabel19.setText("Numéro carnet tontine");
        jPanel25.add(jLabel19);

        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####"))));
        jPanel25.add(jFormattedTextField2);

        jPanel22.add(jPanel25);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel26.setLayout(new java.awt.GridLayout(1, 2));

        jLabel20.setText("Droits d'adhésion");
        jPanel26.add(jLabel20);

        DocumentFilter numericFilter = new DocumentFilter(){

            @Override
            public void insertString(FilterBypass fb, int offset,
                String string, AttributeSet attr)
            throws BadLocationException {
                fb.insertString(offset, string.replaceAll("[^\\d]", ""), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length,
                String text, AttributeSet attrs)
            throws BadLocationException {

                fb.replace(offset, length, text.replaceAll("[^\\d]", ""), attrs);
            }
        };
        ((AbstractDocument) jTextField16.getDocument()).setDocumentFilter(numericFilter);
        jPanel26.add(jTextField16);

        jPanel22.add(jPanel26);

        jPanel27.setLayout(new java.awt.GridLayout(1, 2));

        jLabel21.setText("Part Sociale");
        jPanel27.add(jLabel21);

        ((AbstractDocument) jTextField17.getDocument()).setDocumentFilter(numericFilter);
        jPanel27.add(jTextField17);

        jPanel22.add(jPanel27);

        jPanel29.setLayout(new java.awt.GridLayout(1, 2));

        jLabel25.setText("Date d'adhésion épargne");
        jPanel29.add(jLabel25);

        demoDateField1.setYearDigitsAmount(4);
        jPanel29.add(demoDateField1);

        jPanel22.add(jPanel29);

        jPanel30.setLayout(new java.awt.GridLayout(1, 2));

        jLabel24.setText("Date d'adhésion tontine");
        jPanel30.add(jLabel24);

        demoDateField2.setYearDigitsAmount(4);
        jPanel30.add(demoDateField2);

        jPanel22.add(jPanel30);

        org.jdesktop.layout.GroupLayout jPanel20Layout = new org.jdesktop.layout.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(jPanel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 394, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(jPanel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 337, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Adhésion", jPanel20);

        jButton1.setText("Enregistrer les modifications");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .add(jSeparator1)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 474, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jFormattedTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField3ActionPerformed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        jTextField3.setText(jTextField3.getText().trim());
        jTextField3.setText(jTextField3.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField3FocusLost


    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        // TODO add your handling code here:
        jTextField5.setText(jTextField5.getText().trim());
        jTextField5.setText(jTextField5.getText().replaceAll("\\s+", " "));
        jTextField5.setText(WordUtils.capitalizeFully(jTextField5.getText()));
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
        // TODO add your handling code here:
        jTextField6.setText(jTextField6.getText().trim());
        jTextField6.setText(jTextField6.getText().replaceAll("\\s+", " "));
        jTextField6.setText(jTextField6.getText().substring(0,1)+jTextField6.getText().substring(1));
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
        // TODO add your handling code here:
        jTextField7.setText(jTextField7.getText().trim());
        jTextField7.setText(jTextField7.getText().replaceAll("\\s+", " "));
        jTextField7.setText(WordUtils.capitalizeFully(jTextField7.getText()));
    }//GEN-LAST:event_jTextField7FocusLost

    private void jTextField18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField18FocusLost
        // TODO add your handling code here:
        jTextField18.setText(jTextField18.getText().trim());
        jTextField18.setText(jTextField18.getText().replaceAll("\\s+", ""));
        
    }//GEN-LAST:event_jTextField18FocusLost

    private void jTextField19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField19FocusLost
        // TODO add your handling code here:
        jTextField19.setText(jTextField19.getText().trim());
        jTextField19.setText(jTextField19.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField19FocusLost

    private void jTextField20FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField20FocusLost
        // TODO add your handling code here:
          jTextField20.setText(jTextField20.getText().trim());
          jTextField20.setText(jTextField20.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField20FocusLost

    private void jTextField20KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyTyped
        // TODO add your handling code here:
    if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
        evt.consume();
    } else if (jTextField20.getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else if(jTextField20.getText().endsWith(" ") ){
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else{
         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
    }
    }//GEN-LAST:event_jTextField20KeyTyped
public boolean check_epargne_before_date(int id, Date date ) throws SQLException {
       java.sql.Date sqldate=new java.sql.Date(date.getTime());
       String sql0="SELECT idEpargne FROM Epargne WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte' AND DateEpargne < { d '"+ sqldate.toString() + "' }";
       connect=Connect.ConnectDb();
       Statement stmt = null;
       ResultSet rs = null;
       try {
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            if (rs.next()) {
                     return true;
                  }
                
                  
                  //    JOptionPane.showMessageDialog( this, "hostel name already exists","Error", JOptionPane.ERROR_MESSAGE);
                  //      y3.setText("");
                  //      y3.requestDefaultFocus();
                  //     return;
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
              }
              
           //   connect.close();
              stmt.close();
              rs.close();
             return false;
} 
    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
           if (jComboBox4.getSelectedIndex()==0){
            
                
                jFormattedTextField3.setDocument(new javax.swing.text.PlainDocument());       
            try {
                jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
            } catch (ParseException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
               
        }else {       
           jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory());         
           jFormattedTextField3.setDocument(new PhoneDocument());
           jFormattedTextField3.setText("+");
            
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jFormattedTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField3FocusGained
        // TODO add your handling code here:
          jLabel9.setText("");
        
        if (jComboBox4.getSelectedIndex()==1) {            
             if (jFormattedTextField3.getText().equalsIgnoreCase("+")){
                    jFormattedTextField3.setDocument(new PhoneDocument());
                  jFormattedTextField3.setText("+");
             } else {
                 String before =jFormattedTextField3.getText();
                 before=before.replaceAll("\\s", "");
                 jFormattedTextField3.setText(before);
                 
             }
        }
    }//GEN-LAST:event_jFormattedTextField3FocusGained
private boolean verifynumcarn(int i) throws SQLException {
    String sql0="";
        if (i==0) sql0="SELECT Num_carnet FROM Profil_adulte WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_adulte WHERE Type_adhesion='"+"Epargne & tontine"+"'" + " UNION " +"SELECT Num_carnet FROM Profil_enfant WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_enfant WHERE Type_adhesion='"+"Epargne et tontine"+"'" + " UNION "+ "SELECT Num_carnet FROM Profil_persmorale WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_persmorale WHERE Type_adhesion='"+"Epargne et tontine"+"'";
        else sql0="SELECT Num_carnet FROM Profil_adulte WHERE Type_adhesion='"+"Tontine"+"' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_adulte WHERE Type_adhesion='"+"Epargne et tontine"+"'" + " UNION " +"SELECT Num_carnet FROM Profil_enfant WHERE Type_adhesion='"+"Tontine"+"' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_enfant WHERE Type_adhesion='"+"Epargne et tontine"+"'" + " UNION "+ "SELECT Num_carnet FROM Profil_persmorale WHERE Type_adhesion='"+"Tontine"+"' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_persmorale WHERE Type_adhesion='"+"Epargne et tontine"+"'";
           
            // Vérifier numero carnet 
            connect=Connect.ConnectDb();
            Statement stmt = null;
            ResultSet rs = null;
            
              try {
                  stmt= connect.createStatement();
                  rs=stmt.executeQuery(sql0);
                  if(i==0) {  // compare tontine
                while (rs.next()) {
                    System.out.println("value"+rs.getString(1));
                     if(jFormattedTextField2.getText().equalsIgnoreCase(rs.getString(1))) return true;
                  }
                 } else {
                       while (rs.next()) {
                     if(jFormattedTextField3.getText().equalsIgnoreCase(rs.getString(1))) return true;
                  }
                  }

              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
              }
              
             // connect.close();
              stmt.close();
              rs.close();
            return false;
}


    private void jFormattedTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField3FocusLost
        // TODO add your handling code here:
             
        if (jComboBox4.getSelectedIndex()==1) {  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(jFormattedTextField3.getText(), "");
            if (!phoneUtil.isValidNumber(phoneNumber)){
                jLabel9.setText("Numéro non valide");
                                             
            }
            
            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            jFormattedTextField3.setDocument(new javax.swing.text.PlainDocument());
            jFormattedTextField3.setText(formatted);
        } catch (NumberParseException ex) {
            jLabel9.setText("Numéro non valide");
            Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
        }

            
        }
    }//GEN-LAST:event_jFormattedTextField3FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:       
        {                                         

        Emailvalidator emailValidator = new Emailvalidator();
        
        // Validate carnet num
       if(jComboBox3.getSelectedIndex()==0 || jComboBox3.getSelectedIndex()==2) {
        try {
                  res1 = verifynumcarn(0);
                  System.out.println("res1 vaut"+res1);
                 
             } catch (SQLException ex) {
                 Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox3.getSelectedIndex()==1 || jComboBox3.getSelectedIndex()==2) {
         try {
                  res2 = verifynumcarn(1);
               
             } catch (SQLException ex) {
                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
         
       
      if(jTextField3.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ raison sociale");
      } else if(jTextField6.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ activité principale");
     } else if(jTextField19.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ nom personne de référence");
        } else if(jTextField20.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ prénom personne de référence");
        }
        
        else if(jFormattedTextField3.getText().equalsIgnoreCase("  -  -  -  ") || jFormattedTextField3.getText().equalsIgnoreCase("+") ){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ téléphone");
        } else if (jTextField16.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner les droits d'adhésion ");
           
} else if (jFormattedTextField1.getText().equalsIgnoreCase("0000") || jFormattedTextField2.getText().equalsIgnoreCase("0000")) { 
            
         JOptionPane.showMessageDialog(this, "Numéro de carnet 0000 invalide");
        } else if (jComboBox3.getSelectedIndex()==0 && jFormattedTextField1.getText().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Numéro de carnet Epargne invalide");   
        }else if (jComboBox3.getSelectedIndex()==1 && jFormattedTextField2.getText().isEmpty()) { 
           JOptionPane.showMessageDialog(this, "Numéro de carnet Tontine invalide");   
        } else if (jComboBox3.getSelectedIndex()==2 && (jFormattedTextField1.getText().isEmpty() || jFormattedTextField2.getText().isEmpty())){
           JOptionPane.showMessageDialog(this, "Numéro de carnet invalide"); 
        } else if(jComboBox3.getSelectedIndex()==0 && res1==true) {
           JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");  
        }else if(jComboBox3.getSelectedIndex()==1 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if(jComboBox3.getSelectedIndex()==2 && res1==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé"); 
        } else if (jComboBox3.getSelectedIndex()==2 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        }  else if (demoDateField1.getDate()==null && (jComboBox3.getSelectedIndex()==0 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Epargne"); 
        } else if (demoDateField2.getDate()==null && (jComboBox3.getSelectedIndex()==1 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Tontine"); 
        } else { 
            // Check if someone has already this name
            boolean inserted=false;
            boolean success= true;
            boolean check_epargne=false;
            boolean check_tontine=false;
            Statement stmt = null;
            ResultSet rs2=null;
         
            
            // everything is correct-insert modify in the database
            
            if (!inserted){
            // Check subscription case by case
            //First case chose Epargne and changed the date   
            if(typeAdhesion.equalsIgnoreCase("Epargne")) {
                if(((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne") || ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                     if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) mustdeletetont=true; // must delete tontine data
                    if(dateEpargne.before(demoDateField1.getDate())) {   // N'est pas urgent mais prévoir l'éventualité que la date d'épargne soit vide ou nulle
                         try {
                             check_epargne = check_epargne_before_date(getId(this.nomrais), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                    }

            // Choose Tontine
            } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine")) { 
                    mustdeletetont=true;
                    System.out.println("yes in Tontine");
                         try {  // A tester
                             check_epargne = check_epargne_before_date(getId(this.nomrais), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         
                          if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, Notez que des données épargne sont encore enregistrées pour ce client");
                              saveepargne=1;
                         } else {
                              saveepargne=2;
                          }
                        
           }
            // Case where Tontine was chosen    
            } else if (typeAdhesion.equalsIgnoreCase("Tontine")) {
                  if(((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine") || ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                    if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) mustdeleteep = true;
                       if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) mustdeleteep = true; 
                       if(dateTontine.before(demoDateField2.getDate())) {
                           
                            try {
                             check_tontine = check_tontine_before_date(getId(this.nomrais), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                             
                       }
                  } else if ((((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne"))) {
                       mustdeleteep = true; 
                       try {  // A tester
                       
                            
                             check_tontine = check_tontine_before_date(getId(this.nomrais), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         
                          if (check_tontine==true) {
                              JOptionPane.showMessageDialog(null, "Attention, Notez que des données tontine sont encore enregistrées pour ce client");
                              savetontine=1;
                         } else {
                              savetontine=2;
                         }
                      
                  }
            }
            else if (typeAdhesion.equalsIgnoreCase("Epargne & Tontine")) {
                 //same choice
                 if ((((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine"))){
                      if(dateEpargne.before(demoDateField1.getDate())) {
                      try {
                             check_epargne = check_epargne_before_date(getId(this.nomrais), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                      
                       }
                      
                       if(dateTontine.before(demoDateField2.getDate())) {                         
                            try {
                             check_tontine = check_tontine_before_date(getId(this.nomrais), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                               
                 }
                
            } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne")) {
                    if(dateEpargne.before(demoDateField1.getDate())) {
                         try {
                             check_epargne = check_epargne_before_date(getId(this.nomrais), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                    }
                    
                    // Avertissement tontine 
                    
                     try {  // A tester
                             check_tontine = check_tontine_before_date(getId(this.nomrais), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         
                          if (check_tontine==true) {
                              JOptionPane.showMessageDialog(null, "Attention, Notez que des données tontine sont encore enregistrées pour ce client");
                              savetontine=1;
                         } else {
                              savetontine=2;
                          }
                    

                
            } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine")) {
                if(dateTontine.before(demoDateField2.getDate())) {
                           
                         try {
                             check_tontine = check_tontine_before_date(getId(this.nomrais), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                
                } 
                
                // Avertissement epargne 
                try {  // A tester
                            check_epargne = check_epargne_before_date(getId(this.nomrais), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         
                          if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, Notez que des données épargne sont encore enregistrées pour ce client");
                              saveepargne=1;
                         } else {
                              savetontine=2;
                          }
            } 
            }
            
            if (success==true){
                try {
                    // Now I can do the update
                    connect=Connect.ConnectDb();
                    String sqlmod="UPDATE Profil_persmorale SET Raison_sociale = ?, Date_creation = ?, Lieu_creation = ?, Activite_principale = ?, Adresse = ?, Num_COE = ?, Personne_reference = ?, Telephone_ref = ?, Type_adhesion = ?, Num_carnet = ?, Droit_adhesion = ?, Part_sociale = ?, Date_adhesion_Ep = ?, Date_adhesion_to = ? "+"WHERE idProfil_persmorale='"+this.getId(this.nomrais)+"';";
                    pst=connect.prepareStatement(sqlmod);
                    String numcarn="";
                           if(jComboBox3.getSelectedIndex()==0) {
                               numcarn=jFormattedTextField1.getText();
                           } else if (jComboBox3.getSelectedIndex()==1){
                               numcarn=jFormattedTextField2.getText();
                           } else {
                                numcarn=jFormattedTextField1.getText() +"-"+jFormattedTextField2.getText();
                           }
                    // Initialisations       
                    pst.setString(1, jTextField3.getText().trim());   // Nom
                    if (demoDateField3.getDate()!=null) { pst.setDate(2, new java.sql.Date(demoDateField3.getDate().getTime())); } else { pst.setDate(2, null);}  // Date de création
                    pst.setString(3, jTextField5.getText().trim()); // Lieu de création
                    pst.setString(4, jTextField6.getText().trim());   // Activité principale
                    pst.setString(5, jTextField7.getText().trim()); // Adresse 
                    pst.setString(6, jTextField18.getText().trim()); // Numéro COE
                    String fullname= jTextField19.getText()+ " "+ jTextField20.getText();
                    pst.setString(7, fullname); // Nom personne de référence
                    pst.setString(8, jFormattedTextField3.getText()); // Téléphone 
                    pst.setString(9, (String) jComboBox3.getSelectedItem()); //Type d'adhésion
                    pst.setString(10, numcarn); // Numéro de carnet 
                    pst.setInt(11, Integer.valueOf(jTextField16.getText()));  // Droits d'adhésion
                    pst.setInt(12, Integer.valueOf(jTextField17.getText()));   // Part sociale 
                    if(saveepargne==0) {
                    if (demoDateField1.getDate()!=null &&  demoDateField1.isEnabled()){ pst.setDate(13, new java.sql.Date(demoDateField1.getDate().getTime())); } else pst.setDate(18, null);  // Date d'adhésion épargne 
                    }
                    
                    if(savetontine==0) {
                    if (demoDateField2.getDate()!=null && demoDateField2.isEnabled()){ pst.setDate(14, new java.sql.Date(demoDateField2.getDate().getTime())); } else pst.setDate(19, null);// Date d'adhésion tontine
                    }
                   
                    
                    // Contraintes additionnels
                    if (saveepargne==1) {
                        // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                        savecarn(0);
                        pst.setDate(18, new java.sql.Date(dateEpargne.getTime())); // On garde l'ancienne date
                        System.out.println("savedepargne1");
                    }
                    
                    if (saveepargne==2) {
                        pst.setDate(18, null);
                        System.out.println("savedepargne2");
                    } 
                    
                    if (savetontine==1) {
                        
                        // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                        savecarn(1);
                        pst.setDate(19, new java.sql.Date(dateTontine.getTime())); 
                        
                    }
                    
                     if (savetontine==2) {
                        // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                       pst.setDate(19, null);
                        
                    }
                  
                    pst.execute();
                    
                    if ((mustdeleteep && ! carnetEnrEpargne.isEmpty()) || (mustdeletetont && ! carnetEnrTontine.isEmpty())){
                        System.out.println("it is true");
                        deleteSavedcarn();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Les modifications ont été enregistrées avec succès");
                    this.nomrais=jTextField3.getText();
                    numcarnep=jFormattedTextField1.getText();
                    numcartont=jFormattedTextField2.getText();
                    this.mn.refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(Mod_persmor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            // closing connections 
            if(connect!=null) try {
                connect.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(stmt!=null)    try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(rs2!=null)      try {
                rs2.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(pst!=null)     try {
                pst.close();
            } catch (SQLException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }}
        
        
        
    }   
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
           if(evt.getStateChange() == ItemEvent.DESELECTED) {
               previousitemselection=(String) evt.getItem();
            }
            if (jComboBox3.getSelectedIndex()==0) {            
                if(previousitemselection.equalsIgnoreCase("tontine")) {
                    if(issavedcarn && ! carnetEnrEpargne.isEmpty()) {
                        jFormattedTextField1.setText(carnetEnrEpargne);
                    }
                }

            jFormattedTextField2.setEnabled(false);         
            jFormattedTextField1.setEnabled(true);
            demoDateField2.setEnabled(false);
            demoDateField1.setEnabled(true);
        } else if (jComboBox3.getSelectedIndex()==1){

                if(previousitemselection.equalsIgnoreCase("Epargne")) {
                    if(issavedcarn && ! carnetEnrTontine.isEmpty()) {
                        jFormattedTextField1.setText(carnetEnrTontine);
                    }
                }
                
             jFormattedTextField1.setEnabled(false);
             jFormattedTextField2.setEnabled(true);

             demoDateField1.setEnabled(false);
             demoDateField2.setEnabled(true);
        } else {
            jFormattedTextField1.setEnabled(true);
            jFormattedTextField2.setEnabled(true);
            demoDateField1.setEnabled(true);
            demoDateField2.setEnabled(true);
             
              if(previousitemselection.equalsIgnoreCase("tontine")) {
                    if(issavedcarn && ! carnetEnrEpargne.isEmpty()) {
                        jFormattedTextField1.setText(carnetEnrEpargne);
                    }
                }
              
               if(previousitemselection.equalsIgnoreCase("Epargne")) {
                    if(issavedcarn && ! carnetEnrTontine.isEmpty()) {
                        jFormattedTextField2.setText(carnetEnrTontine);
                    }
                }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

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
            java.util.logging.Logger.getLogger(Mod_persmor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mod_persmor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mod_persmor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mod_persmor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mod_persmor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField3;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables

}
