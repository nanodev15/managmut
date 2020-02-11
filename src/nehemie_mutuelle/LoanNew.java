/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import java.awt.Color;
import java.awt.Component;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.MaskFormatter;
import org.apache.commons.lang.ArrayUtils;


/**
 *
 * @author elommarcarnold
 */
public class LoanNew extends javax.swing.JFrame {
      private Connection conn;
    /** Creates new form LoanNew */
    private Set listofchoices;  
    private Set listofchoicesnew;  
   // private final double MAXLOAN= 150000;
    private final double MAXLOAN= 3000000;
    private final double MINLOAN= 10000;
    private  Vector<String> vlist;
    private MaskFormatter m;
    Boolean firstentry = false;
    private String eligibitystr;
    PreparedStatement pst2;
    public LoanNew() {
        vlist= new Vector<String> ();
        initComponents();
      
        setTitle("Nouveau dossier de prêt");
    }
    
    public class LstRenderer extends JLabel 
     implements ListCellRenderer {
   //ImageIcon icon; 
   //ImageIcon selectIcon;
   Color selectCouleur = Color.RED;
   public  LstRenderer(){
      //icon = new ImageIcon(getClass().getResource("img1.gif"));
     // selectIcon  = new ImageIcon(getClass().getResource("img2.gif"));
   }
   public Component getListCellRendererComponent(JList list, 
        Object value, // valeur à afficher
        int index, // indice d'item
        boolean isSelected, // l'item est-il sélectionné
        boolean cellHasFocus) // La liste a-t-elle le focus
   {
      String s = value.toString();
      if(s.contains(" ") && !s.equals("--Personnes morales"))
           s=s.substring(0, s.lastIndexOf(" "));
    //  s=s.replace(oldChar, newChar)
      if (isSelected) {
         setBackground(list.getSelectionBackground());
         setForeground(selectCouleur);
       // setText(s+"  "+index);
         setText(s);
        // setIcon(selectIcon);
      }else{
         setBackground(list.getBackground());
         setForeground(list.getForeground());
         setText(s);
      //   setIcon(icon);
      }
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      setOpaque(true);
      return this;
   }
}
   
public boolean checkeligibity (int id, String TypeAdherent) {
    
    
   
    
 return true;   
    
}  



public void addcaution (String ident, Double value) throws SQLException {
    String typeepargnant=""; 
    int id=-1;
    if(ident.contains("enf")) { typeepargnant ="Enfant"; id = Integer.valueOf(ident.substring(ident.indexOf("enf") + 3 , ident.length()));}


    if(ident.contains("adu")) { typeepargnant ="Adulte"; id = Integer.valueOf(ident.substring(ident.indexOf("adu") + 3 , ident.length()));}
    else {  typeepargnant = "Pers Morale"; id = Integer.valueOf(ident.substring(ident.indexOf("perm") + 3 , ident.length()));}
    String sql="";
     java.util.Date dt = new java.util.Date();
         java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String currentTime = sdf.format(dt);
    if (typeepargnant.equalsIgnoreCase("enfant")) {
        
       sql = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"caution prêt numero "+jFormattedTextField1.getText()+"', "+value+", '"+id+"', '"+"Enfant"+"');";
   } else if (typeepargnant.equalsIgnoreCase("adulte")) {
       sql = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"caution prêt numero "+jFormattedTextField1.getText()+"', "+value+", '"+id+"', '"+"Adulte"+"');";

   } else {
       sql = "INSERT INTO Epargne VALUES ("+null+", '"+ currentTime + "', '"+"caution prêt numero "+jFormattedTextField1.getText()+"', "+value+", '"+id+"', '"+"Pers Morale"+"');";

   }
    
    Boolean success =false;
     PreparedStatement pst = null;
    try {
        conn = Connect.ConnectDb();
        pst = conn.prepareStatement(sql);                  
        pst.execute();
    } catch (Exception e) {
        e.printStackTrace();
        
    }
    
    // closing
    conn.close();
    if(pst !=null) pst.close();

            
  
    
    
}

// Méthode à remplir après 
public boolean checkselectedeligibity () {
    
    
   
    
 return true;   
    
} 
public Vector getallchilds()  throws SQLException, ParseException {
     conn = Connect.ConnectDb();
     Vector vect = new Vector <String> ();
     vect.add("--Enfants");
     PreparedStatement prepr = conn.prepareStatement ("SELECT Nom, Prenoms, IdProfil_enfant FROM Profil_enfant ORDER BY Nom, Prenoms");
     ResultSet rs = prepr.executeQuery();
     while(rs.next()) {
          vect.add(rs.getString(1)+" "+rs.getString(2)+ " enf"+rs.getInt(3));
          
     }
     
      if (conn != null) {
            conn.close();
        }
        
       if (prepr != null) {
            prepr.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        
     return vect;
}
public String createSalt() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String ts = String.valueOf(System.currentTimeMillis());
    String rand = UUID.randomUUID().toString();
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    String message = ts + rand;
    digest.update(message.getBytes("utf8"));
    byte[] digestBytes = digest.digest();
    String digestStr = javax.xml.bind.DatatypeConverter.printHexBinary(digestBytes);
    return digestStr.substring(0,6);
}

public String refnumbergen () throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
    SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
    String beginstr="";
    if (firstentry==false) { beginstr="LOAN"+format.format(new Date()); System.out.println("true");} 
    else beginstr="LOAN"+format.format(demoDateField1.getDate());
    int seqid=1;
    // SQL 
    
