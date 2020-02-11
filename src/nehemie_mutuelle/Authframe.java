/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author elommarcarnold
 */
public class Authframe extends javax.swing.JFrame implements ListSelectionListener {

    private Connection conn;
    private int NBUSERLIMIT=0;
    private static final int PASSWORD_LENGTH=6;
    private Vector<Vector<Object>> data;
    private String oldidValue="";
    private String oldPass="";
    private int position = -1;
    Boolean alledit =true;

    /**
     * Creates new form Authframe
     */
    public Authframe() {
        initComponents();
        setTitle("Paramètres utilisateur");
    }
    
    public byte[] getEncryptedPassword(String password, byte[] salt)
   throws NoSuchAlgorithmException, InvalidKeySpecException {
  // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
  // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
  String algorithm = "PBKDF2WithHmacSHA1";
  // SHA-1 generates 160 bit hashes, so that's what makes sense here
  int derivedKeyLength = 160;
  // Pick an iteration count that works for you. The NIST recommends at
  // least 1,000 iterations:
  // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
  // iOS 4.x reportedly uses 10,000:
  // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
  int iterations = 20000;

  KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

  SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

  return f.generateSecret(spec).getEncoded();
 }

 public byte[] generateSalt() throws NoSuchAlgorithmException {
  // VERY important to use SecureRandom instead of just Random
  SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

  // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
  byte[] salt = new byte[8];
  random.nextBytes(salt);

  return salt;
 }
 
    
    public boolean checkloginunicity (String login) throws SQLException {
        PreparedStatement pre;
        conn = Connect.ConnectDb();
        String sql0="SELECT * FROM Login WHERE Username = '"+login+"'";
        pre= conn.prepareStatement(sql0);
        ResultSet rst = pre.executeQuery();
        boolean result=true;
        
        if (rst.next()) result=false;
        
        
        conn.close ();
        rst.close();
        pre.close();
        return result;
         
         
    }
    
    
    public int check_password(String password) {
        if (password.length() < PASSWORD_LENGTH) return 1;
        boolean hasdigit= false;
        for (int i=0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch >= '0' && ch <= '9') {
               if (hasdigit == false) hasdigit = true;
            }
            
            
            
            
            
        } 
        
        if (hasdigit == false) return 2;
        
        return 0;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int row = jTable1.getSelectedRow();
        if (row == position) jButton2.setEnabled(true);
        else jButton2.setEnabled(false);
    }
    
    class PasswordCellRenderer extends DefaultTableCellRenderer {
        private static final String ASTERISKS="**************";
        
        public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
            int length=0;
            if(arg1 instanceof String) {
                length= ((String) arg1).length();
                
            } else if (arg1 instanceof char[]) {
                length= ((char[]) arg1).length;     
            } 
          
            setText(asterisks(length));
            return this;        
        }
        private String asterisks(int length) {
             if (length > ASTERISKS.length()) {
                 StringBuilder sb = new StringBuilder(length);
                 for (int i=0; i<length;i++) {
                     sb.append('*');
                 }
                 return sb.toString();
                 
             } else {
                      return ASTERISKS.substring(0, length);
                         }
                 }
             }
        
        
        
     
    public Vector getLoggers() throws Exception {
        conn = Connect.ConnectDb();
        PreparedStatement pre;
        pre = conn.prepareStatement("SELECT * FROM Login");
        ResultSet rs = pre.executeQuery();
       
        if (! Login.connectedUserSurname.equalsIgnoreCase("NEHEMIE")) {
            
            alledit =false;
            
            
        }
        
        String login = Login.connectedlogin;
        int i = 0;
       
        Vector<Vector> membreVector = new Vector<Vector>();
        while (rs.next()) {
            Vector<Object> membre = new Vector<Object>();
           // membre.add(i);
            membre.add(rs.getString("Nom"));
            membre.add(rs.getString("Prenom"));
            membre.add(rs.getString("Username"));
            if (rs.getString("Username").equalsIgnoreCase(login)) position =i;
            Random rand=new Random();
            final int max=8;
            final int min=6;
            
            // Code à améliorer 
            String psswrdstring= String.format("%1$"+(rand.nextInt((max-min)+1)+min)+"s","****");
            psswrdstring = psswrdstring.replace(" ", "*");
            membre.add(psswrdstring);
            membre.add(false);
            //
            membreVector.add(membre);
            i++;
        }

        /*Close the connection after use (MUST)*/
        if (conn != null) {
            conn.close();
        }
        if (pre != null) {
            pre.close();
        }
        if (rs != null) {
            rs.close();
        }
        return membreVector;
    }
    
    
