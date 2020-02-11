/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import nehemie_mutuelle.JSearchTextField;
import nehemie_mutuelle.JIconTextField;
import static nehemie_mutuelle.TontineUser.to2DimArray;


/**
 *
 * @author elommarcarnold
 */
public class main extends javax.swing.JFrame implements ActionListener{
    static PreparedStatement pre =null;
    static Connection conn=null;
    private Vector<Vector<String>> data;
    private JPopupMenu popupMenu;
    private JMenuItem menuItemEpargnes;
    private JMenuItem menuItemEpargnesrapid;
    private JMenuItem menuItemRetraits;
    private JMenuItem menuItemTontine;
    private JMenuItem menuItemMouvements;
    private JMenuItem menuItemModification;
    private JMenuItem menuItemDesactivate;
    private JMenuItem menuItemActivate;
    private JMenuItem menuComplDeletion;
    private JMenuItem menuCarnetAdd;
    private JMenuItem quickTontines;
    private JMenuItem otherTontines;
    private JLabel leftLabel2;
    protected TimerThread timerThread;
    private static ArrayList listphoto;
    private static String urlthe; 
    private JMenuItem menuItemCredits; 
    private JMenuItem suppItemTont;
    private JMenuItem modifyTontsupp;
    private JMenuItem deleteTontsupp;
    DefaultTableCellRenderer rigren;
    DefaultTableCellRenderer rigren2;
    private main mn = this;
    private Map <String, List<String>> tontmap =  new HashMap(); 
    
      
    Vector<String> header = new Vector<String>();
    RowFilter<Object,Object> boolFilter;
    RowFilter< TableModel, Integer > filter2; 
    
        
        

    /**
     * Creates new form main
     */
    public main() throws SQLException {
        //popup menu 
        rigren = new DefaultTableCellRenderer();
        rigren2 = new DefaultTableCellRenderer();
        setTitle("Gestionnaire pour mutuelle Néhémie EBB Djidjolé");
       
        
        initComponents();
        initializetontinesupp();
        
         filter2 = new RowFilter< TableModel, Integer >() {
         @Override
         public boolean include( javax.swing.RowFilter.Entry< ? extends TableModel, ? extends Integer > entry )
         {
//            TableModel model = entry.getModel();
//            return (Boolean)model.getValueAt(7, 1 );
            
        DefaultTableModel personModel =   (DefaultTableModel) entry.getModel();
        boolean active = ((Boolean) personModel.getValueAt(entry.getIdentifier(), 7)).booleanValue();
       return ! active;
         }
      };
        
        
        
        
        
        boolFilter  = new RowFilter<Object,Object>() {
        public boolean include(Entry<? extends Object, ? extends Object> entry) {
       for (int i = entry.getValueCount() - 1; i >= 0; i--) {
       if (entry.getValue(i).equals(true)) {
       return true;
      }
    }
    return false;
 }
};
        
        
      
        this.setLocationRelativeTo(null);
  //      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
     
        originalTableModel = (Vector) ((DefaultTableModel) jTable1.getModel()).getDataVector().clone();
        addDocumentListener();
        jTextField1.getDocument().addDocumentListener(documentListener);
        

        
    }
    
    
    
    private static class backImage extends JComponent {
 
Image i;
 
//Creating Constructer
public backImage(Image i) {
this.i = i;
 
}
 
//Overriding the paintComponent method
@Override
public void paintComponent(Graphics g) {
 
g.drawImage(i, 0, 0, null);  // Drawing image using drawImage method
 
}
}
 
    
    
    
//    public void resizeColumnWidth() {
//    int cumulativeActual = 0;
//    int padding = 15;
//    for (int columnIndex = 0; columnIndex < jTable1.getColumnCount(); columnIndex++) {
//        int width = 50; // Min width
//        TableColumn column = jTable1.getColumnModel().getColumn(columnIndex);
//        for (int row = 0; row < jTable1.getRowCount(); row++) {
//            TableCellRenderer renderer = jTable1.getCellRenderer(row, columnIndex);
//            Component comp = jTable1.prepareRenderer(renderer, row, columnIndex);
//            width = Math.max(comp.getPreferredSize().width + padding, width);
//        }
//        if (columnIndex < jTable1.getColumnCount() - 1) {
//            column.setPreferredWidth(width);
//            cumulativeActual += column.getWidth();
//        } else { //LAST COLUMN
//            //Use the parent's (viewPort) width and subtract the previous columbs actual widths.
//            column.setPreferredWidth((int) jTable1.getParent().getSize().getWidth() - cumulativeActual);
//        }
//    }
//}
    
