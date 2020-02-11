/*
 * Copyright 2010 Georgios Migdos <cyberpython@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * under the License.
 */

package nehemie_mutuelle;
import java.awt.Graphics;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
 
/**
 * 
 *@authorGeorgios Migdos <cyberpython@gmail.com> 
 */
public class JIconTextField extends JTextField{
 
    private Icon icon;
    private Insets dummyInsets;
 
    public JIconTextField() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        super();
//        try {
//    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//        if ("Nimbus".equals(info.getName())) {
//            UIManager.setLookAndFeel(info.getClassName());
//            break;
//        }
//    }
//} catch (Exception e) {
//    // If Nimbus is not available, you can set the GUI to another look and feel.
//}
        //this.icon = null;
        setIcon(createImageIcon("/nehemie_mutuelle/find-16x16.png","icon"));
       try {
  //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       } catch(UnsupportedLookAndFeelException e) {
    e.printStackTrace(); }
//        try {
//    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//        if ("Nimbus".equals(info.getName())) {
//            System.out.println("nimbus");
//            UIManager.setLookAndFeel(info.getClassName());
//            break;
//        }
//    }
//} catch (Exception e) {
//    // If Nimbus is not available, fall back to cross-platform
//    try {
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//    } catch (Exception ex) {
//        // not worth my time
//    }
//}
//      
 
      //  Border border = UIManager.getBorder("TextField.border");
       Border border = UIManager.getBorder("TextField.border");
        System.out.println(border);
       JTextField dummy = new JTextField();
       this.dummyInsets = border.getBorderInsets(dummy);
        
    }
    //Added
    public JIconTextField(int size){
        super(size);
        this.icon = null;

        Border border = UIManager.getBorder("TextField.border");
        JTextField dummy = new JTextField();
        this.dummyInsets = border.getBorderInsets(dummy);
    }
    
    public void setIcon(Icon icon){
        this.icon = icon;
    }
 
    public Icon getIcon(){
        return this.icon;
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
 
        int textX = 2;
 
        if(this.icon!=null){
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int x = dummyInsets.left + 5;//this is our icon's x
            textX = x+iconWidth+2; //this is the x where text should start
            int y = (this.getHeight() - iconHeight)/2;
            icon.paintIcon(this, g, x, y);
        }
 
        setMargin(new Insets(2, textX, 2, 2));
 
    }

    
    protected ImageIcon createImageIcon(String path, String description) {
    java.net.URL imgURL = getClass().getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL, description);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
}
 
}