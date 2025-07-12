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
 * 双人模式选择角色面板
 */
public class CharacterGardenPanel2 extends JPanel {

    public JButton jb1;
    public JButton jb2;
    public JButton jb3;
    public JButton jb4;
    public JButton jb5;
    public JButton jb6;

    public JLabel jl1;
    public JLabel jl2;
    public JLabel jl3;
    
    public JButton backButton; // 返回按钮

    public static int selectedCharacter1 = 0; // Selected character: 0 - None, 1 - Character 1, 2 - Character 2, 3 - Character 3
    public static int selectedCharacter2 = 0;

    public CharacterGardenPanel2(int mode) {
        init(mode);
        selectedCharacter1=0;
        selectedCharacter2=0;
    }

    public void init(int mode) {
    	
    	GardenResourceLoader.loadImg();
		ImageIcon icon = GardenResourceLoader.imgMap.get("face1");
		ImageIcon icon2 = GardenResourceLoader.imgMap.get("face2");
		ImageIcon icon3 = GardenResourceLoader.imgMap.get("face3");
        this.setLayout(null);

        Font font = new Font("微软雅黑", Font.BOLD, 35);
        Font font1= new Font("微软雅黑", Font.BOLD, 25);

        jl1 = new JLabel("选择角色");
        jl1.setFont(font);
        jl1.setForeground(Color.BLACK);
        jl1.setBounds(315, 100, 150, 50);
        
        jl2 = new JLabel("玩家1：");
        jl2.setFont(font1);
        jl2.setForeground(Color.ORANGE);
        jl2.setBounds(340, 150, 150, 50);
        
        jl3 = new JLabel("玩家2：");
        jl3.setFont(font1);
        jl3.setForeground(Color.ORANGE);
        jl3.setBounds(340, 300, 150, 50);

        jb1 = new JButton(icon3);
		jb1.setBorderPainted(true);
		jb1.setContentAreaFilled(true);
		jb1.setBounds(200, 200, icon.getIconWidth(), icon.getIconHeight());

		jb2 = new JButton(icon);
		jb2.setBorderPainted(true);
		jb2.setContentAreaFilled(true);
		jb2.setBounds(350, 200, icon.getIconWidth(), icon.getIconHeight());

		jb3 = new JButton(icon2);
		jb3.setBorderPainted(true);
		jb3.setContentAreaFilled(true);
		jb3.setBounds(500, 200, icon.getIconWidth(), icon.getIconHeight());
		
		jb4 = new JButton(icon3);
		jb4.setBorderPainted(true);
		jb4.setContentAreaFilled(true);
		jb4.setBounds(200, 350, icon.getIconWidth(), icon.getIconHeight());
		
		jb5 = new JButton(icon);
		jb5.setBorderPainted(true);
		jb5.setContentAreaFilled(true);
		jb5.setBounds(350, 350, icon.getIconWidth(), icon.getIconHeight());
		
		jb6 = new JButton(icon2);
		jb6.setBorderPainted(true);
		jb6.setContentAreaFilled(true);
		jb6.setBounds(500, 350, icon.getIconWidth(), icon.getIconHeight());

		backButton = new JButton(); // 返回按钮
		backButton.setBounds(20, 20, 80, 30);
		
		ImageIcon imageIcon1 = new ImageIcon("image/rect6.png"); 
		Image image1 = imageIcon1.getImage(); 
		Image smallImage1 = image1.getScaledInstance(110, 40, Image.SCALE_FAST);
		ImageIcon smallIcon1 = new ImageIcon(smallImage1);
		backButton.setIcon(smallIcon1);	
		
        jb1.addActionListener(new ActionListener() {

            @Override       
            public void actionPerformed(ActionEvent e) {
                selectedCharacter1 = 1;
                if(selectedCharacter2!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });

        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter1 = 2;
                if(selectedCharacter2!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });

        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter1 = 3; 
                if(selectedCharacter2!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });
        
        jb4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter2 = 1;
                if(selectedCharacter1!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });
        
        jb5.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter2 = 2;
                if(selectedCharacter1!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });
        
        jb6.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter2 = 3;
                if(selectedCharacter1!=0) {
                	DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter1, selectedCharacter2);
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() { // 返回按钮的点击事件监听器
            @Override
            public void actionPerformed(ActionEvent e) {
                DreamGardenFrame.setJPanel("MainJPanel"); // 返回到主菜单面板
            }
        });

        this.add(jl1);
        this.add(jl2);
        this.add(jl3);
        this.add(jb1);
        this.add(jb2);
        this.add(jb3);
        this.add(jb4);
        this.add(jb5);
        this.add(jb6);
        this.add(backButton); // 添加返回按钮
    }

    @Override
    protected void paintComponent(Graphics g) {
    	// 绘制角色选择界面的相关内容
		ImageIcon icon = GardenResourceLoader.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, DreamGardenFrame.jp6.getWidth(), DreamGardenFrame.jp6.getHeight(),null);
	}

}
