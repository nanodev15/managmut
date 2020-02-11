/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;
 import java.awt.BorderLayout ;
 import java.awt.Color ;
 import java.awt.Dimension ;
 import java.awt.GridLayout ;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
 import java.awt.event.ActionEvent ;
 import java.awt.event.ActionListener ;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.GregorianCalendar;
 import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;

    import javax.swing.JButton ;
    import javax.swing.JDialog ;
    import javax.swing.JFrame ;
    import javax.swing.JLabel ;
import javax.swing.JOptionPane;
    import javax.swing.JPanel ;
import javax.swing.UIManager;
import static nehemie_mutuelle.TontineUser.conn;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author elommarcarnold
 */
public class Tontinedata extends javax.swing.JFrame{
    private Calendarium cal;
    static boolean crtlkeyispressed=false;
     
    private int IdEpargnant; 
    private String TypeEpargnant; 
    private static Date dateTontine; 
    private static int mise; 
    static Connection conn = null;
    static Connection connect = null;
    static PreparedStatement pre = null;
    private Date startDate;

    /**
     * Creates new form Tontinedata
     */
    public Tontinedata (int IdEpargnant, String TypeEpargnant, Date dateTontine, String startDate) throws ParseException {
        this.IdEpargnant=IdEpargnant;
        this.TypeEpargnant=TypeEpargnant;
        this.dateTontine=dateTontine; 
        System.out.println(dateTontine);
        setstartDate(startDate);
        initComponents();
        KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                if(ke.isControlDown()) { crtlkeyispressed=true; System.out.println("true"); }
                else {crtlkeyispressed=false;}
                return false;
            }
        };
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }
    
    public void setstartDate(String date) throws ParseException{
        final String OLD_FORMAT = "dd/MM/yyyy";
        SimpleDateFormat newFormat = new SimpleDateFormat(OLD_FORMAT);
        startDate=newFormat.parse(date);
    }
    public void fillcotbuttons() throws SQLException{
        connect = Connect.ConnectDb();
        pre=connect.prepareStatement("SELECT JoursTontine FROM Tontine WHERE IdEpargnant='" + this.IdEpargnant + "' AND TypeEpargnant='" + this.TypeEpargnant+ "' AND DateTontine='"+new java.sql.Date(dateTontine.getTime())+"';");
        ResultSet rs = pre.executeQuery();
        if(rs.next()){
            int k=0;
            while (k<31  & cal.button[k].getBackground()== Color.yellow ){
                cal.button[k].setBackground(Color.white);
                     k++;
            }
            String values = rs.getString(1);
            if (values==null || values.isEmpty()) {
                   jLabel4.setText("");
                   jLabel6.setText(""); 
                   return;
            }else {
                String[] bits = values.split(",");
                int nb=Integer.valueOf(bits[bits.length-1]);
                for(int i=0; i<nb; i++){
                   cal.button[i].setBackground(Color.yellow);
                }   
                   jLabel4.setText(String.valueOf(mise*(nb-1)));
                   jLabel6.setText(String.valueOf(nb));
                   
                   
                                     
               
                
            }
        } 
        
         rs.close();
         pre.close();
         connect.close();
    }
    
     public static void setTotalValueLabel(int nb){
          jLabel4.setText(String.valueOf((double) mise *(nb)));
     }
     
      public static void setTotalValueSelected(int nb){
          jLabel6.setText(String.valueOf(nb));
     }
      
    public  Boolean CheckMise(String carnet) {
        connect = Connect.ConnectDb();
        String sql0 = "SELECT Mise FROM Tontine WHERE IdEpargnant= '" + this.IdEpargnant + "' AND TypeEpargnant= '" + this.TypeEpargnant + "' AND DateTontine='"+ new java.sql.Date(Tontinedata.dateTontine.getTime())+"' AND idTontine IN (SELECT idtontine FROM enrtontinesupp WHERE numcarnet = '"+carnet+"');";   
        System.out.println("sql0 vaut"+sql0);
        Statement stmt = null;
        ResultSet rs1 = null;
        try {
                stmt = connect.createStatement();
                rs1 = stmt.executeQuery(sql0);
                //result= rs1.getInt(1);
        } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
                if (rs1.next()) {
                    mise=rs1.getInt(1);
                    System.out.println("mise à ce niveau"+mise);
                    if (rs1.wasNull()) return false;
                   // rs1.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         return true;
}
    
    public  Boolean CheckMise() {
               connect = Connect.ConnectDb();
                
               String sql0 = "SELECT Mise FROM Tontine WHERE IdEpargnant= '" + this.IdEpargnant + "' AND TypeEpargnant= '" + this.TypeEpargnant + "' AND DateTontine='"+ new java.sql.Date(Tontinedata.dateTontine.getTime())+"';";   
               Statement stmt = null;
               ResultSet rs1 = null;
          try {
                stmt = connect.createStatement();
                rs1 = stmt.executeQuery(sql0);
                //result= rs1.getInt(1);
            } catch (SQLException ex) {
                Logger.getLogger(Adhesion_enfant.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (rs1.next()) {
                    mise=rs1.getInt(1);
                    System.out.println("mise à ce niveau"+mise);
                    if(rs1.wasNull() || rs1.getObject(1) == null) return false;
                   // rs1.getInt(1);
                } else {
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(NewEpargne.class.getName()).log(Level.SEVERE, null, ex);
            }
         
         return true;
 }
    
 public void setMise(int mise) {
     this.mise=mise;
     jLabel1.setText(String.valueOf(mise));
 }   
    
//
//    @Override
//    public void keyTyped(KeyEvent ke) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void keyPressed(KeyEvent ke) {
//         if(ke.isControlDown()) {
//        crtlkeyispressed=true;
//             System.out.println("true");
//        }
//  //      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void keyReleased(KeyEvent ke) {
//        
//        crtlkeyispressed=false;
//        
//        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

   

    /**
     * @see @author @author Seahawks
     * @version 1.0
     */
    public  class Calendarium {

        int month = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH);
        int year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        Calendar cal= Calendar.getInstance();
        int monthTont;
        int yearTont;
         
      
        
        JLabel l = new JLabel("", JLabel.CENTER);
        String day = "";
        JDialog d;
    //    JButton[] button = new JButton[49];
        JButton[] button = new JButton[31];
        public Calendarium(JFrame parent) {
            
            d = new JDialog();
            d.setModal(true);
            String[] header = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
            JPanel p1 = new JPanel(new GridLayout(7, 5));
            p1.setPreferredSize(new Dimension(430, 120));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
                if (x > 6) {
                    button[x].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            day = button[selection].getActionCommand();
                            d.dispose();
                        }
                    });
                }
                if (x < 7) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< Previous");
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month--;
                    displayTontineDate();
                }
            });
            p2.add(previous);
            p2.add(l);
            JButton next = new JButton("Next >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month++;
                    displayTontineDate();
                }
            });
            p2.add(next);
            d.add(p1, BorderLayout.CENTER);
            d.add(p2, BorderLayout.SOUTH);
            d.pack();
            d.setLocationRelativeTo(parent);
           displayTontineDate();
            d.setVisible(true);
        }

        /**
         * @see how calendar works
         * @param panel_ins : instance of a panel
         *
         *
         * Description : This constructor is call the calendar
         *         * 
Date : Nov 8, 2011
         *         * 
Coded by : belazy..
         */
        public Calendarium(JPanel panel_ins) {
            
            cal.setTime(dateTontine);
            monthTont= cal.get(Calendar.MONTH);
            yearTont=cal.get(Calendar.YEAR);
            System.out.println("monthTont"+monthTont+"yearTont"+yearTont);
            //d = new JDialog();
           // d.setModal(true);
            String[] header = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
            JPanel p1 = new JPanel(new GridLayout(7, 7));
            p1.setPreferredSize(new Dimension(430, 120));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
                if (x > 6) {
                    button[x].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            day = button[selection].getActionCommand();
                          //  d.dispose();
                        }
                    });
                }
                if (x < 7) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< Previous");
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month--;
                    displayDate();
                }
            });
            p2.add(previous);
            p2.add(l);
            JButton next = new JButton("Next >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month++;
                    displayDate();
                }
            });
            p2.add(next);