      conn = Connect.ConnectDb();
     PreparedStatement prepr = conn.prepareStatement ("SELECT Loanrefnum FROM Loan WHERE Loanrefnum LIKE '"+beginstr+"%' ORDER BY Loanrefnum DESC");
     ResultSet rs = prepr.executeQuery();
     
     if (rs.next()) {
         seqid=  Integer.parseInt(Character.toString((rs.getString(1)).charAt(8)));
     }
     
      if (conn != null) {
            conn.close();
        }
        
       if (prepr != null) {
            prepr.close();
        }
         
         if (rs != null) {
            rs.close();
        }
    if (firstentry==false) firstentry = true;
    return beginstr+seqid+createSalt();
}
public Vector getallpersmor()  throws SQLException, ParseException {
     conn = Connect.ConnectDb();
          Vector vect = new Vector <String> ();

     vect.add("--Personnes morales");
     PreparedStatement prepr = conn.prepareStatement ("SELECT Raison_sociale, IdProfil_persmorale FROM Profil_persmorale ORDER BY Raison_sociale");
     ResultSet rs = prepr.executeQuery();
     while(rs.next()) {
          vect.add(rs.getString(1)+" persm"+rs.getInt(2));
          
     }
     
      if (conn != null) {
            conn.close();
        }
        
       if (prepr != null) {
            prepr.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        
     return vect;
}

public Vector getalladults()  throws SQLException, ParseException {
     conn = Connect.ConnectDb();
          Vector vect = new Vector <String> ();

     vect.add("--Adultes");
     PreparedStatement prepr = conn.prepareStatement ("SELECT Noms, Prenoms, IdProfil_adulte FROM Profil_adulte ORDER BY Noms, Prenoms");
     ResultSet rs = prepr.executeQuery();
     while(rs.next()) {
          vect.add(rs.getString(1)+" "+rs.getString(2)+ " adu"+rs.getString(3));
          
     }
     
      if (conn != null) {
            conn.close();
        }
        
       if (prepr != null) {
            prepr.close();
        }
         
         if (rs != null) {
            rs.close();
        }
        
     return vect;
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jXList1 = new org.jdesktop.swingx.JXList();
        jLabel10 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        try {
            m =new MaskFormatter("***************");
            m.setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
            m.setPlaceholderCharacter('-');
        } catch (Exception e) {

        }
        jFormattedTextField1 = new javax.swing.JFormattedTextField(m);
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        ((AbstractDocument) jTextField3.getDocument()).setDocumentFilter(numericFilter);
        ((AbstractDocument) jTextField4.getDocument()).setDocumentFilter(numericFilter);
        jTextField5 = new javax.swing.JTextField();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        demoDateField1 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        demoDateField2 = new com.jp.samples.comp.calendarnew.DemoDateField();
        jLabel18 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel2.setText("Bénéficiaires");

        jRadioButton1.setText("Prêt de groupe");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton2);
        bg.add(jRadioButton1);
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Prêt individuel");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        jLabel1.setText("Effectif");

        try {
            System.out.println("new ref");
        } catch (Exception e) {

        }

        try {
            vlist.addAll(getallchilds());
            vlist.addAll(getalladults());
            vlist.addAll(getallpersmor());
        } catch (Exception e) {

        }

        final Vector<String>  vl = vlist;
        jXList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = vl.toArray(new String[vl.size()]);// { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jXList1.setCellRenderer(new LstRenderer());
        jXList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jXList1ValueChanged(evt);
            }
        });
        jXList1.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jXList1);

        jLabel10.setText("Type de prêt");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Production agricole", "Activités commerciales & études" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel7.setText("Type de données");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Données précalculés", "Remboursement libre", "Remboursement au terme" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel5.setText("0 Bénéficiaire");

        jLabel14.setText("Responsable du groupe");

        jComboBox3.setEnabled(false);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconval.png"))); // NOI18N
        jToggleButton1.setEnabled(false);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 341, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(46, 46, 46))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel10)
                            .add(jLabel7)
                            .add(jLabel14))
                        .add(104, 104, 104)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jToggleButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jRadioButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(45, 45, 45)
                                .add(jRadioButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(0, 0, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(jComboBox3, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBox1, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, jComboBox2, 0, 1, Short.MAX_VALUE))
                                .add(119, 119, 119))))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jRadioButton1)
                    .add(jLabel1)
                    .add(jRadioButton2))
                .add(42, 42, 42)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLabel5))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(3, 3, 3)
                        .add(jToggleButton1)))
                .add(30, 30, 30)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel10))
                .add(32, 32, 32)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel7)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(jComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21))
        );

        jTabbedPane1.addTab("Données 1/2", jPanel2);

        jLabel3.setText("Frais de dossier (total)");

        jLabel8.setText("Montant de l'emprunt");

        jLabel4.setText("Frais de suivi (total)");

        jLabel6.setText("Caution");

        jLabel9.setText("Responsable du dossier");

        jLabel11.setText("Objet du prêt");

        jScrollPane2.setViewportView(jTextPane1);

        jLabel12.setText("Numéro de dossier:");

        try {

            jFormattedTextField1.setText(refnumbergen());
        } catch (Exception e) {

        }
        jFormattedTextField1.setEnabled(false);

        ((AbstractDocument) jTextField2.getDocument()).setDocumentFilter(numericFilter);

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        ((AbstractDocument) jTextField5.getDocument()).setDocumentFilter(numericFilter);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Payé en espèces", "Débité", " " }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Payé en espèces", "Débité" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Payé en espèces", "Débité" }));

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel11)
                    .add(jLabel9)
                    .add(jLabel8)
                    .add(jLabel4)
                    .add(jLabel3)
                    .add(jLabel12)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 54, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(jTextField1)
                    .add(jScrollPane2)
                    .add(jFormattedTextField1)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField2)
                            .add(jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField4)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField3))
                        .add(18, 18, 18)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jComboBox4, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jComboBox5, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jComboBox6, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .add(51, 51, 51))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jLabel3)
                        .add(jComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jTextField4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(22, 22, 22)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jComboBox6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel6))
                .add(25, 25, 25)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel9)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel11)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel12)
                    .add(jFormattedTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Données 2/2", jPanel3);

        jLabel13.setText("Date d'origine du prêt:");

        demoDateField1.setDate(new Date());
        demoDateField1.setYearDigitsAmount(4);
        demoDateField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField1ActionPerformed(evt);
            }
        });

        jLabel15.setText("Notes");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        jLabel16.setText("Date de fin ");
        jLabel16.setVisible(false);

        demoDateField2.setDate(new Date());
        demoDateField2.setYearDigitsAmount(4);
        demoDateField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demoDateField2ActionPerformed(evt);
            }
        });
        demoDateField2.setVisible(false);

        jLabel18.setText("Intérêts");

        ((AbstractDocument) jTextField6.getDocument()).setDocumentFilter(numericFilter);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(38, 38, 38)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel13)
                    .add(jLabel15)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jLabel18)
                        .add(jLabel16)))
                .add(96, 96, 96)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                    .add(jTextField6))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(22, 22, 22)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(demoDateField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13))
                .add(9, 9, 9)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel16)
                    .add(demoDateField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel15)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 193, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 47, Short.MAX_VALUE)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel18)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(34, 34, 34))
        );

        jTextField6.setEnabled(false);

        jTabbedPane1.addTab("Données 3/3", jPanel4);

        jButton1.setText("Valider");
        jTabbedPane1.setEnabledAt(1, false);
        jTabbedPane1.setEnabledAt(2, false);
        jButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jButton1ItemStateChanged(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator1)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 636, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 456, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap())
        );

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/dossierpret.png"))); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 702, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(16, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 535, Short.MAX_VALUE))
            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(40, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 521, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        // TODO add your handling code here:   
     jComboBox3.removeAllItems();  
     if(jXList1.getSelectedIndices().length > 1) jXList1.setSelectedIndex((jXList1.getSelectedIndices()[0]));
     if (jRadioButton2.isSelected()){
         jXList1.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
         jComboBox3.setEnabled(false);
     } else {
         jComboBox3.setEnabled(true);
     }  
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        // TODO add your handling code here:
        
     if (jRadioButton1.isSelected()){ jXList1.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
     jComboBox3.removeAllItems();
    
     
     
     }
     jToggleButton1.setEnabled(true);
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jXList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jXList1ValueChanged
        // TODO add your handling code here:
        
        
      if (!evt.getValueIsAdjusting()) {
        if (jXList1.getSelectedValue() != null) {
        if(((String) jXList1.getSelectedValue()).equals("--Enfants") || ((String) jXList1.getSelectedValue()).equals("--Adultes") || ((String) jXList1.getSelectedValue()).equals("--Personnes morales")) {
            System.out.println("value"+jXList1.getSelectedValue());
            jXList1.removeSelectionInterval(jXList1.getSelectedIndex(), jXList1.getSelectedIndex());
        }
        

     
         if(listofchoices != null) {
               int newint=-4;
               listofchoicesnew =  new TreeSet(Arrays.asList(ArrayUtils.toObject(jXList1.getSelectedIndices()))); 
               listofchoicesnew.removeAll(listofchoices);
               
               for (Object indic: listofchoicesnew) {
                   newint= (Integer) indic;
               }
           if(newint !=-4) {
           if(((String) jXList1.getElementAt(newint)).equals("--Enfants") || ((String) jXList1.getElementAt(newint)).equals("--Adultes") || ((String) jXList1.getElementAt(newint)).equals("--Personnes morales")) {
          
            jXList1.removeSelectionInterval(newint, newint);
        }}
             
         }

        }
       
        
       

        jLabel5.setText(String.valueOf(jXList1.getSelectedValuesList().size()) + " Bénéficiaire");
        if (jXList1.getSelectedValuesList().size() > 1) jLabel5.setText(jLabel5.getText() + "s");
       
        
        listofchoices = new TreeSet(Arrays.asList(ArrayUtils.toObject(jXList1.getSelectedIndices()))); 
        }
        
       
        if (jXList1.getSelectedValuesList().size()==0) { jTabbedPane1.setEnabledAt(1, false);  jTabbedPane1.setEnabledAt(2, false); }
        else {  jTabbedPane1.setEnabledAt(1, true);  jTabbedPane1.setEnabledAt(2, true);   } 
       
    }//GEN-LAST:event_jXList1ValueChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.out.println("I am in loan new");
        // validation des données 
        if (jTextField2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le montant de l'emprunt");
        } else if (Double.parseDouble(jTextField2.getText()) > MAXLOAN )  {
            JOptionPane.showMessageDialog(this, "Montant supérieur au montant autorisé");
        } else if ((Double.parseDouble(jTextField2.getText()) < MINLOAN )) {
            JOptionPane.showMessageDialog(this, "Montant inférieur au minimum autorisé");
        } else if (jTextField3.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner les frais de suivi");
        } else if (jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner les frais de dossier");
        } else if (jTextField5.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le montant de la caution");
        } else if (jTextField5.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le montant de la caution");
        } else if (jTextField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner le responsable du dossier");
        } else if (jFormattedTextField1.getText().isEmpty()) {
           JOptionPane.showMessageDialog(this, "Veuillez renseigner la reference du dossier");
        } else if (demoDateField1.getDate()== null) {
           JOptionPane.showMessageDialog(this, "Veuillez renseigner la date d'origine du prêt");

        } else if (jRadioButton1.isSelected() && (jComboBox3.getSelectedItem() == null || ((String)jComboBox3.getSelectedItem()).isEmpty())){
         
           JOptionPane.showMessageDialog(this, "Veuillez sélectionner le responsable de groupe");
 
        }else if(checkselectedeligibity()== false) {
          JOptionPane.showMessageDialog(this, eligibitystr);  
            
        } else {
            // insert loan in database
           
            int typecaution=1;
            int typepret= jComboBox2.getSelectedIndex();
            int typedonnees= jComboBox1.getSelectedIndex();
            int typefraissuivi=jComboBox4.getSelectedIndex();
            int typefraisdossier=jComboBox5.getSelectedIndex();    
            String respgro=(String) jComboBox3.getSelectedItem();
            List lst = jXList1.getSelectedValuesList();
            String beneficiaires="";
            for (Object str: lst) {
               // beneficiaires += ((String) str).substring(((String) str).lastIndexOf("renseigner la date d'origine du prêt ")+1, ((String) str).length());
                beneficiaires += ((String) str).substring(((String) str).lastIndexOf(" ")+1, ((String) str).length());

                beneficiaires += ",";      
            }
            
            beneficiaires=beneficiaires.substring(0, beneficiaires.length()-1);
            System.out.println("beneficiares"+beneficiaires);
           boolean success = true; 
           if(jComboBox6.getSelectedIndex()==0) typecaution =0;
           else typecaution =1;
           String sql="INSERT INTO Loan VALUES ("+null+", '"+jFormattedTextField1.getText()+"', '"+Double.valueOf(jTextField2.getText())+"', '"+Double.valueOf(jTextField5.getText())+"', '"+typecaution+"', '"+typepret+"', '"+ typedonnees+"', '"+ beneficiaires+"', '"+Double.valueOf(jTextField3.getText())+"', '"+Double.valueOf(jTextField4.getText())+"', '"+jTextField1.getText()+"', '"+new java.sql.Timestamp(demoDateField1.getDate().getTime())+"', '"+jTextPane1.getText()+ "', '"+typefraissuivi+"', '"+typefraisdossier+"', '"+jTextArea1.getText()+"', '"+respgro+"'" +");"; 
          
           
            try {
                conn= Connect.ConnectDb();
                pst2=conn.prepareStatement(sql);
                pst2.execute();
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                success= false;
            }
            
        //    if (sucess=true) {JOptionPane.showMessageDialog(this, "");
            
            // Close things
            try {
                pst2.close(); 
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Arrived here");
            if (jComboBox1.getSelectedIndex()==0) {   // remboursement pré-calculé
                System.out.println("case 1");
                this.dispose();
                nehemie_mutuelle.loan.Test loantest = new nehemie_mutuelle.loan.Test(jFormattedTextField1.getText(), 0);          
                String [] args= {""};
                try {
                    loantest.main(args);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (typecaution == 1) {
                    // retrait du compte, il faut que l'utilisateur le verifie
                    
                    String name = (String) jComboBox3.getSelectedItem();
                    String identifiants="";
                    //look in vect s.substring(s.lastIndexOf(",") + 1).trim());
                    
                    for (int i = 0; i < vlist.size(); i++)
                        if (((String) vlist.get(i)).contains(name)) identifiants = ((String) vlist.get(i)).substring(((String) vlist.get(i)).lastIndexOf(" ")).trim();
                    
                    
                    System.out.println("identifiants"+identifiants);
                    try {
                        addcaution(identifiants, Double.valueOf(jTextField5.getText().trim()));
                    } catch (SQLException ex) {
                        Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                }
        
        } else if(jComboBox1.getSelectedIndex()==1) {   // Remboursement libre
                this.dispose();
                System.out.println("case 2");
                Double interets=0.0;
                if(jTextField6.getText()!=null && !jTextField6.getText().isEmpty()) {
                    interets= Double.valueOf(jTextField6.getText());
                }
                String sql8="INSERT INTO loanreclibre VALUES("+null+", '"+jFormattedTextField1.getText()+"', '"+Double.valueOf(jTextField2.getText())+"',  '"+ (Double.valueOf(jTextField6.getText())-interets) +"', 'En cours', '"+new java.sql.Date(new Date().getTime())+ "', '"+new java.sql.Date(demoDateField2.getDate().getTime())+"');";
                System.out.println(sql8);
                try {
                conn= Connect.ConnectDb();
                pst2=conn.prepareStatement(sql8);
                pst2.execute();
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                success= false;
            }
            
            if (success=true) JOptionPane.showMessageDialog(this, "Ajout effectué avec succès");
            
            // Close things
            try {
                pst2.close(); 
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (typecaution == 1) {
                    // retrait du compte, il faut que l'utilisateur le verifie
                    
                    String name = (String) jComboBox3.getSelectedItem();
                    String identifiants="";
                    //look in vect s.substring(s.lastIndexOf(",") + 1).trim());
                    
                    for (int i = 0; i < vlist.size(); i++)
                        if (((String) vlist.get(i)).contains(name)) identifiants = ((String) vlist.get(i)).substring(((String) vlist.get(i)).lastIndexOf(" ")).trim();
                    
                    
                    System.out.println("identifiants"+identifiants);
                    try {
                        addcaution(identifiants, Double.valueOf(jTextField5.getText().trim()));
                    } catch (SQLException ex) {
                        Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            
        } else if (jComboBox1.getSelectedIndex()==2) {  // remboursement terme
             this.dispose();
                System.out.println("case 2");
                Double interets=0.0;
                if(jTextField6.getText()!=null && !jTextField6.getText().isEmpty()) {
                    interets= Double.valueOf(jTextField6.getText());
                }
                String sql8="INSERT INTO loanrecterme VALUES("+null+", '"+jFormattedTextField1.getText()+"', '"+Double.valueOf(jTextField2.getText())+"',  '"+ (Double.valueOf(jTextField6.getText())-interets) +"', 'En cours', '"+new java.sql.Date(new Date().getTime())+ "', '"+new java.sql.Date(demoDateField2.getDate().getTime())+"' )";
                try {
                conn= Connect.ConnectDb();
                pst2=conn.prepareStatement(sql8);
                pst2.execute();
                } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                success= false;
                }
            
        //    if (sucess=true) {JOptionPane.showMessageDialog(this, "");
            
            // Close things
            try {
                pst2.close(); 
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (typecaution == 1) {
                    // retrait du compte, il faut que l'utilisateur le verifie
                    
                    String name = (String) jComboBox3.getSelectedItem();
                    String identifiants="";
                    //look in vect s.substring(s.lastIndexOf(",") + 1).trim());
                    
                    for (int i = 0; i < vlist.size(); i++)
                        if (((String) vlist.get(i)).contains(name)) identifiants = ((String) vlist.get(i)).substring(((String) vlist.get(i)).lastIndexOf(" ")).trim();
                    
                    
                    System.out.println("identifiants"+identifiants);
                    try {
                        addcaution(identifiants, Double.valueOf(jTextField5.getText().trim()));
                    } catch (SQLException ex) {
                        Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            
            
            
        }
         if (success) { // afficher loan manageer
                try {
                     loanmanager ln = new loanmanager();
                     ln.setLocationRelativeTo(null);
                     ln.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                     ln.setVisible(true); 
                } catch (Exception ex) {
                    Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
                }
             
         }
        
        
        
        
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton1.isSelected()) {
            
    
        jComboBox3.removeAllItems();
        for (int i=0; i <jXList1.getSelectedValuesList().size(); i++){
            jComboBox3.addItem(((String) jXList1.getSelectedValuesList().get(i)).substring(0, ((String) jXList1.getSelectedValuesList().get(i)).lastIndexOf(" ") ));
        }
        } else {
             jComboBox3.removeAllItems();
        }
        
        
        
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        
        if(jComboBox1.getSelectedIndex()==2 || jComboBox1.getSelectedIndex()==1){
            jLabel16.setVisible(true);
            demoDateField2.setVisible(true);
             jLabel18.setVisible(true);
             jTextField6.setVisible(true);
             jTextField6.setText("0");
        }else {
            jLabel16.setVisible(false);
            demoDateField2.setVisible(false);
            jLabel18.setVisible(false);
             jTextField6.setVisible(false);
             jTextField6.setText("0");

        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jButton1ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ItemStateChanged

    private void demoDateField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demoDateField2ActionPerformed

    private void demoDateField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demoDateField1ActionPerformed
        if(firstentry != false) {
            try {
                // TODO add your handling code here:
                jFormattedTextField1.setText(refnumbergen());
            } catch (SQLException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(LoanNew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_demoDateField1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ItemStateChanged
    
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
            java.util.logging.Logger.getLogger(LoanNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoanNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoanNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoanNew.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoanNew().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField1;
    private com.jp.samples.comp.calendarnew.DemoDateField demoDateField2;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JFormattedTextField jFormattedTextField1;
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
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private org.jdesktop.swingx.JXList jXList1;
    // End of variables declaration//GEN-END:variables

}
