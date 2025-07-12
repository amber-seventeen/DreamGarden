package com.tedu.show;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.tedu.manager.GardenResourceLoader;

public class DreamOverPanel extends JPanel{

	public JButton jb;
	
//	public static MainJPanel jp;
	
	public DreamOverPanel() {
		this.setLayout(null);
//		jp = GameStart.jp;
		ImageIcon icon = GardenResourceLoader.imgMap.get("regame");
		jb = new JButton(icon);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
		jb.setBounds(250, 200, icon.getIconWidth(), icon.getIconHeight());
		jb.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {	
				DreamGardenFrame.setJPanel("MainJPanel");
//				注入
//				gj.setjPanel(jp);		
//				gj.start();
			}
		});
		this.add(jb);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GardenResourceLoader.imgMap.get("over");
		g.drawImage(icon.getImage(), 0, 0, DreamGardenFrame.jp4.getWidth(), DreamGardenFrame.jp4.getHeight(),null);
	}
	
}