//            d.add(p1, BorderLayout.CENTER);
//            d.add(p2, BorderLayout.SOUTH);
//            d.pack();
//            d.setLocationRelativeTo(panel_ins);
//            displayDate();
//            d.setVisible(true);
            
            panel_ins.add(p1, BorderLayout.CENTER);
            panel_ins.add(p2, BorderLayout.SOUTH);
            //panel_ins.pack();
           // d.setLocationRelativeTo(panel_ins);
            displayDate();
            panel_ins.setVisible(true);
        }

        public void displayDate() {
//            for (int x = 7; x < button.length; x++) {
//                button[x].setText("");
//            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "MMMM yyyy");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, 1);
//            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
//            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
//            for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
//                button[x].setText("" + day);
//            }
            
            for (int i=1; i<32; i++) {
                button[i-1].setText("" + i);
            }
            l.setText(sdf.format(cal.getTime()));
//            d.setTitle("CALENDAR");
        }
        
             public void displayTontineDate() {
//            for (int x = 7; x < button.length; x++) {
//                button[x].setText("");
//            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "MMMM yyyy");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(yearTont, monthTont, 1);
//            int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
//            int daysInMonth = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
//            for (int x = 6 + dayOfWeek, day = 1; day <= daysInMonth; x++, day++) {
//                button[x].setText("" + day);
//            }
            
            for (int i=1; i<32; i++) {
                button[i-1].setText("" + i);
           }
            l.setText(StringUtils.capitalize(sdf.format(cal.getTime())));
//            d.setTitle("CALENDAR");
        }
        
        public JPanel CalendariumP1() {
            //d = new JDialog();
           // d.setModal(true);
            String[] header = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
            JPanel p1 = new JPanel(new GridLayout(7, 7));
            p1.setPreferredSize(new Dimension(430, 120));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
                
                button[x].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                              day = button[selection].getActionCommand();
                               button[selection].setBackground(Color.yellow);
                        }
                    });
                
                
                
