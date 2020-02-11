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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
public class Adh_persmor extends javax.swing.JFrame {
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
   
    public Adh_persmor(main mn) {
        setTitle("Adhésion personne morale");
        this.mn=mn; 
        initComponents();                
        this.setLocationRelativeTo(null);
    }


// Email validator 
        private void performSummation(java.awt.event.ActionEvent evt) {
        int total = 0;
        if(jTextField16.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField16.getText());
            }catch(NumberFormatException nbx){
            }
        }

        if(jTextField17.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField17.getText());
            }catch(NumberFormatException nbx){
            }
        }
        
        if(jTextField2.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField2.getText());
            }catch(NumberFormatException nbx){
            }
        }

       

        jTextField5.setText(""+total);

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
       String sql0="SELECT idTontine as id FROM Tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte' AND MONTH(DateTontine) < MONTH('"+sqldate+"') AND YEAR(DateTontine) <= YEAR('"+sqldate+"') UNION SELECT idretraits_tontine as id  FROM retraits_tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte' AND DATE(DateRet) < '"+sqldate+"'";
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
                
                  
                  //    JOptionPane.showMessageDialog( this, "hostel name already exists","Error", JOptionPane.ERROR_MESSAGE);
                  //      y3.setText("");
                  //      y3.requestDefaultFocus();
                  //     return;
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
    public Adh_persmor() {
        initComponents();
    }
    
//     public Adh_persmor(int id) throws SQLException {
//         this.id=id;
//         initComponents();
//        fillform();
//    }
     
