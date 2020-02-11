/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nehemie_mutuelle;

import com.jp.samples.comp.calendarnew.DemoDateField;
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import nehemie_mutuelle.loan.CreditPanelAutom1;
import org.apache.commons.lang3.time.DateUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 *
 * @author elommarcarnold
 */
public class loanmanager extends javax.swing.JFrame{
    private JTable fixed;
    private Vector<Vector> data;
    int fixedColumns= 2;
    Connection connect = null;
    private String openloanref;
    private Date beginDate;
    private int periodicite; 
    private int nbPeriod;
    private BigDecimal monthlypay;
    private BigDecimal montant;
    private BigDecimal totalsummont;
    private Double nominalrate;
    private TableRowSorter<DefaultTableModel> sorter;
    private List<String> orderlist=  Arrays.asList("Espèce", "Chèque", "Virement"); 
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private lnmangersum mangersumgrid;
    private String connectedString = Login.connectedUserfirstName+" "+Login.connectedUserSurname;
    private String respdoss="";
    private Date dateOr;
    private SimpleDateFormat format; 
    private String status="";
    
    
      
    public class YMDRenderer extends DefaultTableCellRenderer {
        private SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/YYYY");
        public void setValue(Object value){
            System.out.println("Entering YMDrenderr");
           try {
                if  (value !=null) {
                    value= sdf.format(value); }
           }
           
           catch (IllegalArgumentException e) {
                e.printStackTrace();
           }
                
               super.setValue(value);
        }
    }
    
    
  class DynamicIcon implements Icon {
    private double percentage;
    public int getIconWidth() {
      return 40;
    }

    public int getIconHeight() {
      return 40;
    }
    
    public DynamicIcon(double percentage){
        this.percentage = percentage;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    //  g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
          
    double degree = 360 * this.percentage;
    double sz = Math.min(100, 100);
    double cx = 10 + 100  * .5;
    double cy = 10  + 100 * .5;
    double or = sz * .5;
    double ir = or * .5; //or - 20;
    Shape inner = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
    Shape outer = new Arc2D.Double(
        cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);
    Area area = new Area(outer);
    area.subtract(new Area(inner));
   
    g2.fill(area);
    g2.dispose();
    }
  }
    
    
    // ProgprogressBar.getPercentComplete();ress bar UI  
  class ProgressCircleUI extends BasicProgressBarUI {
  @Override public Dimension getPreferredSize(JComponent c) {
    Dimension d = super.getPreferredSize(c);
    int v = Math.max(d.width, d.height);
    d.setSize(v, v);
    return d;
  }
  @Override public void paint(Graphics g, JComponent c) {
    Insets b = progressBar.getInsets(); // area for border
    int barRectWidth  = progressBar.getWidth()  - b.right - b.left;
    int barRectHeight = progressBar.getHeight() - b.top - b.bottom;
    if (barRectWidth <= 0 || barRectHeight <= 0) {
      return;
    }

    // draw the cells
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setPaint(progressBar.getForeground());
    double degree = 360 * progressBar.getPercentComplete();
    double sz = Math.min(barRectWidth, barRectHeight);
    double cx = b.left + barRectWidth  * .5;
    double cy = b.top  + barRectHeight * .5;
    double or = sz * .5;
    double ir = or * .5; //or - 20;
    Shape inner = new Ellipse2D.Double(cx - ir, cy - ir, ir * 2, ir * 2);
    Shape outer = new Arc2D.Double(
        cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);
    Area area = new Area(outer);
    area.subtract(new Area(inner));
    g2.fill(area);
    g2.dispose();

     //Deal with possible text painting
    if (progressBar.isStringPainted()) {
      paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
    }
  }
}
    
    
    
    /** Creates new form loanmanager */
    public loanmanager() throws Exception {
        initComponents();
        
        for (int i = 1; i < 4; i++)  jTabbedPane4.setEnabledAt(i, false);
      //  jTabbedPane4.setEnabledAt(WIDTH, rootPaneCheckingEnabled);
        
        // 
        
  
            JFormattedTextField tf =new JFormattedTextField();
           // tf.setColumns(10);            
            try {
                     MaskFormatter dateMask = new MaskFormatter("##/##/####");
                   dateMask.install(tf);
                   
            } catch(ParseException ex) {     
            Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);       
            } 
            
            jTable1.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(tf) {
//                   public void tableChanged(TableModelEvent e) throws ParseException {
//                        Date dat =df.parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7));
//                        jTable1.setValueAt(dat, jTable1.getSelectedRow(), 7);
//                   }
            });
            
            format = new SimpleDateFormat("dd-MM-yyyy");

    }
    
 public class CustomComboBoxEditor extends DefaultCellEditor {

  // Declare a model that is used for adding the elements to the `Combo box`
  private DefaultComboBoxModel model;
  List <String> orderList;

  public CustomComboBoxEditor(List <String> orderList) {
      super(new JComboBox());
      this.model = (DefaultComboBoxModel)((JComboBox)getComponent()).getModel();
      this.orderList=orderList;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      // Add the elements which you want to the model.
      // Here I am adding elements from the orderList(say) which you can pass via constructor to this class.
      if(model.getSize() !=3) {
      for (int i=0; i< orderList.size(); i++)
            model.addElement(orderList.get(i));
      }
      //finally return the component.
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
  } 

        public void tableChanged(TableModelEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
 
 
 
 
public class InputNumTextFieldEditor extends DefaultCellEditor {
 private Number n;
  // Declare a model that is used for adding the elements to the `Combo box`
  //private DefaultComboBoxModel model;
  //List <String> orderList;

  public InputNumTextFieldEditor(JTextField jT) {
      super(jT);
    
  }
  
  @Override
    public Object getCellEditorValue() {
        JTextField ftf = (JTextField)getComponent();
        Object o = ftf.getText();
        
        if (((String) o).equals("")) {return "0";}
        return Double.valueOf((String) o).doubleValue();
 
}
     
    


  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      // Add the elements which you want to the model.
      // Here I am adding elements from the orderList(say) which you can pass via constructor to this class.
 
      //finally return the component.
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
  } 
} 

public class DateFieldEditor extends DefaultCellEditor {

  // Declare a model that is used for adding the elements to the `Combo box`
  //private DefaultComboBoxModel model;
  //List <String> orderList;

  public DateFieldEditor(JFormattedTextField jft) {
      super(jft);
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      // Add the elements which you want to the model.
      // Here I am adding elements from the orderList(say) which you can pass via constructor to this class.
 
      //finally return the component.
      return super.getTableCellEditorComponent(table, value, isSelected, row, column);
  } 
}



public class DateCellRenderer extends DefaultTableCellRenderer {
    
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,  int row, int column) {
        System.out.println("entered");
     //   super.getTableCellRendererComponent(table, value, isSelected, isSelected, row, column);
        if ((value !=null) && (value instanceof Date)) {
            String strDate = df.format((Date)value);
            System.out.println("yes it is a date");
            this.setText(strDate);
        } else { 
            System.out.println("it is not a date ");
            this.setText(" ");        
        }
        return this;
    }
            
      // Add the elements which you want to the model.
      // Here I am adding elements from the orderList(say) which you can pass via constructor to this class.
 
      //finally return the component.
}
    public void UpdateLoans (String loanref) throws Exception {
                System.out.println("opened loan toutcours"+loanref);

        this.openloanref=loanref;
        this.getEpargne();
        jTabbedPane4.setEnabledAt(1, true);
        jTabbedPane4.setEnabledAt(2, false);
        jTabbedPane4.setEnabledAt(3, false);
        
    }
    
     public void UpdateLoansfree (String loanref) throws Exception {
                 System.out.println("opened loan free ref"+loanref);

        this.openloanref=loanref;
        this.getfreeloandata();
        this.getEpargne2();
        jTabbedPane4.setEnabledAt(1, false);
        jTabbedPane4.setEnabledAt(2, true);
        jTabbedPane4.setEnabledAt(3, false);     
    }
     
     
    public void UpdateLoansterme (String loanref) throws Exception {
        this.openloanref=loanref;
        System.out.println("opened loan terme ref"+loanref);
        this.getfreelontermdata();
        this.getEpargne3();
        jTabbedPane4.setEnabledAt(1, false);
        jTabbedPane4.setEnabledAt(2, false);
        jTabbedPane4.setEnabledAt(3, true);     
    }
     
    public lnmangersum  getlnmangersum () {
        return this.mangersumgrid;
    }
    
    
    
    
