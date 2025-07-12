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
 * 单人模式选择角色面板
 */
public class CharacterGardenPanel1 extends JPanel {

    public JButton jb1;
    public JButton jb2;
    public JButton jb3;
    public JButton backButton; // 返回按钮

    public JLabel jl;

    public static int selectedCharacter = 0; // Selected character: 0 - None, 1 - Character 1, 2 - Character 2, 3 - Character 3

    public CharacterGardenPanel1(int mode) {
        init(mode);
    }

    public void init(int mode) {
    	
    	GardenResourceLoader.loadImg();
		ImageIcon icon = GardenResourceLoader.imgMap.get("face1");
		ImageIcon icon2 = GardenResourceLoader.imgMap.get("face2");
		ImageIcon icon3 = GardenResourceLoader.imgMap.get("face3");
        this.setLayout(null);

        Font font = new Font("微软雅黑", Font.BOLD, 35);

        jl = new JLabel("选择角色");
        jl.setFont(font);
        jl.setForeground(Color.BLACK);
        jl.setBounds(315, 100, 150, 50);

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
                selectedCharacter = 1;
                DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter, 2);
            }
        });

        jb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = 2;
                DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter, 3);
            }
        });

        jb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedCharacter = 3;
                DreamGardenFrame.setJPanel("SelectJPanel", mode, selectedCharacter, 1);
            }
        });

        backButton.addActionListener(new ActionListener() { // 返回按钮的点击事件监听器
            @Override
            public void actionPerformed(ActionEvent e) {
                DreamGardenFrame.setJPanel("MainJPanel"); // 返回到主菜单面板
            }
        });

        this.add(jl);
        this.add(jb1);
        this.add(jb2);
        this.add(jb3);
        this.add(backButton); // 添加返回按钮
    }

    @Override
    protected void paintComponent(Graphics g) {
    	// 绘制角色选择界面的相关内容
		ImageIcon icon = GardenResourceLoader.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, DreamGardenFrame.jp5.getWidth(), DreamGardenFrame.jp5.getHeight(),null);
	}

}
