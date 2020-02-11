/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nehemie_mutuelle;

/**
 *
 * @author elommarcarnold
 */
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;
import java.awt.event.*; 
import java.awt.*; 
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*; 
import  javax.swing.WindowConstants;
  
public  class Splash
	{
    
	JProgressBar progress;
	Thread thread;
	
	public static JFrame frame;
		
 public static void main(String[] args) throws MalformedURLException, Exception{
//    try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
//    catch(Exception e){} 
     
       InputStream themepack = main.class.getClass().getResourceAsStream("/nehemie_mutuelle/whistlerthemepack.zip");
       //     System.out.println("url:"+ "file://"+ main.class.getClass().getResourceAsStream("whistlerthemepack.zip"));
//           URL themepack = new URL("file://"+urlthe);
//           System.out.println("url:"+urlthe);
//           // URL themepack = new URL("file://"+new File("src/nehemie_mutuelle/whistlerthemepack.zip").getAbsolutePath() );
          Skin skin = SkinLookAndFeel.loadThemePack(themepack);
          SkinLookAndFeel.setSkin(skin);   
          UIManager.setLookAndFeel("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");  
           
     
  
         // new  Splash("Logo.gif","E.A.S.\u03A8 Version 1.0 Chargement...","icone.gif");//Path de l'image qu'on veut ,message,ainsi que l icone de la fenetre
          new  Splash("logo_MUNE2.jpg","Logiciel de Gestion de la Mutuelle Nehemie Version 1.0","blank.png");//Path de l'image qu'on veut ,message,ainsi que l icone de la fenetre  
         
        }
		public Splash(String imgPath, String message,String icone)
			{
				
				
			frame = 
                              new JFrame("Chargement");
                        frame.setIconImage(
                              Toolkit.getDefaultToolkit().getImage(icone));//icone de la Jframe
                        JPanel panel = 
                             new JPanel();
                        panel.setBackground(
                                  new Color(124,125,235));//Couleur de fond du Panel
                        panel.setSize(450,250);
                        panel.setBounds(0,0,450,250);
			JLabel texte = 
                                new JLabel(message);//Texte de la String 
                        texte.setForeground(Color.ORANGE);
                        JLabel img = 
                               new JLabel();
                      //  img.setText("dfdfdfdf");
//                        img.setIcon(
//                                 new ImageIcon(imgPath));
                        img.setIcon(new ImageIcon(getClass().getResource(imgPath)));
			progress =
                             new JProgressBar(0, 100);
                        //img.setPreferredSize(new Dimension(250,250));
                                
                        panel.setBorder(
                            BorderFactory.createLineBorder(Color.BLACK));
                        progress.setStringPainted(true);
                                
                       //ajout des éléments
                       panel.add("North",img);
                       panel.add("North",texte);
                       panel.add("East",progress); 
                       frame.getContentPane().add(BorderLayout.CENTER, panel);
		       frame.setSize(450,250);
                   //Pour définir le Splash au milieu de l'écran'
                        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                        frame.setLocation((screen.width - frame.getSize().width)/2,(screen.height - frame.getSize().height)/2);
                     // pour que ca ai vraiement l air d un splash :p
                	frame.setUndecorated(true);
                        frame.setVisible(true);
			frame.setResizable(false);
                        //Retaillage de la barre pour qu elle corresponde a la taille de la frame
			progress.setBounds(new Rectangle(10,220,430,20));
			// Création de thread pour afficher la progression de la barre
                                     thread =
                                            new Thread(
                                              new Progression());
                                                thread.start();
                                //On peut ajouter un Thread d'une classe de traitement qui implemente un Runnable
                                //et les switcher.         
                                                System.out.println(thread.getState());
                         while(thread.getState()==Thread.State.RUNNABLE || thread.getState()== Thread.State.TIMED_WAITING){ 
                             System.out.println("run");
                         } 
                         System.out.println(thread.getState());
                         Login log=new Login();
                         log.setLocationRelativeTo(null);
                         log.setVisible(true);
                         
                         
//                         JFrame frame = new JFrame ();
//                         frame.setSize (1000, 1500) ;
//                         frame. setTitle ("Courbe") ;
//                         frame.setVisible(true) ;
//                         
                        // System.exit(0);
                        // thread.interrupt();
                        //  System.exit(0);
			}
		public class Progression  implements Runnable
			{
			public void run()
				{
					for (int j = 1; j < 100; j++) 
						{
							progress.setValue(j);
                                                        progress.setString(j+" %");
							try
							{
							//thread.sleep(40);//determination de la rapiditée de la frame
							thread.sleep(100);
                                                        }
						catch(Exception e)
							{
                                                                e.printStackTrace();
                                                                frame.dispose();//en cas d' erreur pour pas rester bloqué sur le splash
							}
						}
                                                frame.dispose(); 
                                                //System.exit(0);
						//fermeture de la frame lorsque le chargement est teminé
                                                
				}
                      
			}
              
	}

                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                           
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                          
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                           
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                         
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
                                 
          