public static Object[][] to2DimArray(Vector v) {
        Object[][] out = new Object[v.size()][0];
        for (int i = 0; i < out.length; i++) {
            out[i] = ((Vector) v.get(i)).toArray();
        }
        return out;
    } 
   
    
// Code collé bêtement 
class CWDocumentUpperCaseText extends PlainDocument {
  
 public CWDocumentUpperCaseText() {
 }
  
 public void insertString(int ofst, String str, AttributeSet a) throws BadLocationException {
         if (str == null) return;
  
         if (!(isSizeValid(ofst))) {
             return;
         }
  
         char[] upper = str.toCharArray();
         StringBuffer sbuf = new StringBuffer(str.length());
  
         for (int i = 0; i < upper.length; i++) {
             if (isCharValid(upper[i], ofst + i)) {
                 sbuf.append(processChar(upper[i]));
             }
         }
  
         super.insertString(ofst, sbuf.toString(), a);
     }
  
 public boolean isSizeValid( int ofst){return true;}
 public char processChar( char ch){return  Character.toUpperCase(ch);}
 public boolean isCharValid( char ch, int ofst){return true;}// this class allows any characters after they get uppercased
}
  
 //Editor to change case depending on a condition
 class ChangeUpperCaseEditor extends AbstractCellEditor implements TableCellEditor {
  
    private JTextField textField = new JTextField();
  
    /**
 * Gets the tableCellEditorComponent attribute of the
 * ChangeUpperCaseEditor object
 */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                                                     int column) {        
  
                if((table.getRowCount()%2 == 0)){
                //Force to uppercase
                textField.setDocument( new CWDocumentUpperCaseText());
                }
                return   textField;
        }                                                                                                                                                                      
  
        /**
 * Gets the cellEditorValue attribute of the PasswordFieldEditor
 * object
 * 
 * @return The cellEditorValue value
 */
      public Object getCellEditorValue() {
           return textField;
      }
    }// end of ChangeUpperCaseEditor
    
 
 
 private void FirstnameKeyTyped(java.awt.event.KeyEvent evt) {                                     
        // TODO add your handling code here:
     if (!(Character.isAlphabetic(evt.getKeyChar()) || evt.getKeyChar() == '-' ) && !Character.isSpaceChar(evt.getKeyChar())) {
        evt.consume();
    } else if (((JTextField) evt.getComponent()).getText().trim().length() == 0 ) {            //&& Character.isLowerCase(evt.getKeyChar())
            evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else if(((JTextField) evt.getComponent()).getText().endsWith(" ") ){
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }else{
         evt.setKeyChar(Character.toLowerCase(evt.getKeyChar()));
    }
    }
 
 //Editor to change case depending on a condition
 class FirstUppercaseEditor extends AbstractCellEditor implements TableCellEditor {
  
    private JTextField textField = new JTextField();
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                                                     int column) {        
  
                if((table.getRowCount()%2 == 0)){
                //Force to uppercase
                textField.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyTyped(java.awt.event.KeyEvent evt) {
                  FirstnameKeyTyped(evt);
                }
});
                }
                return   textField;
        }                                                                                                                                                                      
  
        /**
 * Gets the cellEditorValue attribute of the PasswordFieldEditor
 * object
 * 
 * @return The cellEditorValue value
 */
      public Object getCellEditorValue() {
           return textField;
      }
    }//  FirstUppercaseEditor
    
    
    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        try {
            // TODO add your handling code here:
            data=getLoggers();
        } catch (Exception ex) {
            Logger.getLogger(Authframe
                .class.getName()).log(Level.SEVERE, null, ex);
        }
        Object[][] out = to2DimArray(data);

        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //jTable4.setFillsViewportHeight(true);

        //sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable4.getModel());
        //jTable4.setRowSorter(sorter);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            out,
            new String [] {
                "Noms", "Prénoms", "Identifiant", "Mot de passe", "newinsert"
            }
        )
        {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            Class[] types = {String.class, String.class, String.class,
                String.class, Boolean.class};

            @Override
            public Class getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (alledit) return true;
                else {
                    if (rowIndex != position) return true;
                    else return false;
                }
            }}
        );
        jTable1.getColumn ("Mot de passe").setCellRenderer(new PasswordCellRenderer());
        jTable1.getColumnModel().getColumn(4).setMinWidth(0);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(0);
        JTextField textField = new JTextField();
        JTextField textField2 = new JTextField();
        textField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FirstnameKeyTyped(evt);
            }});
            textField.setDocument(new CWDocumentUpperCaseText());
            DefaultCellEditor dce1 = new DefaultCellEditor( textField );
            DefaultCellEditor dce2 = new DefaultCellEditor(textField2);
            jTable1.getColumnModel().getColumn(0).setCellEditor(dce1);
            jTable1.getColumnModel().getColumn(1).setCellEditor(dce2);
            Action action = new AbstractAction () {
                public void ActionPerformed(ActionEvent e ){

                }

                @Override
                public void actionPerformed(ActionEvent e) {

                    TableCellListener tcl = (TableCellListener) e.getSource();
                    int column = tcl.getColumn();
                    if (column == 2) {
                        int row = tcl.getRow();
                        oldidValue= (String) tcl.getOldValue();
                    }

                    if (column==3) {
                        oldPass = (String)  tcl.getOldValue();
                    }
                }
            };
            TableCellListener tcl = new TableCellListener (jTable1, action);
            jTable1.setRowHeight(25);
            jScrollPane1.setViewportView(jTable1);

            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/add-icon.png"))); // NOI18N
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
            if(!Login.connectedlogin.equalsIgnoreCase("nehemie")) jButton1.setEnabled(false);

            jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/save-icon.png"))); // NOI18N
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/delete_symbol.png"))); // NOI18N
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });
            if(!Login.connectedlogin.equalsIgnoreCase("nehemie")) jButton3.setEnabled(false);

            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/useraccount.png"))); // NOI18N

            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 792, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(18, 18, 18)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(layout.createSequentialGroup()
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(layout.createSequentialGroup()
                            .add(87, 87, 87)
                            .add(jButton1)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(jButton2)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                            .add(jButton3)
                            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(layout.createSequentialGroup()
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 37, Short.MAX_VALUE)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 384, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(52, 52, 52))))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.addRow(new Object [] {"Column 1", "Column 2", "Column 3", "", true});
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(jTable1.getSelectedRow()== -1) {
             JOptionPane.showMessageDialog(this, "Veuillez selectionner l'utilisateur à supprimer");
        } else {
             DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
             model.removeRow(jTable1.getSelectedRow());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        if(jTable1.getSelectedRow()== -1) {
             JOptionPane.showMessageDialog(this, "Veuillez selectionner l'utilisateur à enregistrer");
        } else {
            // Check validity
            if ( ((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 0))).isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Veuillez renseigner un nom");
            } else if(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 2))).isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Veuillez renseigner un identifiant");
            } else if (((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 2))).trim().length() < 5) {
                  JOptionPane.showMessageDialog(this, "L'identifiant doit etre de 5 caractères au moins");
            } else { 
               if((boolean) jTable1.getValueAt(jTable1.getSelectedRow(), 4) == true) {  // it is a new
                    boolean loginuni=true;
                    try {
                       loginuni = checkloginunicity(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 2))).trim());
                    } catch (SQLException ex) {
                       Logger.getLogger(Authframe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     if (loginuni == false){
                        JOptionPane.showMessageDialog(this, "Cet identifiant est déjà utilisé");
                     } else {
                         
                        
                         if ((check_password(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 3))).trim())) == 1) {
                             JOptionPane.showMessageDialog(this, "Le mot de passe doit avoir au moins "+PASSWORD_LENGTH+ " caractères");
                         } else if ((check_password(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 3))).trim())) == 2) {
                             JOptionPane.showMessageDialog(this, "Le mot de passe doit contenir au moins un chiffre");

                         }  else {   // Bon mot de passe donc insertion
                             
                               conn = Connect.ConnectDb();
                               boolean success=true;
                               PreparedStatement pst=null;
                               String sql = "INSERT INTO Login(idLogin,Username, Salt, Password, Nom, Prenom) VALUES(?,?,?,?,?,?)";
                               byte[] salt = new byte[8];
                               try {
                                 salt=this.generateSalt();
                               } catch (NoSuchAlgorithmException ex) {
                                 Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                               }       
                            
                  // byte []data = {-71, 51, -42, -107, -45, 15, -78, 62};
                               byte[] encryptedAttemptedPassword=null;
             
                              try {
                                  encryptedAttemptedPassword=getEncryptedPassword(((String) jTable1.getValueAt(jTable1.getSelectedRow(), 3)).trim(), salt);
                                  for (int i=0; i<encryptedAttemptedPassword.length; i++)
                                   System.out.println("pass"+encryptedAttemptedPassword[i]);
                
                                } catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                                    success = false;
                                } catch (InvalidKeySpecException ex) {
                                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                                    success =false;
                                }
        
                    try {
                          pst = conn.prepareStatement(sql);
                          pst.setString(1, null);
                          pst.setString(2, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 2)));
                          pst.setBinaryStream(3,new ByteArrayInputStream(salt),salt.length);
                          pst.setBinaryStream(4,new ByteArrayInputStream(encryptedAttemptedPassword),encryptedAttemptedPassword.length);
                          pst.setString(5, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0)));