//                if (x > 6) {
//                    button[x].addActionListener(new ActionListener() {
//                        public void actionPerformed(ActionEvent ae) {
//                            day = button[selection].getActionCommand();
//                          //  d.dispose();
//                        }
//                    });
//                }
                if (x < 7) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
            JPanel p2 = new JPanel(new GridLayout(1, 3));
            JButton previous = new JButton("<< Previous");
            previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month--;
                    displayDate();
                }
            });
            p2.add(previous);
            p2.add(l);
            JButton next = new JButton("Next >>");
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    month++;
                    displayDate();
                }
            });
            p2.add(next);
//            d.add(p1, BorderLayout.CENTER);
//            d.add(p2, BorderLayout.SOUTH);
//            d.pack();
//            d.setLocationRelativeTo(panel_ins);
//            displayDate();
//            d.setVisible(true);
            
        //    panel_ins.add(p1, BorderLayout.CENTER);
         //   panel_ins.add(p2, BorderLayout.SOUTH);
            //panel_ins.pack();
           // d.setLocationRelativeTo(panel_ins);
            displayDate();
            return p1;
        }
        
        public ArrayList<JPanel> CalendariumP2() {
            //d = new JDialog();
           // d.setModal(true);
            String[] header = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
            JPanel p1 = new JPanel(new GridLayout(7, 7));
            p1.setPreferredSize(new Dimension(430, 120));

            for (int x = 0; x < button.length; x++) {
                final int selection = x;
                button[x] = new JButton();
                button[x].setFocusPainted(false);
                button[x].setBackground(Color.white);
       //         if (x > 6) {
                 final int j=x;
//                    button[x].addKeyListener(new KeyListener() {
//
//                    @Override
//                    public void keyTyped(KeyEvent ke) {
//                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    }
//
//                    @Override
//                    public void keyPressed(KeyEvent ke) {
//                        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                     crtlkeyispressed=true;
//                        System.out.println("true");
////                     if(ke.isControlDown()) {
////                         for(int i=0; i<=selection; i++){
////                             button[i].setBackground(Color.yellow);
////                         }
////                         
////                     }
//                    }
//
//                    @Override
//                    public void keyReleased(KeyEvent ke) {
//                        crtlkeyispressed=false;
//                        
//                       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//                    }
//                        
//                        
//                    });
                    button[x].setContentAreaFilled(false);
                    button[x].setOpaque(true);
                    button[x].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    button[x].addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            
                            // Detecter si la touche CRTL est appuyée 
                            if(crtlkeyispressed){
                                int i;
                                for(i=0; i<=selection; i++){
                                     button[i].setBackground(Color.yellow);
                                     
                                 }
                                
                                if(i<31){
                                    while(i<31 && button[i].getBackground()== Color.yellow) {
                                        button[i].setBackground(Color.white);
                                        i++;
                                        
                                   }
                                
                                }
                               setTotalValueLabel(selection);
                                setTotalValueSelected(selection+1);
                            } else {
                            
                            
                            
                            if (j==0) {
                            day = button[selection].getActionCommand();
                            if (button[selection].getBackground() != Color.yellow) {
                            button[selection].setBackground(Color.yellow);
                             setTotalValueLabel(selection);
                             setTotalValueSelected(selection+1);
                            
                            }else {
                               if (button[1].getBackground() == Color.white) {
                                    button[selection].setBackground(Color.white);
                                    setTotalValueLabel(selection); 
                                    setTotalValueSelected(selection);
                               }    
    
                            }
                            } else {
                                
                            day = button[selection].getActionCommand();
                            if (button[selection].getBackground() != Color.yellow && (button[0].getBackground()==Color.yellow) && (button[j-1].getBackground()==Color.yellow)) {
                            button[selection].setBackground(Color.yellow);
                            setTotalValueLabel(selection); 
                            setTotalValueSelected(selection+1);
                            }else {
                               if(button[selection].getBackground() == Color.yellow) {
                               if((j+1) !=31) {
                                if ( button[j+1].getBackground()==Color.white) {
                                     button[selection].setBackground(Color.white);
                                     setTotalValueLabel(selection-1); 
                                     setTotalValueSelected(selection);
                               }} else {
                                   button[selection].setBackground(Color.white);
                                    setTotalValueLabel(selection-1);
                                    setTotalValueSelected(selection);
                               }
                             }
                             }   
                            }
                          //  d.dispose();
                        }}
                    });
       //         }
                if (x < 1) {
                    button[x].setText(header[x]);
                    button[x].setForeground(Color.red);
                }
                p1.add(button[x]);
            }
              JPanel p2 = new JPanel(new GridLayout(1, 3));
              final JButton next = new JButton("Suivant >>");
              final JButton previous = new JButton("<< Précédent");
              Date curDate = new Date ();
              Calendar c = Calendar.getInstance();
              c.setTime (curDate);
              int curMonth = c.get (Calendar.MONTH);
              int curYear = c.get (Calendar.YEAR);
              c.setTime(DateUtils.addMonths(dateTontine, -1));
              int preMonth= c.get (Calendar.MONTH);
              int preYear=c.get (Calendar.YEAR);
              c.setTime(DateUtils.addMonths(startDate, -1));
              int prevlimMonth=c.get(Calendar.MONTH);
              int prevlimYear=c.get (Calendar.YEAR);
              
              if(preMonth == prevlimMonth && preYear ==  prevlimYear ) previous.setEnabled(false);
             previous.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (next.isEnabled()== false) next.setEnabled(true);
                    monthTont--;
                    displayTontineDate();
                    dateTontine=DateUtils.addMonths(dateTontine, -1);
                    // Checking range
                    Calendar c = Calendar.getInstance();
                    c.setTime (DateUtils.addMonths(dateTontine, -1));
                    int preMonth = c.get (Calendar.MONTH);
                    int preYear = c.get (Calendar.YEAR);
                    c.setTime (DateUtils.addMonths(startDate, -1));
                    int prevlimMonth=  c.get (Calendar.MONTH);
                    int prevlimYear = c.get (Calendar.YEAR);
                    if(preMonth == prevlimMonth && preYear==prevlimYear)  previous.setEnabled(false);
                    
                    
                    if (CheckMise()==false) {
                        jLabel1.setText("");
                        int i=0;
                        while(i<31  && button[i].getBackground()== Color.yellow ){
                            button[i].setBackground(Color.white);
                            i++;
                        }
                        jLabel4.setText("");
                        jLabel6.setText("");
                    } else {
                        try {
                            jLabel1.setText(String.valueOf(mise));
                            fillcotbuttons();
                        } catch (SQLException ex) {
                            Logger.getLogger(Tontinedata.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            p2.add(previous);
            p2.add(l);
           
              
      //      TimeZone tz = TimeZone.getDefault ();
              
              c.setTime (DateUtils.addMonths(dateTontine, 1));
              int nextMonth=  c.get (Calendar.MONTH);
              int nextYear = c.get (Calendar.YEAR);
              if(curMonth == nextMonth && curYear==nextYear)  next.setEnabled(false);
              next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (previous.isEnabled()== false) previous.setEnabled(true);
                    monthTont++;
                    dateTontine=DateUtils.addMonths(dateTontine, 1);
                    
                     // checking range
                    Calendar c = Calendar.getInstance();
                    Date curDate = new Date ();
                    c.setTime (curDate);
                    int curMonth = c.get (Calendar.MONTH);
                    int curYear = c.get (Calendar.YEAR);
                    c.setTime (DateUtils.addMonths(dateTontine, 1));
                    int nextMonth=  c.get (Calendar.MONTH);
                    int nextYear = c.get (Calendar.YEAR);
                    if(curMonth == nextMonth && curYear==nextYear)  next.setEnabled(false);
                    
                    
                    
                    if (CheckMise()==false) {
                        jLabel1.setText("");
                        int i=0;
                        while(i<31  && button[i].getBackground()== Color.yellow ){
                            button[i].setBackground(Color.white);
                            i++;
                        }
                        jLabel4.setText("");
                        jLabel6.setText("");
                    } else {
                        try {
                            jLabel1.setText(String.valueOf(mise));
                            fillcotbuttons();
                        } catch (SQLException ex) {
                            Logger.getLogger(Tontinedata.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    displayTontineDate();
                }
            });
            p2.add(next);
//            d.add(p1, BorderLayout.CENTER);
//            d.add(p2, BorderLayout.SOUTH);
//            d.pack();
//            d.setLocationRelativeTo(panel_ins);
//            displayDate();
//            d.setVisible(true);
            
        //    panel_ins.add(p1, BorderLayout.CENTER);
         //   panel_ins.add(p2, BorderLayout.SOUTH);
            //panel_ins.pack();
           // d.setLocationRelativeTo(panel_ins);
            displayTontineDate();
            ArrayList <JPanel> panelList = new ArrayList<JPanel>();
            panelList.add(p1);
            panelList.add(p2);
            return panelList;
        }
        

        public String setPickedDate() {
            if (day.equals("")) {
                return day;
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                    "dd-MM-yyyy");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(year, month, Integer.parseInt(day));
            return sdf.format(cal.getTime());
        }
        
        public void selectAll() {
            for (int x = 0; x < button.length; x++) {
                  button[x].setBackground(Color.yellow);
            }
        }
        
        public void UnselectAll() {
            for (int x = 0; x < button.length; x++) {
                  button[x].setBackground(Color.white);
            }
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
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cal = new Calendarium(jPanel1);
        JPanel p1= cal.CalendariumP1();

        //Panel1.setLayout(new java.awt.BorderLayout());
        //jPanel1.add(p1, BorderLayout.CENTER);
        //JPanel p2= cal.CalendariumP2();
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setText("Mise");
        ArrayList<JPanel> list= cal.CalendariumP2();
        jPanel1.add(list.get(0), BorderLayout.CENTER);
        jPanel1.add(list.get(1), BorderLayout.SOUTH);

        jLabel1.setText("jLabel1");

        jLabel3.setText("Total");

        jLabel4.setText("jLabel4");
        jLabel4.setText("");

        jButton1.setText("Enregistrer");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Nombre");

        jLabel6.setText("jLabel6");
        jLabel6.setText("");

        jCheckBox1.setText("Select. tout");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSeparator1)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(47, 47, 47)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(jCheckBox1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel2)
                        .add(18, 18, 18)
                        .add(jLabel1))
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 448, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
            .add(layout.createSequentialGroup()
                .add(45, 45, 45)
                .add(jLabel3)
                .add(18, 18, 18)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jLabel5)
                .add(35, 35, 35)
                .add(jLabel6)
                .add(54, 54, 54))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel1)
                    .add(jCheckBox1))
                .add(18, 18, 18)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 261, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(jButton1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jLabel4)
                    .add(jLabel5)
                    .add(jLabel6))
                .add(0, 9, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:
        if (jCheckBox1.isSelected()){ 
            cal.selectAll();
            setTotalValueLabel(30);
            setTotalValueSelected(31);
        } else {
            cal.UnselectAll();
            setTotalValueLabel(0);
            setTotalValueSelected(0);
        }
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        // Enregistrements des paramètres
        if(jLabel1.getText().isEmpty()) {
            final MiseTontine miseTontine= new MiseTontine(this.IdEpargnant, this.TypeEpargnant, dateTontine, 0);
            miseTontine.setLocationRelativeTo(null);
            miseTontine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            miseTontine.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    if (miseTontine.getMise()!=0){
                setMise(miseTontine.getMise());
            
            }
            }
        });
                miseTontine.setVisible(true);
        } else if (cal.button[0].getBackground() != Color.yellow){
            JOptionPane.showMessageDialog(null, "Veuillez remplir les cotisations");
        } else {
            Boolean success =true;
            // insertion dans la base de données. 
             connect = Connect.ConnectDb();
             String sql="UPDATE Tontine SET JoursTontine = ('";
             int i = 0;
             while (i< 31 && cal.button[i].getBackground() != Color.white){
                 sql = sql+String.valueOf(i+1)+",";
                 i++;
             }
             
            sql=sql.substring(0, sql.length()-1);
            sql=sql+"') WHERE IdEpargnant='"+this.IdEpargnant+"' AND TypeEpargnant='"+this.TypeEpargnant+"' AND DateTontine='"+ new java.sql.Date(this.dateTontine.getTime())+"';";
            System.out.println("sql: "+sql);
            try {
                 pre=connect.prepareStatement(sql);
                } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(MiseTontine.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                 pre.execute();
               } catch (SQLException ex) {
                 success=false;
                 Logger.getLogger(MiseTontine.class.getName()).log(Level.SEVERE, null, ex);
               }
                
               if (success) {
               JOptionPane.showMessageDialog(null, "Les cotisations ont été enregistrés avec succès");
               }
                
                
            
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Tontinedata.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tontinedata.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tontinedata.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tontinedata.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tontinedata data = null;
                try {
                    data = new Tontinedata(0, null, null,null);
                } catch (ParseException ex) {
                    Logger.getLogger(Tontinedata.class.getName()).log(Level.SEVERE, null, ex);
                }
                 data.setVisible(true);
              //  Calendarium cal = new Calendarium(data);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private static javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
