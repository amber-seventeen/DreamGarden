package com.tedu.show;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.tedu.controller.GameThread;
import com.tedu.element.DreamEnemy;
import com.tedu.element.Player;
import com.tedu.element.GardenObj;
import com.tedu.manager.SceneElementManager;
import com.tedu.manager.GardenElement;
import com.tedu.manager.GardenResourceLoader;
import com.tedu.manager.GardenBGMPlayer;

/**
 * 游戏的主要面板
 * 主要进行元素的显示，同时进行界面的刷新(多线程)
 * 
 * @多线程刷新
 * 1、本类实现Runnable接口
 * 2、定义内部类
 * 
 */
public class GardenMainPanel extends JPanel implements Runnable {
	
	// 联动元素管理器
	private SceneElementManager em;
	
//	private JPanel btnPanel;
	private JButton bgmBtn;	// BGM的按钮
	private boolean bgmStatus = false; // BGM的是否开启
	
	private JButton runBtn;
	private JButton overBtn;
	private JButton menuBtn; // 菜单按钮
    private JPanel menuPanel; // 菜单面板
    private JPanel statusJPanel = new JPanel(); //状态面板
    private Image bufferImage;//缓冲图像变量
	public static DreamOverPanel jp;
	
	public GardenMainPanel() {
		init();
//		System.out.println(this.getLayout());
//		System.out.println(this.getWidth());
//		System.out.println(this.getHeight());
	}
	
	protected void init() {
		// 得到元素管理器的单例
		em = SceneElementManager.getManager();
		JPanel timePanel = new JPanel();
		JPanel player1Panel = new JPanel();
		JPanel player2Panel = new JPanel();


//		this.setLayout(null);
//		JButton bgmBtn = new JButton("Click Me");
//		bgmBtn.setBounds(300, 200, 100,50);
//		this.add(bgmBtn);
		
		// 布局嵌套。BorderLayout.SOUTH和FlowLayout.CENTER)实现按钮组底部居中
		this.setLayout(new BorderLayout());
		JPanel btnPanel = new JPanel();
		btnPanel.setOpaque(false);	// 将面板设置为透明，否则灰色面板会遮挡地图
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bgmBtn = new JButton("开启 BGM");
		runBtn = new JButton("暂停游戏");
		overBtn = new JButton("结束游戏");
		overBtn.setBounds(90,150,50,20);//设置按钮大小以及位置
		Font f=new Font("宋体",Font.BOLD,13);
		
		bgmBtn.setFont(f);
		runBtn.setFont(f);
		overBtn.setFont(f);
		
		bgmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取当前游戏线程的BGM
				GardenBGMPlayer bgm = ((GameThread) DreamGardenFrame.gj.getThread()).getBgm();
				
				if (bgmStatus) { // 如果已经开了，就关掉
					bgm.over();
					bgmBtn.setText("开启 BGM");
				} else { // 如果处于关闭，就开启
					bgm.play();
					bgmBtn.setText("关闭 BGM");
				}
				bgmStatus = !bgmStatus; // 状态取反
				
				// 注意全局监听是作用于整个窗体，而并非面板
				// 点击按钮后，焦点落在了按钮上，窗体需要重新获得焦点，按钮监听才会有效
				DreamGardenFrame.gj.requestFocus();
//				GameMainJPanel.this.requestFocus();
			}
		});
		
		runBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取游戏线程
				GameThread thread = (GameThread) DreamGardenFrame.gj.getThread();
				// 获取当前游戏线程的BGM
				GardenBGMPlayer bgm = thread.getBgm();
				if (thread.isPause()) { // 如果已经暂停，则继续游戏
					thread.setPause(false);
					runBtn.setText("暂停游戏");
					if (bgmStatus) { // 如果已经开了BGM，就关掉
						bgm.play();
					}
					DreamGardenFrame.gj.requestFocus();
					
				} else {
					thread.setPause(true);
					runBtn.setText("继续游戏");
					if (bgmStatus) {
						bgm.over();
					}
					// 注意
					// 暂停期间让窗体失去焦点，让键盘监听失效
					// 否则暂停期间，人物可以切换方向
				}
				
			}
		});
		
