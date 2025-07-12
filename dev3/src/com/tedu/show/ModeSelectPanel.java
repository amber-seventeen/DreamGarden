package com.tedu.show;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tedu.manager.GardenResourceLoader;


/**
 * 选关卡面板
 */
public class ModeSelectPanel extends JPanel{
	
	public JButton jb1;
	public JButton jb2;
	
	public JButton backButton; // 返回按钮
	
	public JLabel jl;
	
	public static int map = 0;//选择关卡
	
//	public static GameMainJPanel jp;
	
	public ModeSelectPanel(int mode, int character1, int character2) {
	    init(mode, character1,character2);
	}
	
	public void init(int mode, int character1, int character2) {
		
		this.setLayout(null);
		
		Font font = new Font("微软雅黑",Font.BOLD,25);
		
		jl = new JLabel("地图选择");
		jl.setFont(font);
		jl.setForeground(Color.BLACK);
		jl.setBounds(300, 80, 150,50);
		
		jb1 = new JButton();
		jb1.setBackground(Color.ORANGE);
		jb1.setOpaque(true);
		jb1.setBorderPainted(false);
		jb1.setBounds(180, 150, 350,200);
		
		jb2 = new JButton();
		jb2.setBackground(Color.ORANGE);
		jb2.setOpaque(true);
		jb2.setBorderPainted(false);
		jb2.setBounds(180, 400, 350,200);
		
		backButton = new JButton(); // 返回按钮
		backButton.setBounds(20, 20, 80, 30);
		
		ImageIcon imageIcon1 = new ImageIcon("image/rect6.png"); 
		Image image1 = imageIcon1.getImage(); 
		Image smallImage1 = image1.getScaledInstance(110, 40, Image.SCALE_FAST);
		ImageIcon smallIcon1 = new ImageIcon(smallImage1);
		backButton.setIcon(smallIcon1);	
		
		ImageIcon imageIcon2 = new ImageIcon("image/1.png"); 
		Image image2 = imageIcon2.getImage(); 
		Image smallImage2 = image2.getScaledInstance(350, 200, Image.SCALE_FAST);
		ImageIcon smallIcon2 = new ImageIcon(smallImage2);
		jb1.setIcon(smallIcon2);	
		
		ImageIcon imageIcon3 = new ImageIcon("image/2.png"); 
		Image image3 = imageIcon3.getImage(); 
		Image smallImage3 = image3.getScaledInstance(350, 200, Image.SCALE_FAST);
		ImageIcon smallIcon3 = new ImageIcon(smallImage3);
		jb2.setIcon(smallIcon3);	
		
//		jp=new GameMainJPanel(gj);		

		// 地图①
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				map = 1;

				DreamGardenFrame.setJPanel("GameMainJPanel", mode, character1, character2);
			}
		});


		// 地图②（同理改）
		jb2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				map = 3;                                  // ← 只改这一行
				DreamGardenFrame.setJPanel("GameMainJPanel",
						mode, character1, character2);
			}
		});

		System.out.println("CharacterSelectJPanel"+mode);
		backButton.addActionListener(new ActionListener() { // 返回按钮的点击事件监听器
            @Override
            public void actionPerformed(ActionEvent e) {
            	 if (mode == 1) {
                     DreamGardenFrame.setJPanel("CharacterSelectJPanel", 1, 0, 0); // 返回到单人模式选择角色面板
                 } else if (mode == 2) {
                     DreamGardenFrame.setJPanel("CharacterSelectJPanel2", 2, 0, 0); // 返回到双人模式选择角色面板
                 }
            }
        });
		
		this.add(jl);
		this.add(jb1);
		this.add(jb2);
		this.add(backButton); // 添加返回按钮
//		gj.setFocusable(true);
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GardenResourceLoader.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, DreamGardenFrame.jp2.getWidth(), DreamGardenFrame.jp2.getHeight(),null);
	}

}
