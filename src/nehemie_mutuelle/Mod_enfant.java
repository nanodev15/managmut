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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author elommarcarnold
 */
public class Mod_enfant extends javax.swing.JFrame {

    Connection connect = null;
    PreparedStatement pst = null;
    PreparedStatement pst2 = null;
    String enf_photourl = "";  // Photo de l'enfant
    String repleg_photourl = "";  // Photo du représentant légal
    private int id; // id enfant
    private String typeAdhesion = "";  // Type d'adhesion à modifier (enregistré)
    private Date dateEpargne;    // Date d'adhesion epargne
    private Date dateTontine;    // Date d'adhesion Tontine
    private String nom = ""; //nom
    private String prenom = ""; //prénom
    private String numcarnep = ""; // numero carnet épargne enregistré
    private String numcartont = ""; // numéro carnet tontine enregistré
    private main mn; // Objet main lié pour la modification
    private boolean issavedcarn;  // check if carnet is saved
    private boolean res1 = false;  // Check if numcarnet epargne exists already
    private boolean res2 = false; // Check if numcarnet tontine exists already

    private String previousitemselection = "";
    // "/nehemie_mutuelle/blank.png"; // Images def
   int typerepleg; // Type

//     private String savednumcarnep="";
//     private String savednumcarntont="";
    private int saveepargne = 0; // may we save epargne carnet number
    private int savetontine = 0; // may we save tontine carnet number
    private String carnetEnrEpargne = "";
    private String carnetEnrTontine = "";
    private boolean mustdeleteep = false; // boolean to check we are in the case to delete epargne recording
    private boolean mustdeletetont = false; // boolean to check we are in the case to delete tontine recording

    
// Valeurs par défaut
   String nomdef; // Nom
   String prenomdef; // Prénom          
   int sexdef; // Sexe
   String addrdef; // Adresses
   String emaildef; // Email
   String teldef; // Téléphone
   String imagedef="";
   int idreplegact;
   
   
   String replegal_photourl; // Photo du rep leg
               
    /**
     * Creates new form Mod_enf
     */
    public Mod_enfant() {
        initComponents();
    }
    
    public Mod_enfant(String nom, String prenom, main mn) throws SQLException {
         this.id=getId(nom, prenom);
         this.mn=mn;
         initComponents();
         fillform();
    }

    private int getId(String nomEpargnant, String prenomEpargnant) throws SQLException {
        connect = Connect.ConnectDb();
        String sql01 = "SELECT idProfil_enfant FROM Profil_enfant WHERE Nom= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
        Statement stmt = null;
        System.out.println("sql01" + sql01);
        connect = Connect.ConnectDb();

        ResultSet rs1 = null;
        int result = 0;

        try {
            stmt = connect.createStatement();
            rs1 = stmt.executeQuery(sql01);
            //result= rs1.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (rs1.next()) {

                result = rs1.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }

        //    if(connect!=null) connect.close();
        if (stmt != null) {
            stmt.close();
        }
        if (rs1 != null) {
            rs1.close();
        }
        return result;

    }