//		jp = new OverJPanel(gj);
		// 结束游戏的按钮的点击事件
		overBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameThread th = (GameThread) DreamGardenFrame.gj.getThread();
				GardenBGMPlayer bgm = th.getBgm();
				if (bgmStatus) {
					bgm.over();
					bgmBtn.setText("开启BGM");
				}
				
				th.setIsOver(true);
				
//				GameJFrame.setJPanel("OverJPanel");
//				gj.setjPanel(jp);
//				gj.setThread(null, 1);
//				gj.start();
				// 注意全局监听是作用于整个窗体，而并非面板
				// 点击按钮后，焦点落在了按钮上，窗体需要重新获得焦点，按钮监听才会有效
				DreamGardenFrame.gj.requestFocus();
			}
		});
		// 创建菜单按钮
		menuBtn = new JButton(" 菜单 ");
        menuBtn.setFont(f);
        menuBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 切换菜单面板的可见性
                menuPanel.setVisible(!menuPanel.isVisible());
            }
        });

        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.add(overBtn);
        menuPanel.add(runBtn);
        menuPanel.add(bgmBtn);
        menuPanel.setVisible(false);

        JPanel menuContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
       
        menuContainer.setOpaque(false);
        menuContainer.add(menuBtn);
        
        Font font = new Font("微软雅黑", Font.BOLD, 26);
      
        
     // 创建时间面板
        JLabel timeLabel = new JLabel("游戏进行时间："+GameThread.getGameTime()/30+"s");
        timeLabel.setFont(font);
        timeLabel.setForeground(Color.BLUE);
//        timePanel.add(timeLabel);
        Player player1 = new Player();
        Player player2 = new Player();
        List<GardenObj> playerList= em.getElementsByKey(GardenElement.PLAYER);
        List<GardenObj> enemyList = em.getElementsByKey(GardenElement.ENEMY);
        for(GardenObj player:playerList)
        {
        	if(((Player)player).getPlayerNum()==1) player1 =(Player)player;
        	if(((Player)player).getPlayerNum()==2) player2 =(Player)player;
        }
        
        //角色1状态面板
        player1Panel.setLayout(new BoxLayout(player1Panel, BoxLayout.Y_AXIS));
        JLabel player1AvatarLabel = new JLabel("魔法少女1号");
        player1AvatarLabel.setFont(font);
        player1AvatarLabel.setForeground(Color.PINK);
        JLabel imageLabel1 = new JLabel();
        imageLabel1.setIcon(player1.getIcon());
        JLabel player1LifeLabel = new JLabel("生命值: "+ player1.getHp());
        JLabel player1BombLabel = new JLabel("魔法阵数量:"+ player1.getBubbleNum());
        JLabel player1PowerLabel = new JLabel("魔法阵威力:"+ player1.getPower());
        JLabel player1CDLabel = new JLabel("魔法阵爆炸CD:"+ player1.getTtl()/30.0+"s");
        player1Panel.add(player1AvatarLabel);
        player1Panel.add(imageLabel1);
        player1Panel.add(player1LifeLabel);
        player1Panel.add(player1BombLabel);
        player1Panel.add(player1PowerLabel);
        player1Panel.add(player1CDLabel);
        
      //角色2状态面板
        player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));
        JLabel player2AvatarLabel = new JLabel();
        if(enemyList.size()!=0) {
        	player2AvatarLabel.setText("黑魔法女巫1号");
        }
        else{
        	player2AvatarLabel.setText("魔法少女2号");
        }
        player2AvatarLabel.setFont(font);
        player2AvatarLabel.setForeground(Color.PINK);
        JLabel player2LifeLabel = new JLabel("生命值: "+ player2.getHp());
        JLabel player2BombLabel = new JLabel("魔法阵数量:"+ player2.getBubbleNum());
        JLabel player2PowerLabel = new JLabel("魔法阵威力:"+ player2.getPower());
        JLabel player2CDLabel = new JLabel("魔法阵爆炸CD:"+ player2.getTtl()/30.0+"s");
        player2Panel.add(player2AvatarLabel);
        player2Panel.add(player2LifeLabel);
        player2Panel.add(player2BombLabel);
        player2Panel.add(player2PowerLabel);
        player2Panel.add(player2CDLabel);

        
        
        statusJPanel.setLayout(new BoxLayout(statusJPanel, BoxLayout.Y_AXIS));
        // 创建一个空白面板作为辅助面板
        JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false); // 将面板设置为透明
        emptyPanel.setPreferredSize(new Dimension(0, 0)); // 设置面板大小为0，不占据空间

        // 将空白面板添加到 statusJPanel，使其垂直居中