//     public Adh_persmor(String nom, String prenom, main mn) throws SQLException {
//         this.id=getId(nom, prenom);
//         this.mn=mn;
//         initComponents();
//         fillform();
//    }
     
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
//    public void fillform() throws SQLException {
//         String sql0="SELECT * From Profil_adulte WHERE idProfil_adulte='"+this.id+"'";
//         
//        
//            // Vérifier numero carnet 
//            connect=Connect.ConnectDb();
//            // check carnets enregistrés
//            
//            issavedcarn=check_saved_carn();
//            Statement stmt = null;
//            ResultSet rs = null;
//           
//            stmt= connect.createStatement();
//            rs=stmt.executeQuery(sql0);
//            while(rs.next()){
//                  jTextField3.setText(rs.getString(2)); // Nom
//                  nom=jTextField3.getText();
//                  jTextField4.setText(rs.getString(3)); // Prenom
//                  prenom=jTextField4.getText();
//                  demoDateField3.setDate(rs.getDate(6)); // Date de naissance
//                  jTextField6.setText((rs.getString(8))); // Lieu de naissance
//                  if(rs.getString(4).equalsIgnoreCase("homme")) { jComboBox4.setSelectedIndex(0);  // Sexe 
//                  } else {
//                      jComboBox4.setSelectedIndex(1);
//                  }
//                  if(rs.getString(5)!= null) {   // Photo 
//                  Image temp = new ImageIcon(rs.getString(5)).getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH);
//                  jLabel2.setIcon(new ImageIcon(temp));
//                  adulte_photourl=rs.getString(5);
//                      
//                  }
//                  
//                  if(rs.getString(7)!= null) { // Situation matrimoniale
//                      if (rs.getString(7).equalsIgnoreCase("mariage")) jComboBox1.setSelectedIndex(0);
//                      else if (rs.getString(7).equalsIgnoreCase("célibat")) jComboBox1.setSelectedIndex(1);
//                      else if (rs.getString(7).equalsIgnoreCase("divorce")) jComboBox1.setSelectedIndex(2);
//                      else jComboBox1.setSelectedIndex(3);
//                  }
//                  
//                  jTextField8.setText(rs.getString(9));   //Nationalité
//                  jTextField9.setText(rs.getString(10));  // Profession
//                  jTextField10.setText(rs.getString(11)); //Adresse 
//                  jTextField11.setText(rs.getString(12));  // Quartier
//                  if(rs.getString(13).startsWith("+")) jComboBox2.setSelectedIndex(1);  // Type de téléphone nat ou international
//                  else if(rs.getString(13)!=null || ! rs.getString(13).isEmpty()) jComboBox2.setSelectedIndex(0);
//                  jFormattedTextField1.setText((rs.getString(13)));  // Téléphone 
//                  jTextField14.setText(rs.getString(14));     // Email 
//                  
//                  // formattage téléphone 
//       if (jComboBox2.getSelectedIndex()==1){  //international
//            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//            Phonenumber.PhoneNumber phoneNumber;
//        try {
//            phoneNumber = phoneUtil.parse(jFormattedTextField1.getText(), "");
//            if (!phoneUtil.isValidNumber(phoneNumber)){
//                jLabel26.setText("Le numéro n'est pas valide");
//                                             
//            }
//            
//            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//            jFormattedTextField1.setDocument(new javax.swing.text.PlainDocument());
//            jFormattedTextField1.setText(formatted);
//        } catch (NumberParseException ex) {
//            jLabel26.setText("Le numéro n'est pas valide");
//            Logger.getLogger(Mod_adulte.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//            
//        }
//       
//       // partie adhésion
//       if(rs.getString(15).equalsIgnoreCase("Epargne")) {
//           jComboBox3.setSelectedIndex(0);// Type d'adhésion
//           jFormattedTextField3.setEnabled(false);
//           demoDateField2.setEnabled(false);
//           if(rs.getString(16) !=null)    jFormattedTextField2.setText(rs.getString(16));  // Numéro carnet epargne
//           demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion épargne
//           demoDateField2.setDate(rs.getDate(20)); 
//           demoDateField2.setEnabled(false);
//           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
//           numcarnep=rs.getString(16);
//           
//       } else if(rs.getString(15).equalsIgnoreCase("Tontine")) {
//           jComboBox3.setSelectedIndex(1);// Type d'adhésion
//           jFormattedTextField2.setEnabled(false);
//           demoDateField2.setEnabled(true);
//          if(rs.getString(16) !=null)   jFormattedTextField3.setText(rs.getString(16));  // Numéro carnet tontine
//            demoDateField2.setDate(rs.getDate(20)); // Date d'adhésion tontine
//            demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion épargne
//            demoDateField1.setEnabled(false);
//           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
//           numcartont=rs.getString(16);
//       } else if(rs.getString(15).equalsIgnoreCase("Epargne & Tontine")) {
//           jComboBox3.setSelectedIndex(2);// Type d'adhésion
//          if(rs.getString(16) !=null) jFormattedTextField2.setText(rs.getString(16).substring(0, 4));  // Numéro carnet Epargne
//           if(rs.getString(16) !=null) jFormattedTextField3.setText(rs.getString(16).substring(5, rs.getString(16).length())); // Numéro carnet Tontine
//           demoDateField1.setDate(rs.getDate(19)); // Date d'adhésion Epargne
//           demoDateField2.setDate(rs.getDate(20)); // Date d'adhésion tontine
//           dateEpargne=demoDateField1.getDate();  // Date d'adhésion épargne recorded
//           dateTontine=demoDateField2.getDate();  // Date d'adhésion Tontine recorded
//           numcarnep=rs.getString(16).substring(0, 4);
//           numcartont=rs.getString(16).substring(5, rs.getString(16).length());
//
//       }
//              
//       jTextField16.setText(String.valueOf(rs.getInt(17)));   // Droits d'adhésion
//       jTextField17.setText(String.valueOf(rs.getInt(18)));   // Part Sociale
//}
//       typeAdhesion=(String) jComboBox3.getSelectedItem();    // Type d'adhesion recorded
//       if(!jFormattedTextField2.isEnabled() && !carnetEnrEpargne.isEmpty()) jFormattedTextField2.setText(carnetEnrEpargne);           
//       if(!jFormattedTextField3.isEnabled() && !carnetEnrTontine.isEmpty()) jFormattedTextField3.setText(carnetEnrTontine);      
//      // closing connections 
//      if (connect!=null) connect.close();
//      if (stmt!=null) stmt.close();
//      if(rs!=null)  rs.close();
//                 
//               
//        
//    }  

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
        jTextField7 = new javax.swing.JTextField();
        jPanel46 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jPanel47 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jPanel48 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jPanel49 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel50 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jPanel51 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
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
        jPanel28 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel45 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/adhpers_mor.png"))); // NOI18N

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        jPanel4.setLayout(new java.awt.GridLayout(12, 1, 0, 5));

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

        jLabel5.setText("Activité principale");
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

        jLabel6.setText("Date de création");
        jPanel7.add(jLabel6);

        demoDateField3.setYearDigitsAmount(4);
        jPanel7.add(demoDateField3);

        jPanel4.add(jPanel7);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel8.setLayout(new java.awt.GridLayout(1, 2));

        jLabel7.setText("Lieu de création");
        jPanel8.add(jLabel7);

        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jPanel8.add(jTextField6);

        jPanel4.add(jPanel8);

        jPanel31.setLayout(new java.awt.GridLayout(1, 2));

        jLabel23.setText("Numéro C.O.E");
        jPanel31.add(jLabel23);
        jPanel31.add(jTextField7);

        jPanel4.add(jPanel31);

        jPanel46.setLayout(new java.awt.GridLayout(1, 2));

        jLabel36.setText("Adresse");
        jPanel46.add(jLabel36);

        jTextField8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField8FocusLost(evt);
            }
        });
        jPanel46.add(jTextField8);

        jPanel4.add(jPanel46);

        jPanel47.setLayout(new java.awt.GridLayout(1, 2));

        jLabel37.setText("Personne de ref, Nom");
        jPanel47.add(jLabel37);

        ((AbstractDocument) jTextField12.getDocument()).setDocumentFilter(filter);
        jTextField12.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField12FocusLost(evt);
            }
        });
        jPanel47.add(jTextField12);

        jPanel4.add(jPanel47);

        jPanel48.setLayout(new java.awt.GridLayout(1, 2));

        jLabel38.setText("                               Prénoms");
        jPanel48.add(jLabel38);

        jTextField15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField15FocusLost(evt);
            }
        });
        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField15KeyTyped(evt);
            }
        });
        jPanel48.add(jTextField15);

        jPanel4.add(jPanel48);

        jPanel49.setLayout(new java.awt.GridLayout(1, 2));

        jLabel39.setText("Telephone");
        jPanel49.add(jLabel39);

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "National", "International" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });
        jPanel49.add(jComboBox4);

        jPanel4.add(jPanel49);

        jPanel50.setLayout(new java.awt.GridLayout(1, 2));
        jPanel50.add(jLabel40);

        try{
            jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField4ActionPerformed(evt);
            }
        });
        jFormattedTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField4FocusLost(evt);
            }
        });
        jPanel50.add(jFormattedTextField4);

        jPanel4.add(jPanel50);

        jPanel51.setLayout(new java.awt.GridLayout(1, 2));
        jPanel51.add(jLabel41);

        jLabel42.setForeground(new java.awt.Color(255, 3, 0));
        jPanel51.add(jLabel42);

        jPanel4.add(jPanel51);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 391, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Identification", jPanel1);

        jPanel22.setLayout(new java.awt.GridLayout(10, 1, 0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(1, 2));

        jLabel17.setText("Type d'adhésion");
        jPanel23.add(jLabel17);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Epargne", "Tontine", "Epargne et tontine" }));
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
        jPanel25.add(jFormattedTextField3);

        jPanel22.add(jPanel25);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel26.setLayout(new java.awt.GridLayout(1, 2));

        jLabel20.setText("Droits d'adhésion");
        jPanel26.add(jLabel20);

        DocumentListener documentListener = new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSummation(null);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                performSummation(null);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

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
        ((AbstractDocument) jTextField17.getDocument()).setDocumentFilter(numericFilter);
        ((AbstractDocument) jTextField2.getDocument()).setDocumentFilter(numericFilter);
        ((AbstractDocument) jTextField5.getDocument()).setDocumentFilter(numericFilter);
        jTextField16.setText("2500");
        jTextField16.getDocument().addDocumentListener(documentListener);
        jTextField17.getDocument().addDocumentListener(documentListener);
        jTextField2.getDocument().addDocumentListener(documentListener);
        jPanel26.add(jTextField16);

        jPanel22.add(jPanel26);

        jPanel27.setLayout(new java.awt.GridLayout(1, 2));

        jLabel21.setText("Part Sociale");
        jPanel27.add(jLabel21);
        jPanel27.add(jTextField17);

        jPanel22.add(jPanel27);

        jPanel28.setLayout(new java.awt.GridLayout(1, 2));

        jLabel22.setText("Dépôt initial");
        jPanel28.add(jLabel22);
        jPanel28.add(jTextField2);

        jPanel22.add(jPanel28);

        jPanel45.setLayout(new java.awt.GridLayout(1, 2));

        jLabel35.setText("Total");
        jPanel45.add(jLabel35);

        jTextField5.setText(jTextField16.getText());
        jPanel45.add(jTextField5);

        jPanel22.add(jPanel45);

        jPanel29.setLayout(new java.awt.GridLayout(1, 2));

        jLabel25.setText("Date d'adhésion épargne");
        jPanel29.add(jLabel25);

        demoDateField1.setYearDigitsAmount(4);
        demoDateField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField1ActionPerformed(evt);
            }
        });
        jPanel29.add(demoDateField1);

        jPanel22.add(jPanel29);

        jPanel30.setLayout(new java.awt.GridLayout(1, 2));

        jLabel24.setText("Date d'adhésion tontine");
        jPanel30.add(jLabel24);

        demoDateField2.setYearDigitsAmount(4);
        demoDateField2.setEnabled(false);
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
                .add(jPanel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 424, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Adhésion", jPanel20);

        jButton2.setText("Enregistrer");
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
            .add(layout.createSequentialGroup()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
            .add(jSeparator1)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton2)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    
     private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }
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
                        jFormattedTextField3.setText(carnetEnrTontine); // modifié je pense bien que c'est 3 au lieu de 2
                    }
                }
        }
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
//        // TODO add your handling code here:
//               // TODO add your handling code here:
//    if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
//        evt.consume();
//    } else if (jTextField4.getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
//            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
//    }else if(jTextField4.getText().endsWith(" ") ){
//        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
//    }else{
//         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
//    }
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

    private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
        // TODO add your handling code here:
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
     // TODO add your handling code here:
        
        // Validate carnet num
      
        if(jComboBox3.getSelectedIndex()==0 || jComboBox3.getSelectedIndex()==2) {
        try {
                  res1 = verifynumcarn(0);
                 
             } catch (SQLException ex) {
                 Logger.getLogger(Adh_persmor.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox3.getSelectedIndex()==1 || jComboBox3.getSelectedIndex()==2) {
         try {
                  res2 = verifynumcarn(1);
               
             } catch (SQLException ex) {
                 Logger.getLogger(Adh_persmor.class.getName()).log(Level.SEVERE, null, ex);
             }
         
           
        }
        System.out.println("res1"+res1+"res2"+res2);
        if(jTextField3.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ raison sociale");
//        } else if(jFormattedTextField1.getText().equalsIgnoreCase("dd/MM/yyyy")){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ date de création");
//        } else if(demoDateField3.getDate()==null){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ date de création");
//       } else if(jTextField6.getText().isEmpty()){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ lieu de création");
        } else if(jTextField4.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ activité principale");
//        } else if(jTextField8.getText().isEmpty()){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ adresse");
//        } else if(jTextField7.getText().isEmpty()){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ numéro COE");
        } else if(jTextField12.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ nom personne de référence");
        } else if(jTextField15.getText().isEmpty()){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ prénom personne de référence");
        }
        
        else if(jFormattedTextField4.getText().equalsIgnoreCase("  -  -  -  ") || jFormattedTextField4.getText().equalsIgnoreCase("+") ){
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ téléphone");
        } else if (jTextField16.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner les droits d'adhésion ");
           
//        } else if (!isDouble(jTextField16.getText())){
//            JOptionPane.showMessageDialog(this, "Les droits d'adhésion doivent être des chiffres");
//        } else if (!isDouble(jTextField17.getText())){
//            JOptionPane.showMessageDialog(this, "La part sociale doit être des chiffres");
//        } else if (!isDouble(jTextField2.getText())){
//            JOptionPane.showMessageDialog(this, "Le dépôt initial doit être des chiffres");
//        }else if (jTextField17.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner la part sociale ");
        } else if (jFormattedTextField2.getText().equalsIgnoreCase("0000") || jFormattedTextField3.getText().equalsIgnoreCase("0000")) { 
            
         JOptionPane.showMessageDialog(this, "Numéro de carnet 0000 invalide");
        } else if (jComboBox3.getSelectedIndex()==0 && jFormattedTextField2.getText().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Numéro de carnet Epargne invalide");   
        }else if (jComboBox3.getSelectedIndex()==1 && jFormattedTextField3.getText().isEmpty()) { 
           JOptionPane.showMessageDialog(this, "Numéro de carnet Tontine invalide");   
        } else if (jComboBox3.getSelectedIndex()==2 && (jFormattedTextField2.getText().isEmpty() || jFormattedTextField3.getText().isEmpty())){
           JOptionPane.showMessageDialog(this, "Numéro de carnet invalide"); 
        } else if(jComboBox3.getSelectedIndex()==0 && res1==true) {
           JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");  
        }else if(jComboBox3.getSelectedIndex()==1 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if(jComboBox3.getSelectedIndex()==2 && res1==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé"); 
        } else if(jComboBox3.getSelectedIndex()==2 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        }  else if (demoDateField1.getDate()==null && (jComboBox3.getSelectedIndex()==0 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Epargne"); 
        } else if (demoDateField2.getDate()==null && (jComboBox3.getSelectedIndex()==1 ||  jComboBox3.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Tontine"); 
        } else { 
            boolean success= true;
            String sql01="SELECT idProfil_persmorale FROM Profil_persmorale WHERE Raison_sociale= '" + jTextField3.getText() +"'";
             Statement stmt = null;
             boolean inserted=false;
            ResultSet rs2=null;
             connect=Connect.ConnectDb();
             try {
                   stmt= connect.createStatement();
                   rs2=stmt.executeQuery(sql01);
              } catch (SQLException ex) {
                  Logger.getLogger(Adh_persmor.class.getName()).log(Level.SEVERE, null, ex);
              }
              try {
                if (rs2.next() ){
                    JOptionPane.showMessageDialog(null, "Attention la personne morale indiquée est déjà enregistré");
                    success=false;
                    inserted=true;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
              
             if (! inserted) {
                 
              String numcarn="";
                           if(jComboBox3.getSelectedIndex()==0) {
                               numcarn=jFormattedTextField2.getText();
                           } else if (jComboBox3.getSelectedIndex()==1){
                               numcarn=jFormattedTextField3.getText();
                           } else {
                                numcarn=jFormattedTextField2.getText() +"-"+jFormattedTextField3.getText();
                           }
            
            int partsociale=0;
            if(!jTextField17.getText().isEmpty())
                partsociale=Integer.parseInt(jTextField17.getText());
             
    //         String datecrea2=jFormattedTextField1.getText();
    //       sqlDate=new java.sql.Date(demoDateField3.getDate().getTime());
             java.sql.Date sqlDate=null;
            java.sql.Date sqlDate2=null;
            java.sql.Date sqlDate3=null;
            if (demoDateField3.getDate()!=null) sqlDate=new java.sql.Date(demoDateField3.getDate().getTime());
            if (demoDateField1.getDate()!=null)  sqlDate2=new java.sql.Date(demoDateField1.getDate().getTime());
            if (demoDateField2.getDate()!=null) sqlDate3=new java.sql.Date(demoDateField2.getDate().getTime());
//             DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            try {
//                datecrea= formatter.parse(datecrea2);
//                sqlDate= new java.sql.Date(datecrea.getTime());
//            } catch (ParseException ex) {
//                Logger.getLogger(Adh_persmor.class.getName()).log(Level.SEVERE, null, ex);
//                success=false;
//            }
             // On vérifie si la personne morale n'existe pas déjà 
             
             String fullname= jTextField12.getText()+ " "+ jTextField15.getText();
             String sql = "INSERT INTO Profil_persmorale " + 
                     "VALUES ("+ null +", '"+ jTextField3.getText(); 
             
             if (sqlDate==null){
                 sql=sql+"', null, '"+jTextField6.getText()+"', '"+jTextField4.getText()+"', '"+jTextField8.getText()+"', '"+jTextField7.getText()+"', '"+fullname+"', '"+jFormattedTextField4.getText()+"', '"+String.valueOf(jComboBox3.getSelectedItem()).trim()+"', '"+numcarn+"', '"+Integer.valueOf(jTextField16.getText().replace(" ", ""))+"', '"+partsociale;
            } else if (sqlDate !=null) {
                  sql=sql+"', '"+sqlDate +"', '"+jTextField6.getText()+"', '"+jTextField4.getText()+
                     "', '"+jTextField8.getText()+"', '"+jTextField7.getText()+"', '"+fullname+"', '"+jFormattedTextField4.getText()+"', '"+String.valueOf(jComboBox3.getSelectedItem()).trim()+"', '"+numcarn+"', '"+Integer.valueOf(jTextField16.getText().replace(" ", ""))+"', '"+partsociale;   
            } 
             
             if (sqlDate2==null){
                         sql=sql+"', null,";
                     } else if (sqlDate2!=null){
                          sql=sql+"','"+sqlDate2+"',";
                     } 
                     
                     if (sqlDate3==null){ 
                         sql=sql+"null"+", 1);";
                     } else if (sqlDate3!=null){   
                         sql=sql+"'"+sqlDate3+"',1);";
                     }              
             
               
                System.out.println(sql);
                int generatdkey = 0;

            try {  
                pst=connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_persmor.class.getName()).log(Level.SEVERE, null, ex);
                success=false;
            }
            try {
                
                pst.execute();
                ResultSet rst= pst.getGeneratedKeys();
                generatdkey=0;
                if(rst.next()){
                    generatdkey= rst.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_persmor.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur d'enregistrement");
                success=false;
            
            }
             if(!jTextField2.getText().isEmpty() && jTextField2.isEnabled()) {
                             java.util.Date dt = new java.util.Date();
                 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String currentTime = sdf.format(dt);
                            String sql002 = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"Dépot initial"+"', '"+jTextField2.getText()+"', '"+generatdkey+"', '"+"Pers Morale"+"');";
                try {                  
                    pst=connect.prepareStatement(sql002);
                    pst.execute();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur d'enregistrement du dépot initial");
                    Logger.getLogger(Adhesion_persmor.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                try {
                    pst.close();
                    connect.close();
                    if (rs2!=null) rs2.close();
                    if (stmt!=null) stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Adhesion_persmor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            
            }
             
            if (success) {
            JOptionPane.showMessageDialog(null, "Enregistrement effectué avec succès");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField8.setText("");
            jTextField7.setText("");
            demoDateField3.setDate(null);
            demoDateField1.setDate(null);
            demoDateField2.setDate(null);
            jFormattedTextField2.setText("");
            jFormattedTextField3.setText("");
            jComboBox4.setSelectedIndex(0);
             jFormattedTextField4.setText("");
           // jFormattedTextField1.setText("dd/MM/yyyy");
            jTextField12.setText("");
            jTextField15.setText("");
            jTextField16.setText("");
            jTextField17.setText("");
            jTextField2.setText("");
            jTextField5.setText("");
         //   jTextField10.setText("(+IND-)NN-NN-NN-NN");
            jTextField6.setText("");
            this.mn.refresh();

                    }
            
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        jTextField3.setText(jTextField3.getText().trim());
        jTextField3.setText(jTextField3.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField3FocusLost

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        // TODO add your handling code here:
            if (jComboBox4.getSelectedIndex()==0){
             try {
          jFormattedTextField4.setDocument(new javax.swing.text.PlainDocument());       
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
                } catch (java.text.ParseException ex) {
                 ex.printStackTrace();
                
         }
        }else {
       
           jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory());  
          
           jFormattedTextField4.setDocument(new PhoneDocument());
           jFormattedTextField4.setText("+");
            
        }
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jFormattedTextField4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField4FocusGained
        // TODO add your handling code here:
       jLabel42.setText("");
        
        if (jComboBox4.getSelectedIndex()==1) {
           
              
             if (jFormattedTextField4.getText().equalsIgnoreCase("+")){
                
                    jFormattedTextField4.setDocument(new PhoneDocument());
                  jFormattedTextField4.setText("+");
             } else {
                 String before =jFormattedTextField4.getText();
                 before=before.replaceAll("\\s", "");
                 jFormattedTextField4.setText(before);
                 
             }}
    }//GEN-LAST:event_jFormattedTextField4FocusGained

    private void jFormattedTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField4FocusLost
        // TODO add your handling code here:

        if (jComboBox4.getSelectedIndex()==1){  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(jFormattedTextField4.getText(), "");
            if (!phoneUtil.isValidNumber(phoneNumber)){
                jLabel42.setText("Le numéro n'est pas valide");
                jLabel42.setForeground(Color.red);                                             // set color to red 
            }
            
            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            jFormattedTextField4.setDocument(new javax.swing.text.PlainDocument());
            jFormattedTextField4.setText(formatted);
        } catch (NumberParseException ex) {
            jLabel42.setText("Le numéro n'est pas valide");
            jLabel42.setForeground(Color.red); 
            Logger.getLogger(Adh_persmor.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jFormattedTextField4FocusLost

    private void jFormattedTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField4ActionPerformed

    private void demoDateField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demoDateField1ActionPerformed

    private void jTextField15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField15FocusLost
        // TODO add your handling code here:
          jTextField15.setText(jTextField15.getText().trim());
          jTextField15.setText(jTextField15.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField15FocusLost

    private void jTextField15KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15KeyTyped
        // TODO add your handling code here:
         // TODO add your handling code here:
   if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
        evt.consume();
    } else if (jTextField15.getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else if(jTextField15.getText().endsWith(" ") ){
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else{
         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
    }
    }//GEN-LAST:event_jTextField15KeyTyped

    private void jTextField12FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField12FocusLost
        // TODO add your handling code here:
         jTextField12.setText(jTextField12.getText().trim());
         jTextField12.setText(jTextField12.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField12FocusLost

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
                new Adh_persmor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField3;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

}
