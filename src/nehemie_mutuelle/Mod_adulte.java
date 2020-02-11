/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import static java.lang.System.exit;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
//import javax.swing.JFormattedTextField;
//import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author elommarcarnold
 */
public class Mod_adulte extends javax.swing.JFrame {
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
     private String nom=""; //same
     private String prenom=""; //same
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
   // Email validator 
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
          
    private static boolean isAllUpper(String s){
        for(char c:s.toCharArray()){
            if(Character.isLetter(c) && Character.isLowerCase(c)){
                return false;
          }
        }
        return true;
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
        
    
    
    public boolean check_saved_carn() {
       String sql0="SELECT * FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Adulte'";
       connect=Connect.ConnectDb();
       Statement stmt = null;
       ResultSet rs = null;
       try {
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            if (rs.next()) {
                     if(rs.getString(2).equalsIgnoreCase("Epargne")) carnetEnrEpargne= rs.getString(3);
                     else carnetEnrTontine = rs.getString(3);
                       stmt.close();
                       rs.close();
                     //  connect.close();
                     return true ;
              }
                
                  
                  //    JOptionPane.showMessageDialog( this, "hostel name already exists","Error", JOptionPane.ERROR_MESSAGE);
                  //      y3.setText("");
                  //      y3.requestDefaultFocus();
                  //     return;
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
              }
              
           //   connect.close();
             
             
        return false;
        
    }
          
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

public void savecarn(int i) throws SQLException{
    System.out.println("called"); 
   // exit(0);
    String sql="";
     if (i==0) { // epargne
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Epargne"+"', '"+numcarnep+"', '"+ this.getId(nom, prenom)+"', '"+"Adulte"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";   
     } else {
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Tontine"+"', '"+numcarnep+"', '"+ this.getId(nom, prenom)+"', '"+"Adulte"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";    
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
     
     private int getId(String nomEpargnant, String prenomEpargnant) throws SQLException {
        connect = Connect.ConnectDb();
        
            String sql01 = "SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
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

    /** Creates new form Mod_adulte */
    public Mod_adulte() {
        initComponents();
    }
    
     public Mod_adulte(int id) throws SQLException {
         this.id=id;
         initComponents();
         fillform();
    }
     
     public Mod_adulte(String nom, String prenom, main mn) throws SQLException {
         this.id=getId(nom, prenom);
         this.mn=mn;
         initComponents();
         fillform();
    }
     
//    public void check_saved_carn() throws SQLException {
//         String sql0="SELECT * From carnet_enr WHERE idadherent='"+this.id+"' AND typeadherent='"+"Adulte"+"'"; 
//         Statement stmt = null;
//         ResultSet rs = null;
//         stmt= connect.createStatement();
//         rs=stmt.executeQuery(sql0);
//         while(rs.next()){
//               if(rs.getString(id).equalsIgnoreCase("Epargne"))  carnetEnrEpargne = rs.getString(3);
//               if(rs.getString(id).equalsIgnoreCase("Tontine"))  carnetEnrTontine = rs.getString(3);
//         }
//         
//        if (stmt!=null) stmt.close();
//        if(rs!=null)  rs.close();
//         
//    } 
    public void fillform() throws SQLException {
         String sql0="SELECT * From Profil_adulte WHERE idProfil_adulte='"+this.id+"'";
         
        
            // Vérifier numero carnet 
            connect=Connect.ConnectDb();
            // check carnets enregistrés
            
            issavedcarn=check_saved_carn();
            Statement stmt = null;
            ResultSet rs = null;
           
            stmt= connect.createStatement();
            rs=stmt.executeQuery(sql0);
            while(rs.next()){
                  jTextField3.setText(rs.getString(2)); // Nom
                  nom=jTextField3.getText();
                  jTextField4.setText(rs.getString(3)); // Prenom
                  prenom=jTextField4.getText();
                  demoDateField3.setDate(rs.getDate(6)); // Date de naissance
                  jTextField6.setText((rs.getString(8))); // Lieu de naissance
                  if(rs.getString(4).equalsIgnoreCase("homme") || rs.getString(4).equalsIgnoreCase("masculin")) { jComboBox4.setSelectedIndex(0);  // Sexe 
                  } else {
                      jComboBox4.setSelectedIndex(1);
                  }
                  if(rs.getString(5)!= null) {   // Photo 
                  Image temp = new ImageIcon(rs.getString(5)).getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH);
                  jLabel2.setIcon(new ImageIcon(temp));
                  adulte_photourl=rs.getString(5);
                      
                  }
                  
                  if(rs.getString(7)!= null) { // Situation matrimoniale
                      if (rs.getString(7).equalsIgnoreCase("mariage")) jComboBox1.setSelectedIndex(0);
                      else if (rs.getString(7).equalsIgnoreCase("célibat")) jComboBox1.setSelectedIndex(1);
                      else if (rs.getString(7).equalsIgnoreCase("divorce")) jComboBox1.setSelectedIndex(2);
                      else jComboBox1.setSelectedIndex(3);
                  }
                  
                  jTextField8.setText(rs.getString(9));   //Nationalité
                  jTextField9.setText(rs.getString(10));  // Profession
                  jTextField10.setText(rs.getString(11)); //Adresse 
                  jTextField11.setText(rs.getString(12));  // Quartier
                  if(rs.getString(13).startsWith("+")) jComboBox2.setSelectedIndex(1);  // Type de téléphone nat ou international
                  else if(rs.getString(13)!=null || ! rs.getString(13).isEmpty()) jComboBox2.setSelectedIndex(0);
                  jFormattedTextField1.setText((rs.getString(13)));  // Téléphone 
                  jTextField14.setText(rs.getString(14));     // Email 
                  
                  // formattage téléphone 
       if (jComboBox2.getSelectedIndex()==1){  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(jFormattedTextField1.getText(), "");
            if (!phoneUtil.isValidNumber(phoneNumber)){
                jLabel26.setText("Le numéro n'est pas valide");
                                             
            }
            
            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            jFormattedTextField1.setDocument(new javax.swing.text.PlainDocument());
            jFormattedTextField1.setText(formatted);
        } catch (NumberParseException ex) {
            jLabel26.setText("Le numéro n'est pas valide");
            Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
        }

            
        }
       
       // partie adhésion
       if(rs.getString(15).equalsIgnoreCase("Epargne")) {
           jComboBox3.setSelectedIndex(0);// Type d'adhésion
           jFormattedTextField3.setEnabled(false);
           demoDateField2.setEnabled(false);
           if(rs.getString(16) !=null)    jFormattedTextField2.setText(rs.getString(16));  // Numéro carnet epargne
           demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion épargne
           demoDateField2.setDate(rs.getDate(20)); 
           demoDateField2.setEnabled(false);
           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
           numcarnep=rs.getString(16);
           
       } else if(rs.getString(15).equalsIgnoreCase("Tontine")) {
           jComboBox3.setSelectedIndex(1);// Type d'adhésion
           jFormattedTextField2.setEnabled(false);
           demoDateField2.setEnabled(true);
          if(rs.getString(16) !=null)   jFormattedTextField3.setText(rs.getString(16));  // Numéro carnet tontine
            demoDateField2.setDate(rs.getDate(20)); // Date d'adhésion tontine
            demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion épargne
            demoDateField1.setEnabled(false);
           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
           numcartont=rs.getString(16);
       } else if(rs.getString(15).equalsIgnoreCase("Epargne & Tontine")) {
           jComboBox3.setSelectedIndex(2);// Type d'adhésion
          if(rs.getString(16) !=null) jFormattedTextField2.setText(rs.getString(16).substring(0, 4));  // Numéro carnet Epargne
           if(rs.getString(16) !=null) jFormattedTextField3.setText(rs.getString(16).substring(5, rs.getString(16).length())); // Numéro carnet Tontine
           demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion Epargne
           demoDateField2.setDate(rs.getDate(20)); // Date d'adhésion tontine
           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
           numcarnep=rs.getString(16).substring(0, 4);
           numcartont=rs.getString(16).substring(5, rs.getString(16).length());

       }
              
       jTextField16.setText(String.valueOf(rs.getInt(17)));   // Droits d'adhésion
       jTextField17.setText(String.valueOf(rs.getInt(18)));   // Part Sociale
}
       typeAdhesion=(String) jComboBox3.getSelectedItem();    // Type d'adhesion recorded
       if(!jFormattedTextField2.isEnabled() && !carnetEnrEpargne.isEmpty()) jFormattedTextField2.setText(carnetEnrEpargne);           
       if(!jFormattedTextField3.isEnabled() && !carnetEnrTontine.isEmpty()) jFormattedTextField3.setText(carnetEnrTontine);      
      // closing connections 
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
        jTextField4 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        demoDateField3 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel14 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel25 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
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
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        jTextField1.setText("jTextField1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/mod_adul.png"))); // NOI18N

        jPanel4.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel6.setLayout(new java.awt.GridLayout(1, 2));

        jLabel4.setText("Nom");
        jPanel6.add(jLabel4);

        DocumentFilter filter = new UppercaseDocumentFilter();
        ((AbstractDocument) jTextField3.getDocument()).setDocumentFilter(filter);
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jPanel6.add(jTextField3);

        jPanel4.add(jPanel6);

        jPanel5.setLayout(new java.awt.GridLayout(1, 2));

        jLabel5.setText("Prénom");
        jPanel5.add(jLabel5);

        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });
        jPanel5.add(jTextField4);

        jPanel4.add(jPanel5);

        jPanel7.setLayout(new java.awt.GridLayout(1, 2));

        jLabel6.setText("Date de naiss.");
        jPanel7.add(jLabel6);

        demoDateField3.setYearDigitsAmount(4);
        jPanel7.add(demoDateField3);

        jPanel4.add(jPanel7);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        jLabel7.setText("Lieu de naiss.");
        jPanel8.add(jLabel7);

        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jPanel8.add(jTextField6);

        jPanel4.add(jPanel8);

        jPanel31.setLayout(new java.awt.GridLayout(1, 2));

        jLabel23.setText("Sexe");
        jPanel31.add(jLabel23);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));
        jPanel31.add(jComboBox4);

        jPanel4.add(jPanel31);

        jPanel10.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel11.setLayout(new java.awt.GridLayout(1, 2));

        jLabel11.setText("Photo");
        jPanel11.add(jLabel11);

        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel11.add(jLabel2);

        jPanel10.add(jPanel11);

        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 0, 5));
        jPanel9.add(jLabel3);

        jButton3.setText("Uploader la photo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton3);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(40, Short.MAX_VALUE))))
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22))
        );

        jTabbedPane1.addTab("Etat civil", jPanel1);

        jPanel4.setBounds(20, 20, 200, 170);

        jPanel12.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel13.setLayout(new java.awt.GridLayout(1, 2));

        jLabel8.setText("Situation matrimoniale");
        jPanel13.add(jLabel8);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mariage", "Célibat", "Divorce", "Veuvage" }));
        jPanel13.add(jComboBox1);

        jPanel12.add(jPanel13);

        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jLabel9.setText("Nationalité");
        jPanel14.add(jLabel9);

        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField8FocusLost(evt);
            }
        });
        jPanel14.add(jTextField8);

        jPanel12.add(jPanel14);

        jPanel15.setLayout(new java.awt.GridLayout(1, 2));

        jLabel10.setText("Profession");
        jPanel15.add(jLabel10);

        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jPanel15.add(jTextField9);

        jPanel12.add(jPanel15);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jLabel12.setText("Adresse");
        jPanel16.add(jLabel12);

        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });
        jPanel16.add(jTextField10);

        jPanel12.add(jPanel16);

        jPanel17.setLayout(new java.awt.GridLayout(1, 2));

        jLabel13.setText("Quartier");
        jPanel17.add(jLabel13);

        jTextField11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField11FocusLost(evt);
            }
        });
        jPanel17.add(jTextField11);

        jPanel12.add(jPanel17);

        jPanel18.setLayout(new java.awt.GridLayout(1, 2));

        jLabel14.setText("Telephone");
        jPanel18.add(jLabel14);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "National", "International" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jPanel18.add(jComboBox2);

        jPanel12.add(jPanel18);

        jPanel19.setLayout(new java.awt.GridLayout(1, 2));
        jPanel19.add(jLabel15);

        try{
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField1FocusLost(evt);
            }
        });
        jPanel19.add(jFormattedTextField1);

        jPanel12.add(jPanel19);

        jPanel21.setLayout(new java.awt.GridLayout(1, 2));

        jLabel16.setText("Email");
        jPanel21.add(jLabel16);

        jTextField14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField14FocusLost(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField14KeyTyped(evt);
            }
        });
        jPanel21.add(jTextField14);

        jPanel12.add(jPanel21);

        jLabel26.setForeground(new java.awt.Color(255, 3, 0));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 394, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel26)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 70, Short.MAX_VALUE)
                .add(jLabel26)
                .add(48, 48, 48))
        );

        jTabbedPane1.addTab("Etat civil(suite)", jPanel2);

        jPanel22.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

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
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        // caret position
        //MouseListener ml = new MouseAdapter()
        //{
            //    public void mousePressed(final MouseEvent e)
            //    {
                //        SwingUtilities.invokeLater(new Runnable()
                    //        {
                        //            public void run()
                        //            {
                            //                JFormattedTextField ftf = (JFormattedTextField)e.getSource();
                            //                int offset = ftf.viewToModel(e.getPoint());
                            //                ftf.setCaretPosition(offset);
                            //            }
                        //        });
                //    }
            //};

        //jFormattedTextField2.addMouseListener(ml);
        jFormattedTextField2.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jFormattedTextField2CaretUpdate(evt);
            }
        });
        jFormattedTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusGained(evt);
            }
        });
        jPanel24.add(jFormattedTextField2);

        jPanel22.add(jPanel24);

        jPanel25.setLayout(new java.awt.GridLayout(1, 2));

        jLabel19.setText("Numéro carnet tontine");
        jPanel25.add(jLabel19);

        try {
            jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField3FocusGained(evt);
            }
        });
        jPanel25.add(jFormattedTextField3);

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
                .add(28, 28, 28)
                .add(jPanel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 389, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel20Layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(jPanel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 308, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Adhésion", jPanel20);

        jButton2.setText("Enregistrer les modifications");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jTabbedPane1)
            .add(layout.createSequentialGroup()
                .add(117, 117, 117)
                .add(jButton2)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jSeparator1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (jComboBox2.getSelectedIndex()==0){
            
                
                jFormattedTextField1.setDocument(new javax.swing.text.PlainDocument());       
            try {
                jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
            } catch (ParseException ex) {
                Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
               
        }else {       
           jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory());  
          
           jFormattedTextField1.setDocument(new PhoneDocument());
            jFormattedTextField1.setText("+");
            
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jFormattedTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField1FocusGained
        // TODO add your handling code here:
       jLabel26.setText("");
        
        if (jComboBox2.getSelectedIndex()==1) {            
             if (jFormattedTextField1.getText().equalsIgnoreCase("+")){
                    jFormattedTextField1.setDocument(new PhoneDocument());
                  jFormattedTextField1.setText("+");
             } else {
                 String before =jFormattedTextField1.getText();
                 before=before.replaceAll("\\s", "");
                 jFormattedTextField1.setText(before);
                 
             }
        }
    }//GEN-LAST:event_jFormattedTextField1FocusGained

    private void jFormattedTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField1FocusLost
        // TODO add your handling code here:
        
        if (jComboBox2.getSelectedIndex()==1){  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(jFormattedTextField1.getText(), "");
            if (!phoneUtil.isValidNumber(phoneNumber)){
                jLabel26.setText("Le numéro n'est pas valide");
                                             
            }
            
            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            jFormattedTextField1.setDocument(new javax.swing.text.PlainDocument());
            jFormattedTextField1.setText(formatted);
        } catch (NumberParseException ex) {
            jLabel26.setText("Le numéro n'est pas valide");
            Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
        }

            
        }
    }//GEN-LAST:event_jFormattedTextField1FocusLost

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // TODO add your handling code here:
//             
           if(evt.getStateChange() == ItemEvent.DESELECTED) {
               previousitemselection=(String) evt.getItem();
            }
            if (jComboBox3.getSelectedIndex()==0) {
//            if(previousitemselection.equalsIgnoreCase("Tontine")) {
//               jFormattedTextField3.setEnabled(false);
//               
                if(previousitemselection.equalsIgnoreCase("tontine")) {
                    if(issavedcarn && ! carnetEnrEpargne.isEmpty()) {
                        jFormattedTextField2.setText(carnetEnrEpargne);
                    }
                }
//            
//            }    
//            
       
            //jFormattedTextField3.setText("");
            jFormattedTextField3.setEnabled(false);
           
            jFormattedTextField2.setEnabled(true);
            //jFormattedTextField2.setText("");
           // demoDateField2.setDate(null);
            demoDateField2.setEnabled(false);
           // demoDateField1.setDate(null);
            demoDateField1.setEnabled(true);
        } else if (jComboBox3.getSelectedIndex()==1){
             //jFormattedTextField2.setText("");
            
             
                if(previousitemselection.equalsIgnoreCase("Epargne")) {
                    if(issavedcarn && ! carnetEnrTontine.isEmpty()) {
                        jFormattedTextField2.setText(carnetEnrTontine);
                    }
                }
                
             jFormattedTextField2.setEnabled(false);
             jFormattedTextField3.setEnabled(true);
           //  jFormattedTextField3.setText("");
           //  demoDateField1.setDate(null);
             demoDateField1.setEnabled(false);
           //  demoDateField2.setDate(null);
             demoDateField2.setEnabled(true);
        } else {
            jFormattedTextField2.setEnabled(true);
           // jFormattedTextField2.setText("");
            jFormattedTextField3.setEnabled(true);
            // jFormattedTextField3.setText("");
          //   demoDateField1.setDate(null);
             demoDateField1.setEnabled(true);
          //   demoDateField2.setDate(null);
             demoDateField2.setEnabled(true);
             
              if(previousitemselection.equalsIgnoreCase("tontine")) {
                    if(issavedcarn && ! carnetEnrEpargne.isEmpty()) {
                        jFormattedTextField2.setText(carnetEnrEpargne);
                    }
                }
              
               if(previousitemselection.equalsIgnoreCase("Epargne")) {
                    if(issavedcarn && ! carnetEnrTontine.isEmpty()) {
                        jFormattedTextField2.setText(carnetEnrTontine);
                    }
                }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
               // TODO add your handling code here:
    if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
        evt.consume();
    } else if (jTextField4.getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else if(jTextField4.getText().endsWith(" ") ){
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else{
         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
    }
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        // TODO add your handling code here:
        jTextField4.setText(jTextField4.getText().trim());
        jTextField4.setText(jTextField4.getText().replaceAll("\\s+", " "));
        
    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
        // TODO add your handling code here:
        jTextField6.setText(jTextField6.getText().trim());
        jTextField6.setText(jTextField6.getText().replaceAll("\\s+", " "));
        jTextField6.setText(WordUtils.capitalizeFully(jTextField6.getText()));
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField8FocusLost
        // TODO add your handling code here:
        jTextField8.setText(jTextField8.getText().trim());
        jTextField8.setText(jTextField8.getText().replaceAll("\\s+", " "));
        jTextField8.setText(WordUtils.capitalizeFully(jTextField8.getText()));
    }//GEN-LAST:event_jTextField8FocusLost

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        // TODO add your handling code here:
        jTextField9.setText(jTextField9.getText().trim());
        jTextField9.setText(jTextField9.getText().replaceAll("\\s+", " "));
        jTextField9.setText(WordUtils.capitalizeFully(jTextField9.getText()));
    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
        // TODO add your handling code here:
        jTextField10.setText(jTextField10.getText().trim());
        jTextField10.setText(jTextField10.getText().replaceAll("\\s+", " "));
        jTextField10.setText(WordUtils.capitalizeFully(jTextField10.getText()));
    }//GEN-LAST:event_jTextField10FocusLost

    private void jTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField11FocusLost
        // TODO add your handling code here:
         // TODO add your handling code here:
        jTextField11.setText(jTextField11.getText().trim());
        jTextField11.setText(jTextField11.getText().replaceAll("\\s+", " "));
        jTextField11.setText(WordUtils.capitalizeFully(jTextField11.getText()));
    }//GEN-LAST:event_jTextField11FocusLost

    private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
        // TODO add your handling code here:
         jTextField14.setText(jTextField14.getText().trim());
    }//GEN-LAST:event_jTextField14KeyTyped

    private void jFormattedTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusGained
        // TODO add your handlie:
        jFormattedTextField2.setCaretPosition(jFormattedTextField2.getText().trim().length());
    }//GEN-LAST:event_jFormattedTextField2FocusGained

    private void jFormattedTextField2CaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jFormattedTextField2CaretUpdate
        // TODO add your handling code here:
        //if(jFormattedTextField2.getCaretPosition() > jFormattedTextField2.getText().length() )         jFormattedTextField2.setCaretPosition(jFormattedTextField2.getText().length());
    }//GEN-LAST:event_jFormattedTextField2CaretUpdate

    private void jFormattedTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField3FocusGained
        // TODO add your handling code here:
        jFormattedTextField3.setCaretPosition(jFormattedTextField3.getText().trim().length());
    }//GEN-LAST:event_jFormattedTextField3FocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Code non optimisé
        // Validation beforre update
        Mod_adulte.Emailvalidator emailValidator = new Mod_adulte.Emailvalidator();
        
        // Validate carnet num
      
        if(jComboBox3.getSelectedIndex()==0 || jComboBox3.getSelectedIndex()==2) {
        try {
                  res1 = verifynumcarn(0);
                  System.out.println("res1 vaut"+res1);
                 
             } catch (SQLException ex) {
                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox3.getSelectedIndex()==1 || jComboBox3.getSelectedIndex()==2) {
         try {
                  res2 = verifynumcarn(1);
               
             } catch (SQLException ex) {
                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
         
//        System.out.println("res1"+res1+"res2"+res2);
//        System.out.println("verity"+numcarnep.equalsIgnoreCase(jFormattedTextField2.getText()));
       // verification des champs
        if(jTextField3.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Nom");
        } else if (! isAllUpper(jTextField3.getText())) {
            JOptionPane.showMessageDialog(this, "Nom tout en majuscules");
        } else if(jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ prénom");
        } else if (jFormattedTextField1.getText().equalsIgnoreCase("  -  -  -  ") || jFormattedTextField1.getText().equalsIgnoreCase("+") ){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Téléphone");
        } else if (! jTextField14.getText().isEmpty() &&  !emailValidator.validate(jTextField14.getText())){
            JOptionPane.showMessageDialog(this, "L'email indiqué n'est pas valide");

       } else if (jTextField16.getText().isEmpty()){
              JOptionPane.showMessageDialog(this, "Veuillez renseigner les droits d'adhésion");    

       } else if (jFormattedTextField2.getText().equalsIgnoreCase("0000") || jFormattedTextField3.getText().equalsIgnoreCase("0000")) { 
            
         JOptionPane.showMessageDialog(this, "Numéro de carnet 0000 invalide");
        } else if (jComboBox3.getSelectedIndex()==0 && jFormattedTextField2.getText().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Numéro de carnet Epargne invalide");   
        }else if (jComboBox3.getSelectedIndex()==1 && jFormattedTextField3.getText().isEmpty()) { 
           JOptionPane.showMessageDialog(this, "Numéro de carnet Tontine invalide");   
        } else if (jComboBox3.getSelectedIndex()==2 && (jFormattedTextField2.getText().isEmpty() || jFormattedTextField3.getText().isEmpty())){
           JOptionPane.showMessageDialog(this, "Numéro de carnet invalide"); 
           
        } else if(jComboBox3.getSelectedIndex()==0 && (res1==true && !numcarnep.equalsIgnoreCase(jFormattedTextField2.getText()))) {
           JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");  
        } else if(jComboBox3.getSelectedIndex()==1 && (res2==true && !numcartont.equalsIgnoreCase(jFormattedTextField3.getText()))) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if(jComboBox3.getSelectedIndex()==2 && (res1==true && !numcarnep.equalsIgnoreCase(jFormattedTextField2.getText()))) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé"); 
        } else if(jComboBox3.getSelectedIndex()==2 && (res2==true && !numcartont.equalsIgnoreCase(jFormattedTextField3.getText())) ) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if (demoDateField1.getDate()==null && (jComboBox3.getSelectedIndex()==0 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Epargne"); 
        } else if (demoDateField2.getDate()==null && (jComboBox3.getSelectedIndex()==1 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Tontine"); 
        } else{
            // Check if someone has already this name
            boolean inserted=false;
            boolean success= true;
            boolean check_epargne=false;
            boolean check_tontine=false;
            Statement stmt = null;
            ResultSet rs2=null;
            if(! jTextField3.getText().equalsIgnoreCase(nom) || ! jTextField4.getText().equalsIgnoreCase(prenom)) {  // to change
            String sql01="SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + jTextField3.getText() + "' AND lower(Prenoms)= '"+jTextField4.getText().toLowerCase(Locale.FRENCH)+ "'";
            
            connect=Connect.ConnectDb();
           
          
             try {
                   stmt= connect.createStatement();
                   rs2=stmt.executeQuery(sql01);
              } catch (SQLException ex) {
                  Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
              }
             
            try {
                if (rs2.next() ){
                    JOptionPane.showMessageDialog(null, "Attention la personne indiquée est déjà enregistré");
                    success=false;
                    inserted=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            
            // everything is correct-insert modify in the database
            
            if (!inserted){
            // Check subscription case by case
            //First case chose Epargne and changed the date   
            if(typeAdhesion.equalsIgnoreCase("Epargne")) {
                if( ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne") || ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                     if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) mustdeletetont=true; // must delete tontine data
                     System.out.println("demodatefield"+demoDateField1.getDate());
                     System.out.println("dateEpargne"+dateEpargne);
                    if(dateEpargne.before(demoDateField1.getDate())) {
                         try {
                             System.out.println("nom"+nom+"prenom"+prenom);
                             check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
                             check_epargne = check_epargne_before_date(getId(nom, prenom), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
                             check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                             
                       }
                  } else if ((((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne"))) {
                       mustdeleteep = true; 
                       try {  // A tester
                             System.out.println("nom"+nom+"prenom"+prenom);
                            
                             check_tontine = check_tontine_before_date(getId(nom, prenom), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
                             check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                      
                       }
                      
                       if(dateTontine.before(demoDateField2.getDate())) {
                           
                            try {
                             check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                               
                 }
                
            } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne")) {
                    if(dateEpargne.before(demoDateField1.getDate())) {
                         try {
                             check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_epargne==true) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                    }
                    
                    // Avertissement tontine 
                    
                     try {  // A tester
                             check_tontine = check_tontine_before_date(getId(nom, prenom), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
                             check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                         }
                         
                         if (check_tontine==false) {
                              JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                              success = false; 
                         }
                
                } 
                
                // Avertissement epargne 
                try {  // A tester
                             check_epargne = check_epargne_before_date(getId(nom, prenom), new Date());
                         } catch (SQLException ex) {
                             Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
                      System.out.println("nom"+nom+"prenom"+prenom);
                    String sqlmod="UPDATE Profil_adulte SET Noms = ?, Prenoms = ?, Sexe = ?, Photo = ?, Date_naissance = ?, matrimonial = ?, Lieu_naissance = ?, Nationalité = ?, Profession = ?, Adresse = ?, Quartier = ?, Telephone = ?, Email = ?, Type_adhesion = ?, Num_carnet = ?, Droit_adhesion = ?, Part_sociale = ?, Date_adhesion_ep = ?, Date_adhesion_to = ? "+"WHERE idProfil_adulte='"+this.getId(nom, prenom)+"';";
                    pst=connect.prepareStatement(sqlmod);
                    String numcarn="";
                           if(jComboBox3.getSelectedIndex()==0) {
                               numcarn=jFormattedTextField2.getText();
                           } else if (jComboBox3.getSelectedIndex()==1){
                               numcarn=jFormattedTextField3.getText();
                           } else {
                                numcarn=jFormattedTextField2.getText() +"-"+jFormattedTextField3.getText();
                           }
                    pst.setString(1, jTextField3.getText());   // Nom
                    pst.setString(2, jTextField4.getText());  // Prenom
                    pst.setString(3, ((String) jComboBox4.getSelectedItem()).toLowerCase()); // Sexe
                    pst.setString(4, adulte_photourl);   // Photo
                    if (demoDateField3.getDate()!=null) { pst.setDate(5, new java.sql.Date(demoDateField3.getDate().getTime())); } else { pst.setDate(5, null);}  // Date de naissance
                    pst.setString(6, (String) jComboBox1.getSelectedItem());  // Situation matrimoniale
                    pst.setString(7, jTextField6.getText()); // Lieu de naissance
                    pst.setString(8, jTextField8.getText()); // Nationalité 
                    pst.setString(9, jTextField9.getText()); // Profession
                    pst.setString(10,jTextField10.getText()); // Adresse 
                    pst.setString(11,jTextField11.getText()); // Quartier
                    pst.setString(12, jFormattedTextField1.getText()); // Téléphone
                    pst.setString(13, jTextField14.getText()); // Email
                    pst.setString(14, (String) jComboBox3.getSelectedItem()); //Type d'adhésion
                    pst.setString(15, numcarn); // Numéro de carnet 
                    pst.setInt(16, Integer.valueOf(jTextField16.getText()));  // Droits d'adhésion
                    pst.setInt(17, Integer.valueOf(jTextField17.getText()));   // Part sociale 
                    if(saveepargne==0) {
                    if (demoDateField1.getDate()!=null &&  demoDateField1.isEnabled()){ pst.setDate(18, new java.sql.Date(demoDateField1.getDate().getTime())); } else pst.setDate(18, null);  // Date d'adhésion épargne 
                    }
                    
                    if(savetontine==0) {
                    if (demoDateField2.getDate()!=null && demoDateField2.isEnabled()){ pst.setDate(19, new java.sql.Date(demoDateField2.getDate().getTime())); } else pst.setDate(19, null);// Date d'adhésion tontine
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
                    System.out.println("sur le point d'être executé");
                    pst.execute();
                    
                    if ((mustdeleteep && ! carnetEnrEpargne.isEmpty()) || (mustdeletetont && ! carnetEnrTontine.isEmpty())){
                        System.out.println("it is true");
                        deleteSavedcarn();
                    }
                    
                    JOptionPane.showMessageDialog(null, "Les modifications ont été enregistrées avec succès");
                    nom=jTextField3.getText();
                    prenom=jTextField4.getText();
                    numcarnep=jFormattedTextField2.getText();
                    numcartont=jFormattedTextField3.getText();
                    this.mn.refresh();
                } catch (SQLException ex) {
                    Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
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
        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

               JFileChooser chooser = new JFileChooser();
               int returnVal = chooser.showOpenDialog(null);
               if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        adulte_photourl=chooser.getSelectedFile().getCanonicalPath().replace("\\", "\\\\");
                        System.out.println(chooser.getSelectedFile().getCanonicalPath());
                    } catch (IOException ex) {
                        Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ;
                }
                
           if (adulte_photourl!= null && !adulte_photourl.equals("")) {
            try {
                Image temp = new ImageIcon(adulte_photourl).getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(temp));
            } catch (Exception e) {
            }
        } else {
            try {
                Image temp = new ImageIcon(
                        getClass().getResource("blank.png")).getImage().getScaledInstance(210, 210,
                        Image.SCALE_SMOOTH);
                  jLabel2.setIcon(new ImageIcon(temp));
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        jTextField3.setText(jTextField3.getText().trim());
        jTextField3.setText(jTextField3.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusLost
        // TODO add your handling code here:
         jTextField14.setText(jTextField14.getText().trim());
    }//GEN-LAST:event_jTextField14FocusLost

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
            java.util.logging.Logger.getLogger(Mod_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mod_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mod_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mod_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mod_adulte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField3;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

}
