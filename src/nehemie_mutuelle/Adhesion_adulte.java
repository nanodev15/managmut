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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import org.apache.commons.lang.WordUtils;

/**
 *
 * @author elommarcarnold
 */
public class Adhesion_adulte extends javax.swing.JFrame {
    String adulte_photourl="";
    Connection connect=null;
    PreparedStatement pst=null;
    private boolean res1=false;
    private boolean res2=false;
  //  Connect conn=new Connect();
    private main main;

    /**
     * Creates new form Adhesion_adulte
     */
    public Adhesion_adulte(main mn) {
        initComponents();
        this.main=mn;
        this.setLocationRelativeTo(null);
    }
     
     private boolean verifynumcarn(int i) throws SQLException {
        String sql0="";
        if (i==0) sql0="SELECT Num_carnet FROM Profil_adulte WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_adulte WHERE Type_adhesion='"+"Epargne et tontine"+"'" + " UNION " +"SELECT Num_carnet FROM Profil_enfant WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_enfant WHERE Type_adhesion='"+"Epargne et tontine"+"'" + " UNION "+ "SELECT Num_carnet FROM Profil_persmorale WHERE Type_adhesion='"+"Epargne"+"' UNION SELECT LEFT(Num_carnet,4) FROM Profil_persmorale WHERE Type_adhesion='"+"Epargne et tontine"+"'";
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
                     if(jFormattedTextField1.getText().equalsIgnoreCase(rs.getString(1))) return true;
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
              
              connect.close();
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
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ButtonGroup bg = new ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        ButtonGroup bg2 = new ButtonGroup();
        jCheckBox4 = new javax.swing.JCheckBox();
        bg2.add(jCheckBox4);
        jCheckBox5 = new javax.swing.JCheckBox();
        bg2.add(jCheckBox5);
        jCheckBox6 = new javax.swing.JCheckBox();
        bg2.add(jCheckBox6);
        jCheckBox7 = new javax.swing.JCheckBox();
        bg2.add(jCheckBox7);
        jLabel8 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        demoDateField3 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("Nom");

        jCheckBox1.setText("Mr");
        bg.add(jCheckBox1);

        jCheckBox2.setText("Mme");
        bg.add(jCheckBox2);

        jCheckBox3.setText("Mlle");
        bg.add(jCheckBox3);

        DocumentFilter filter = new UppercaseDocumentFilter();
        ((AbstractDocument) jTextField1.getDocument()).setDocumentFilter(filter);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });

        jLabel2.setText("Prénoms");

        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jLabel4.setText("Photo");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/blank.png"))); // NOI18N
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton1.setText("Choisir la photo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Date de naissance");

        jLabel7.setText("Lieu de naissance");

        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });

        demoDateField1.setYearDigitsAmount(4);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField2))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCheckBox1)
                                .addComponent(jLabel1)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jCheckBox2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jCheckBox3))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGap(38, 38, 38)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton1)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(35, 35, 35)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(demoDateField1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(demoDateField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel12.setText("Tel");

        jLabel13.setText("Email");

        jTextField9.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField9FocusLost(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField9KeyTyped(evt);
            }
        });

        jLabel14.setText("Type d'adhesion");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Epargne", "Tontine", "Epargne & Tontine" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jCheckBox4.setText("Marié(e)");

        jCheckBox5.setText("Célibataire");

        jCheckBox6.setText("Divorcé(e)");

        jCheckBox7.setText("Veuf(ve)");

        jLabel8.setText("Nationalité");

        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });

        jLabel9.setText("Profession");

        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });

        jLabel10.setText("Adresse");

        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });

        jLabel11.setText("Quartier");

        jTextField7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField7FocusLost(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "National", "International" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

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

        jLabel20.setText("");

        jLabel21.setText("Num Carnet:");

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel10))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jCheckBox4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox7))
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addGap(0, 13, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel21))
                        .addGap(56, 56, 56)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField7)
                                .addComponent(jTextField4)
                                .addComponent(jTextField9)
                                .addComponent(jComboBox1, 0, 173, Short.MAX_VALUE)
                                .addComponent(jTextField5)
                                .addComponent(jTextField6))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jFormattedTextField3))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(35, 35, 35))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel15.setText("Versements initiaux");

        jLabel16.setText("Droits d'adhésion en frcs");

        jTextField10.setText("2500");
        jFormattedTextField3.setEnabled(false);
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
        ((AbstractDocument) jTextField10.getDocument()).setDocumentFilter(numericFilter);
        ((AbstractDocument) jTextField11.getDocument()).setDocumentFilter(numericFilter);
        ((AbstractDocument) jTextField12.getDocument()).setDocumentFilter(numericFilter);

        jTextField10.getDocument().addDocumentListener(documentListener);
        jTextField11.getDocument().addDocumentListener(documentListener);
        jTextField12.getDocument().addDocumentListener(documentListener);

        jLabel17.setText("Part sociale");

        jLabel18.setText("Dépôt");

        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jLabel19.setText("TOTAL");

        jTextField13.setText("2500");

        jLabel22.setText("Date d'adhésion");

        jLabel23.setText("Epargne:");

        jLabel24.setText("Tontine:");

        demoDateField2.setYearDigitsAmount(4);

        demoDateField3.setYearDigitsAmount(4);
        demoDateField3.setEnabled(false);
        demoDateField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(18, 18, 18)
                                .addComponent(demoDateField3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(demoDateField2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(demoDateField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel17)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18)
                                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel19)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23)))
                            .addComponent(demoDateField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel24)))
                .addContainerGap(114, Short.MAX_VALUE))
        );

        jButton2.setText("Enregistrer");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(556, 556, 556)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(118, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private static boolean isAllUpper(String s){
        for(char c:s.toCharArray()){
            if(Character.isLetter(c) && Character.isLowerCase(c)){
                return false;
          }
        }
        return true;
    }
    
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
    
    private static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
    
     private void performSummation(java.awt.event.ActionEvent evt) {
        int total = 0;
        if(jTextField10.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField10.getText());
            }catch(NumberFormatException nbx){
            }
        }

        if(jTextField11.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField11.getText());
            }catch(NumberFormatException nbx){
            }
        }
        
        if(jTextField12.getText().trim().length() > 0){
            try{
                total += Integer.parseInt(jTextField12.getText());
            }catch(NumberFormatException nbx){
            }
        }

       

        jTextField13.setText(""+total);

    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Emailvalidator emailValidator = new Emailvalidator();
        
        // Validate carnet num
      
        if(jComboBox1.getSelectedIndex()==0) {
        try {
                  res1 = verifynumcarn(0);
                 
             } catch (SQLException ex) {
                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
             }
        } else if (jComboBox1.getSelectedIndex()==1) {
         try {
                  res2 = verifynumcarn(1);
               
             } catch (SQLException ex) {
                 Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        // TODO add your handling code here:
        if(jTextField1.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Nom");
        } else if (! isAllUpper(jTextField1.getText())) {
            JOptionPane.showMessageDialog(this, "Nom tout en majuscules");
        } else if(jTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ prénom");
//        } else if(jFormattedTextField1.getText().equalsIgnoreCase("dd/MM/yyyy")){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ date de naissance");
//        } else if(!jFormattedTextField1.isValid()) {
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ date de naissance au format dd/MM/yyyy");
//        } else if (demoDateField1.getDate()==null){
//              JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ date de naissance");
//        } else if (jTextField3.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ lieu de naissance");
//        } 
        } else if (! jCheckBox4.isSelected() && ! jCheckBox5.isSelected() && ! jCheckBox6.isSelected() && ! jCheckBox7.isSelected()){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la situation matrimoniale");
//        }else if (jTextField4.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner la nationalité");
//        } else if (jTextField5.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner la profession");
//        
//        } else if (jTextField6.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner l'adresse");
//        }  
//            else if (jTextField7.getText().isEmpty()){
//            JOptionPane.showMessageDialog(this, "Veuillez renseigner le quartier");
//        } else if(jTextField8.getText().equalsIgnoreCase("(+IND-)NN-NN-NN-NN")){
//             JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ téléphone");
        }else if (jFormattedTextField2.getText().equalsIgnoreCase("  -  -  -  ") || jFormattedTextField2.getText().equalsIgnoreCase("+") ){
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le champ Téléphone");
        } else if (! jTextField9.getText().isEmpty() &&  !emailValidator.validate(jTextField9.getText())){
            JOptionPane.showMessageDialog(this, "L'email indiqué n'est pas valide");
//        } else if (!isDouble(jTextField10.getText())){
//            JOptionPane.showMessageDialog(this, "Les droits d'adhésion doivent être des chiffres");
//        } else if (!isDouble(jTextField11.getText())){
//            JOptionPane.showMessageDialog(this, "La part sociale doit être des chiffres");
//        } else if (!isDouble(jTextField12.getText())){
//            JOptionPane.showMessageDialog(this, "Le dépôt initial doit être des chiffres");
        } else if (jTextField10.getText().isEmpty()){
              JOptionPane.showMessageDialog(this, "Veuillez renseigner les droits d'adhésion");    
//        } else if (jTextField11.getText().isEmpty()){
//              JOptionPane.showMessageDialog(this, "Veuillez renseigner la part sociale");    
//        
//        } 
       } else if (jFormattedTextField1.getText().equalsIgnoreCase("0000") || jFormattedTextField3.getText().equalsIgnoreCase("0000")) { 
            
         JOptionPane.showMessageDialog(this, "Numéro de carnet 0000 invalide");
        } else if (jComboBox1.getSelectedIndex()==0 && jFormattedTextField1.getText().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Numéro de carnet Epargne invalide");   
        }else if (jComboBox1.getSelectedIndex()==1 && jFormattedTextField3.getText().isEmpty()) { 
           JOptionPane.showMessageDialog(this, "Numéro de carnet Tontine invalide");   
        } else if (jComboBox1.getSelectedIndex()==2 && (jFormattedTextField1.getText().isEmpty() || jFormattedTextField3.getText().isEmpty())){
           JOptionPane.showMessageDialog(this, "Numéro de carnet invalide"); 
        } else if(jComboBox1.getSelectedIndex()==0 && res1==true) {
           JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé");  
        }else if(jComboBox1.getSelectedIndex()==1 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if(jComboBox1.getSelectedIndex()==2 && res1==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet épargne est déjà utilisé"); 
        } else if(jComboBox1.getSelectedIndex()==2 && res2==true) {    
            JOptionPane.showMessageDialog(this, "Ce numéro carnet tontine est déjà utilisé"); 
        } else if (demoDateField2.getDate()==null && (jComboBox1.getSelectedIndex()==0 ||  jComboBox1.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Epargne"); 
        } else if (demoDateField3.getDate()==null && (jComboBox1.getSelectedIndex()==1 ||  jComboBox1.getSelectedIndex()==2)) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'adhésion Tontine"); 
        } else if(! jCheckBox1.isSelected() && ! jCheckBox2.isSelected() && ! jCheckBox3.isSelected()){
            JOptionPane.showMessageDialog(this, "Veuillez choisir un titre de civilité");

        } else {
                           String numcarn="";
                           if(jComboBox1.getSelectedIndex()==0) {
                               numcarn=jFormattedTextField1.getText();
                           } else if (jComboBox1.getSelectedIndex()==1){
                               numcarn=jFormattedTextField3.getText();
                           } else {
                                numcarn=jFormattedTextField1.getText() +"-"+jFormattedTextField3.getText();
                           }
            String sql01="SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + jTextField1.getText() + "' AND lower(Prenoms)= '"+jTextField2.getText().toLowerCase(Locale.FRENCH)+ "'";
            Statement stmt = null;
            connect=Connect.ConnectDb();
            boolean inserted=false;
            ResultSet rs2=null;
            boolean success= true;
             try {
                   stmt= connect.createStatement();
                   rs2=stmt.executeQuery(sql01);
              } catch (SQLException ex) {
                  Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
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
            // everything is correct-insert data into database 
            if (! inserted) {
          
             String sexe="femme";
             String situation_matri = null;
             if (jCheckBox1.isSelected())
                 sexe="homme";
             if (jCheckBox4.isSelected()) {
                 situation_matri="mariage";
             } else if (jCheckBox5.isSelected()){
                 situation_matri="célibat";
             } else if (jCheckBox6.isSelected()){
                 situation_matri="divorce";
             } else if (jCheckBox7.isSelected()){
                 situation_matri="veuvage";
             }
             // part sociale 
             int partsociale=0;
            if(!jTextField12.getText().isEmpty())
                partsociale=Integer.parseInt(jTextField12.getText());
             // Dates à insérer
            java.sql.Date sqlDate=null;
            java.sql.Date sqlDate2=null;
            java.sql.Date sqlDate3=null;
            if (demoDateField1.getDate()!=null) sqlDate=new java.sql.Date(demoDateField1.getDate().getTime());
            if (demoDateField2.getDate()!=null)  sqlDate2=new java.sql.Date(demoDateField2.getDate().getTime());
            if (demoDateField3.getDate()!=null) sqlDate3=new java.sql.Date(demoDateField3.getDate().getTime());
             
             
//             String datenaiss=jFormattedTextField1.getText();
//             DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            try {
//                datenais= formatter.parse(datenaiss);
//                sqlDate= new java.sql.Date(datenais.getTime());
//            } catch (ParseException ex) {
//                Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
//            }
//             sqlDate=new java.sql.Date(demoDateField1.getDate().getTime());
             
            
             String sql = "INSERT INTO Profil_adulte " + 
                     "VALUES ("+ null +", '"+ jTextField1.getText()+ "', '"+ jTextField2.getText() +"', '"+ sexe+"', '"+adulte_photourl;
             if(sqlDate==null) {
                 sql=sql+"', null, '" +situation_matri+"', '"+jTextField4.getText()+"', '"+jTextField3.getText()+"', '"+jTextField5.getText()+"', '"+jTextField6.getText()+ "', '"+jTextField7.getText()+"', '"+jFormattedTextField2.getText()+"', '"+jTextField9.getText()+"', '"+String.valueOf(jComboBox1.getSelectedItem())+ "', '"+numcarn+"', '"+ Integer.valueOf(jTextField10.getText())+ "', '"+partsociale;
             } else if(sqlDate !=null) {
                 sql=sql+"', '"+sqlDate +"', '" +situation_matri+"', '"+jTextField4.getText()+"', '"+jTextField3.getText()+"', '"+jTextField5.getText()+"', '"+jTextField6.getText()+ "', '"+jTextField7.getText()+"', '"+jFormattedTextField2.getText()+"', '"+jTextField9.getText()+"', '"+String.valueOf(jComboBox1.getSelectedItem())+ "', '"+numcarn+"', '"+ Integer.valueOf(jTextField10.getText())+ "', '"+partsociale; 
             }
             
              if (sqlDate2==null){
                         sql=sql+"', null,";
                     } else if (sqlDate2!=null){
                          sql=sql+"','"+sqlDate2+"',";
                     } 
                     
                     if (sqlDate3==null){ 
                         sql=sql+"null, 1"+");";
                     } else if (sqlDate3!=null){   
                         sql=sql+"'"+sqlDate3+"', 1);";
                     }  
            int generatdkey = 0;
            System.out.println("adulte_photo_url"+ adulte_photourl);
            System.out.println(sql);
            try {  
                pst=connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                
                pst.execute();
                 ResultSet rst= pst.getGeneratedKeys();
                  generatdkey=0;
                       if(rst.next()){
                           generatdkey= rst.getInt(1);
                       }
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Erreur d'enregistrement");
                success=false;
            
            }
            
             if(!jTextField12.getText().isEmpty() && jTextField12.isEnabled()) {
                             java.util.Date dt = new java.util.Date();
                 java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 String currentTime = sdf.format(dt);
                 String sql002 = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"Dépot initial"+"', '"+jTextField12.getText()+"', '"+generatdkey+"', '"+"Adulte"+"');";
                try {                  
                    pst=connect.prepareStatement(sql002);
                    pst.execute();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur d'enregistrement du dépot initial");
                    Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                try {
                    pst.close();
                    connect.close();
                    if (rs2!=null) rs2.close();
                    if (stmt!=null) stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
                }
                             
          
            if (success) {
            JOptionPane.showMessageDialog(null, "Enregistrement effectué avec succès");
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
         //   jFormattedTextField1.setText("dd/MM/yyyy");
            demoDateField1.setDate(null);
            jTextField5.setText("");
            jTextField6.setText("");
            jTextField7.setText("");
          //  jTextField8.setText("(+IND-)NN-NN-NN-NN");
            jFormattedTextField2.setText("");
            jTextField9.setText("");
            jTextField10.setText("");
            jTextField11.setText("");
            jTextField12.setText("");
            jTextField13.setText("");
            jFormattedTextField1.setText("");
            jFormattedTextField3.setText("");
            demoDateField2.setDate(null);
            demoDateField3.setDate(null);
            demoDateField1.setDate(null);
            this.main.refresh();
                    }
            
            }}        
            
        
    }//GEN-LAST:event_jButton2ActionPerformed
   
            
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        adulte_photourl=chooser.getSelectedFile().getCanonicalPath().replace("\\", "\\\\");
                        System.out.println(chooser.getSelectedFile().getCanonicalPath());
                    } catch (IOException ex) {
                        Logger.getLogger(Adhesion_adulte.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ;
                }
                
           if (adulte_photourl!= null && !adulte_photourl.equals("")) {
            try {
                Image temp = new ImageIcon(adulte_photourl).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                jLabel5.setIcon(new ImageIcon(temp));
                //JLabel picture = new JLabel(new ImageIcon(temp));
                //jLabel1.setBounds(250, 20, 180, 180);
                //jPanel1.add(picture);
                System.out.println(adulte_photourl);
            } catch (Exception e) {
            }
        } else {
            try {
                Image temp = new ImageIcon(
                        getClass().getResource("blank.png")).getImage().getScaledInstance(180, 180,
                        Image.SCALE_SMOOTH);
                //JLabel picture = new JLabel(new ImageIcon(temp));
                  jLabel5.setIcon(new ImageIcon(temp));
               // jLabel5.setBounds(250, 20, 180, 180);
               // jPanel1.add(picture);
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        jTextField1.setText(jTextField1.getText().trim());
        jTextField1.setText(jTextField1.getText().replaceAll("\\s+", " "));
        
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        // TODO add your handling code here:
    if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
        evt.consume();
    } else if (jTextField2.getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else if(jTextField2.getText().endsWith(" ") ){
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else{
         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
    }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        // TODO add your handling code here:
        jTextField2.setText(jTextField2.getText().trim());
        jTextField2.setText(jTextField2.getText().replaceAll("\\s+", " "));
    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
        jTextField3.setText(jTextField3.getText().trim());
        jTextField3.setText(jTextField3.getText().replaceAll("\\s+", " "));
        jTextField3.setText(WordUtils.capitalizeFully(jTextField3.getText()));
    }//GEN-LAST:event_jTextField3FocusLost

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField5FocusLost
        // TODO add your handling code here:
        jTextField5.setText(jTextField5.getText().trim());
        jTextField5.setText(jTextField5.getText().replaceAll("\\s+", " "));
        jTextField5.setText(WordUtils.capitalizeFully(jTextField5.getText()));
    }//GEN-LAST:event_jTextField5FocusLost

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        // TODO add your handling code here:
        jTextField4.setText(jTextField4.getText().trim());
        jTextField4.setText(jTextField4.getText().replaceAll("\\s+", " "));
        jTextField4.setText(WordUtils.capitalizeFully(jTextField4.getText()));
    }//GEN-LAST:event_jTextField4FocusLost

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField6FocusLost
        // TODO add your handling code here:
        jTextField6.setText(jTextField6.getText().trim());
        jTextField6.setText(jTextField6.getText().replaceAll("\\s+", " "));
        jTextField6.setText(WordUtils.capitalizeFully(jTextField6.getText()));
    }//GEN-LAST:event_jTextField6FocusLost

    private void jTextField7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField7FocusLost
        // TODO add your handling code here:
        jTextField7.setText(jTextField7.getText().trim());
        jTextField7.setText(jTextField7.getText().replaceAll("\\s+", " "));
        jTextField7.setText(WordUtils.capitalizeFully(jTextField7.getText()));
    }//GEN-LAST:event_jTextField7FocusLost

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
         if (jComboBox2.getSelectedIndex()==0){
             try {
                 System.out.println(jFormattedTextField2.getDocument());
          jFormattedTextField2.setDocument(new javax.swing.text.PlainDocument());       
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##-##-##-##")));
                } catch (java.text.ParseException ex) {
                 ex.printStackTrace();
                
         }
        }else {
       
          System.out.println(jFormattedTextField2.getDocument());
           jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory());  
          
           jFormattedTextField2.setDocument(new PhoneDocument());
            jFormattedTextField2.setText("+");
            
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jFormattedTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusGained
        // TODO add your handling code here:
           jLabel20.setText("");
        
        if (jComboBox2.getSelectedIndex()==1) {
           
              
             if (jFormattedTextField2.getText().equalsIgnoreCase("+")){
                  System.out.println("trueequal");
                    jFormattedTextField2.setDocument(new PhoneDocument());
                  jFormattedTextField2.setText("+");
             } else {
                 String before =jFormattedTextField2.getText();
                 before=before.replaceAll("\\s", "");
                 jFormattedTextField2.setText(before);
                 
             }
        }
    }//GEN-LAST:event_jFormattedTextField2FocusGained

    private void jFormattedTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextField2FocusLost
        // TODO add your handling code here:
          if (jComboBox2.getSelectedIndex()==1){  //international
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber;
        try {
            phoneNumber = phoneUtil.parse(jFormattedTextField2.getText(), "");
            if (!phoneUtil.isValidNumber(phoneNumber)){
                jLabel20.setText("Le numéro n'est pas valide");
                jLabel20.setForeground(Color.red);                                             // set color to red 
            }
            
            String formatted = phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            jFormattedTextField2.setDocument(new javax.swing.text.PlainDocument());
            jFormattedTextField2.setText(formatted);
        } catch (NumberParseException ex) {
            jLabel20.setText("Le numéro n'est pas valide");
            jLabel20.setForeground(Color.red); 
            Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }

            
        }
    }//GEN-LAST:event_jFormattedTextField2FocusLost

    private void jTextField9KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyTyped
        // TODO add your handling code here:
          jTextField9.setText(jTextField9.getText().trim());
    }//GEN-LAST:event_jTextField9KeyTyped

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
         if (jComboBox1.getSelectedIndex()==0){
            jTextField10.setText("2500");
            jFormattedTextField3.setText("");
            jFormattedTextField3.setEnabled(false);
           
            jFormattedTextField1.setEnabled(true);
            jFormattedTextField1.setText("");
            jTextField12.setEnabled(true);
            demoDateField3.setDate(null);
            demoDateField3.setEnabled(false);
            demoDateField2.setDate(null);
            demoDateField2.setEnabled(true);
        } else if (jComboBox1.getSelectedIndex()==1){
             jTextField10.setText("500");
             jFormattedTextField1.setText("");
             jFormattedTextField1.setEnabled(false);
             jFormattedTextField3.setEnabled(true);
             jFormattedTextField3.setText("");
             jTextField12.setEnabled(false);
             demoDateField2.setDate(null);
             demoDateField2.setEnabled(false);
             demoDateField3.setDate(null);
             demoDateField3.setEnabled(true);
        } else {
             jTextField10.setText("3000");
            jFormattedTextField1.setEnabled(true);
            jFormattedTextField1.setText("");
            jFormattedTextField3.setEnabled(true);
            jFormattedTextField3.setText("");
            jTextField12.setEnabled(true);
             demoDateField2.setDate(null);
             demoDateField2.setEnabled(true);
             demoDateField3.setDate(null);
             demoDateField3.setEnabled(true);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void demoDateField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demoDateField3ActionPerformed

    private void jTextField9FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField9FocusLost
        // TODO add your handling code here:
         jTextField9.setText(jTextField9.getText().trim());
    }//GEN-LAST:event_jTextField9FocusLost

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

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
            java.util.logging.Logger.getLogger(Adhesion_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Adhesion_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Adhesion_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Adhesion_adulte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Adhesion_adulte(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