public void getEpargne2() throws Exception {  // getLoan remboursements libre
         // Updatefrontpage
DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

symbols.setGroupingSeparator(' ');
formatter.setDecimalFormatSymbols(symbols);
//this.mangersumgrid.getButton(2).setIcon();
         // First get List of ID payed
         boolean existed=false;
         connect= Connect.ConnectDb();
         String sql0="SELECT * FROM rembours_libre WHERE idloan='"+this.openloanref+"' ";
         PreparedStatement pre = connect.prepareStatement(sql0);
         ResultSet rs = pre.executeQuery();
         List<Map<String, Object>> list = new ArrayList<>();
    //     List<Integer> listids =new ArrayList<>();
         if (rs.next()) {
             existed = true;
             ResultSetMetaData md = rs.getMetaData();
             int columns = md.getColumnCount();
             
             Map<String, Object> row = new HashMap<>(columns);
             for (int i = 1; i <= columns; ++i) {
            row.put(md.getColumnName(i), rs.getObject(i));
          
            }
        list.add(row);
        }
         
         
       while (rs.next()) {
         ResultSetMetaData md = rs.getMetaData();
         int columns = md.getColumnCount();    
        Map<String, Object> row = new HashMap<>(columns);
        for (int i = 1; i <= columns; ++i) {
            row.put(md.getColumnName(i), rs.getObject(i));
           
        }
        list.add(row);
        }

       // setting params
    
      
         // Remove data from old table
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        
        String sum="";
        System.out.println("This is the value of montant"+montant);
        if (montant != null) {sum= formatter.format((double) montant.doubleValue());}       
       // Grid updating
         this.mangersumgrid.getButton(0).setText("<html>Numéro dossier: <br>"+"<font color='#ff00000'>"+openloanref+"</font></html>");
         this.mangersumgrid.getButton(1).setText("<html>Montant emprunté: <br>"+"<font color='#ff0000'>"+sum+" XOF"+"</font></html>");
         if (totalsummont == null) totalsummont = new BigDecimal(0);
         double perc = (totalsummont.doubleValue()/montant.doubleValue())*100;
         String rounded = String.format("%.00f", perc);
         this.mangersumgrid.getButton(2).setText("<html>Pourcentage remboursé: <br>"+"<font color='#ff0000'>"+rounded+"</font></html>");
    
        
        
      // l.setBorder(border);
      
       
       //  this.mangersumgrid.getButton(2).setVerticalAlignment(SwingConstants.CENTER);
      //   this.mangersumgrid.getButton(2).setIcon(icon);
         
   
         this.mangersumgrid.getButton(3).setText("<html>Responsable du dossier: <br>"+"<font color='#ff00000'>"+respdoss+"</font></html>"); 
         this.mangersumgrid.getButton(4).setText("");

       
}
     
     
     
    
//       public static Object[][] to2DimArray(Vector v) {
//        Object[][] out = new Object[v.size()][0];
//        for (int i = 0; i < out.length; i++) {
//            out[i] = ((Vector) v.get(i)).toArray();
//        }
//        return out;
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jSplitPane1.addPropertyChangeListener("dividerLocation", new PropertyChangeListener()
            {
                @Override
                public void propertyChange(PropertyChangeEvent e)
                {
                    int location = ((Integer)e.getNewValue()).intValue();
                    System.out.println(location);

                    if (location > 150)
                    {
                        JSplitPane splitPane = (JSplitPane)e.getSource();
                        splitPane.setDividerLocation( 150 );
                    }
                }
            });
            jPanel10 = new javax.swing.JPanel();
            mangersumgrid = new lnmangersum(0);
            jPanel1 = mangersumgrid;
            jPanel9 = new javax.swing.JPanel();
            jScrollPane1 = new javax.swing.JScrollPane();
            jTable1 = new javax.swing.JTable() {
                public boolean getScrollableTracksViewportWidth(){
                    return getPreferredSize().width < getParent().getWidth();
                }
            };
            jButton4 = new javax.swing.JButton();
            jButton5 = new javax.swing.JButton();
            jButton14 = new javax.swing.JButton();
            jPanel3 = new javax.swing.JPanel();
            jScrollPane2 = new javax.swing.JScrollPane();
            jTable2 = new javax.swing.JTable();
            jButton6 = new javax.swing.JButton();
            jButton7 = new javax.swing.JButton();
            jButton8 = new javax.swing.JButton();
            jButton9 = new javax.swing.JButton();
            jPanel4 = new javax.swing.JPanel();
            jScrollPane3 = new javax.swing.JScrollPane();
            jTable3 = new javax.swing.JTable();
            jButton10 = new javax.swing.JButton();
            jButton11 = new javax.swing.JButton();
            jButton12 = new javax.swing.JButton();
            jButton13 = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setResizable(false);

            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/loan2.png"))); // NOI18N

            jPanel2.setBackground(new java.awt.Color(191, 212, 196));
            jPanel2.setPreferredSize(new java.awt.Dimension(150, 546));

            jButton1.setFont(new java.awt.Font("Trebuchet MS", 3, 12)); // NOI18N
            jButton1.setText("Nouveau Dossier");
            jButton1.setBackground(new Color(0x2dce98));
            jButton1.setForeground(Color.black);

            // customize the button with your own look
            jButton1.setUI(new StyledButtonUI());
            jButton1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            jButton2.setFont(new java.awt.Font("Trebuchet MS", 3, 12)); // NOI18N
            jButton2.setText("Ouvrir un Dossier");
            jButton2.setBackground(new Color(0x2dce98));
            jButton2.setForeground(Color.black);
            // customize the button with your own look
            jButton2.setUI(new StyledButtonUI());
            jButton2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jButton3.setFont(new java.awt.Font("Trebuchet MS", 3, 12)); // NOI18N
            jButton3.setText("Echéances à payer");
            jButton3.setBackground(new Color(0x2dce98));
            jButton3.setForeground(Color.black);
            // customize the button with your own look
            jButton3.setUI(new StyledButtonUI());
            jButton3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

            org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jButton3)
                        .add(jButton1)
                        .add(jButton2))
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(22, 22, 22)
                    .add(jButton1)
                    .add(18, 18, 18)
                    .add(jButton2)
                    .add(18, 18, 18)
                    .add(jButton3)
                    .addContainerGap(407, Short.MAX_VALUE))
            );

            jSplitPane1.setLeftComponent(jPanel2);

            jPanel10.setMaximumSize(new java.awt.Dimension(838, 652));
            jPanel10.setLayout(new java.awt.BorderLayout());

            jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
            jPanel1.setMaximumSize(new java.awt.Dimension(838, 652));
            jPanel1.setLayout(new java.awt.GridLayout(1, 0));
            jPanel10.add(jPanel1, java.awt.BorderLayout.CENTER);

            jTabbedPane4.addTab("Résumé", jPanel10);

            jTable1.setAutoResizeMode (JTable.AUTO_RESIZE_OFF);
            jTable1.setPreferredScrollableViewportSize(new Dimension(1000,70));
            jTable1.setFillsViewportHeight(true);
            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null, null, null, null, null, null}
                },
                new String [] {
                    "Num", "Date Echéance", "Capital", "Intérêts", "Echéance", "Pénalités", "Total dû", "Total remboursé", "Type rembours.", "Date rembours.", "Enreg. par", "Saved"
                }
            ){

                Class[] types = {Object.class, Object.class, Object.class, Object.class, Double.class, Double.class, Double.class, Double.class, String.class, String.class, String.class,
                    Boolean.class};
                @Override
                //  public Class getColumnClass(int columnIndex) {
                    //     return this.types[columnIndex];
                    //  }

                public Class getColumnClass(int column)
                {
                    //      for (int row = 0; row < getRowCount(); row++)
                    //      {
                        //          Object o = getValueAt(row, column);

                        //          if (o != null)
                        //          return o.getClass();
                        //        }

                    return types[column];
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    if (column == 0 || column == 1 || column == 2 || column ==3 || column == 4 || column == 6 || column == 10) return false;
                    return true;
                }

            }
        );
        jTable1.getColumnModel().getColumn(0).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(90);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(140);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(130);
        //jTable1.getColumnModel().getColumn(7).setPreferredWidth(130);
        jTable1.getColumnModel().getColumn(11).setWidth(0);
        jTable1.getColumnModel().getColumn(11).setMinWidth(0);
        jTable1.getColumnModel().getColumn(11).setMaxWidth(0);

        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Boolean status = (Boolean) table.getModel().getValueAt(row, 11);
                if (status==true) {
                    setBackground(new Color(173,190,230));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });

        jTable1.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Boolean status = (Boolean) table.getModel().getValueAt(row, 11);
                if (status==true) {
                    setBackground(new Color(173,190,230));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });

        jTable1.setDefaultRenderer(String.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Boolean status = (Boolean) table.getModel().getValueAt(row, 11);
                if (status==true) {
                    setBackground(new Color(173,190,230));
                    setForeground(Color.WHITE);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });

        jTable1.getColumnModel().getColumn(1).setCellRenderer(new YMDRenderer());

        // Fixed jTable operations
        jTable1.setAutoCreateColumnsFromModel(false);
        jTable1.addPropertyChangeListener(new PropertyChangeListener () {

            public void propertyChange(PropertyChangeEvent e)
            {
                //  Keep the fixed table in sync with the main table

                if ("selectionModel".equals(e.getPropertyName()))
                {
                    fixed.setSelectionModel(jTable1.getSelectionModel() );
                }

                if ("model".equals(e.getPropertyName()))
                {
                    fixed.setModel(jTable1.getModel() );
                }
            }
        });  // A modifier
        //  Use the existing table to create a new table sharing
        //  the DataModel and ListSelectionModel
        int totalColumns = jTable1.getColumnCount();
        fixed = new JTable();
        fixed.setAutoCreateColumnsFromModel(false);
        fixed.setModel(jTable1.getModel());
        fixed.setSelectionModel(jTable1.getSelectionModel());
        fixed.setFocusable(false);

        //  Remove the fixed columns from the main table
        //  and add them to the fixed table
        for (int i = 0; i < fixedColumns; i++){
            TableColumnModel columnModel = jTable1.getColumnModel();
            TableColumn column = columnModel.getColumn(0);
            columnModel.removeColumn(column);
            fixed.getColumnModel().addColumn(column);
        }

        //  Add the fixed table to the scroll pane

        fixed.setPreferredScrollableViewportSize(fixed.getPreferredSize());

        // Other

        fixed.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                Boolean status = (Boolean) (((Date) table.getModel().getValueAt(row, 1)).before(new Date()) && ((Boolean) jTable1.getValueAt(row, 9)== false));
                Boolean paidstatus = (Boolean) (((Date) table.getModel().getValueAt(row, 1)).before(new Date()) && ((Boolean) jTable1.getValueAt(row, 9)== true) && (Boolean) ((Double) jTable1.getValueAt(row, 5) < (Double) jTable1.getValueAt(row, 4)));
                Boolean alrepaidstatus = (Boolean) (((Date) table.getModel().getValueAt(row, 1)).before(new Date()) && ((Boolean) jTable1.getValueAt(row, 9)== true) && (Boolean) ((Double) jTable1.getValueAt(row, 5) >= (Double) jTable1.getValueAt(row, 4)));
                if (status==true) {
                    setBackground(new Color(147,58,22));
                    setForeground(Color.WHITE);
                } else {
                    if (paidstatus)
                    setBackground(new Color(0,0,255));
                    else if (alrepaidstatus) {
                        setBackground(new Color(255,255, 0));
                    } else {
                        setBackground(table.getBackground());
                    }
                    setForeground(table.getForeground());
                }
                return this;
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setRowHeaderView(fixed);
        jScrollPane1.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());
        // Synchronize scrolling of the row header with the main table
        jScrollPane1.getRowHeader().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e)
            {
                //  Sync the scroll pane scrollbar with the row header
                JViewport viewport = (JViewport) e.getSource();
                jScrollPane1.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
            }
        });  // A modifier
        sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());

        // Nox jComboBox column
        TableColumn comboCol1 = jTable1.getColumnModel().getColumn(6);
        comboCol1.setCellEditor(new CustomComboBoxEditor(this.orderlist));
        JTextField jt = new JTextField();

        jt.addKeyListener(new KeyListener () {
            public void keyPressed(KeyEvent e) { }
            public void keyReleased(KeyEvent e) { }
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar(); // Get the typed character
                // Don't ignore backspace or delete
                // Don't ignore backspace or delete
                if (c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    // If the key was not a number then discard it (this is a sloppy way to check)
                    if (!(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')) {

                        //if point character typed increase numberofpointcharacters by one
                        if ((c == '.')) {

                            // If the input string already contains a decimal point, don't
                            // do anything to it.
                            if (((JTextField) e.getComponent()).getText().indexOf(".") < 0)
                            {
                                // don't consume it
                            }
                            else
                            {
                                e.consume(); // Ignore this key
                                return;
                            }

                        }
                        else{

                            e.consume(); // Ignore this key
                        }

                    }
                }
            }
        });

        TableColumn col2 = jTable1.getColumnModel().getColumn(3);
        System.out.println("Valeur des colonnes"+(String) col2.getHeaderValue());
        col2.setCellEditor(new InputNumTextFieldEditor (jt));
        TableColumn col3 = jTable1.getColumnModel().getColumn(5);
        col3.setCellEditor(new InputNumTextFieldEditor(jt));

        //col4.setCellEditor(new DefaultCellEditor(jt));
        //System.out.println("arrived here");
        //jTable1.getColumnModel().getColumn(7).setCellRenderer(new DateCellRenderer());
        //jTable1.getColumnModel().getColumn(8).setCellRenderer(new DateCellRenderer());
        //jTable1.getColumnModel().getColumn(6).setCellRenderer(new DateCellRenderer());

        jTable1.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn()==5) {
                    System.out.println("valeur 1 "+jTable1.getValueAt(jTable1.getSelectedRow(), 2));
                    System.out.println("valeur 2 "+jTable1.getValueAt(jTable1.getSelectedRow(), 3));
                    System.out.println("valeur 3 "+jTable1.getValueAt(jTable1.getSelectedRow(), 4));
                    double value1= Double.valueOf((double) (jTable1.getValueAt(jTable1.getSelectedRow(), 2)));
                    double value2 = 0.0;
                    if ((jTable1.getValueAt(jTable1.getSelectedRow(), 3)).getClass() == java.lang.Double.class) {
                        value2 = ((Double) (jTable1.getValueAt(jTable1.getSelectedRow(), 3))).doubleValue();
                    } else {
                        value2=  Double.valueOf((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 3)));
                    }
                    System.out.println("value1"+value1);
                    System.out.println("value2"+value2);
                    jTable1.setValueAt((double) value1+value2, jTable1.getSelectedRow(), 4);
                }
            }
        });

        // Col 4
        TableColumn col4 = jTable1.getColumnModel().getColumn(7);
        JFormattedTextField jft = new JFormattedTextField(df);
        jft.setColumns(10);
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.install(jft);
        } catch (ParseException ex) {
            Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);
        }

        //col4.setCellRenderer(new DateCellRenderer());
        //col4.setCellEditor(new DefaultCellEditor(jft));

        //jTable1.getModel().addTableModelListener(this);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconval.png"))); // NOI18N
        jButton4.setText("Valider");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/delete_symbol.png"))); // NOI18N
        jButton5.setText("Supprimer");
        jButton5.setToolTipText("Supprimer les valeurs");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/Modify-icon.png"))); // NOI18N
        jButton14.setText("Changer le statut");
        jButton14.setToolTipText("Supprimer les valeurs");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 816, Short.MAX_VALUE)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jButton4)
                .add(18, 18, 18)
                .add(jButton5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton14)
                .add(27, 27, 27))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .add(68, 68, 68)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 304, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(44, 44, 44)
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton4)
                    .add(jButton5)
                    .add(jButton14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Remboursements autom", jPanel9);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "Date rembours.", "Montant", "Type rembours.", "Libellé", "Dern. Mod", "Idenr"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getColumnModel().getColumn(6).setWidth(0);
        jTable2.getColumnModel().getColumn(6).setMinWidth(0);
        jTable2.getColumnModel().getColumn(6).setMaxWidth(0);
        jScrollPane2.setViewportView(jTable2);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/add-icon.png"))); // NOI18N
        jButton6.setText("Nouveau");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/Edit.png"))); // NOI18N
        jButton7.setText("Editer");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/delete_symbol.png"))); // NOI18N
        jButton8.setText("Supprimer");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/penalty.png"))); // NOI18N
        jButton9.setText("Penalité");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jButton6)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton8)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton9)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(61, 61, 61)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton6)
                    .add(jButton7)
                    .add(jButton8)
                    .add(jButton9))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Remboursements libres", jPanel3);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "Date remb.", "Montant", "Type remb.", "Libelle", "Dern mod", "idrembours"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.getColumnModel().getColumn(6).setWidth(0);
        jTable3.getColumnModel().getColumn(6).setMinWidth(0);
        jTable3.getColumnModel().getColumn(6).setMaxWidth(0);
        jScrollPane3.setViewportView(jTable3);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/add-icon.png"))); // NOI18N
        jButton10.setText("Nouveau");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/Edit.png"))); // NOI18N
        jButton11.setText("Editer");
        jButton11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jButton11ItemStateChanged(evt);
            }
        });
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/delete_symbol.png"))); // NOI18N
        jButton12.setText("Supprimer");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/penalty.png"))); // NOI18N
        jButton13.setText("Penalités");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(jButton10)
                .add(12, 12, 12)
                .add(jButton11)
                .add(4, 4, 4)
                .add(jButton12)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton13)
                .addContainerGap(377, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane3)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 354, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(61, 61, 61)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton10)
                    .add(jButton11)
                    .add(jButton12)
                    .add(jButton13))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Remboursements à terme", jPanel4);

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jTabbedPane4)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jTabbedPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 546, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel8);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 546, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void getEpargne() throws Exception {  // getLoan remboursements automatiques
         // Updatefrontpage
System.out.println("I am in getEpargne");
DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

symbols.setGroupingSeparator(' ');
formatter.setDecimalFormatSymbols(symbols);


  
  final JSlider width = new JSlider(JSlider.HORIZONTAL, 1, 150, 75);
  final JSlider height = new JSlider(JSlider.VERTICAL, 1, 150, 75);
 // final JLabel dynamicLabel = new JLabel(icon);


  
 
         
         
//         this.mangersumgrid.getButton(5).setText("<html>Date du premier paiement: <br>"+"<font color='#ff0000'>"+"20/12/2014"+"</font></html>");
//         this.mangersumgrid.getButton(6).setText("<html>Dernière échéance: <br>"+"<font color='#ff0000'>"+"20/12/2015"+"</font></html>");


          // test Pie chart
//        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        dataset.addValue(34.0, "Series 1", "Category 1");
//        dataset.addValue(23.0, "Series 1", "Category 2");
//        dataset.addValue(54.0, "Series 1", "Category 3");
//        final JFreeChart chart = ChartFactory.createBarChart(
//            "Bar Chart", 
//            "Category",
//            "Series",
//            dataset,
//            PlotOrientation.VERTICAL,
//            true,
//            true,
//            false
//        );
//        final ChartPanel chartPanel = new ChartPanel(chart);
//        chartPanel.setPreferredSize(new Dimension(200, 100));
//        final JInternalFrame frame = new JInternalFrame("Frame 1", true);
//        frame.getContentPane().add(chartPanel);
         
JProgressBar progressBar;  // Comprendre UI
//Where the GUI is constructed:
progressBar = new JProgressBar(0, 100);
progressBar.setValue(50);
progressBar.setStringPainted(true);
progressBar.setUI(new ProgressCircleUI());
progressBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
progressBar.setStringPainted(true);
progressBar.setFont(progressBar.getFont().deriveFont(24f));
progressBar.setForeground(Color.ORANGE);



//this.mangersumgrid.getButton(2).setIcon();
         // First get List of ID payed
         boolean existed=false;
         connect= Connect.ConnectDb();
         String sql0="SELECT * FROM rembours_auto WHERE idloan='"+this.openloanref+"' ";
         PreparedStatement pre = connect.prepareStatement(sql0);
         ResultSet rs = pre.executeQuery();
         List<Map<String, Object>> list = new ArrayList<>();
         List<Integer> listids =new ArrayList<>();
         if (rs.next()) {
             existed = true;
             ResultSetMetaData md = rs.getMetaData();
             int columns = md.getColumnCount();
             
             Map<String, Object> row = new HashMap<>(columns);
             for (int i = 1; i <= columns; ++i) {
            row.put(md.getColumnName(i), rs.getObject(i));
            if(i==3) listids.add((Integer)rs.getObject(i));
            }
        list.add(row);
        }
         
         
       while (rs.next()) {
         ResultSetMetaData md = rs.getMetaData();
         int columns = md.getColumnCount();    
        Map<String, Object> row = new HashMap<>(columns);
        for (int i = 1; i <= columns; ++i) {
            row.put(md.getColumnName(i), rs.getObject(i));
            if(i==3) listids.add((Integer)rs.getObject(i));
        }
        list.add(row);
        }

        
         
         
         
         
         
         // setting params
        setloanparams2();
        System.out.println("set loans params");
         // Remove data from old table
        DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        
        
        
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
             String typerembours;
           
             pos=listids.indexOf((Integer)1);
           
             if(pos !=-1) {
                 System.out.println("found");
                 Map <String, Object> rel = list.get(pos);
                   System.out.println("typeremb2"+rel.get("listmod"));
                 
              if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
              else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
              else 
                 typerembours = "Virement";
                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, (Double) rel.get("penalite"), (double) Math.round(echeance*100)/100+(double) rel.get("penalite"), (Double) rel.get("sumrembours"),typerembours, df.format((Date) rel.get("daterembours")) , (String) rel.get("listemod"), Boolean.TRUE});

              } else {
                 System.out.println("notfound");
             ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(1),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, null, (double) Math.round(echeance*100)/100, null, null, df.format(new Date()) , null, false});
              // Login.connectedUserfirstName+ " "+Login.connectedUserSurname
             }                                                          // Numecheance, Date Echeance, Capital, Intérêts, CapitalEcheance l
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
               
               if ((int) rel.get("typerembours") == 0) typerembours = "Espèce";
               else if ((int) rel.get("typerembours") == 1) typerembours = "Chèque"; 
               else 
                typerembours = "Virement";
               
                 ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i),mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, (Double) rel.get("penalite"), (double) Math.round(echeance*100)/100+(double) rel.get("penalite"), (Double) rel.get("sumrembours"),typerembours, df.format((Date) rel.get("daterembours")) , (String) rel.get("listemod"), Boolean.TRUE});
                } else {    
               //  ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capdu*100)/ 100,(double) Math.round(interets*100)/ 100, (double) Math.round(capr*100)/100, (double) Math.round(echeance*100)/100 });
               ((DefaultTableModel) jTable1.getModel()).addRow(new Object[] {new Integer(i) ,mensdat,(double) Math.round(capr*100)/100,(double) Math.round(interets*100)/100, (double) Math.round(echeance*100)/100, null, (double) Math.round(echeance*100)/100, null, null, df.format(new Date()), null, false});
               // 
             }}
         
             
        String sum="";
        System.out.println("This is the value of montant"+montant);
        if (montant != null) {sum= formatter.format((double) montant.doubleValue());} 
    
         DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
         String strDate = dateFormat.format(dateOr);
         if(montant == null) System.out.println("montant null");
         if(totalsummont == null) System.out.println("totalsumm null");
         System.out.println("valeur de totalsum"+totalsummont);
         String sumpaid = "";
     
         double perc = (totalsummont.doubleValue()/montant.doubleValue())*100;
         String rounded = String.format("%.00f", perc);
         if (totalsummont != null) {sumpaid= formatter.format((double) totalsummont.doubleValue());} 

         Icon icon = new DynamicIcon(100/100);
       // Grid updating
         this.mangersumgrid.getButton(0).setText("<html>Numéro dossier: <br>"+"<font color='#ff00000'>"+openloanref+"</font></html>");
         this.mangersumgrid.getButton(1).setText("<html>Montant emprunté: <br>"+"<font color='#ff0000'>"+sum+" XOF"+"</font></html>");
         this.mangersumgrid.getButton(2).setText("<html>Pourcentage remboursé: <br>"+"<font color='#ff0000'>"+rounded+"</font></html>");
    
        
        
      // l.setBorder(border);
      
       
       //  this.mangersumgrid.getButton(2).setVerticalAlignment(SwingConstants.CENTER);
      //   this.mangersumgrid.getButton(2).setIcon(icon);
         
   
         this.mangersumgrid.getButton(3).setText("<html>Responsable du dossier: <br>"+"<font color='#ff00000'>"+respdoss+"</font></html>"); 
         this.mangersumgrid.getButton(4).setText("<html>Date d'origine du prêt: <br>"+"<font color='#ff0000'>"+strDate+"</font></html>");
         this.mangersumgrid.getButton(5).setText("<html>Montant remboursé: <br>"+"<font color='#ff0000'>"+sumpaid+" XOF"+"</font></html>");

       //  return null;
     }