//        statusJPanel.add(emptyPanel);
        //statusJPanel.add(timePanel);
        statusJPanel.add(player1Panel);
        statusJPanel.add(player2Panel);
  
        this.add(menuContainer, BorderLayout.NORTH);
//        this.add(menuPanel, BorderLayout.EAST);
        
     // 创建一个容器面板，使用 BorderLayout 布局
        JPanel containerPanel = new JPanel(new BorderLayout());

        // 将 menuPanel 添加到容器面板的北部（顶部）
        containerPanel.add(menuPanel, BorderLayout.NORTH);

        // 创建一个垂直方向的 BoxLayout 布局，用于 statusJPanel 内部的组件布局
        BoxLayout statusPanelLayout = new BoxLayout(statusJPanel, BoxLayout.Y_AXIS);
        statusJPanel.setLayout(statusPanelLayout);

        // 将 statusJPanel 添加到容器面板的中心区域
        containerPanel.add(statusJPanel, BorderLayout.CENTER);

        // 将容器面板添加到 GameMainJPanel 中
        this.add(containerPanel, BorderLayout.EAST);

//        this.add(statusJPanel, BorderLayout.EAST);  
	}

	// 状态面板实时刷新 —— 支持两名玩家 + 两名 AI
	// 状态面板实时刷新 —— 两人 + 两机
	private void refreshStatusPanel() {

		/* ---------- 0. 清空 ---------- */
		statusJPanel.removeAll();
		Font font = new Font("微软雅黑", Font.BOLD, 20);

		/* ---------- 1. 游戏计时 ---------- */
		JLabel timeLabel = new JLabel("游戏进行时间：" + GameThread.getGameTime() / 30 + "s");
		timeLabel.setFont(font);
		timeLabel.setForeground(Color.BLUE);
//		statusJPanel.add(timeLabel);

		/* ---------- 2. 玩家列表 ---------- */
		List<GardenObj> players = em.getElementsByKey(GardenElement.PLAYER);
		for (GardenObj eo : players) {
			Player p = (Player) eo;

			JPanel pane = new JPanel();
			pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

			JLabel title = new JLabel("魔法少女" + p.getPlayerNum()+"号");
			title.setFont(new Font("微软雅黑", Font.BOLD, 16));
			title.setForeground(Color.PINK);
			pane.add(title);

			JLabel avatar = new JLabel();
			String faceKey;

			switch (p.getSkinKey()) {          // 根据选中的皮肤键决定头像
				case "player1":  faceKey = "face3"; break;
				case "player2":  faceKey = "face1"; break;
				case "player3":  faceKey = "face2"; break;
				default:         faceKey = "face3"; break;
			}
			avatar.setIcon(GardenResourceLoader.imgMap.get(faceKey));
			pane.add(avatar);

			pane.add(new JLabel("生命值: " + p.getHp()));
			pane.add(new JLabel("魔法阵数量: " + p.getBubbleNum()));
			pane.add(new JLabel("魔法阵威力: " + p.getPower()));
			pane.add(new JLabel(String.format("魔法阵爆炸CD: %.2fs", p.getTtl() / 30.0)));

			statusJPanel.add(pane);
		}

		/* ---------- 3. AI 敌人列表 ---------- */
		List<GardenObj> enemies = em.getElementsByKey(GardenElement.ENEMY);
		int enemyIdx = 1;
		for (GardenObj eo : enemies) {
			DreamEnemy ai = (DreamEnemy) eo;

			JPanel pane = new JPanel();
			pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

			JLabel title = new JLabel("黑魔法女巫" + (enemyIdx++)+"号");
			title.setFont(new Font("微软雅黑", Font.BOLD, 16));
			title.setForeground(Color.MAGENTA);
			pane.add(title);

			// 给 AI 一个统一头像，或根据需要换图
			pane.add(new JLabel(new ImageIcon(GardenResourceLoader.imgMap.get("face4").getImage())));

			pane.add(new JLabel("生命值: " + ai.getHp()));
			pane.add(new JLabel("魔法阵数量: " + ai.getBubbleNum()));
			pane.add(new JLabel("魔法阵威力: " + ai.getPower()));
			pane.add(new JLabel(String.format("魔法阵爆炸CD: %.2fs", ai.getTtl() / 30.0)));

			statusJPanel.add(pane);
		}

		/* ---------- 4. 重新布局并刷新 ---------- */
		statusJPanel.setLayout(new GridLayout(1, statusJPanel.getComponentCount()));
		statusJPanel.revalidate();
		statusJPanel.repaint();
	}



	// 重写绘画方法
	/**
	 * 绘画时是有固定的顺序，先绘画的图片会在底层，后绘画的图片会覆盖先绘画的
	 * 约定：本方法只执行一次，想实时刷新需要使用多线程
	 */
	@Override
	public void paint(Graphics g) {
	    // 创建双缓冲图像
	    Image bufferImage = createImage(getWidth(), getHeight());
	    Graphics bufferGraphics = bufferImage.getGraphics();

	    // 在缓冲图像上进行绘制
	    super.paint(bufferGraphics);
	    // 所有元素的显示
	    Map<GardenElement, List<GardenObj>> all = em.getGameElements();
	    for (GardenElement ge : GardenElement.values()) {
	        List<GardenObj> list = all.get(ge);
	        for (int i = 0; i < list.size(); i++) {
	            GardenObj obj = list.get(i);
	            obj.showElement(bufferGraphics);
	        }
	    }

	    // 将缓冲图像绘制到面板上
	    g.drawImage(bufferImage, 0, 0, this);

	    // 刷新状态面板
	    refreshStatusPanel();
	}

//	public void paint(Graphics g) {
//		
//		super.paint(g);
//		
//		// 所有元素的显示
//		Map<GameElement, List<ElementObj>> all = em.getGameElements();
//		// GameElement.values()是隐藏方法，无法点进去
//		// 返回的数组的顺序时是枚举变量声明时的顺序
//		for (GameElement ge : GameElement.values()) {
//			List<ElementObj> list = all.get(ge);
////			if(ge.equals(GameElement.MAPS)) {
//////			if(ge == GameElement.MAPS) {
////				Collections.sort(list);
////			}
//			for (int i = 0; i < list.size(); i++) {
//				ElementObj obj = list.get(i);
//				obj.showElement(g);
//			}
//		}
//		refreshStatusPanel();
//		// 重新绘制子组件，否则paint之后会遮挡住组件
//		super.paintChildren(g);	
//	}
	
	@Override
	public void run() {
		while (true) {
			this.repaint();
			// 一般情况下，通过休眠来控制速度
			
			try {
				// 50 ms 刷新一次
				// 即：1秒刷新20次
				Thread.sleep(33); 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
 
}
