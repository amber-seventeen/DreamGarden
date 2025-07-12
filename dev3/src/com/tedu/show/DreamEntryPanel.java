package com.tedu.show;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tedu.manager.GardenResourceLoader;

/**
 * @说明 主菜单
 *
 */
public class DreamEntryPanel extends JPanel{
	
	public JButton jb1;
	public JButton jb2;
	public JButton jb3;
	public JButton jb4;
	
//	public static GameMainJPanel jp;
//	public static SelectJPanel jp;
	
	public DreamEntryPanel() {
		
		GardenResourceLoader.loadImg();
		ImageIcon icon = GardenResourceLoader.imgMap.get("single");
		ImageIcon icon2 = GardenResourceLoader.imgMap.get("double");
		ImageIcon icon3 = GardenResourceLoader.imgMap.get("shuoming");
		ImageIcon icon4 = GardenResourceLoader.imgMap.get("lianji");
		this.setLayout(null);
		
		jb1 = new JButton(icon);
		jb1.setBorderPainted(false);
		jb1.setContentAreaFilled(false);
		jb1.setBounds(310, 200, icon.getIconWidth(), icon.getIconHeight());
		
		jb2 = new JButton(icon2);
		jb2.setBorderPainted(false);
		jb2.setContentAreaFilled(false);
		jb2.setBounds(310, 300, icon2.getIconWidth(), icon2.getIconHeight());
		
		jb3 = new JButton(icon3);
		jb3.setBorderPainted(false);
		jb3.setContentAreaFilled(false);
		jb3.setBounds(310, 500, icon2.getIconWidth(), icon2.getIconHeight());
		
		jb4 = new JButton(icon4);
		jb4.setBorderPainted(false);
		jb4.setContentAreaFilled(false);
		jb4.setBounds(310, 400, icon4.getIconWidth(), icon4.getIconHeight());
		
//		jp = new SelectJPanel(gj);
//		jp = new GameMainJPanel();
//		实例化监听
//		GameListener listener = new GameListener();
//		实例化主线程
//		GameThread th = new GameThread();
		
		jb1.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						null,
						"【幻境试炼】\n\n" +
								"你将独自进入「奇想庭园」的梦幻迷宫，\n" +
								"用灵弹对抗潜伏其中的黑魔法女巫，\n" +
						"收集魔法花碎片，积蓄力量，直至驱逐潜藏的邪恶女巫。" ,
						"奇想庭园：幻境试炼",
						JOptionPane.INFORMATION_MESSAGE
				);

				DreamGardenFrame.setJPanel("CharacterSelectJPanel",1,0,0);
//				Object[] options = { "确定" }; 
//				JOptionPane.showOptionDialog(null, "单人模式尚未开发，敬请期待", "提示", 
//				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
//				null, options, null); 			
			}
		});
		jb2.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						null,
						"【双生对决】\n\n" +
								"两位魔法学徒踏入「奇想庭园」，在幻境中切磋灵弹技艺。\n" +
								"在不断破坏幻象、布置陷阱的对抗中，\n"+
								"磨炼实力，为未来的共同作战做准备。",
						"奇想庭园：双生对决",
						JOptionPane.INFORMATION_MESSAGE
				);
				DreamGardenFrame.setJPanel("CharacterSelectJPanel2",2,0,0);
//				注入
//				gj.setjPanel(jp);		
//				gj.setKeyListener(listener);
//				gj.setThread(th);
//				gj.start();
			}
		});
		jb3.addActionListener(new ActionListener() {				
			@Override
			public void actionPerformed(ActionEvent e) {			
				Object[] options = { "确定" }; 
				JOptionPane.showOptionDialog(null, "<html><body>"
								+ "<tr>绯樱花：增加魔法阵的威力</tr>"
								+ "<tr>橙辉花：增加魔法阵的放置数量</tr>"
						+ "<tr>暮蓝玫瑰：增加生命值</tr>"
								+ "<tr>粉团花：减少魔法阵爆炸时间</tr>"
								+ "<tr>雾蓝花：增加移动速度</tr>"
						+"<tr>注意：</tr>"
								+ "<tr>玩家1 方向键为WASD 释放魔法阵请按空格</tr>"
								+ "<tr>玩家2 方向键为上下左右 施放魔法阵请按为回车</tr></body></html>"
						, "魔法道具说明",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
				null, options, null); 
			}
		});
		jb4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						null,
						"【并肩作战】\n\n" +
								"两位魔法少女一同进入「奇想庭园」，\n" +
								"面对邪恶的黑魔法女巫，\n" +
								"在幻境中相互掩护，共同突破，\n"+
								"迎来最后的宿命对决。",
						"奇想庭园：并肩作战",
						JOptionPane.INFORMATION_MESSAGE
				);
				// 人机对战，mode=4，两名玩家由后续面板选择
				DreamGardenFrame.setJPanel("CharacterSelectJPanel2", 4, 0, 0);
			}
		});

		this.add(jb1);
		this.add(jb2);
		this.add(jb3);
		this.add(jb4);
//		gj.setFocusable(true);
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		ImageIcon icon = GardenResourceLoader.imgMap.get("ground");
		g.drawImage(icon.getImage(), 0, 0, DreamGardenFrame.jp1.getWidth(), DreamGardenFrame.jp1.getHeight(),null);
	}
	
}