public void getEpargne3() throws Exception {  // getLoan remboursements libre
    
   
// Updatefrontpage
DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

symbols.setGroupingSeparator(' ');
formatter.setDecimalFormatSymbols(symbols);
//this.mangersumgrid.getButton(2).setIcon();
         // First get List of ID payed
         boolean existed=false;
         connect= Connect.ConnectDb();
         String sql0="SELECT * FROM rembours_terme WHERE idloan='"+this.openloanref+"' ";
         PreparedStatement pre = connect.prepareStatement(sql0);
         ResultSet rs = pre.executeQuery();
//         List<Map<String, Object>> list = new ArrayList<>();
//    //     List<Integer> listids =new ArrayList<>();
//         if (rs.next()) {
//             existed = true;
//             ResultSetMetaData md = rs.getMetaData();
//             int columns = md.getColumnCount();
//             
//             Map<String, Object> row = new HashMap<>(columns);
//             for (int i = 1; i <= columns; ++i) {
//             row.put(md.getColumnName(i), rs.getObject(i));
//          
//            }
//        list.add(row);
//        }
//         
//         
//       while (rs.next()) {
//         ResultSetMetaData md = rs.getMetaData();
//         int columns = md.getColumnCount();    
//         Map<String, Object> row = new HashMap<>(columns);
//         for (int i = 1; i <= columns; ++i) {
//            row.put(md.getColumnName(i), rs.getObject(i));
//           
//        }
//        list.add(row);
//        }

       // setting params
    
      
         // Remove data from old table
        DefaultTableModel dm = (DefaultTableModel) jTable3.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        
        String sum="";
        System.out.println("This is the value of montant"+montant);
        if (montant != null) {sum= formatter.format((double) montant.doubleValue());}       
       // Grid updating
         this.mangersumgrid.getButton(0).setText("<html>Numéro dossier: <br>"+"<font color='#ff00000'>"+openloanref+"</font></html>");
         this.mangersumgrid.getButton(1).setText("<html>Montant emprunté: <br>"+"<font color='#ff0000'>"+sum+" XOF"+"</font></html>");
         if (totalsummont == null) totalsummont = new BigDecimal(0);
         double perc = (totalsummont.doubleValue()/montant.doubleValue())*100;
         String rounded = String.format("%.00f", perc);
         this.mangersumgrid.getButton(2).setText("<html>Pourcentage remboursé: <br>"+"<font color='#ff0000'>"+rounded+"</font></html>");
    
        
        
      // l.setBorder(border);
      
       
       //  this.mangersumgrid.getButton(2).setVerticalAlignment(SwingConstants.CENTER);
      //   this.mangersumgrid.getButton(2).setIcon(icon);
         
   
         this.mangersumgrid.getButton(3).setText("<html>Responsable du dossier: <br>"+"<font color='#ff00000'>"+respdoss+"</font></html>"); 
         this.mangersumgrid.getButton(4).setText("");
//         this.mangersumgrid.getButton(4).setText("<html>Date d'origine du prêt: <br>"+"<font color='#ff0000'>"+strDate+"</font></html>");

       //  return null; 
      
       String typerembours;
       int i = 1; 
       while (rs.next()) {
            if ((int) rs.getInt("typerembours") == 0) typerembours = "Espèce";
               else if ((int) rs.getInt("typerembours") == 1) typerembours = "Chèque"; 
               else 
               typerembours = "Virement";
           
    ((DefaultTableModel) jTable3.getModel()).addRow(new Object[] {new Integer(i), new java.util.Date(((java.sql.Date) rs.getDate(3)).getTime()), (double) rs.getDouble(6), typerembours, rs.getString(5), rs.getString(7), rs.getInt(1)});
     i++;
        }
         
             
       
       
  jTable3.getColumn("Date remb.").setCellRenderer(new DateCellRenderer());
     
       
//    
//         DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//         String strDate = dateFormat.format(dateOr);
//         if(montant == null) System.out.println("montant null");
//         if(totalsummont == null) System.out.println("totalsumm null");
//         double perc = (totalsummont.doubleValue()/montant.doubleValue())*100;
//         String rounded = String.format("%.00f", perc);
//         Icon icon = new DynamicIcon(100/100);
       
}
     
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
             
