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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Graphics;
 
import javax.swing.JPanel;
 
public class SeparatorPanel extends JPanel {
 
    private static final long serialVersionUID = 1L;
 
    protected Color leftColor;
    protected Color rightColor;
 
    public SeparatorPanel(Color leftColor, Color rightColor) {
        this.leftColor = leftColor;
        this.rightColor = rightColor;
        setOpaque(false);
    }
 
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(leftColor);
        g.drawLine(0, 0, 0, getHeight());
        g.setColor(rightColor);
        g.drawLine(1, 0, 1, getHeight());
    }
 
}