    public boolean check_saved_carn() {
        String sql0 = "SELECT * FROM carnet_enr WHERE idadherent='" + id + "' AND typeadherent='Enfant'";
        connect = Connect.ConnectDb();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connect.createStatement();
            rs = stmt.executeQuery(sql0);
            if (rs.next()) {
                if (rs.getString(2).equalsIgnoreCase("Epargne")) {
                    carnetEnrEpargne = rs.getString(3);
                } else {
                    carnetEnrTontine = rs.getString(3);
                }
                stmt.close();
                rs.close();
                //  connect.close();
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private boolean verifynumcarn(int i) throws SQLException {
        String sql0 = "";
        if (i == 0) {
            sql0 = "SELECT Num_carnet FROM Profil_adulte WHERE Type_adhesion='" + "Epargne" + "' UNION SELECT LEFT(Num_carnet,4) FROM Profil_adulte WHERE Type_adhesion='" + "Epargne & tontine" + "'" + " UNION " + "SELECT Num_carnet FROM Profil_enfant WHERE Type_adhesion='" + "Epargne" + "' UNION SELECT LEFT(Num_carnet,4) FROM Profil_enfant WHERE Type_adhesion='" + "Epargne et tontine" + "'" + " UNION " + "SELECT Num_carnet FROM Profil_persmorale WHERE Type_adhesion='" + "Epargne" + "' UNION SELECT LEFT(Num_carnet,4) FROM Profil_persmorale WHERE Type_adhesion='" + "Epargne et tontine" + "'";
        } else {
            sql0 = "SELECT Num_carnet FROM Profil_adulte WHERE Type_adhesion='" + "Tontine" + "' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_adulte WHERE Type_adhesion='" + "Epargne et tontine" + "'" + " UNION " + "SELECT Num_carnet FROM Profil_enfant WHERE Type_adhesion='" + "Tontine" + "' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_enfant WHERE Type_adhesion='" + "Epargne et tontine" + "'" + " UNION " + "SELECT Num_carnet FROM Profil_persmorale WHERE Type_adhesion='" + "Tontine" + "' UNION SELECT RIGHT(Num_carnet,4) FROM Profil_persmorale WHERE Type_adhesion='" + "Epargne et tontine" + "'";
        }

        // Vérifier numero carnet 
        connect = Connect.ConnectDb();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connect.createStatement();
            rs = stmt.executeQuery(sql0);
            if (i == 0) {  // compare tontine
                while (rs.next()) {
                    if (jFormattedTextField2.getText().equalsIgnoreCase(rs.getString(1))) {
                        return true;
                    }
                }
            } else {
                while (rs.next()) {
                    if (jFormattedTextField3.getText().equalsIgnoreCase(rs.getString(1))) {
                        return true;
                    }
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

    private static class Emailvalidator {

        private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN
                = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public Emailvalidator() {
            pattern = Pattern.compile(EMAIL_PATTERN);
        }

        /**
         * Validate hex with regular expression
         *
         * @param hex hex for validation
         * @return true valid hex, false invalid hex
         */
        public boolean validate(final String hex) {

            matcher = pattern.matcher(hex);
            return matcher.matches();

        }
    }

    public boolean check_epargne_before_date(int id, Date date) throws SQLException {
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String sql0 = "SELECT idEpargne FROM Epargne WHERE IdEpargnant='" + id + "' AND TypeEpargnant='Adulte' AND DateEpargne < { d '" + sqldate.toString() + "' }";
        connect = Connect.ConnectDb();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connect.createStatement();
            rs = stmt.executeQuery(sql0);
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
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Epargne"+"', '"+numcarnep+"', '"+ this.getId(nom, prenom)+"', '"+"Enfant"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";   
     } else {
        sql="INSERT INTO carnet_enr VALUES ("+null+", '"+"Tontine"+"', '"+numcarnep+"', '"+ this.getId(nom, prenom)+"', '"+"Enfant"+"', '"+ new java.sql.Timestamp((new Date()).getTime()) + "')";    
     }
     
    pst2=connect.prepareStatement(sql);
    pst2.execute();
    pst2.close();
}
    
    public void deleteSavedcarn() throws SQLException {
        String sql= "DELETE FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Enfant'";
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
    
    public boolean check_tontine_before_date(int id, Date date) throws SQLException {
        java.sql.Date sqldate = new java.sql.Date(date.getTime());
        String sql0 = "SELECT idTontine as id FROM Tontine WHERE IdEpargnant='" + id + "' AND TypeEpargnant='Adulte' AND MONTH(DateTontine) < MONTH('" + sqldate + "') AND YEAR(DateTontine) <= YEAR('" + sqldate + "') UNION SELECT idretraits_tontine as id  FROM retraits_tontine WHERE IdEpargnant='" + id + "' AND TypeEpargnant='Adulte' AND DATE(DateRet) < '" + sqldate + "'";
        connect = Connect.ConnectDb();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connect.createStatement();
            rs = stmt.executeQuery(sql0);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox2 = new JComboBox(new Object[] {
            "Nouveau rep. légal", "Rep. actuel",
            new JSeparator(JSeparator.HORIZONTAL),

        });
        jPanel18 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel21 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jPanel39 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jComboBox8 = new javax.swing.JComboBox();
        jPanel40 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel42 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jPanel32 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/modadhenfant.png"))); // NOI18N

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

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

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/blank.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel11.add(jLabel2);

        jPanel10.add(jPanel11);

        jPanel9.setLayout(new java.awt.GridLayout(1, 2, 0, 5));
        jPanel9.add(jLabel3);

        jButton3.setText("Uploader la photo");
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
                .add(18, 18, 18)
                .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22))
        );

        jTabbedPane1.addTab("Etat civil", jPanel1);

        jPanel4.setBounds(20, 20, 200, 170);

        jPanel12.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

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

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 394, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jPanel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(146, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Etat civil(suite)", jPanel2);

        jPanel13.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jLabel8.setText("Type");
        jPanel14.add(jLabel8);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Parent", "Tuteur" }));
        jPanel14.add(jComboBox1);

        jPanel13.add(jPanel14);

        jPanel16.setLayout(new java.awt.GridLayout(1, 2));

        jLabel9.setText("Rep. légal");
        jPanel16.add(jLabel9);

        jComboBox2.setRenderer(new SeparatorComboBoxRenderer());
        ResultSet rst=null;
        String sql="SELECT * FROM representant_legal ORDER BY TRIM(Nom) ASC , TRIM(Prenom) ASC;";
        connect=Connect.ConnectDb();
        Statement stmt = null;
        try {
            stmt= connect.createStatement();
            rst=stmt.executeQuery(sql);
            while(rst.next()) {
                jComboBox2.addItem(rst.getString(3)+", "+rst.getString(4));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });
        jPanel16.add(jComboBox2);

        jPanel13.add(jPanel16);

        jPanel18.setLayout(new java.awt.GridLayout(1, 2));

        jLabel12.setText("Nom");
        jPanel18.add(jLabel12);

        DocumentFilter filter2 = new UppercaseDocumentFilter();
        ((AbstractDocument) jTextField10.getDocument()).setDocumentFilter(filter2);
        jTextField10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField10FocusLost(evt);
            }
        });
        jPanel18.add(jTextField10);

        jPanel13.add(jPanel18);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel19.setLayout(new java.awt.GridLayout(1, 2));

        jLabel14.setText("Prénoms");
        jPanel19.add(jLabel14);

        jTextField13.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField13FocusLost(evt);
            }
        });
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });
        jPanel19.add(jTextField13);

        jPanel13.add(jPanel19);

        jPanel33.setLayout(new java.awt.GridLayout(1, 2));

        jLabel27.setText("Sexe");
        jPanel33.add(jLabel27);

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Homme", "Femme" }));
        jPanel33.add(jComboBox6);

        jPanel13.add(jPanel33);

        jPanel21.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel34.setLayout(new java.awt.GridLayout(1, 2));

        jLabel15.setText("Photo");
        jPanel34.add(jLabel15);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/blank.png"))); // NOI18N
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel34.add(jLabel16);

        jPanel21.add(jPanel34);

        jPanel35.setLayout(new java.awt.GridLayout(1, 2, 0, 5));
        jPanel35.add(jLabel28);

        jButton4.setText("Uploader la photo");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton4);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(40, Short.MAX_VALUE))))
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jPanel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22))
        );

        jTabbedPane1.addTab("Rep légal", jPanel3);

        jPanel37.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel38.setLayout(new java.awt.GridLayout(1, 2));

        jLabel29.setText("Adresse");
        jPanel38.add(jLabel29);

        jTextField14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField14FocusLost(evt);
            }
        });
        jPanel38.add(jTextField14);

        jPanel37.add(jPanel38);

        jPanel39.setLayout(new java.awt.GridLayout(1, 2));

        jLabel30.setText("Téléphone");
        jPanel39.add(jLabel30);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "national", "international" }));
        jComboBox8.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox8ItemStateChanged(evt);
            }
        });
        jPanel39.add(jComboBox8);

        jPanel37.add(jPanel39);

        jPanel40.setLayout(new java.awt.GridLayout(1, 2));
        jPanel40.add(jLabel31);

        try {
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jFormattedTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextField2FocusLost(evt);
            }
        });
        jPanel40.add(jFormattedTextField2);

        jPanel37.add(jPanel40);

        jPanel42.setLayout(new java.awt.GridLayout(1, 2));
        jPanel42.add(jLabel34);

        jLabel35.setForeground(new java.awt.Color(229, 10, 10));
        jPanel42.add(jLabel35);

        jPanel37.add(jPanel42);

        jPanel4.setBounds(20, 20, 200, 170);
        jPanel41.setLayout(new java.awt.GridLayout(1, 2));

        jLabel32.setText("Email");
        jPanel41.add(jLabel32);

        jTextField20.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField20FocusLost(evt);
            }
        });
        jPanel41.add(jTextField20);

        jPanel37.add(jPanel41);

        jPanel43.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel44.setLayout(new java.awt.GridLayout(1, 2));
        jPanel43.add(jPanel44);

        org.jdesktop.layout.GroupLayout jPanel36Layout = new org.jdesktop.layout.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel36Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel36Layout.createSequentialGroup()
                        .add(jPanel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel36Layout.createSequentialGroup()
                        .add(jPanel37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(40, Short.MAX_VALUE))))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel36Layout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jPanel37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 159, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 182, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Rep légal(suite)", jPanel36);

        jPanel22.setLayout(new java.awt.GridLayout(9, 1, 0, 5));

        jPanel23.setLayout(new java.awt.GridLayout(1, 2));

        jLabel17.setText("Type d'adhésion");
        jPanel23.add(jLabel17);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Epargne", "Tontine", "Epargne et tontine" }));
        jPanel23.add(jComboBox3);

        jPanel22.add(jPanel23);

        jPanel32.setLayout(new java.awt.GridLayout(1, 2));

        jLabel26.setText("Bénéficiaires");
        jPanel32.add(jLabel26);

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tuteur/Parent", "Enfant" }));
        jPanel32.add(jComboBox5);

        jPanel22.add(jPanel32);

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
        jPanel26.add(jTextField16);

        jPanel22.add(jPanel26);

        jPanel27.setLayout(new java.awt.GridLayout(1, 2));

        jLabel21.setText("Part Sociale");
        jPanel27.add(jLabel21);
        jPanel27.add(jTextField17);

        jPanel22.add(jPanel27);

        jPanel29.setLayout(new java.awt.GridLayout(1, 2));

        jLabel25.setText("Date d'adhésion épargne");
        jPanel29.add(jLabel25);
        jPanel29.add(demoDateField1);

        jPanel22.add(jPanel29);

        jPanel30.setLayout(new java.awt.GridLayout(1, 2));

        jLabel24.setText("Date d'adhésion tontine");
        jPanel30.add(jLabel24);
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
                .addContainerGap(138, Short.MAX_VALUE))
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
            .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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

    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-') && !Character.isSpaceChar(evt.getKeyChar())) {
            evt.consume();
        } else if (jTextField4.getText().trim().length() == 0) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        } else if (jTextField4.getText().endsWith(" ")) {
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        } else {
            evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
        }

    }//GEN-LAST:event_jTextField4KeyTyped

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        jTextField3.setText(jTextField3.getText().trim());
        jTextField3.setText(jTextField3.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField3FocusLost

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

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        // TODO add your handling code here:
        jTextField9.setText(jTextField9.getText().trim());
        jTextField9.setText(jTextField9.getText().replaceAll("\\s+", " "));
        jTextField9.setText(WordUtils.capitalizeFully(jTextField9.getText()));
    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField11FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField11FocusLost
        // TODO add your handling code here:
        jTextField11.setText(jTextField11.getText().trim());
        jTextField11.setText(jTextField11.getText().replaceAll("\\s+", " "));
        jTextField11.setText(WordUtils.capitalizeFully(jTextField11.getText()));
    }//GEN-LAST:event_jTextField11FocusLost

    private void jTextField10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField10FocusLost
        // TODO add your handling code here:
        jTextField10.setText(jTextField10.getText().trim());
        jTextField10.setText(jTextField10.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField10FocusLost

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
        // TODO add your handling code here:
        if (!Character.isAlphabetic(evt.getKeyChar()) && !Character.isSpaceChar(evt.getKeyChar())) {
            evt.consume();
        } else if (jTextField13.getText().trim().length() == 0) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        } else if (jTextField13.getText().endsWith(" ")) {
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        } else {
            evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
        }
    }//GEN-LAST:event_jTextField13KeyTyped

    private void jTextField13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField13FocusLost
        // TODO add your handling code here:
        jTextField13.setText(jTextField13.getText().trim());
        jTextField13.setText(jTextField13.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField13FocusLost

    private void jTextField14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField14FocusLost
        // TODO add your handling code here:
        jTextField14.setText(jTextField14.getText().trim());
        jTextField14.setText(jTextField14.getText().replaceAll("\\s+", " "));
        jTextField14.setText(WordUtils.capitalizeFully(jTextField14.getText()));
    }//GEN-LAST:event_jTextField14FocusLost

    private void jComboBox8ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox8ItemStateChanged
        if (jComboBox8.getSelectedIndex() == 0) {
            try {
                jFormattedTextField2.setDocument(new javax.swing.text.PlainDocument());
                jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
        } else {
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory());
            jFormattedTextField2.setDocument(new PhoneDocument());
            jFormattedTextField2.setText("+");
        }
    }//GEN-LAST:event_jComboBox8ItemStateChanged

    private void jFormattedTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusGained
        // TODO add your handling code here:
        jLabel35.setText("");

        if (jComboBox8.getSelectedIndex() == 1) {

            if (jFormattedTextField2.getText().equalsIgnoreCase("+")) {
                jFormattedTextField2.setDocument(new PhoneDocument());
                jFormattedTextField2.setText("+");
            } else {
                String before = jFormattedTextField2.getText();
                before = before.replaceAll("\\s", "");
                jFormattedTextField2.setText(before);

            }
        }
    }//GEN-LAST:event_jFormattedTextField2FocusGained

    private void jFormattedTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusLost
        // TODO add your handling code here:
        if (jComboBox8.getSelectedIndex() == 1) {  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
            try {
                phoneNumber = phoneUtil.parse(jFormattedTextField2.getText(), "");
                if (!phoneUtil.isValidNumber(phoneNumber)) {
                    jLabel35.setText("Le numéro n'est pas valide");
                    jLabel35.setForeground(Color.red);                                             // set color to red 
                }

                String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                jFormattedTextField2.setDocument(new javax.swing.text.PlainDocument());
                jFormattedTextField2.setText(formatted);
            } catch (NumberParseException ex) {
                jLabel35.setText("Le numéro n'est pas valide");
                jLabel35.setForeground(Color.red);
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jFormattedTextField2FocusLost

    private void jTextField20FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField20FocusLost
        // TODO add your handling code here:
        jTextField20.setText(jTextField20.getText().trim());
    }//GEN-LAST:event_jTextField20FocusLost

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            jLabel35.setText("");
            if (jComboBox2.getSelectedIndex() == 0) {
                jTextField10.setText(""); // Nom
                jTextField13.setText("");  // Prénom
                jComboBox6.setSelectedIndex(0); //Sexe
                jTextField14.setText(""); // Adresse
                jTextField20.setText(""); // Email
                jFormattedTextField2.setText("");  // Téléphone
                Image temp = new ImageIcon(
                        getClass().getResource("blank.png")).getImage().getScaledInstance(200, 200,
                                Image.SCALE_SMOOTH);
                jLabel16.setIcon(new ImageIcon(temp)); // Photo
                jComboBox1.setSelectedIndex(0); //Type repr.

            } else if (jComboBox2.getSelectedIndex() == 1) {  // Remplir les valeurs du rep leg
                // Valeurs par défaut       
                jTextField10.setText(nomdef); // Nom
                jTextField13.setText(prenomdef);  // Prénom
                jComboBox6.setSelectedIndex(sexdef); //Sexe
                jTextField14.setText(addrdef); // Adresse
                jTextField20.setText(emaildef); // Email
                jFormattedTextField2.setText(teldef);  // Téléphone
                if(imagedef == null) System.out.println("null imagedef");
                System.out.println("imagedef"+imagedef);
                if(getClass().getResource(imagedef) != null) {
                Image temp = new ImageIcon(
                        getClass().getResource(imagedef)).getImage().getScaledInstance(200, 200,
                                Image.SCALE_SMOOTH);
                jLabel16.setIcon(new ImageIcon(temp)); // Photo
                jComboBox1.setSelectedIndex(typerepleg); //Type repr.
                }

            } else {
                String nom = ((String) jComboBox2.getSelectedItem()).split(", ")[0];
                String prenom = ((String) jComboBox2.getSelectedItem()).split(", ")[1];
                jTextField10.setText(nom);
                jTextField13.setText(prenom);
                ResultSet rst = null;
                String sql0 = "SELECT * FROM representant_legal WHERE Nom= '" + nom + "' AND lower(Prenom)= '" + prenom.toLowerCase(Locale.FRENCH) + "'";
                connect = Connect.ConnectDb();
                Statement stmt = null;
                try {
                    stmt = connect.createStatement();
                    rst = stmt.executeQuery(sql0);
                    int i = 0;
                    while (rst.next()) {
                        if (rst.getString(2).equalsIgnoreCase("parent")) {  // Type repr
                            jComboBox1.setSelectedIndex(0);
                        } else {
                            jComboBox1.setSelectedIndex(1);
                        }

                        jTextField14.setText(rst.getString(5)); //adresse
                        if (rst.getString(6).startsWith("+")) {
                            jComboBox8.setSelectedIndex(1);
                        } else {
                            jComboBox8.setSelectedIndex(0);
                        }
                        jFormattedTextField2.setText(rst.getString(6));
                        jTextField20.setText(rst.getString(7));
                        if (rst.getString(8).trim().equalsIgnoreCase("masculin") || rst.getString(8).trim().equalsIgnoreCase("homme")) {
                            jComboBox6.setSelectedIndex(0);
                        } else {
                            jComboBox6.setSelectedIndex(1);
                        }
                        try {
                            Image temp = new ImageIcon(rst.getString(9)).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                            jLabel16.setIcon(new ImageIcon(temp));
                        } catch (Exception e) {
                        }
                        i++;
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {

                    if (rst != null) {
                        rst.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged
    public boolean check_rep_leg(String nom, String prenom) throws SQLException {
        boolean result = false;
        String sql0 = "SELECT id_representant_legal FROM representant_legal WHERE Nom= '" + jTextField10.getText() + "' AND lower(Prenom)= '" + jTextField13.getText().toLowerCase(Locale.FRENCH) + "'";
        connect = Connect.ConnectDb();
        PreparedStatement pre = connect.prepareStatement(sql0);
        ResultSet rst = pre.executeQuery();
        if (rst.next()) {
            result = true;
        }

        connect.close();
        rst.close();
        pre.close();

        return result;
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Logique de modification
       
       
        boolean replegstatus = false;
        Emailvalidator emailValidator = new Emailvalidator();
        if (!jTextField10.getText().isEmpty() && !jTextField10.getText().isEmpty()) {
            try {
                replegstatus = check_rep_leg(jTextField10.getText(), jTextField13.getText());
            } catch (SQLException ex) {
                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Validate carnet num
        if (jComboBox3.getSelectedIndex() == 0 || jComboBox3.getSelectedIndex() == 2) {
            try {
                res1 = verifynumcarn(0);

            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (jComboBox3.getSelectedIndex() == 1 || jComboBox3.getSelectedIndex() == 2) {
            try {
                res2 = verifynumcarn(1);

            } catch (SQLException ex) {
                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Nom");
        } else if (jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ prénom");
        } else if (jFormattedTextField2.getText().equalsIgnoreCase("  -  -  -  ") || jFormattedTextField2.getText().equalsIgnoreCase("+")) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Téléphone");
        } else if (!jTextField20.getText().isEmpty() && !emailValidator.validate(jTextField20.getText())) {
            JOptionPane.showMessageDialog(this, "L'email indiqué n'est pas valide");

        } else if (jTextField10.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le Nom du représentant légal");
        } else if (jTextField13.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le prénom du représentant légal");
        } else if (jTextField16.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner les droits d'adhésion");

        } else if (jFormattedTextField1.getText().equalsIgnoreCase("0000") || jFormattedTextField3.getText().equalsIgnoreCase("0000")) {
            JOptionPane.showMessageDialog(this, "Numéro de carnet 0000 invalide");
        } else if (jComboBox3.getSelectedIndex() == 0 && jFormattedTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Numéro de carnet Epargne invalide");
        } else if (jComboBox3.getSelectedIndex() == 1 && jFormattedTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Numéro de carnet Tontine invalide");
        } else if (jComboBox3.getSelectedIndex() == 2 && (jFormattedTextField1.getText().isEmpty() || jFormattedTextField3.getText().isEmpty())) {
            JOptionPane.showMessageDialog(this, "Numéro de carnet invalide");
        } else if (jComboBox3.getSelectedIndex() == 0 && (res1 == true && !numcarnep.equalsIgnoreCase(jFormattedTextField1.getText()))) {
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");
        } else if (jComboBox3.getSelectedIndex() == 1 && (res2 == true && !numcartont.equalsIgnoreCase(jFormattedTextField3.getText()))) {
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé");
        } else if (jComboBox3.getSelectedIndex() == 2 && (res1 == true && !numcarnep.equalsIgnoreCase(jFormattedTextField1.getText()))) {
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");
        } else if (jComboBox3.getSelectedIndex() == 2 && (res2 == true && !numcartont.equalsIgnoreCase(jFormattedTextField3.getText()))) {
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé");
        } else if (demoDateField1.getDate() == null && (jComboBox3.getSelectedIndex() == 0 || jComboBox3.getSelectedIndex() == 2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Epargne");
        } else if (demoDateField2.getDate() == null && (jComboBox3.getSelectedIndex() == 1 || jComboBox3.getSelectedIndex() == 2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Tontine");
        } else if (replegstatus == true && jComboBox2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Attention le représentant légal indiqué est déjà enregistré. Veuillez le sélectionner dans la liste déroulante");
        } else {
            // Check if someone has already this name
            boolean inserted = false;
            boolean success = true;
            boolean check_epargne = false;
            boolean check_tontine = false;
            Statement stmt = null;
            ResultSet rs2 = null;
            if (!jTextField3.getText().equalsIgnoreCase(nom) || !jTextField4.getText().equalsIgnoreCase(prenom)) {  // to change
                String sql01 = "SELECT idProfil_enfant FROM Profil_enfant WHERE Nom= '" + jTextField3.getText() + "' AND lower(Prenoms)= '" + jTextField4.getText().toLowerCase(Locale.FRENCH) + "'";

                connect = Connect.ConnectDb();

                try {
                    stmt = connect.createStatement();
                    rs2 = stmt.executeQuery(sql01);
                } catch (SQLException ex) {
                    Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    if (rs2.next()) {
                        JOptionPane.showMessageDialog(null, "Attention la personne indiquée est déjà enregistré");
                        success = false;
                        inserted = true;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // On s'arrête ici pour le moment 
            // everything is correct-insert modify in the database
            if (!inserted) {
                //First case chose Epargne and changed the date   
                if (typeAdhesion.equalsIgnoreCase("Epargne")) {
                    if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne") || ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                        if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                            mustdeletetont = true; // must delete tontine data
                        }
                        System.out.println("demodatefield" + demoDateField1.getDate());
                        System.out.println("dateEpargne" + dateEpargne);
                        if (dateEpargne !=null && dateEpargne.before(demoDateField1.getDate())) {
                            try {
                                System.out.println("nom" + nom + "prenom" + prenom);
                                check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_epargne == true) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }
                        } else if (dateEpargne ==null) {
                            // INSERT dateEpargne
                           success =true;
                            
                            
                            
                        }

                        // Choose Tontine
                    } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine")) {
                        mustdeletetont = true;
                        try { // A tester
                            check_epargne = check_epargne_before_date(getId(nom, prenom), new Date());
                        } catch (SQLException ex) {
                            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (check_epargne == true) {
                            JOptionPane.showMessageDialog(null, "Attention, Notez que des données épargne sont encore enregistrées pour ce client");
                            saveepargne = 1;
                        } else {
                            saveepargne = 2;
                        }

                    }
                    // Case where Tontine was chosen    
                } else if (typeAdhesion.equalsIgnoreCase("Tontine")) {
                    if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine") || ((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                        if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine")) {
                            mustdeleteep = true;
                        }
                        if (dateTontine.before(demoDateField2.getDate())) {

                            try {
                                check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_tontine == false) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }

                        }
                    } else if ((((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne"))) {
                        mustdeleteep = true;
                        try {  // A tester
                            System.out.println("nom" + nom + "prenom" + prenom);

                            check_tontine = check_tontine_before_date(getId(nom, prenom), new Date());
                        } catch (SQLException ex) {
                            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (check_tontine == true) {
                            JOptionPane.showMessageDialog(null, "Attention, Notez que des données tontine sont encore enregistrées pour ce client");
                            savetontine = 1;
                        } else {
                            savetontine = 2;
                        }

                    }
                } else if (typeAdhesion.equalsIgnoreCase("Epargne & Tontine")) {
                    //same choice
                    if ((((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne & Tontine"))) {
                        if (dateEpargne.before(demoDateField1.getDate())) {
                            try {
                                check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_epargne == true) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }

                        }

                        if (dateTontine.before(demoDateField2.getDate())) {

                            try {
                                check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_tontine == false) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }

                        }

                    } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Epargne")) {
                        if (dateEpargne.before(demoDateField1.getDate())) {
                            try {
                                check_epargne = check_epargne_before_date(getId(nom, prenom), demoDateField1.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_epargne == true) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements épargne antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }
                        }

                    // Avertissement tontine 
                        try {  // A tester
                            check_tontine = check_tontine_before_date(getId(nom, prenom), new Date());
                        } catch (SQLException ex) {
                            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (check_tontine == true) {
                            JOptionPane.showMessageDialog(null, "Attention, Notez que des données tontine sont encore enregistrées pour ce client");
                            savetontine = 1;
                        } else {
                            savetontine = 2;
                        }
                    } else if (((String) jComboBox3.getSelectedItem()).equalsIgnoreCase("Tontine")) {
                        if (dateTontine.before(demoDateField2.getDate())) {

                            try {
                                check_tontine = check_tontine_before_date(getId(nom, prenom), demoDateField2.getDate());
                            } catch (SQLException ex) {
                                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (check_tontine == false) {
                                JOptionPane.showMessageDialog(null, "Attention, vous devez supprimer les enregistrements Tontine antérieur à la nouvelle date d'adhésion");
                                success = false;
                            }

                        }

                        // Avertissement epargne 
                        try {  // A tester
                            check_epargne = check_epargne_before_date(getId(nom, prenom), new Date());
                        } catch (SQLException ex) {
                            Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        if (check_epargne == true) {
                            JOptionPane.showMessageDialog(null, "Attention, Notez que des données épargne sont encore enregistrées pour ce client");
                            saveepargne = 1;
                        } else {
                            savetontine = 2;
                        }
                    }
                }

                if (success == true) {
                    try {
                        // Now I can do the update
                        connect = Connect.ConnectDb();
                        String sqlmod = "UPDATE Profil_enfant SET Nom = ?, Prenoms = ?, Photo = ?, Date_naissance = ?, Lieu_naissance = ?, Quartier = ?,  Profession = ?, Type_adhesion = ?, Beneficiaires = ?, Id_representant_legal = ?, sexe = ?,  Num_carnet = ?, Droit_adhesion = ?, Part_sociale = ?, Date_adhesion_ep = ?, Date_adhesion_to = ? " + "WHERE idProfil_enfant='" + this.getId(nom, prenom) + "';";
                        pst = connect.prepareStatement(sqlmod);
                        String numcarn = "";
                        if (jComboBox3.getSelectedIndex() == 0) {
                            numcarn = jFormattedTextField1.getText();
                        } else if (jComboBox3.getSelectedIndex() == 1) {
                            numcarn = jFormattedTextField3.getText();
                        } else {
                            numcarn = jFormattedTextField1.getText() + "-" + jFormattedTextField3.getText();
                        }
                        pst.setString(1, jTextField3.getText());   // Nom
                        pst.setString(2, jTextField4.getText());  // Prenom
                        pst.setString(3, enf_photourl);  // Photo
                        if (demoDateField3.getDate() != null) {
                            pst.setDate(4, new java.sql.Date(demoDateField3.getDate().getTime()));
                        } else {
                            pst.setDate(4, null);
                        }  // Date de naissance
                        pst.setString(5, jTextField6.getText()); // Lieu de naissance
                        pst.setString(6, jTextField10.getText()); // Quartier
                        pst.setString(7, jTextField9.getText()); // Profession
                        pst.setString(8, (String) jComboBox3.getSelectedItem()); //Type d'adhésion
                        pst.setString(9, (String) jComboBox5.getSelectedItem()); //Bénéficiaires
                        
                        //Représentant légal
                       if(jComboBox2.getSelectedIndex()==1) {
                        pst.setInt(10, idreplegact);    
                       } else if(jComboBox2.getSelectedIndex()==0) {
                       String sql1 = "INSERT INTO representant_legal " + 
                      "VALUES ("+ null +", '"+ (String) jComboBox1.getSelectedItem() + "', '"+ jTextField10.getText().trim() +"', '"+ jTextField13.getText().trim() +
                      "', '"+jTextField14.getText().trim()+"', '"+jFormattedTextField2.getText()+"', '"+jTextField20.getText().trim()+"', '"+String.valueOf(jComboBox6.getSelectedItem()).trim()+ "', '"+replegal_photourl+
                      "');";
                       
                       PreparedStatement pst2=connect.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
                       pst2.execute();
                       ResultSet rst= pst2.getGeneratedKeys();
                       int generatedkey=0;
                       if(rst.next()){
                           generatedkey= rst.getInt(1);
                       }
                       
                       if(pst2!=null) pst2.close();
                       if(rst!=null) rst.close();
                       pst.setInt(10, generatedkey); 
                       } else {
                           
                           int generated=0;
                           String sql4="SELECT id_representant_legal FROM representant_legal WHERE Nom= '" + jTextField10.getText() + "' AND lower(Prenom)= '"+jTextField13.getText().toLowerCase(Locale.FRENCH)+ "'";
                           connect=Connect.ConnectDb();
                           Statement stmt2 = null;                         
                           ResultSet rst=null;
                           try {
                              stmt2= connect.createStatement();
                              rst=stmt2.executeQuery(sql4);

                           } catch (SQLException ex) {
                             Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
                           }
                           
                           if(rst.next()){
                              generated= rst.getInt(1); 
                           }
                           
                           if(stmt!=null) stmt.close();
                           if(rst!=null) rst.close();
                           
                            pst.setInt(10, generated);
                                                    
                       }

                       pst.setString(11, (String) jComboBox4.getSelectedItem()); // Sexe
                       pst.setString(12, numcarn);
                       pst.setInt(13, Integer.valueOf(jTextField16.getText()));  // Droits d'adhésion
                       pst.setInt(14, Integer.valueOf(jTextField17.getText()));   // Part sociale 
                          if (saveepargne == 0) {
                            if (demoDateField1.getDate() != null && demoDateField1.isEnabled()) {
                                pst.setDate(15, new java.sql.Date(demoDateField1.getDate().getTime()));
                            } else {
                                pst.setDate(15, null);  // Date d'adhésion épargne 
                            }
                        }

                        if (savetontine == 0) {
                            if (demoDateField2.getDate() != null && demoDateField2.isEnabled()) {
                                pst.setDate(16, new java.sql.Date(demoDateField2.getDate().getTime()));
                            } else {
                                pst.setDate(16, null);// Date d'adhésion tontine
                            }
                        }

                        // Contraintes additionnels
                        if (saveepargne == 1) {
                            // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                            savecarn(0);
                            pst.setDate(15, new java.sql.Date(dateEpargne.getTime())); // On garde l'ancienne date
                            System.out.println("savedepargne1");
                        }

                        if (saveepargne == 2) {
                            pst.setDate(15, null);
                            System.out.println("savedepargne2");
                        }

                        if (savetontine == 1) {

                            // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                            savecarn(1);
                            pst.setDate(16, new java.sql.Date(dateTontine.getTime()));

                        }

                        if (savetontine == 2) {
                            // Dans ce cas on laisse la date epargne à la place et on enregistre le numero carnet
                            pst.setDate(16, null);

                        }
                       
                      
                        pst.execute();
                        // On s'arrête ici 
                        if ((mustdeleteep && !carnetEnrEpargne.isEmpty()) || (mustdeletetont && !carnetEnrTontine.isEmpty())) {
                            System.out.println("it is true");
                            deleteSavedcarn();
                        }
                        
                        

                        JOptionPane.showMessageDialog(null, "Les modifications ont été enregistrées avec succès");
                        nom = jTextField3.getText();
                        prenom = jTextField4.getText();
                        numcarnep = jFormattedTextField1.getText();
                        numcartont = jFormattedTextField3.getText();
                        this.mn.refresh();
                    } catch (SQLException ex) {
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                // closing connections 
                if (connect != null) {
                    try {
                        connect.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                replegal_photourl=chooser.getSelectedFile().getCanonicalPath().replace("\\", "\\\\");
            } catch (IOException ex) {
                Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (replegal_photourl!= null && !replegal_photourl.equals("")) {
            try {
                Image temp = new ImageIcon(replegal_photourl).getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                jLabel16.setIcon(new ImageIcon(temp));
                //JLabel picture = new JLabel(new ImageIcon(temp));
                //jLabel1.setBounds(250, 20, 180, 180);
                //jPanel1.add(picture);
              
            } catch (Exception e) {
            }
        } else {
            try {
                Image temp = new ImageIcon(
                    getClass().getResource("blank.png")).getImage().getScaledInstance(90, 90,
                    Image.SCALE_SMOOTH);
                replegal_photourl= getClass().getResource("blank.png").getPath();
                //JLabel picture = new JLabel(new ImageIcon(temp));
                jLabel16.setIcon(new ImageIcon(temp));
                // jLabel5.setBounds(250, 20, 180, 180);
                // jPanel1.add(picture);
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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

    public void fillform() throws SQLException {
      
        String sql0 = "SELECT * From Profil_enfant WHERE idProfil_enfant='" + this.id + "'";
        // Vérifier numero carnet 
        connect = Connect.ConnectDb();
        issavedcarn = check_saved_carn();
        Statement stmt = null;
        ResultSet rs = null;

        stmt = connect.createStatement();
        rs = stmt.executeQuery(sql0);
        while (rs.next()) {
            jTextField3.setText(rs.getString(2)); // Nom
            nom = jTextField3.getText();
            jTextField4.setText(rs.getString(3)); // Prenom
            prenom = jTextField4.getText();
            demoDateField3.setDate(rs.getDate(5)); // Date de naissance
            jTextField6.setText((rs.getString(6))); // Lieu de naissance
            if (rs.getString(12).equalsIgnoreCase("homme") || rs.getString(12).equalsIgnoreCase("masculin")) {
                jComboBox4.setSelectedIndex(0);  // Sexe 
            } else {
                jComboBox4.setSelectedIndex(1);
            }

            if (rs.getString(4) != null) {   // Photo 
                Image temp = new ImageIcon(rs.getString(4)).getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(temp));
                enf_photourl = rs.getString(5);
            }

            jTextField9.setText(rs.getString(8));  // Profession
            jTextField10.setText(rs.getString(7)); // Quartier

            // Représentant légal
            String sql3 = "SELECT * FROM representant_legal WHERE id_representant_legal='" + rs.getInt(11) + "'";
            idreplegact= rs.getInt(11);
            ResultSet rs2 = null;
            Statement stmt2 = connect.createStatement();
            rs2 = stmt2.executeQuery(sql3);
            while (rs2.next()) {
                if (rs2.getString(2).equalsIgnoreCase("parent")) {
                    jComboBox1.setSelectedIndex(0);
                    typerepleg=0;
                } else {
                    jComboBox1.setSelectedIndex(1);
                    typerepleg=1;
                }

                // Rep Leg combo box, la procédure se fera après
                jTextField10.setText(rs2.getString(3)); // Nom rep leg
                nomdef=rs2.getString(3); // Nom
                jTextField13.setText(rs2.getString(4)); // Prenom rep leg
                prenomdef = rs2.getString(4); // Prénom    
                if (rs2.getString(8).equalsIgnoreCase("masculin") || rs2.getString(8).equalsIgnoreCase("homme")) {  // Sexe rep leg
                    jComboBox6.setSelectedIndex(0);
                    sexdef=0;
                } else {
                    jComboBox6.setSelectedIndex(1);
                    sexdef =1;
                }
                jTextField14.setText(rs2.getString(5)); // Adresse
                addrdef = rs2.getString(5); // Adresses
                jTextField20.setText(rs2.getString(7)); // Email 
                String emaildef = rs2.getString(7); // Email
                if (rs2.getString(6).startsWith("+")) { // Téléphone
                    jComboBox8.setSelectedIndex(1);
                } else {
                    jComboBox8.setSelectedIndex(0);
                }
                jFormattedTextField2.setText(rs2.getString(6));
                teldef=rs2.getString(6);
               
            
                // formattage téléphone 
                if (jComboBox8.getSelectedIndex() == 1) {  //international
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber phoneNumber;
                    try {
                        phoneNumber = phoneUtil.parse(jFormattedTextField1.getText(), "");
                        if (!phoneUtil.isValidNumber(phoneNumber)) {                System.out.println("Il est entré ici et vaut"+replegal_photourl);

                            jLabel35.setText("Le numéro n'est pas valide");

                        }

                        String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
                        jFormattedTextField2.setDocument(new javax.swing.text.PlainDocument());
                        jFormattedTextField2.setText(formatted);
                    } catch (NumberParseException ex) {
                        jLabel35.setText("Le numéro n'est pas valide");
                        Logger.getLogger(Mod_enfant.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
              
                Image temp2 = new ImageIcon(rs2.getString(7)).getImage().getScaledInstance(210, 210, Image.SCALE_SMOOTH);
                if (rs2.getString(7)==null) {
                    temp2= new ImageIcon(
                        getClass().getResource("blank.png")).getImage().getScaledInstance(200, 200,
                                Image.SCALE_SMOOTH);
                }
                jLabel16.setIcon(new ImageIcon(temp2));
                if (rs2.getString(7) !=null) imagedef=rs2.getString(9); // Images def
                else imagedef="blank.png";
                repleg_photourl = rs2.getString(4);
            }

            // Fill combobox of rep leg
            jComboBox2.setSelectedIndex(1);

            /**
             * Repr leg fini  *
             */
            // partie adhésion
            if (rs.getString(9).equalsIgnoreCase("Epargne")) {
                jComboBox3.setSelectedIndex(0);  // Type d'adhésion
                jFormattedTextField3.setEnabled(false);
                // demoDateField2.setEnabled(false);
                if (rs.getString(13) != null) {
                    jFormattedTextField1.setText(rs.getString(13));  // Numéro carnet epargne
                }
                demoDateField1.setDate(rs.getDate(16)); // Date d'adhésion épargne
                demoDateField2.setDate(rs.getDate(17));
                demoDateField2.setEnabled(false);
                dateEpargne = demoDateField1.getDate();  // Date d'adhésion épargne recorded
                numcarnep = rs.getString(13);

            } else if (rs.getString(9).equalsIgnoreCase("Tontine")) {
                jComboBox3.setSelectedIndex(1);// Type d'adhésion
                jFormattedTextField1.setEnabled(false);
                demoDateField2.setEnabled(true);
                if (rs.getString(13) != null) {
                    jFormattedTextField3.setText(rs.getString(13));  // Numéro carnet tontine
                }
                demoDateField2.setDate(rs.getDate(17)); // Date d'adhésion tontine
                demoDateField1.setDate(rs.getDate(16)); // Date d'adhésion épargne
                demoDateField1.setEnabled(false);
                dateTontine = demoDateField2.getDate();  // Date d'adhésion Tontine recorded
                numcartont = rs.getString(13);
            } else if (rs.getString(9).equalsIgnoreCase("Epargne & Tontine") || rs.getString(9).equalsIgnoreCase("Epargne et Tontine")) {
                jComboBox3.setSelectedIndex(2);// Type d'adhésion
                if (rs.getString(16) != null) {
                    jFormattedTextField1.setText(rs.getString(13).substring(0, 4));  // Numéro carnet Epargne
                    jFormattedTextField3.setText(rs.getString(13).substring(5, rs.getString(13).length())); // Numéro carnet Tontine
                }
                demoDateField1.setDate(rs.getDate(16)); // Date d'adhésion Epargne
                demoDateField2.setDate(rs.getDate(17)); // Date d'adhésion tontine
                dateEpargne = demoDateField1.getDate();  // Date d'adhésion épargne recorded
                dateTontine = demoDateField2.getDate();  // Date d'adhésion Tontine recorded
                numcarnep = rs.getString(13).substring(0, 4);
                numcartont = rs.getString(13).substring(5, rs.getString(13).length());

            }

            jTextField16.setText(String.valueOf(rs.getInt(14)));   // Droits d'adhésion
            jTextField17.setText(String.valueOf(rs.getInt(15)));   // Part Sociale
        }

        typeAdhesion = (String) jComboBox3.getSelectedItem();    // Type d'adhesion recorded
        if (!jFormattedTextField1.isEnabled() && !carnetEnrEpargne.isEmpty()) {
            jFormattedTextField1.setText(carnetEnrEpargne);
        }
        if (!jFormattedTextField3.isEnabled() && !carnetEnrTontine.isEmpty()) {
            jFormattedTextField3.setText(carnetEnrTontine);
        }

// closing connections 
        if (connect != null) {
            connect.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (rs != null) {
            rs.close();
        }
          
    }

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
            java.util.logging.Logger.getLogger(Mod_enfant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mod_enfant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mod_enfant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mod_enfant.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mod_enfant().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField3;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox8;
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
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
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
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

}