//              System.out.println("Tcapr"+Tcapr);
//             System.out.println("Tint"+Tinterets);
//         
//            System.out.println("Total echeances"+echeance*nbEcheances);
//             jLabel10.setText(String.valueOf(nbEcheances));
//             jLabel11.setText(String.valueOf(mensdat));
//             jLabel12.setText(String.valueOf((double) Math.round(capdue*100)/100));
//             jLabel13.setText(String.valueOf((double) Math.round(Tinterets*100)/ 100));
//             jLabel14.setText(String.valueOf((double) Math.round(Tcapr*100)/100));
//             jLabel15.setText(String.valueOf((double) Math.round(echeance*nbEcheances*100)/ 100));

             
             
         }
         
 
     }
     
//      public void addYearComboitem() {
//        for (int i=0; i< jTable1.getModel().getRowCount(); i++) {
//            Date d= (Date) jTable1.getModel().getValueAt(i, 1);
//            Calendar c = Calendar.getInstance();
//            c.setTime(d);
//            int year = c.get(Calendar.YEAR);
//            if(jComboBox1.getItemCount() >2 && ! ((String) jComboBox1.getItemAt(jComboBox1.getItemCount()-1)).equalsIgnoreCase(String.valueOf(year))) {
//                jComboBox1.addItem(String.valueOf(year));
//            } else if (jComboBox1.getItemCount() ==2) {
//                jComboBox1.addItem(String.valueOf(year));
//            }
//
//        }
//    }
 
     
   
     private void setloanparams() throws SQLException {
         connect = Connect.ConnectDb();
         String sql ="SELECT firstpaydate, frequency, nbfreq, monthlypayment, nominalrate, FROM loanrecautom WHERE loanref = '"+this.openloanref+"'";
         String sql2 ="SELECT Montant FROM Loan WHERE loanrefnum = '"+this.openloanref+"'";

         PreparedStatement pst=connect.prepareStatement(sql);
         ResultSet rst =pst.executeQuery();
         while (rst.next()) {
             beginDate=new Date(((java.sql.Date)rst.getDate(1)).getTime());
             periodicite = rst.getInt(2);
             nbPeriod = rst.getInt(3);
             monthlypay=rst.getBigDecimal(4);
             nominalrate = rst.getDouble(5);
 //            montant = rst.getBigDecimal(6);
         }
         
         pst=connect.prepareStatement(sql2);
         rst= pst.executeQuery();
         while (rst.next()) {
             montant = rst.getBigDecimal(1);
         }
         
         
         rst.close();
         pst.close();
         connect.close();
          
     }
     
     private void setloanparams2() throws SQLException {
         System.out.println("in setparams2");
         connect = Connect.ConnectDb();
         String sql ="SELECT firstpaydate, frequency, nbfreq, monthlypayment, nominalrate, Montant, Responsable, DateOri, Statut FROM loanrecautom, Loan WHERE loanref = '"+this.openloanref+"'";
         String sql2 ="SELECT SUM(sumrembours) FROM rembours_auto WHERE idloan='"+this.openloanref+"'";
         
         PreparedStatement pst=connect.prepareStatement(sql);
         PreparedStatement pst2=connect.prepareStatement(sql2);
         ResultSet rst =pst.executeQuery();
         ResultSet rst2 =pst2.executeQuery();
         totalsummont = new BigDecimal(0.0);
                  

         while (rst.next()) {
             beginDate=new Date(((java.sql.Date)rst.getDate(1)).getTime());
             periodicite = rst.getInt(2);
             nbPeriod = rst.getInt(3);
             monthlypay=rst.getBigDecimal(4);
             nominalrate = rst.getDouble(5);
             montant = rst.getBigDecimal(6);
             respdoss = rst.getString(7);
             dateOr = rst.getDate(8);
             status = rst.getString(9);
         }
         
        while (rst2.next()) {
              totalsummont = rst2.getBigDecimal(1);
              System.out.println("totalsum0"+totalsummont);
        }
        
        if (totalsummont == null){  totalsummont =new BigDecimal(0.0); System.out.println("Total summ is null");    }
         rst.close();
         pst.close();
         pst2.close();
         rst2.close();
         connect.close();
          
     }
     
     
     public void getfreeloandata() throws Exception {  // getLoan
         
         // First get List of ID payed
         boolean existed=false;
         connect= Connect.ConnectDb();
         String sql0="SELECT * FROM rembours_libre WHERE idloan='"+this.openloanref+"' ORDER BY DateEnr";
         String sql1 ="SELECT Montant, Responsable  FROM Loan WHERE loanrefnum = '"+this.openloanref+"'";
         String sql2 ="SELECT SUM(montant) FROM rembours_libre WHERE idloan='"+this.openloanref+"'";
         PreparedStatement pre = connect.prepareStatement(sql0);
         PreparedStatement pre1 = connect.prepareStatement(sql1);
         PreparedStatement pre2 = connect.prepareStatement(sql2);
         ResultSet rs = pre.executeQuery();
         ResultSet rs2 = pre1.executeQuery();
         ResultSet rs3 = pre2.executeQuery();
         List<Map<String, Object>> list = new ArrayList<>();
         List<Integer> listids =new ArrayList<>(); 
         
        // Remove data from old table
        DefaultTableModel dm = (DefaultTableModel) jTable2.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        int i= 0;
      
         // Fill table
        
          while (rs.next())  {   // A vérifier
              int j=rs.getInt("typerembours");
              String typerembours = "";
              if (j==0) typerembours = "Epargne";
              if (j==1) typerembours = "Chèque";
              if (j==2) typerembours = "Virement";
              
             ((DefaultTableModel) jTable2.getModel()).addRow(new Object[] {new Integer(i) , rs.getDate("DateEnr"), rs.getDouble("montant"), typerembours, rs.getString("libelle"), rs.getString("lastmod"), rs.getInt("idrembours_libre")});
             i++;           
         }
          
            while (rs2.next()) {   // A vérifier
               montant = rs2.getBigDecimal(1);
               respdoss = rs2.getString(2);
              
            }
            
             while (rs3.next()) {   // A vérifier
               totalsummont = rs3.getBigDecimal(1);
              
            }
             
             
             
     
     
     }
     
     public void getfreelontermdata() throws Exception {  // getLoan
         
         // First get List of ID payed
         boolean existed=false;
         connect= Connect.ConnectDb();
         String sql0="SELECT * FROM rembours_terme WHERE idloan='"+this.openloanref+"' ORDER BY daterembours";
         String sql1 ="SELECT Montant, Responsable  FROM Loan WHERE loanrefnum = '"+this.openloanref+"'";
         String sql2 ="SELECT SUM(montantpay) FROM rembours_terme WHERE idloan='"+this.openloanref+"'";
         PreparedStatement pre = connect.prepareStatement(sql0);
         PreparedStatement pre1 = connect.prepareStatement(sql1);
         PreparedStatement pre2 = connect.prepareStatement(sql2);
         
         ResultSet rs = pre.executeQuery();
         ResultSet rs2 = pre1.executeQuery();
         ResultSet rs3 = pre2.executeQuery();
         List<Map<String, Object>> list = new ArrayList<>();
         List<Integer> listids =new ArrayList<>(); 
         
        // Remove data from old table
        DefaultTableModel dm = (DefaultTableModel) jTable3.getModel();
        int rowCount = dm.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            dm.removeRow(i);
        }
        int i= 0;
      
         // Fill table
        
          while (rs.next()) {   // A vérifier
              int j=rs.getInt("typerembours");
              String typerembours = "";
              if (j==0) typerembours = "Epargne";
              if (j==1) typerembours = "Chèque";
              if (j==2) typerembours = "Virement";
              
              System.out.println("Typerembours vaut"+ typerembours);
              
             ((DefaultTableModel) jTable3.getModel()).addRow(new Object[] {new Integer(i) , rs.getDate("daterembours"), rs.getDouble("montantpay"), typerembours, rs.getString("libelle"), rs.getString("lastmod"),  rs.getInt("idrembours_terme")});
             i++;
              
         }
          
         while (rs2.next()) {   // A vérifier
               montant = rs2.getBigDecimal(1);
               respdoss = rs2.getString(2);
              
        }
            
        while (rs3.next()) {   // A vérifier
               totalsummont = rs3.getBigDecimal(1);
              
       }
             
             
     
 }
     
     
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LoanNew lnnew = new LoanNew();
   //     lnnew.setLocationRelativeTo(null);
  //      lnnew.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lnnew.setVisible(true);
        