if (jTable1.getValueAt(jTable1.getSelectedRow(), 1) != null) pst.setString(6, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1)));


                   } catch (SQLException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                          success =false;
                   }

                    try {
                         int  rowsaffected = pst.executeUpdate();
          
                    } catch (SQLException ex) {
                         Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                         success = false;
                    }
                    
                    
                    if (success) JOptionPane.showMessageDialog(this, "Insertion effectué avec succès");
                    else JOptionPane.showMessageDialog(this, "Erreur d'insertion"); 
        
                             
                              
                             try {
                                 conn.close();
                                 pst.close(); 
                                 
                             } catch (SQLException ex) {
                                 Logger.getLogger(Authframe.class.getName()).log(Level.SEVERE, null, ex);
                             }
                  
                             
                             
                             
                         }
                             
                     }
                 
            
                
               }else {     // It is not a new 
                   
                    boolean loginuni=true;
                    
                    try {
                       loginuni = checkloginunicity(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 2))).trim());
                    } catch (SQLException ex) {
                       Logger.getLogger(Authframe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     if ( oldidValue.equalsIgnoreCase((String) jTable1.getValueAt(jTable1.getSelectedRow(), 2)) && loginuni == false){
                        JOptionPane.showMessageDialog(this, "Cet identifiant est déjà utilisé");
                     } else {
                          Boolean error = false;
                          if (!oldPass.isEmpty()) {
                             System.out.println("oldpass"+oldPass);
                         if ((check_password(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 3))).trim())) == 1) {
                              error = true;
                             JOptionPane.showMessageDialog(this, "Le mot de passe doit avoir au moins "+PASSWORD_LENGTH+ " caractères");
                         } else if ((check_password(((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 3))).trim())) == 2) {
                             error = true;
                             JOptionPane.showMessageDialog(this, "Le mot de passe doit contenir au moins un chiffre");
                         }} 
                          
                           if (error == false) {
                             
                               Boolean success = true;
                               conn = Connect.ConnectDb();
                               byte[] salt = new byte[8];
                               try {
                                 salt=this.generateSalt();
                               } catch (NoSuchAlgorithmException ex) {
                                 success = false;  
                                 Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                               }       
                            
   
                               byte[] encryptedAttemptedPassword=null;
             
                              try {
                                  encryptedAttemptedPassword=getEncryptedPassword(((String) jTable1.getValueAt(jTable1.getSelectedRow(), 3)).trim(), salt);
                                  for (int i=0; i<encryptedAttemptedPassword.length; i++)
                                   System.out.println("pass"+encryptedAttemptedPassword[i]);
                
                                } catch (NoSuchAlgorithmException ex) {
                                    success= false;
                                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                                   
                                } catch (InvalidKeySpecException ex) {
                                    success = false;
                                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                             
                                }
                              
                                // Modification 
                              System.out.println("oldvalue"+this.oldidValue);
                              if (oldidValue.isEmpty()) 
                                  oldidValue = ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 2)).trim();
                              String sql = "UPDATE Login SET Username = ?, Salt = ?, Password = ?, Nom = ?, Prenom = ? WHERE Username = ?"   ;
                              PreparedStatement pst=null;
                             try {
                                 pst = conn.prepareStatement(sql);
                                 pst.setString(1, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 2)).trim());
                                 pst.setBinaryStream(2,new ByteArrayInputStream(salt),salt.length);
                                 pst.setBinaryStream(3,new ByteArrayInputStream(encryptedAttemptedPassword),encryptedAttemptedPassword.length);
                                 pst.setString(4, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0)).trim());
                                 if (jTable1.getValueAt(jTable1.getSelectedRow(), 1) != null) pst.setString(5, ((String) jTable1.getValueAt(jTable1.getSelectedRow(), 1)).trim());
                                 pst.setString(6, oldidValue);
                                 
                                 pst.executeUpdate();


                             } catch (SQLException ex) {
                                 success = false; 
                                 Logger.getLogger(Authframe.class.getName()).log(Level.SEVERE, null, ex);
                             }
                             
                             
                             if (success == true) {
                                JOptionPane.showMessageDialog(this, "Modifications effectués avec succès");

                             } else {
                                JOptionPane.showMessageDialog(this, "Les mises à jour n'ont pas pu été faites");

                             }
                             
                             
                             
                             try {
                                 conn.close();
                                 if(pst != null) pst.close();
                             } catch (SQLException ex) {
                                 Logger.getLogger(Authframe.class.getName()).log(Level.SEVERE, null, ex);
                             }
                             
                             
                              
                              
                              
                         }
                   
                   
               }}
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Authframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Authframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Authframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Authframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new Authframe().setVisible(true);
                
               
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}