    public class MyNewCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column) {
             
            Component cell= super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
             if(row % 2 == 0)
        {
            setForeground(Color.black);        
            setBackground(new Color(208,213,253));            
        }    
        else
        {    
            setBackground(Color.white);    
            setForeground(Color.black);    
        } 
            
             
//            for(int i=1;i<table.getColumnCount();i++) {
//            if((Integer)table.getValueAt(row, i) < 40)  
//            {  
//                setForeground(Color.black);          
//                setBackground(Color.red);              
//            }      
//            else 
//            {      
//                setBackground(Color.white);      
//                setForeground(Color.black);      
//            }   
//             
//           
//        }
             return cell;
          }
 
}
    
    public class PlainCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           Component cellComponent = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);        
       
   cellComponent.setFont(cellComponent.getFont().deriveFont(Font.PLAIN));
      
            
             

             return cellComponent;
          }
 
}
    
    public class BoldCellRenderer extends DefaultTableCellRenderer {
     
        @Override
        public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column) {
           Component cellComponent = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);        
       
   cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
   setHorizontalAlignment(SwingConstants.LEFT);
      
            
             

             return cellComponent;
          }
 
}
    
    public String getSearchText(){
        return jTextField1.getText();
    }
    
    public class TimerThread extends Thread {
 
        protected boolean isRunning;
 
        protected JLabel dateLabel;
        protected JLabel timeLabel;
 
        protected SimpleDateFormat dateFormat = 
                new SimpleDateFormat("EEE, d MMM yyyy");
        protected SimpleDateFormat timeFormat =
                new SimpleDateFormat("h:mm a");
 
        public TimerThread(JLabel dateLabel, JLabel timeLabel) {
            this.dateLabel = dateLabel;
            this.timeLabel = timeLabel;
            this.isRunning = true;
        }
 
        @Override
        public void run() {
            while (isRunning) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Calendar currentCalendar = 
                            Calendar.getInstance();
                        Date currentTime = 
                            currentCalendar.getTime();
                        dateLabel.setText(dateFormat
                            .format(currentTime));
                        timeLabel.setText(timeFormat
                            .format(currentTime));
                    }
                });
 
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                }
            }
        }
 
        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
 
    }
 
    public void searchTableContents(String searchString) {
   
    DefaultTableModel currtableModel = (DefaultTableModel) jTable1.getModel();
    //To empty the table before search
    currtableModel.setRowCount(0);
    //To search for contents from original table content
    for (Object rows : originalTableModel) {
        Vector rowVector = (Vector) rows;
        for (Object column : rowVector) {
  //          if (column !=null && column.toString().contains(searchString) ) {
            if (column !=null && column.toString().toLowerCase().contains(searchString.toLowerCase())) {
                //content found so adding to table
                currtableModel.addRow(rowVector);
                break;
            }
        }
}
}
    
    private void addDocumentListener() {
    documentListener = new DocumentListener() {
        public void changedUpdate(DocumentEvent documentEvent) {
           
            search();
        }

        public void insertUpdate(DocumentEvent documentEvent) {
           
            search();
        }

        public void removeUpdate(DocumentEvent documentEvent) {
            
            search();
        }

        private void search() {
            
            searchTableContents(jTextField1.getText());
        }
    };
 //   searchOnType.setSelected(true);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jStatusBar1 = new nehemie_mutuelle.JStatusBar();
        jSplitPane1 = new javax.swing.JSplitPane();
        jXTaskPaneContainer1 = new org.jdesktop.swingx.JXTaskPaneContainer();
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPane2 = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPane3 = new org.jdesktop.swingx.JXTaskPane();
        jXTaskPane4 = new org.jdesktop.swingx.JXTaskPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        header.add("ID");
        header.add("Nom/RS");
        header.add("Prénom/Type d'activité");
        header.add("Type_adhésion");
        header.add("T");
        header.add("Num carn Ep");
        header.add("Num carn Tont");
        header.add("Activate");
        header.add("id");
        header.add("type");

        try {
            // TODO add your handling code here:
            data=getMembres();
        } catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        //jTable1.setModel(new javax.swing.table.DefaultTableModel(
            //            data,header
            //        ));
    System.out.println(data);

    // new javax.swing.table.DefaultTableModel(
        //    data,header
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        try {
            jTextField1 = new JSearchTextField();
            jXLabel1 = new org.jdesktop.swingx.JXLabel();
            jToolBar1 = new javax.swing.JToolBar();
            jToggleButton1 = new javax.swing.JToggleButton();
            jButton1 = new javax.swing.JButton();
            jButton2 = new javax.swing.JButton();
            jMenuBar1 = new javax.swing.JMenuBar();
            jMenu5 = new javax.swing.JMenu();
            jMenu6 = new javax.swing.JMenu();
            jMenu1 = new javax.swing.JMenu();
            jMenuItem1 = new javax.swing.JMenuItem();
            jMenuItem2 = new javax.swing.JMenuItem();
            jMenuItem3 = new javax.swing.JMenuItem();

            jMenu3.setText("jMenu3");

            jMenu4.setText("jMenu4");

            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            setResizable(false);

            JLabel leftLabel = new JLabel(
                "Connecté "+Login.connectedUserSurname+ " "+Login.connectedUserfirstName);
            leftLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/log-in.png")));
            jStatusBar1.setLeftComponent(leftLabel);
            leftLabel2=new JLabel(data.size()+" membres");
            jStatusBar1.addRightComponent(leftLabel2);
            final JLabel dateLabel = new JLabel();
            dateLabel.setHorizontalAlignment(JLabel.CENTER);
            jStatusBar1.addRightComponent(dateLabel);

            final JLabel timeLabel = new JLabel();
            timeLabel.setHorizontalAlignment(JLabel.CENTER);
            jStatusBar1.addRightComponent(timeLabel);
            timerThread = new TimerThread(dateLabel, timeLabel);
            timerThread.start();
            jStatusBar1.setBackground(new java.awt.Color(249, 249, 249));
            jStatusBar1.setMinimumSize(new java.awt.Dimension(200, 6));
            jStatusBar1.setPreferredSize(new java.awt.Dimension(200, 23));

            jSplitPane1.setDividerLocation(190);
            jSplitPane1.setDividerSize(5);
            jSplitPane1.setResizeWeight(1.0);
            jSplitPane1.setName(""); // NOI18N

            jXTaskPaneContainer1.setBackground(new java.awt.Color(157, 185, 219));
            jXTaskPaneContainer1.setPreferredSize(new java.awt.Dimension(250, 73));
            org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
            verticalLayout1.setGap(14);
            jXTaskPaneContainer1.setLayout(verticalLayout1);

            jXTaskPane1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/icon_adhesion0.png"))); // NOI18N
            jXTaskPane1.setTitle("Adhésions");
            jXTaskPaneContainer1.add(jXTaskPane1);
            jXTaskPane1.add(new AbstractAction() { {
                putValue(Action.NAME, "Adhésion enfant");
                putValue(Action.SHORT_DESCRIPTION, "Adhésion d'un enfant (moins de 18 ans)");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconschild.png")));

            }
            public void actionPerformed(ActionEvent e) {
                Adh_enfant adhenfant = new Adh_enfant(mn);
                adhenfant.setLocationRelativeTo(null);
                adhenfant.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                adhenfant.setVisible(true);
            }

        });

        jXTaskPane1.add(new AbstractAction() { {
            putValue(Action.NAME, "Adhésion adulte");
            putValue(Action.SHORT_DESCRIPTION, "Adhésion d'un adulte (plus de 18 ans)");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/adult-avatar.png")));

        }
        public void actionPerformed(ActionEvent e) {
            Adh_adulte adhadulte = new Adh_adulte(mn);
            adhadulte.setLocationRelativeTo(null);
            adhadulte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            adhadulte.setVisible(true);
        }

    });

    jXTaskPane1.add(new AbstractAction() { {
        putValue(Action.NAME, "Adhésion personne morale");
        putValue(Action.SHORT_DESCRIPTION, "Adhésion d'une personne morale");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/icon_persmor.png")));

    }
    public void actionPerformed(ActionEvent e) {
        Adh_persmor adhpersmor = new Adh_persmor(mn);
        adhpersmor.setLocationRelativeTo(null);
        adhpersmor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        adhpersmor.setVisible(true);
    }

    });

    jXTaskPane2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/accounting.png"))); // NOI18N
    jXTaskPane2.setTitle("Comptabilité");
    jXTaskPaneContainer1.add(jXTaskPane2);
    jXTaskPane2.add(new AbstractAction() { {
        putValue(Action.NAME, "Bilan Epargne");
        putValue(Action.SHORT_DESCRIPTION, "Bilan annuel et par mois Epargne");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/coins.png")));
    }
    public void actionPerformed(ActionEvent e) {
        try {
            Epargneview2 ep = new Epargneview2();
            ep.setLocationRelativeTo(null);
            ep.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ep.setVisible(true);
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }});

    jXTaskPane2.add(new AbstractAction() { {
        putValue(Action.NAME, "Bilan Tontine");
        putValue(Action.SHORT_DESCRIPTION, "Bilan tontine par an et par mois ");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconstontine.png")));

    }
    public void actionPerformed(ActionEvent e) {
        TontineSynthese tont = new TontineSynthese();
        tont.setLocationRelativeTo(null);
        tont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tont.setVisible(true);
    }

    });

    jXTaskPane2.add(new AbstractAction() { {
        putValue(Action.NAME, "Comptabilité mutuelle");
        putValue(Action.SHORT_DESCRIPTION, "Comptabilité de toute la mutuelle/ Entrées et sorties");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/icons-calculator.png")));

    }

    public void actionPerformed(ActionEvent e) {
        try {
            Compta_mutuelle compt = new Compta_mutuelle();
            compt.setLocationRelativeTo(null);
            compt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            compt.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }});

    jXTaskPane3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/icon-loan.png"))); // NOI18N
    jXTaskPane3.setTitle("Prêts et crédits");
    jXTaskPaneContainer1.add(jXTaskPane3);
    jXTaskPane3.add(new AbstractAction() { {
        putValue(Action.NAME, "Nouveau dossier de prêt");
        putValue(Action.SHORT_DESCRIPTION, "Ajouter un nouveau dossier");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/folder.png")));

    }
    public void actionPerformed(ActionEvent e) {

        nehemie_mutuelle.LoanNew loanew = new nehemie_mutuelle.LoanNew();
        loanew.setLocationRelativeTo(null);
        loanew.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loanew.setVisible(true);

    }

    });

    jXTaskPane3.add(new AbstractAction() { {
        putValue(Action.NAME, "Gestionnaire de prêts");
        putValue(Action.SHORT_DESCRIPTION, "Gestionnaire de prêts");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/loan_manager.png")));

    }
    public void actionPerformed(ActionEvent e) {
        try {
            loanmanager loanman = new loanmanager();
            loanman.setLocationRelativeTo(null);
            loanman.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loanman.setVisible(true);
        } catch (Exception l) {
            l.printStackTrace();
        }

    }

    });

    jXTaskPane3.add(new AbstractAction() { {
        putValue(Action.NAME, "Simulateur de crédits");
        putValue(Action.SHORT_DESCRIPTION, "Simulateur de crédits");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/diagram.png")));

    }
    public void actionPerformed(ActionEvent e) {
        nehemie_mutuelle.loan.Test loantest = new nehemie_mutuelle.loan.Test();
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
    }

    });

    jXTaskPane4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/icons-statistics.png"))); // NOI18N
    jXTaskPane4.setTitle("Rapports");
    jXTaskPaneContainer1.add(jXTaskPane4);
    jXTaskPane4.add(new AbstractAction() { {
        putValue(Action.NAME, "Nouveau rapport");
        putValue(Action.SHORT_DESCRIPTION, "Nouveau rapport");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/share.png")));

    }
    public void actionPerformed(ActionEvent e) {
        try  {
            Report rep = new Report();
            rep.setVisible(true);
            rep.setLocationRelativeTo(null);
            rep.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            rep.setVisible(true);

        } catch(SQLException exp){
            exp.printStackTrace();
        }

    }} );

    jXTaskPane4.add(new AbstractAction() { {
        putValue(Action.NAME, "Statistiques");
        putValue(Action.SHORT_DESCRIPTION, "Statistiques");
        putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/diagram2.png")));

    }
    public void actionPerformed(ActionEvent e) {
        StatisticsInterface stats = new StatisticsInterface();
        stats.setVisible(true);
        stats.setLocationRelativeTo(null);
        stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        stats.setVisible(true);

    }

    });

    jSplitPane1.setLeftComponent(jXTaskPaneContainer1);

    jPanel1.setBackground(new java.awt.Color(238, 245, 248));

    jTable1.setFont(new java.awt.Font("EB Garamond 08", 0, 13)); // NOI18N
    jTable1.setModel(new javax.swing.table.DefaultTableModel(data,header){

        Class[] types = {Integer.class, String.class, String.class,
            String.class,  String.class, String.class, String.class, boolean.class, Integer.class, String.class};

        @Override
        public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    });
    popupMenu = new JPopupMenu();
    menuItemEpargnes = new JMenuItem("Epargnes");
    menuItemRetraits = new JMenuItem("Retraits");
    menuItemTontine = new JMenuItem("Tontine");
    menuItemMouvements = new JMenuItem("Mouvements");
    menuItemModification = new JMenuItem("Modifier");
    menuItemActivate = new JMenuItem("Activer");
    menuItemDesactivate = new JMenuItem("Désactiver");
    menuComplDeletion = new JMenuItem("Suppression complète");
    menuCarnetAdd = new JMenuItem("Ajouter carnet tontine");
    suppItemTont = new JMenuItem("Supprimer carnet tontine");
    otherTontines = new JMenu("Comptes tontines supplémentaires");
    menuItemEpargnesrapid = new JMenuItem("Enregistrement Epargne rapide");
    quickTontines = new JMenuItem("Enregistrement tontine rapide");
    modifyTontsupp = new JMenuItem("Modifier tontine supplémentaire");
    deleteTontsupp = new JMenuItem("Supprimer tontine supplémentaire");

    menuItemEpargnes.addActionListener(this);
    menuItemRetraits.addActionListener(this);
    menuCarnetAdd.addActionListener(this);
    menuItemTontine.addActionListener(this);
    menuItemMouvements.addActionListener(this);
    menuItemModification.addActionListener(this);
    menuItemActivate.addActionListener (this);
    menuItemDesactivate.addActionListener(this);
    otherTontines.addActionListener(this);
    menuItemEpargnesrapid.addActionListener(this);
    quickTontines.addActionListener(this);
    menuComplDeletion.addActionListener (this);
    suppItemTont.addActionListener(this);
    modifyTontsupp.addActionListener(this);
    deleteTontsupp.addActionListener(this);

    popupMenu.add(menuItemEpargnes);
    popupMenu.add(menuItemRetraits);
    popupMenu.add(menuItemTontine);
    popupMenu.add(menuItemMouvements);
    popupMenu.add(menuItemModification);
    popupMenu.add(menuItemActivate);
    popupMenu.add(menuItemDesactivate);
    popupMenu.add(menuComplDeletion);
    popupMenu.add(menuCarnetAdd);
    popupMenu.add(otherTontines);
    popupMenu.add(menuItemEpargnesrapid);
    popupMenu.add(quickTontines);
    popupMenu.add(suppItemTont);
    popupMenu.add(modifyTontsupp);
    popupMenu.add(deleteTontsupp);

    jTable1.setAutoCreateRowSorter(true);
    jTable1.setComponentPopupMenu(popupMenu);
    DefaultTableCellRenderer rigren = new DefaultTableCellRenderer();
    rigren.setHorizontalAlignment(SwingConstants.LEFT);
    jTable1.getColumnModel().getColumn(0).setCellRenderer(rigren);
    jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTable1.getTableHeader().setBackground(Color.LIGHT_GRAY);
    jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTable1MouseClicked(evt);
        }
        public void mouseReleased(java.awt.event.MouseEvent evt) {
            jTable1MouseReleased(evt);
        }
    });
    jScrollPane1.setViewportView(jTable1);
    jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        @Override
        public void valueChanged(ListSelectionEvent event){
            if (jTable1.getSelectedRow() > -1) {
                popupMenu.getComponent(0).setEnabled(true);
                popupMenu.getComponent(1).setEnabled(true);
                popupMenu.getComponent(2).setEnabled(true);
                popupMenu.getComponent(3).setEnabled(true);
                popupMenu.getComponent(8).setEnabled(true);
                popupMenu.getComponent(9).setEnabled(true);
                popupMenu.getComponent(10).setEnabled(true);
                popupMenu.getComponent(11).setEnabled(true);
                popupMenu.getComponent(12).setEnabled(true);
                popupMenu.getComponent(13).setEnabled(true);
                popupMenu.getComponent(14).setEnabled(true);

                int row=jTable1.getSelectedRow();
                String product_type=jTable1.getValueAt(row, 3).toString();
                jLabel6.setText("");
                jLabel8.setText("");

                if (jTable1.getValueAt(row, 5) != null) {
                    jLabel6.setText(jTable1.getValueAt(row, 5).toString());
                }
                if (jTable1.getValueAt(row, 6) != null)
                jLabel8.setText(jTable1.getValueAt(row, 6).toString());
                System.out.println("type de produit" + product_type);
                if(product_type.equalsIgnoreCase("Epargne  Enfant") ||product_type.equalsIgnoreCase("Epargne Enfant") || product_type.equalsIgnoreCase("Epargne Adulte") || product_type.equalsIgnoreCase("Epargne Pers Morale")) {
                    popupMenu.getComponent(2).setEnabled(false);
                    popupMenu.getComponent(8).setEnabled(false);
                    popupMenu.getComponent(9).setEnabled(false);
                    popupMenu.getComponent(11).setEnabled(false);
                    popupMenu.getComponent(12).setEnabled(false);
                    popupMenu.getComponent(13).setEnabled(false);
                    popupMenu.getComponent(14).setEnabled(false);
                }
                if (product_type.equalsIgnoreCase("Tontine Enfant") ||  product_type.equalsIgnoreCase("Tontine  Enfant")|| product_type.equalsIgnoreCase("Tontine Adulte") || product_type.equalsIgnoreCase("Tontine Pers Morale")) {
                    popupMenu.getComponent(0).setEnabled(false);
                    popupMenu.getComponent(1).setEnabled(false);
                    popupMenu.getComponent(3).setEnabled(false);
                    popupMenu.getComponent(10).setEnabled(false);

                }

                boolean activated = (boolean) jTable1.getValueAt(row, 7);
                if (activated) {
                    popupMenu.getComponent(5).setEnabled(false);
                    popupMenu.getComponent(6).setEnabled(true);
                } else {
                    popupMenu.getComponent(5).setEnabled(true);
                    popupMenu.getComponent(6).setEnabled(false);
                }

            }
        }
    }

    );

    jTable1.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object value,
            boolean bln, boolean bln1, int i, int i1) {
            String myVal = ((String)value).trim();
            JLabel lbl1 = new JLabel();
            lbl1.setAlignmentX(JLabel.CENTER);
            lbl1.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/manicon.png")));
            JLabel lbl2 = new JLabel();
            lbl2.setAlignmentX(JLabel.CENTER);
            lbl2.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/womanicon.png")));
            JLabel lbl3 = new JLabel();
            lbl3.setAlignmentX(JLabel.CENTER);
            lbl3.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/persmoral_icon.png")));

            if (myVal.equalsIgnoreCase("Masculin") || myVal.equalsIgnoreCase("masculin") || myVal.equalsIgnoreCase("homme") || myVal.equalsIgnoreCase("homme")) return lbl1;
            else if (myVal.equalsIgnoreCase("Feminin") || myVal.equalsIgnoreCase("feminin") || myVal.equalsIgnoreCase("féminin") || myVal.equalsIgnoreCase("femme"))  return lbl2;
            return lbl3;

        }

    });
    jTable1.setGridColor (Color.white);
    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    jTable1.getColumnModel().getColumn(1).setCellRenderer(new BoldCellRenderer());
    jTable1.getColumnModel().getColumn(2).setCellRenderer(new PlainCellRenderer());
    jTable1.getColumnModel().getColumn(3).setCellRenderer(new PlainCellRenderer());
    jTable1.getColumnModel().getColumn(7).setMinWidth(0);
    jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(7).setWidth(0);
    jTable1.getColumnModel().getColumn(8).setMinWidth(0);
    jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(8).setWidth(0);
    jTable1.getColumnModel().getColumn(9).setMinWidth(0);
    jTable1.getColumnModel().getColumn(9).setMaxWidth(0);
    jTable1.getColumnModel().getColumn(9).setWidth(0);
    jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
    jTable1.getColumnModel().getColumn(0).setMaxWidth(35);

    jTable1.getColumnModel().getColumn(1).setPreferredWidth(130);
    jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(3).setPreferredWidth(150);
    jTable1.getColumnModel().getColumn(4).setPreferredWidth(25);
    jTable1.getColumnModel().getColumn(4).setMaxWidth(25);

    //jTable1.getColumn(4).setPreferredWidth(10);

    jTable1.getColumnModel().getColumn(5).setPreferredWidth(120);
    jTable1.getColumnModel().getColumn(6).setPreferredWidth(120);
    ((DefaultTableCellRenderer)jTable1.getColumnModel().getColumn(4).getCellRenderer()).setAlignmentX(RIGHT_ALIGNMENT);

    jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

    jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
    // print first column value from selected row
    //System.out.println(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());

    //jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("blnk.png")));
    jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override

        public void valueChanged(ListSelectionEvent event) {

            if (jTable1.getSelectedRow() > -1 && ((int) jTable1.getValueAt(jTable1.getSelectedRow(), 0)) <= listphoto.size()) {  //&& jTable1.getSelectedRow() <= (listphoto.size()-1)
                System.out.println("listvaluechanged");
                String photo =(String) listphoto.get( ((int) jTable1.getValueAt(jTable1.getSelectedRow(), 0)) -1);
                File f = new File(photo);
                if(f.exists() && !f.isDirectory()) {
                    // do something

                    Image temp = new ImageIcon(photo).getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                    //          try {
                        //BufferedImage image = ImageIO.read(getClass().getResource(photo));
                        //ImageIcon icon = new ImageIcon(image);
                        jLabel1.setIcon(new javax.swing.ImageIcon(temp));
                        //jLabel1.setIcon(icon);
                        //} catch(IOException ex){
                        //        System.out.println (ex.toString());
                        //        System.out.println("Could not find file " + photo);
                        //    }
                } else {
                    jLabel1.setIcon(null);
                }
                // print first column value from selected row
                //System.out.println(jTable.getValueAt(jTable.getSelectedRow(), 0).toString());
                //jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(photo)));
            }  else {
                jLabel1.setIcon(null);
            }
        }
    });

    jLabel2.setText("Photo");

    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Adhérent"));

    jLabel5.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
    jLabel5.setText("Num. Carn Epargne");

    jLabel6.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
    jLabel6.setForeground(new java.awt.Color(226, 67, 39));

    jLabel7.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
    jLabel7.setText("Num. Carn Tontine");

    jLabel8.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
    jLabel8.setForeground(new java.awt.Color(33, 186, 29));

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(24, 24, 24)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel5)
                .addComponent(jLabel7))
            .addGap(77, 77, 77)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jLabel6)
                .addComponent(jLabel8))
            .addContainerGap(322, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(21, 21, 21)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(jLabel6))
            .addGap(18, 18, 18)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel7)
                .addComponent(jLabel8))
            .addContainerGap(41, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel5Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(40, 40, 40)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jLabel2))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
        jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2)
            .addGap(18, 18, 18)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(26, 26, 26))
    );

    jLabel3.setText("recherche:");

    } catch (Exception ex) {
        Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
    }

    jXLabel1.setText("Liste des adhérents");
    jXLabel1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addGap(26, 26, 26)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(25, 25, 25))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())))
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1)
                        .addComponent(jLabel3))
                    .addGap(23, 23, 23))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(23, 23, 23))
    );

    jSplitPane1.setRightComponent(jPanel1);

    jToolBar1.setBackground(new java.awt.Color(72, 128, 195));
    jToolBar1.setFloatable(false);
    jToolBar1.setRollover(true);

    jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/boolean.png"))); // NOI18N
    jToggleButton1.setText("Utilisateurs désactivés");
    jToggleButton1.setFocusable(false);
    jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jToggleButton1.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            jToggleButton1ItemStateChanged(evt);
        }
    });
    jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jToggleButton1ActionPerformed(evt);
        }
    });
    jToolBar1.add(jToggleButton1);

    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/coins.png"))); // NOI18N
    jButton1.setText("Enregistrer epargnes");
    jButton1.setFocusable(false);
    jButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });
    jToolBar1.add(jButton1);

    jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/iconstontine.png"))); // NOI18N
    jButton2.setText("Enregistrer tontines");
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton2ActionPerformed(evt);
        }
    });
    jToolBar1.add(jButton2);

    jMenu5.setText("Fichier");
    jMenuBar1.add(jMenu5);

    jMenu6.setText("Edition");
    jMenuBar1.add(jMenu6);

    jMenu1.setText("Paramètres");

    jMenuItem1.setText("Comptes utilisateurs");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem1ActionPerformed(evt);
        }
    });
    jMenu1.add(jMenuItem1);

    jMenuItem2.setText("Date initialisation");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem2ActionPerformed(evt);
        }
    });
    jMenu1.add(jMenuItem2);

    jMenuItem3.setText("Exporter base de données");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jMenuItem3ActionPerformed(evt);
        }
    });
    jMenu1.add(jMenuItem3);

    jMenuBar1.add(jMenu1);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jStatusBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jSplitPane1)
            .addContainerGap())
        .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jStatusBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        Authframe authframe = new Authframe();
        authframe.setLocationRelativeTo(null);
        authframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        authframe.setVisible(true);    
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseReleased
        // TODO add your handling code here:
  if (jTable1.getSelectedRow() !=-1) {
  if (evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt)) {
        String product_type=jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString();
        if(product_type.toLowerCase().contains(new String("tontine".toCharArray())))  {
					// JPopupMenu popup = mypopupmnu();
 				 
			
        if (evt.isPopupTrigger() || SwingUtilities.isRightMouseButton(evt)) {
					// JPopupMenu popup = mypopupmnu();
                System.out.println("tontmap"+tontmap);
		List <String> str;
                str = tontmap.get((String) jTable1.getValueAt(jTable1.getSelectedRow(), 9)+ (String) String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 8)));
                if (str != null) {
                Iterator iterator = str.iterator();
                otherTontines.setEnabled(true);
                modifyTontsupp.setEnabled(true);
                deleteTontsupp.setEnabled(true);
                otherTontines.removeAll();
          
                while(iterator.hasNext()) {
                       final JMenuItem it = new JMenuItem((String) iterator.next());
                       it.addActionListener(new ActionListener() {
                             public void actionPerformed(ActionEvent ev) {
                                      TontineUser  tont;
                                 try {
                                      tont = new TontineUser(it.getText(), (String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)), (String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), (String)(jTable1.getValueAt(jTable1.getSelectedRow(), 9)));
                                      tont.setLocationRelativeTo(null);
                                      tont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                      tont.setVisible(true);

                                 } catch (SQLException ex) {
                                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                                 } catch (ParseException ex) {
                                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                                 }
                                    
                        }
                        });
                       otherTontines.add(it);
                }
        } else  {
            
            otherTontines.setEnabled(false);
            modifyTontsupp.setEnabled(false);
            deleteTontsupp.setEnabled(false);
        
                }}
	}
			
	

	  		
   
  
  }}
    }//GEN-LAST:event_jTable1MouseReleased

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        IntializeDate ini;
        try {
             ini = new IntializeDate();
             ini.setLocationRelativeTo(null);
             ini.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
             ini.setVisible(true); 
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jToggleButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton1ItemStateChanged
                 TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>((DefaultTableModel)jTable1.getModel());       

      if(evt.getStateChange()==ItemEvent.SELECTED){
             sorter.setRowFilter(filter2);
             jTable1.setRowSorter(sorter);
       
      } else if(evt.getStateChange()==ItemEvent.DESELECTED){
              sorter.setRowFilter(null);
                jTable1.setRowSorter(sorter);
      }
             // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        BatchEpargne batch;
        try {
            batch = new BatchEpargne();
             batch.setLocationRelativeTo(null);
        batch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        batch.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
     
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         BatchTontine batch;
        try {
             batch = new BatchTontine();
             batch.setLocationRelativeTo(null);
        batch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        batch.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
         Connection conn = Connect.ConnectDb2();
         Boolean success = true;
       
         String dumpee = db2sql.dumpDB(conn);
         PrintWriter writer;
        try {
            
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = new Date();
            writer = new PrintWriter("export_" +format.format(date), "UTF-8");
            writer.println(dumpee);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }


       
        
        if (success){
            JOptionPane.showMessageDialog(this, "Base de données exportée avec succès");
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    public void refresh(){
         int id =-1;
         if(jTable1.getSelectedRow() !=-1) {
              id = (int) jTable1.getValueAt(jTable1.getSelectedRow(), 0);
         }
         
         try {
                 // TODO add your handling code here:
                     data=getMembres();
                } catch (Exception ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
                    jTable1.setModel(new javax.swing.table.DefaultTableModel(data,header){

 Class[] types = {Integer.class, String.class, String.class,
        String.class,  String.class, String.class, String.class, boolean.class };
                      

    @Override
    public Class getColumnClass(int columnIndex) {
        return this.types[columnIndex];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    

});
                     originalTableModel = (Vector) ((DefaultTableModel) jTable1.getModel()).getDataVector().clone();
                     if (!jTextField1.getText().isEmpty()){
                         searchTableContents(jTextField1.getText());
                     }
                     leftLabel2.setText(jTable1.getRowCount()+" membres");
                     
                     // table widths 
            jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jTable1.setFillsViewportHeight(true);
            jTable1.setPreferredScrollableViewportSize(new Dimension(1000,130));
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(220);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(320);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(230);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(210);
            
            
            
            
            
            
            
            
//            DefaultTableCellRenderer rigren2 = new DefaultTableCellRenderer();
//            rigren2.setHorizontalAlignment(SwingConstants.LEFT);
//Font fo =new Font(jTable1.getFont().getName(), Font.BOLD, 15);
//            rigren2.setFont(jTable1.getFont().deriveFont(Font.BOLD));
   //         jTable1.getColumnModel().getColumn(1).setCellRenderer(rigren2);
            
// end ajust jtable 
//            DefaultTableCellRenderer rigren = new DefaultTableCellRenderer();
//rigren.setHorizontalAlignment(SwingConstants.LEFT);
//rigren.setForeground(Color.BLUE);
jTable1.getColumnModel().getColumn(0).setCellRenderer(rigren);



// Nouveau ajout 
jTable1.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {

    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value,
            boolean bln, boolean bln1, int i, int i1) {
        String myVal = ((String)value).trim();
        JLabel lbl1 = new JLabel();
        lbl1.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/manicon.png")));
        JLabel lbl2 = new JLabel();
        lbl2.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/womanicon.png")));
        JLabel lbl3 = new JLabel();
        lbl3.setIcon((ImageIcon) new javax.swing.ImageIcon(getClass().getResource("/nehemie_mutuelle/persmoral_icon.png")));
        if (myVal.equalsIgnoreCase("Masculin") || myVal.equalsIgnoreCase("masculin") || myVal.equalsIgnoreCase("homme") || myVal.equalsIgnoreCase("homme")) return lbl1;
        else if (myVal.equalsIgnoreCase("Feminin") || myVal.equalsIgnoreCase("feminin") || myVal.equalsIgnoreCase("féminin") || myVal.equalsIgnoreCase("femme"))  return lbl2;
        return lbl3;
        
    }
});
jTable1.setGridColor (Color.white);
jTable1.getColumnModel().getColumn(1).setCellRenderer(new BoldCellRenderer());
jTable1.getColumnModel().getColumn(2).setCellRenderer(new PlainCellRenderer());
jTable1.getColumnModel().getColumn(3).setCellRenderer(new PlainCellRenderer());
jTable1.getColumnModel().getColumn(7).setMinWidth(0);
jTable1.getColumnModel().getColumn(7).setMaxWidth(0);
jTable1.getColumnModel().getColumn(7).setWidth(0);




//if (id !=-1){
//    
//    for (int i=0; i<jTable1.getRowCount();i++){
//    if ((int) jTable1.getValueAt(i, 0)== id) id=i;
//    }
//    
//    jTable1.setRowSelectionInterval(id, id);
//}  // Code à revoir
        
    }
    
    
 private int getId(String nomEpargnant, String prenomEpargnant, String typeEpargnant) {
        conn = Connect.ConnectDb();
        if (typeEpargnant.equalsIgnoreCase("adulte")) {
            String sql01 = "SELECT idProfil_adulte FROM Profil_adulte WHERE Noms= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
            Statement stmt = null;
            conn = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = conn.createStatement();
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
            return result;
        } else if (typeEpargnant.equalsIgnoreCase("enfant")) {
            String sql01 = "SELECT idProfil_enfant FROM Profil_enfant WHERE Nom= '" + nomEpargnant + "' AND lower(Prenoms)= '" + prenomEpargnant.toLowerCase(Locale.FRENCH) + "'";
            Statement stmt = null;

            conn = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = conn.createStatement();

                rs1 = stmt.executeQuery(sql01);

                //    System.out.println("valeur"+rs1.getInt("idProfil_enfant"));
                //  result= rs1.getInt("idProfil_enfant");
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
            return result;
        } else if (typeEpargnant.equalsIgnoreCase("pers morale")) {
            String sql01 = "SELECT idProfil_persmorale FROM Profil_persmorale WHERE Raison_sociale= '" + nomEpargnant + "'";
            Statement stmt = null;
            conn = Connect.ConnectDb();

            ResultSet rs1 = null;
            int result = 0;

            try {
                stmt = conn.createStatement();
                rs1 = stmt.executeQuery(sql01);
                //  result= rs1.getInt(1);
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

            return result;

        }

        return 0;
    }    
// Adhesion adulte

    public  Vector  getMembres() throws Exception {
        
    conn = Connect.ConnectDb();
    int i=1;

   // pre = conn.prepareStatement("SELECT * FROM Profil_enfant WHERE Active = true");
    pre = conn.prepareStatement("SELECT * FROM Profil_enfant");

    ResultSet rs = pre.executeQuery();
    listphoto = new ArrayList <String>();
   // Vector<Vector<String>> membreVector = new Vector<Vector<String>>();
    Vector<Vector> membreVector = new Vector<Vector>();
    Boolean flag = false;
    while(rs.next()) {
     //   Vector<String> membre = new Vector<String>();
     //   membre.add(String.valueOf(i)); 
        Vector<Object> membre = new Vector<Object>();
        membre.add(i);
        membre.add(rs.getString("Nom")); 
        membre.add(rs.getString("Prenoms")); 
        membre.add(rs.getString("Type_adhesion")+" Enfant");
        membre.add(rs.getString("sexe"));
       
        if (rs.getString("Type_adhesion").contains("Epargne") || rs.getString("Type_adhesion").contains("epargne")){
            flag = true;
            if (rs.getString("Num_carnet") !=null) membre.add(rs.getString("Num_carnet").substring(0, 4));
            else membre.add(null);
        } else {
           membre.add(null); 
        }
        if (rs.getString("Type_adhesion").contains("et tontine") || rs.getString("Type_adhesion").contains("et Tontine") ||  rs.getString("Type_adhesion").toLowerCase().equalsIgnoreCase("& Tontine")) {
            if (rs.getString("Num_carnet") !=null) { membre.add(rs.getString("Num_carnet").substring(5, rs.getString("Num_carnet").length()));
        } else {
           membre.add(null); 
        }} else if (rs.getString("Type_adhesion").contains("tontine") || rs.getString("Type_adhesion").contains("Tontine") ||  rs.getString("Type_adhesion").contains("Tontine")) {
            if (rs.getString("Num_carnet") != null) {
                membre.add(rs.getString("Num_carnet").substring(0,4));
                } else {
            membre.add(null);     
            }
        } else {
            membre.add(null);
        }
        membre.add(rs.getBoolean("active"));
        membre.add(rs.getInt(1));
        membre.add(new String("Enfant"));
        listphoto.add(rs.getString("Photo"));
   //
        membreVector.add(membre);
        i++;
    }
    
    
    pre = conn.prepareStatement("SELECT * FROM Profil_adulte WHERE Active = true");
    rs = pre.executeQuery();
    
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
//        Vector<String> membre = new Vector<String>();
 //       membre.add(String.valueOf(i)); 
        membre.add(i);;
        membre.add(rs.getString("Noms")); 
        membre.add(rs.getString("Prenoms")); 
        membre.add(rs.getString("Type_adhesion")+" Adulte");
        membre.add(rs.getString("sexe"));
        
        flag = false;
         if (rs.getString("Type_adhesion").contains("Epargne") || rs.getString("Type_adhesion").contains("epargne")){
             flag = true;
             if (rs.getString("Num_carnet") !=null)  membre.add(rs.getString("Num_carnet").substring(0, 4));
            else membre.add(null); 
        } else {
           membre.add(null); 
        }
        if (rs.getString("Type_adhesion").contains("et tontine") || rs.getString("Type_adhesion").contains("et Tontine") ||  rs.getString("Type_adhesion").toLowerCase().contains("& tontine")) {
            if (rs.getString("Num_carnet") !=null) { membre.add(rs.getString("Num_carnet").substring(5, rs.getString("Num_carnet").length()));}
            else membre.add(null); 
       
        } else if (rs.getString("Type_adhesion").contains("tontine") || rs.getString("Type_adhesion").contains("Tontine")) {
            if (rs.getString("Num_carnet") != null) {
                membre.add(rs.getString("Num_carnet").substring(0,4));
                } else {
            membre.add(null);     
            }
        } else {
            
           membre.add(null); 
        }
      
        membre.add(rs.getBoolean("active"));
        membre.add(rs.getInt(1));
        membre.add(new String("Adulte"));
        
   //
        listphoto.add(rs.getString("Photo"));
        membreVector.add(membre);
        
        
        i++;
    }
    
    pre = conn.prepareStatement("SELECT * FROM Profil_persmorale WHERE Active = true");
    rs = pre.executeQuery();
    while(rs.next()) {
        Vector<Object> membre = new Vector<Object>();
 //       Vector<String> membre = new Vector<String>();
//        membre.add(String.valueOf(i)); 
        membre.add(i); 
        membre.add(rs.getString("Raison_sociale")); 
        membre.add(rs.getString("Activite_principale")); 
        membre.add(rs.getString("Type_adhesion")+" Pers Morale");
        membre.add("");
        
   //
        
        
         if (rs.getString("Type_adhesion").contains("Epargne") || rs.getString("Type_adhesion").contains("epargne") ){
            if (rs.getString("Num_carnet") !=null) membre.add(rs.getString("Num_carnet").substring(0, 4));
            else membre.add(null);
        } else {
           membre.add(null); 
        }
         
        if (rs.getString("Type_adhesion").contains("et tontine") || rs.getString("Type_adhesion").contains("et Tontine") || rs.getString("Type_adhesion").toLowerCase().contains("& tontine")) {
            if (rs.getString("Num_carnet") !=null) { membre.add(rs.getString("Num_carnet").substring(5, rs.getString("Num_carnet").length()));}
            else { membre.add(null);}
        } else if (rs.getString("Type_adhesion").contains("tontine") || rs.getString("Type_adhesion").contains("Tontine")) {
            if (rs.getString("Num_carnet") != null) {
                membre.add(rs.getString("Num_carnet").substring(0,4));
                } else {
            membre.add(null);     
            }
        } else {
            membre.add(null); 
        }
        membre.add(rs.getBoolean("active"));
        membre.add(rs.getInt(1));
        membre.add(new String("Pers morale"));
        membreVector.add(membre);
        i++;
    }
        System.out.println("listphoto"+listphoto.get(0));
        
        
        
  
    

/*Close the connection after use (MUST)*/
    if(conn!=null)
        conn.close();
    
    this.initializetontinesupp();

    return membreVector;
}
    
    
private void initializetontinesupp () throws SQLException {
    
    if(!tontmap.isEmpty()) tontmap.clear();
    conn  = Connect.ConnectDb2();
    String sql = "SELECT * FROM tontinesupp";
    PreparedStatement pre = conn.prepareStatement(sql);
    ResultSet rs = pre.executeQuery();
    
    while(rs.next())  {
        if (tontmap.get(rs.getString(4)+String.valueOf(rs.getInt(3)))== null) {
           
            List <String> thetont = new ArrayList<>();
            thetont.add(rs.getString(2));
            tontmap.put(rs.getString(4)+String.valueOf(rs.getInt(3)), thetont);
       } else {
              
          ((List <String>) tontmap.get(rs.getString(4)+String.valueOf(rs.getInt(3)))).add(rs.getString(2));
                
            
       }
    }
        if (rs!=null)  rs.close();
        if (pre !=null) pre.close ();
        if (conn !=null) conn.close();
        System.out.println("tontmapinit"+tontmap);	
} 
    
        
        
        








    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception, IOException {
        /* Set the Nimbus look and feel */

        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        
//  try{UIManager.setLookAndFeel(new WindowsLookAndFeel());}
//catch(Exception e){}      
 // actual code 
//try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
//catch(Exception e){}
      
       
           // System.out.println("true");
           // URL themepack = new URL( "file:///home/elommarcarnold/Téléchargements/skins/whistlerthemepack.zip" );
//            URL themepack = new URL("file://"+main.class.getClass().getResource("/nehemie_mutuelle/whistlerthemepack.zip").getPath());
//            System.out.println("url:"+ "file://"+ main.class.getClass().getResource("/nehemie_mutuelle/whistlerthemepack.zip").getPath());
////           URL themepack = new URL("file://"+urlthe);
////           System.out.println("url:"+urlthe);
////           // URL themepack = new URL("file://"+new File("src/nehemie_mutuelle/whistlerthemepack.zip").getAbsolutePath() );
//          Skin skin = SkinLookAndFeel.loadThemePack(themepack);
//          SkinLookAndFeel.setSkin(skin);   
//          UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");  
//           

        InputStream themepack = main.class.getClass().getResourceAsStream("/nehemie_mutuelle/whistlerthemepack.zip");
       //     System.out.println("url:"+ "file://"+ main.class.getClass().getResourceAsStream("whistlerthemepack.zip"));
//           URL themepack = new URL("file://"+urlthe);
//           System.out.println("url:"+urlthe);
//           // URL themepack = new URL("file://"+new File("src/nehemie_mutuelle/whistlerthemepack.zip").getAbsolutePath() );
          Skin skin = SkinLookAndFeel.loadThemePack(themepack);
          SkinLookAndFeel.setSkin(skin);   
          UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");  
 
// adding created component to the JFrame using my backImage class
 
 

 
         
	  
      
   

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                try {
                    new main().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
     // Les actions clic droits sont ici
      public void actionPerformed(ActionEvent event) {
         
       
      
       
 
            
          
          
          
        EpargneContext epargne;
        EpargneContextRet epargne2;
        EpargneContext_rel epargne3;
        TontineUser tont = null;
        JMenuItem menu = (JMenuItem) event.getSource();
        Boolean value = menu == menuItemRetraits;
        System.out.println("Item booolan "+ value);
        if (menu == menuItemRetraits)  System.out.println("true, we ar here ");         
        if (menu == menuItemEpargnes) {
            if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                epargne = new EpargneContext((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant" );  
            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
                 epargne = new EpargneContext((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte" );  
            } else {
                epargne = new EpargneContext((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),"", "Pers Morale" );  
            }
  
      
      epargne.setLocationRelativeTo(null);
      epargne.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      epargne.setVisible(true);
     
    

        } else if (menu == menuItemRetraits) {
             System.out.println("it is true and we are herer");
            if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant" );  
            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
                 epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte" );  
            } else {
                epargne2 = new EpargneContextRet((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),"", "Pers Morale" );  
            }
  
      
            epargne2.setLocationRelativeTo(null);
            epargne2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            epargne2.setVisible(true);
        } else if (menu == menuItemTontine) {
            System.out.println("MenuItemCredits2");
             if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                 try {
                     tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant");
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }

             }else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
                 try {
                     tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte");
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
             } else {
                 try { 
                     tont= new TontineUser((String) (jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Pers Morale");
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }
             
            tont.setLocationRelativeTo(null);
            tont.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            tont.setVisible(true);
        } else if (menu == menuItemMouvements) {
          if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Enfant" );  
            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
                 epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), "Adulte" );  
            } else {
                epargne3 = new EpargneContext_rel((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),"", "Pers Morale" );  
            }
  
      
            epargne3.setLocationRelativeTo(null);
            epargne3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            epargne3.setVisible(true);    
    } else if (menu==menuItemModification){
        
        // je gère pour le moment les adultes 
          if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")){
              try {
                  Mod_adulte modadulte= new Mod_adulte((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), this);
                  modadulte.setLocationRelativeTo(null);
                  modadulte.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                  modadulte.setVisible(true);    
              } catch (SQLException ex) {
                  Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
              }
          } else if ((((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant"))) {
              try {
                  Mod_enfant modenf= new Mod_enfant((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)),(String)(jTable1.getValueAt(jTable1.getSelectedRow(), 2)), this);
                  modenf.setLocationRelativeTo(null);
                  modenf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                  modenf.setVisible(true);    
              } catch (SQLException ex) {
                  Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
              }
          } else {  // personne morale
              try {
                  Mod_persmor modpersmor= new Mod_persmor((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 1)), this);
                  modpersmor.setLocationRelativeTo(null);
                  modpersmor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                  modpersmor.setVisible(true);    
              } catch (SQLException ex) {
                  Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
        
    } else if (menu == menuItemActivate) {
        conn = Connect.ConnectDb();
        String nom = "";
        String prenom = "";
        String sql ="";
        boolean success = false;
        
        if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
            nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            sql= "UPDATE Profil_enfant SET Active = true WHERE Nom = '"+nom+"' AND Prenoms ='"+prenom+"' ";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
                   

             }else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
           
             nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            sql= "UPDATE Profil_adulte SET Active = true WHERE Noms = '"+nom+"' AND Prenoms ='"+prenom+"' ";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
                
            } else {
                 nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                 sql= "UPDATE Profil_persmorale SET Active = true WHERE Raison_sociale = '"+nom+"'";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
             }
        
          if (success ) { 
              JOptionPane.showMessageDialog(null, "Activation effectué avec succès");
              this.refresh();
          }
        
          
        
    } else if (menu == menuItemDesactivate) {
        
         conn = Connect.ConnectDb();
        String nom = "";
        String prenom = "";
        String sql ="";
        boolean success = false;
        
        if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
            nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            sql= "UPDATE Profil_enfant SET Active = false WHERE Nom = '"+nom+"' AND Prenoms ='"+prenom+"' ";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                   this.refresh();
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
                   

             }else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
           
             nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            sql= "UPDATE Profil_adulte SET Active = false WHERE Noms = '"+nom+"' AND Prenoms ='"+prenom+"' ";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
                
            } else {
                 nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                 sql= "UPDATE Profil_persmorale SET Active = false WHERE Raison_sociale = '"+nom+"'";
                  PreparedStatement pst = null;
                 try {
                   pst = conn.prepareStatement(sql);
                   pst.executeUpdate();
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
             }
        
          if (success ) { 
              JOptionPane.showMessageDialog(null, "Désactivation effectué avec succès");
              this.refresh();
          }
        
        
    } else if (menu == quickTontines) {
               String nom ="";
               String prenom ="";
               if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                    nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                    prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                   try {
                       QuickTontine quick = new QuickTontine(getId(nom, prenom, "enfant"), "enfant");
                       quick.setLocationRelativeTo(null);
                       quick.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                       quick.setVisible(true);  
                   } catch (SQLException ex) {
                       Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                       
                   }
            } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
                    nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                    prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                    try {
                       QuickTontine quick = new QuickTontine(getId(nom, prenom, "adulte"), "adulte");
                       quick.setLocationRelativeTo(null);
                       quick.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                       quick.setVisible(true);  
                   } catch (SQLException ex) {
                       Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                   }
                    
                    System.out.println("in adulte");
            } else {
                    nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                    prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                    
                    try {
                       QuickTontine quick = new QuickTontine(getId(nom, prenom, "pers morale"), "pers morale");
                       quick.setLocationRelativeTo(null);
                       quick.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                       quick.setVisible(true);  
                   } catch (SQLException ex) {
                       Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                   }
                
            }
                 
                 
            } else if (menu== menuItemEpargnesrapid){
                String nom, prenom; int id;
                if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")){ 
                     nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                     prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                     id= getId(nom, prenom, "Adulte");
                     EpargneRapid epar = new EpargneRapid(id, "Adulte");
                     epar.setLocationRelativeTo(null);
                     epar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                     epar.setVisible(true);  
                     System.out.println("in adulte");
                } else if(((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
                     nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                     prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                     id= getId(nom, prenom, "Enfant");
                     EpargneRapid epar = new EpargneRapid(id, "Enfant");
                     epar.setLocationRelativeTo(null);
                     epar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                     epar.setVisible(true);  
                     System.out.println("in enfant");
                    
                } else  {
                    nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
                    prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
                    id= getId(nom, prenom,  "Pers Morale");
                    EpargneRapid epar = new EpargneRapid(id, "Pers Morale");
                     epar.setLocationRelativeTo(null);
                     epar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                     epar.setVisible(true);  
                     System.out.println("in persmorale");
                }
                
                
                
                
         } else if (menu == menuComplDeletion) {
        int i=JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer cet utilisateur ainsi que toutes ses données ?");
        if (i==0) {
            conn = Connect.ConnectDb();
        String nom = "";
        String prenom = "";
        String sql ="";
        int id;
        boolean success = false;
        Statement stmt = null;
            try {
                stmt = conn.createStatement();
            } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")){
            nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            id =getId(nom, prenom, "enfant");
            sql= "DELETE FROM Profil_enfant WHERE idProfil_enfant='"+id+"'";
            String sql2 = "DELETE FROM Epargne WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Enfant'";
            String sql3 = "DELETE FROM Tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Enfant'";
            String sql4 = "DELETE FROM retraits_tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Enfant'";
            String sql5 = "DELETE FROM Exceptionftc WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Enfant'";
            String sql6 =  "DELETE FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Enfant'";
            System.out.println("sql6"+sql6);
                 PreparedStatement pst = null;
                 try {
                  stmt.execute(sql);
                   stmt.execute(sql2);
                    stmt.execute(sql3);
                     stmt.execute(sql4);
                      stmt.execute(sql5);
                       stmt.execute(sql6);
                       
                       System.out.println("sql vaut"+sql);
                       System.out.println("sql2 vaut"+sql2);
                       System.out.println("sql3 vaut"+sql3);
                       System.out.println("sql4 vaut"+sql4);
                       System.out.println("sql5 vaut"+sql5);
                       System.out.println("sql6 vaut"+sql6);
         
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (stmt != null) 
                     stmt.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
                 
             } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
            nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            id =getId(nom, prenom, "adulte");
            sql= "DELETE FROM Profil_adulte WHERE idProfil_adulte='"+id+"'";
            String sql2 = "DELETE FROM Epargne WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte'";
            String sql3 = "DELETE FROM Tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte'";
            String sql4 = "DELETE FROM retraits_tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte'";
            String sql5 = "DELETE FROM Exceptionftc WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Adulte'";
            String sql6 =  "DELETE FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Adulte'";

                  PreparedStatement pst = null;
                 try {
                  stmt.executeQuery(sql);
                   stmt.execute(sql2);
                    stmt.execute(sql3);
                     stmt.execute(sql4);
                      stmt.execute(sql5);
                   stmt.execute(sql6);

                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (stmt != null) 
                     stmt.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
             
                 
             } else  {
                 
            nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
            prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
            id =getId(nom, prenom, "pers morale");
            sql= "DELETE FROM Profil_persmorale WHERE idProfil_persmorale='"+id+"'";
            String sql2 = "DELETE FROM Epargne WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Pers Morale'";
            String sql3 = "DELETE FROM Tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Pers Morale'";
            String sql4 = "DELETE FROM retraits_tontine WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Pers Morale'";
            String sql5 = "DELETE FROM Exceptionftc WHERE IdEpargnant='"+id+"' AND TypeEpargnant='Pers Morale'";
            String sql6 =  "DELETE FROM carnet_enr WHERE idadherent='"+id+"' AND typeadherent='Pers Morale'";

                  PreparedStatement pst = null;
                 try {
                   stmt.executeQuery(sql);
                   stmt.executeQuery(sql2);
                   stmt.executeQuery(sql3);
                   stmt.executeQuery(sql4);
                   stmt.executeQuery(sql5);
                   
                   stmt.executeQuery(sql6);
                  
                   success = true;
                 } catch (SQLException ex) {
                     Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
               try {  
                 if (pst != null) 
                     pst.close();
                 if(conn != null)
                     conn.close();
               } catch (SQLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } 
                 
                 
                 
                 
                 
             }
        
             if (success) {
                 JOptionPane.showMessageDialog(null, "Suppression effectuée avec succès. Veuillez supprimer aussi l'ulisateur dans les prêts"); 
                 this.refresh();
                         
             } else {
            JOptionPane.showMessageDialog(null, "Erreur de suppression");  // A revoir

             }
        
            
            
        }
    } else if (menu == menuCarnetAdd) {
           String nom, prenom;
           int id;
           newcarnetplus quick; 
           
          
           if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Enfant")) {
               nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
               prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
               id =getId(nom, prenom, "enfant");
               quick= new newcarnetplus(id, "Enfant");
               
           } else if (((String)(jTable1.getValueAt(jTable1.getSelectedRow(), 3))).contains("Adulte")) {
               nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
               prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
               id =getId(nom, prenom, "adulte");
               quick= new newcarnetplus(id, "Adulte");
               
               
           } else {
               nom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 1);
             //  prenom = (String) jTable1.getValueAt(jTable1.getSelectedRow(), 2);
               id =getId(nom, "", "pers morale");
               quick= new newcarnetplus(id, "Pers Morale");
               
           }
          quick.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                try {
                    initializetontinesupp();
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }});
          quick.setLocationRelativeTo(null);
          quick.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
          quick.setVisible(true);  
    } else if (menu==modifyTontsupp){
        List <String> str;
        str = tontmap.get((String) jTable1.getValueAt(jTable1.getSelectedRow(), 9)+ (String) String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 8)));
        String param="";
        
        if (str.size() !=0) {
        for (int i=0; i< str.size(); i++) {
            param = param + str.get(i)+",";
        }
        param = param.substring(0, param.length() - 1);
       
        
        
        Modifytontsupp modifysupp = new Modifytontsupp(param);
        modifysupp.setLocationRelativeTo(null);
        modifysupp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        modifysupp.setVisible(true); 
        
          } else {
             JOptionPane.showMessageDialog(null, "Pas de carnet supplémentaire");
        }
        
        
      }
    }
   
    DocumentListener documentListener;
    Vector originalTableModel;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private nehemie_mutuelle.JStatusBar jStatusBar1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.swingx.JXLabel jXLabel1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane2;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane3;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane4;
    private org.jdesktop.swingx.JXTaskPaneContainer jXTaskPaneContainer1;
    // End of variables declaration//GEN-END:variables
}