//        JDialog jdialog = new JDialog(( Frame )null );
//        jdialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        jdialog.setVisible(true); 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Loanopeninglist lnlist = new Loanopeninglist(this);
        lnlist.setLocationRelativeTo(null);
        lnlist.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        lnlist.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
          
     //   Date repayDate = 
        if(jTable1.getSelectedRow()==-1) {
             JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'échéance");
        } else if (jTable1.getValueAt(jTable1.getSelectedRow(), 5) == null) {
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le montant remboursé");
        } else if (jTable1.getValueAt(jTable1.getSelectedRow(), 6) == null) {
             JOptionPane.showMessageDialog(this, "Veuillez renseigner le type de remboursement");
      //  } else if ()
        } else if ((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 7)) == null) {
             JOptionPane.showMessageDialog(this, "Veuillez renseigner la date");
      //  } else if ()
        } else {
            Date date = null;
            try {
                 date = df.parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7));
                  if (!((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7)).equals(df.format(date))) {
                            date = null;
                   }
                   } catch (ParseException ex) {
                         ex.printStackTrace();
                    }
              if (date == null) {
    // Invalid date format
                  JOptionPane.showMessageDialog(this, "La date de remboursement n'est pas valide");

              } else {
                  
    // Valid date format
                  if(date.before(beginDate)) {
                  
                   JOptionPane.showMessageDialog(this, "La date de remboursement doit postérieur à la date d'origine prêt");
   
                  } else { 
                      
                      int i=0;
                      boolean typestringcase =false;
                      
                      if (jTable1.getValueAt(jTable1.getSelectedRow(), 5).getClass()== String.class) {
                        typestringcase = true;  
                      
                      if((Double.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 5))) < (double) jTable1.getValueAt(jTable1.getSelectedRow(), 4)) {
                    
                         i=JOptionPane.showConfirmDialog(this, "Attention, le montant remboursé est inférieur au montant de l'échéance. Voulez-vous continuer ?");
                      }

                       } else {
                           if (((double) jTable1.getValueAt(jTable1.getSelectedRow(), 5)) < (double) jTable1.getValueAt(jTable1.getSelectedRow(), 4)) {
                    
                         i=JOptionPane.showConfirmDialog(this, "Attention, le montant remboursé est inférieur au montant de l'échéance. Voulez-vous continuer ?");
                      }
                          
                      }
                       Boolean success =true;
                      // A corriger
                      if (i==0) {
                              // insertion dans la base de données
                             if ((boolean) jTable1.getValueAt(jTable1.getSelectedRow(), 9) == false) {
                          
                           connect = Connect.ConnectDb(); 
                           PreparedStatement pst=null;
                           String sql = "INSERT INTO rembours_auto(idrembours_auto,idloan,numrepyed,echeance,dateprevech,penalite,daterembours,sumrembours,typerembours,listemod) VALUES(?,?,?,?,?,?,?,?,?,?)";
                  try {
                          pst = connect.prepareStatement(sql);
                          pst.setString(1, null);
                          pst.setString(2, this.openloanref);
                          pst.setInt(3, (int) fixed.getValueAt(jTable1.getSelectedRow(), 0));
                          pst.setDouble(4, (double) jTable1.getValueAt(jTable1.getSelectedRow(), 4));
                          pst.setDate(5,  new java.sql.Date(((Date)fixed.getValueAt(jTable1.getSelectedRow(), 1)).getTime()));
                          double penal = 0;
                          if(jTable1.getValueAt(jTable1.getSelectedRow(), 3) == null) {
                            penal =0;  
                          }
                          else if (typestringcase == true && (String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).isEmpty()) {
                           penal =0;
                          }
                                   
                          else {
                              if (jTable1.getValueAt(jTable1.getSelectedRow(), 3).getClass() == String.class) {
                                  penal = Double.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 3));
                              } else {
                           penal = (double) jTable1.getValueAt(jTable1.getSelectedRow(), 3);
                              }
                          }
                          pst.setDouble(6, penal);  // penalité
                          pst.setDate(7, new java.sql.Date((df.parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7))).getTime()));   // daterembours
                         // pst.setDouble(8,  Double.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 5)));  // sumrembours
                          pst.setDouble(8,  Double.valueOf((Double) jTable1.getValueAt(jTable1.getSelectedRow(), 5)));  // sumrembours   changed value here
                          int typeremb;
                          if (((String)jTable1.getValueAt(jTable1.getSelectedRow(), 6)).equals("Espèce")){
                              typeremb=0;
                          } else if (((String)jTable1.getValueAt(jTable1.getSelectedRow(), 6)).equals("Chèque")){
                              typeremb=1;
                          } else {
                              typeremb =2;
                          }
                          pst.setInt(9,typeremb);  // Type de remboursement
                          pst.setString(10, connectedString);   // Utiliser celui qui est connecté et cacher la colonne @todo
                          int  rowsaffected = pst.executeUpdate();
                          System.out.println("rowsaffected"+rowsaffected);
                    
                   } catch (SQLException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                          success =false;
                   } catch (ParseException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        success =false;
                   }
                                            
                   } else {
                        // Now just do modifications    
                                 
                                  connect = Connect.ConnectDb(); 
                           PreparedStatement pst=null;
                           String sql = "UPDATE rembours_auto SET numrepyed = ?, echeance = ?, dateprevech = ?, penalite = ?, daterembours = ? ,sumrembours = ?, typerembours = ?, listemod= ? WHERE idloan ='"+this.openloanref+"' AND numrepyed = '"+(int) fixed.getValueAt(jTable1.getSelectedRow(), 0)+"' ";
                  try {
                          pst = connect.prepareStatement(sql);
                          
                          pst.setInt(1, (int) fixed.getValueAt(jTable1.getSelectedRow(), 0));
                          pst.setDouble(2, (double) jTable1.getValueAt(jTable1.getSelectedRow(), 4));
                          pst.setDate(3,  new java.sql.Date(((Date)fixed.getValueAt(jTable1.getSelectedRow(), 1)).getTime()));
                          double penal = 0;
                          if(jTable1.getValueAt(jTable1.getSelectedRow(), 3) == null) {
                            penal =0;  
                          }
                          else if (typestringcase == true && (String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).isEmpty()) {
                           penal =0;
                          }
                                   
                          else {
                           penal = (double) jTable1.getValueAt(jTable1.getSelectedRow(), 3);       
                          }
                          pst.setDouble(4, penal);  // penalité
                          pst.setDate(5, new java.sql.Date((df.parse((String) jTable1.getValueAt(jTable1.getSelectedRow(), 7))).getTime()));   // daterembours
                          pst.setDouble(6,  Double.valueOf((Double) jTable1.getValueAt(jTable1.getSelectedRow(), 5)));  // sumrembours   changed value here
                          int typeremb;
                          if (((String)jTable1.getValueAt(jTable1.getSelectedRow(), 6)).equals("Espèce")){
                              typeremb=0;
                          } else if (((String)jTable1.getValueAt(jTable1.getSelectedRow(), 6)).equals("Chèque")){
                              typeremb=1;
                          } else {
                              typeremb =2;
                          }
                          pst.setInt(7,typeremb);  // Type de remboursement
                          System.out.println("connected String"+connectedString);
                          pst.setString(8, connectedString);   // Utiliser celui qui est connecté et cacher la colonne @todo
                          int  rowsaffected = pst.executeUpdate();
                    
                   } catch (SQLException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                          success =false;
                   } catch (ParseException ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                        success =false;
                   }
                             
                    }
                    
                   if(success) {
                      JOptionPane.showMessageDialog(this, "Enregistrement ou modifications effectués avec succès");

                   }
                  
                          
                          
                    }
                
              }
        }
        }  
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        echeances newech;
        try {
            newech = new echeances ();
            newech.setloanmanager(this);
            newech.setLocationRelativeTo(null);
        newech.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        newech.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
                           Boolean success =true;
                           connect = Connect.ConnectDb(); 
                           PreparedStatement pst=null;
                           String sql = "DELETE FROM rembours_auto WHERE idloan ='"+this.openloanref+"' AND numrepyed = '"+(int) fixed.getValueAt(jTable1.getSelectedRow(), 0)+"' ";
                  try {
                          pst = connect.prepareStatement(sql);
                         
                          int  rowsaffected = pst.executeUpdate();
                    
                   } catch (SQLException ex) {
                          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                          success =false;
                   } 
                  
                  if (success) {
                      JOptionPane.showMessageDialog(this, "Suppression des données effectuée avec succès");
                               try {
                                   UpdateLoans(openloanref);
                               } catch (Exception ex) {
                                   Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);
                               }
                  } else {
                     JOptionPane.showMessageDialog(this, "Erreur lors de la suppression");

                  }
                                            
                   
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        
        CreditLibre crlbre = new CreditLibre(this, this.openloanref, "new", true);
        crlbre.setLocationRelativeTo(null);
        crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crlbre.setVisible(true);
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'enregistrement à modifier");
        } else {
         CreditLibre crlbre = new CreditLibre(this, this.openloanref, "update", (int) jTable2.getValueAt(jTable2.getSelectedRow(), 6), true);
         System.out.println("Je suis dans update");
         crlbre.setLocationRelativeTo(null);
         crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         crlbre.setVisible(true);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
          if (jTable2.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'enregistrement à Supprimer");
        } else {
              
              int dialogResult = JOptionPane.showConfirmDialog (null, "Voulez-vous vraiment supprimer cet enregistrement ?","Warning", 0);
              if(dialogResult == JOptionPane.YES_OPTION){
  // Saving code here
              
          Boolean success = true;
              String sql="DELETE FROM rembours_libre WHERE idrembours_libre='"+(int) jTable2.getValueAt(jTable2.getSelectedRow(), 6)+"'";
              Statement stmt=null;
              Connection conn;
              conn = Connect.ConnectDb();
              try {
                 stmt = conn.createStatement();
                 stmt.executeUpdate(sql);
                
                 
             } catch (SQLException ex) {
                 success = false; 
                 Logger.getLogger(CreditLibre.class.getName()).log(Level.SEVERE, null, ex);

             }
             
             
             if (success) JOptionPane.showMessageDialog(this, "Suppression effectuée avec succès");
             else JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement");
             
              try {
                getfreeloandata();
            } catch (Exception ex) {
                Logger.getLogger(CreditLibre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        CreditLibre crlbre = new CreditLibre(this, this.openloanref, "penality", true);
        crlbre.setLocationRelativeTo(null);
        crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crlbre.setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
      // TODO add your handling code here:
        CreditTerme crlbre = new CreditTerme(this, this.openloanref, "new", false);
        crlbre.setLocationRelativeTo(null);
        crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crlbre.setVisible(true);
     
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jButton11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ItemStateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
         // TODO add your handling code here:
        if (jTable3.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'enregistrement à modifier");
        } else {
         CreditTerme crlbre = new CreditTerme(this, this.openloanref, "update", (int) jTable3.getValueAt(jTable3.getSelectedRow(), 6), false);
         crlbre.setLocationRelativeTo(null);
         crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         crlbre.setVisible(true);
        }
    }//GEN-LAST:event_jButton11ActionPerformed
            
    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here: //  à réécrire
         if (jTable3.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner l'enregistrement à Supprimer");
        } else {
          Boolean success = true;
              String sql="DELETE FROM rembours_terme WHERE idrembours_terme='"+(int) jTable3.getValueAt(jTable3.getSelectedRow(), 6)+"'";
              Statement stmt=null;
              Connection conn;
              conn = Connect.ConnectDb();
              try {
                 stmt = conn.createStatement();
                 stmt.executeUpdate(sql);
                
                 
             } catch (SQLException ex) {
                 success = false; 
                 Logger.getLogger(CreditLibre.class.getName()).log(Level.SEVERE, null, ex);

             }
             
             
             if (success) JOptionPane.showMessageDialog(this, "Modification effectuée avec succès");
             else JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement");
             
              try {
                getfreelontermdata();
            } catch (Exception ex) {
                Logger.getLogger(CreditLibre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        
        CreditLibre crlbre = new CreditLibre(this, this.openloanref, "penality", false);
        crlbre.setLocationRelativeTo(null);
        crlbre.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        crlbre.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        Changingloanstatus chng = new Changingloanstatus(status);
        chng.setLocationRelativeTo(null);
        chng.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chng.setVisible(true);
        
    }//GEN-LAST:event_jButton14ActionPerformed

    
    public void tableChanged(TableModelEvent e) {
        System.out.println("changed in gen");
        if(e.getColumn()==7) {
            System.out.println("changed now");
            JFormattedTextField tf =new JFormattedTextField();
            try {
                   MaskFormatter dateMask = new MaskFormatter("##/##/####");
                   dateMask.install(tf);
                   
            } catch(ParseException ex) {     
            Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);       
            } 
            
            jTable1.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(tf));

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
            java.util.logging.Logger.getLogger(loanmanager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(loanmanager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(loanmanager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(loanmanager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                
                
                try {
           
        InputStream themepack = main.class.getClass().getResourceAsStream("/nehemie_mutuelle/whistlerthemepack.zip");
       //     System.out.println("url:"+ "file://"+ main.class.getClass().getResourceAsStream("whistlerthemepack.zip"));
//           URL themepack = new URL("file://"+urlthe);
//           System.out.println("url:"+urlthe);
//           // URL themepack = new URL("file://"+new File("src/nehemie_mutuelle/whistlerthemepack.zip").getAbsolutePath() );
                    Skin skin = SkinLookAndFeel.loadThemePack(themepack);
                     SkinLookAndFeel.setSkin(skin);   
                    UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");  
                   loanmanager ln =  new loanmanager();
                   ln.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                   ln.setVisible(true);
                    
                } catch (Exception ex) {
                    Logger.getLogger(loanmanager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    static Image iconToImage(Icon icon) {
          if (icon instanceof ImageIcon) {
              return ((ImageIcon)icon).getImage();
          } else {
//              int w = icon.getIconWidth();
//              int h = icon.getIconHeight();
//              GraphicsEnvironment ge = 
//              GraphicsEnvironment.getLocalGraphicsEnvironment();
//              GraphicsDevice gd = ge.getDefaultScreenDevice();
//              GraphicsConfiguration gc = gd.getDefaultConfiguration();
//              BufferedImage image = gc.createCompatibleImage(120, 120);
//              Graphics2D g = image.createGraphics();
//              icon.paintIcon(null, g, 0, 0);
//              g.dispose();
//              return image;
                /** On dessine l'icone dans un bufferedImage **/
       BufferedImage image = new BufferedImage( 120 , 120 , BufferedImage.TYPE_INT_RGB );
       icon.paintIcon(null, image.getGraphics() , 0 , 0 );
       return image;
          }
      }
    
    
    
    public void disable (int i) {
        int j=1;
        for (j=2; j < jTabbedPane4.getTabCount() && j != i; j++) {
        jTabbedPane4.setEnabledAt(j, false);
            System.out.println("j"+j);
        }
         jTabbedPane4.setEnabledAt(i, true);
         jTabbedPane4.setSelectedIndex(i);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables

}
