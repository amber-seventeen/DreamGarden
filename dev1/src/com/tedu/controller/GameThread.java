package com.tedu.controller;

import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;


import com.tedu.element.DreamEnemy;
import com.tedu.element.Player;
import com.tedu.element.GardenObj;
import com.tedu.manager.SceneElementManager;
import com.tedu.manager.GardenElement;
import com.tedu.manager.GardenResourceLoader;
import com.tedu.manager.GardenBGMPlayer;
import com.tedu.show.DreamGardenFrame;

/**
 * 游戏的主线程，用于控制游戏的加载
 * 
 * @功能 控制游戏加载，关卡，游戏运行自动化
 *     游戏判定：地图切换，资源释放和重新读取
 * 
 * @说明 为了使用另一种方式创建线程，这里使用继承Thread
 *     一般使用接口方式实现
 * 
 * @author cjc
 * @create 2023年7月10日
 */
public class GameThread extends Thread {

	// 联动元素管理器
	private SceneElementManager em;
	// 游戏进行时的背景音乐
	private GardenBGMPlayer bgm;

	// 选择地图
	private int map = 0; // 默认值为0，即未选择地图

	public int getMap() {
		return this.map;
	}

	public void setMap(int map) {
		this.map = map;
	}

	private int vectory = 1;// 设置是否胜利，0代表胜利
	
	private static long gameTime = 3L;

	public static long getGameTime() {
		return gameTime;
	}

	// 游戏进程是否结束
	private boolean isOver = false;

	// 暂停判定符
	private boolean isPause;

	private int mode;
	
	//
	/**  选择角色 character1 第一玩家 
	 *           character2 第二玩家 */
	private static int character1;
	public static int getCharacter1() {
		return character1;
	}

	private static int character2;
	public static int getCharacter2() {
		return character2;
	}

	/** @param mode 1单人 2双人 */
	public GameThread(int map, int mode, int character1, int character2) {
		this.mode = mode;
		this.map = map;
		GameThread.character1=character1;
		this.character2=character2;
		em = SceneElementManager.getManager();

	}
	
	@Override
	public void run() {
		// super.run();

		// 扩展，可以将true变为一个变量用于游戏进程控制（例如：暂停）
		// while (true) {
		// 游戏开始前：读进度条，加载游戏资源（场景）
		gameLoad();

		// 游戏进行时
		gameRun();

		// 游戏场景结束时：游戏资源回收
		gameOver();

		// try {
		// // 由于继承了Thread类，可以直接调用sleep
		// sleep(50);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

	}

	/**
	 * 游戏加载
	 */
	private void gameLoad() {
		// System.out.println("gameLoad");

		// 加载图片资源。注意必须在加载地图、人物之前
		GardenResourceLoader.loadImg();

		// 加载游戏音乐（包括音效和背景音乐）
		GardenResourceLoader.loadMusic();

		// 加载地图，10 可以设置成变量，切换关卡
		GardenResourceLoader.MapLoad(this.map);

		/*
		 * 加载玩家。可以考虑传参，来控制单人或双人
		 * 示例："100,100,player";
		 * 参数说明：
		 * 初始x的坐标，初始y的坐标，图片的key（对应GameData.pro中的key）
		 * 
		 * 最后一个参数用于记录玩家1号还是2号
		 * mode=3代表双人且联机
		 */
		if(mode==2) {
			if(character1==1&&character2==2) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1",
						"528,480,player2,65,87,68,83,32,2");
			}
			else if(character1==1&&character2==3) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1",
						"528,480,player3,65,87,68,83,32,2");
			}
			else if(character1==2&&character2==3) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1",
						"528,480,player3,65,87,68,83,32,2");
			}
			else if(character1==2&&character2==1) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1",
						"528,480,player1,65,87,68,83,32,2");
			}
			else if(character1==3&&character2==1) {
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1",
						"528,480,player1,65,87,68,83,32,2");
			}
			else if(character1==3&&character2==2) {
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1",
						"528,480,player2,65,87,68,83,32,2");
			}
			
			else if(character1==1&&character2==1) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1",
						"528,480,player1,65,87,68,83,32,2");
			}
			else if(character1==2&&character2==2) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1",
						"528,480,player2,65,87,68,83,32,2");
			}
			else if(character1==3&&character2==3) {
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1",
						"528,480,player3,65,87,68,83,32,2");
			}
		}
		/*----------------------------------------------------------
		 * mode == 4   两人本地 + 两个 AI 敌人
		 * ---------------------------------------------------------*/
		else if (mode == 4) {

			/* ---------- 1. 先加载两名本地玩家 ---------- */
			if (character1 == 1 && character2 == 2) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1",
						"528,480,player2,65,87,68,83,32,2");
			} else if (character1 == 1 && character2 == 3) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1",
						"528,480,player3,65,87,68,83,32,2");
			} else if (character1 == 2 && character2 == 1) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1",
						"528,480,player1,65,87,68,83,32,2");
			} else if (character1 == 2 && character2 == 3) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1",
						"528,480,player3,65,87,68,83,32,2");
			} else if (character1 == 3 && character2 == 1) {
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1",
						"528,480,player1,65,87,68,83,32,2");
			} else { // (3,2) 或两人选同角色等剩余情况
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1",
						"528,480,player2,65,87,68,83,32,2");
			}

			/* ---------- 2. 再加载两名 AI 敌人 ---------- */
			// 注意最后一个数字改成 3、4 只用来区分“第三、第四个角色”，
			// 关键在于前 3 个字段：x, y, 图片 key
			GardenResourceLoader.loadEnemy("288,288,enemy1,0,0,0,0,0,3");
			GardenResourceLoader.loadEnemy("432,144,enemy1,0,0,0,0,0,4");
		}
//		else if(mode==3)
//		{
//			connected=true;
//		if(Network.job==false)
//			GameLoad.loadPlayer("528,480,player2,37,38,39,40,10,2");
//			else {
//				GameLoad.loadPlayer("144,144,player1,37,38,39,40,10,1");
//			}
//		}
		else {
			if(character1==1) {
				GardenResourceLoader.loadPlayer("144,144,player1,37,38,39,40,10,1");
				GardenResourceLoader.loadEnemy("528,432,enemy1,65,87,68,83,32,2");
			}
			
			else if(character1==2) {
				GardenResourceLoader.loadPlayer("144,144,player2,37,38,39,40,10,1");
				GardenResourceLoader.loadEnemy("528,432,enemy1,65,87,68,83,32,2");
			}
			
			else if(character1==3) {
				GardenResourceLoader.loadPlayer("144,144,player3,37,38,39,40,10,1");
				GardenResourceLoader.loadEnemy("528,432,enemy1,65,87,68,83,32,2");
			}
		}
		
		// GameLoad.loadEnemy("144,144,player2,65,87,68,83,32,2");
		// GameLoad.loadPlayer("144,144,player1,37,38,39,40,17,1");
		
		// 加载NPC...

	}

	/**
	 * 游戏进行时
	 * 
	 * @任务说明 1、自动化玩家的移动、碰撞等
	 *       2、新元素的增加（例如：NPC挂了之后出现道具）
	 *       3、暂停
	 */
	

	private void gameRun() {

		// 开始循环播放背景音乐。暂时放这里，可能会改位置
		bgm = GardenResourceLoader.musicMap.get("bgm0").setLoop(true);
		// bgm.play();
	
		
		// 预留扩展，true可以改为变量，用于控制关卡结束等
		while (!isOver) {
			// System.out.println("gameRun");
			if (!isPause) {
				
				Map<GardenElement, List<GardenObj>> all = em.getGameElements();

				
				fclicked();
				moveAndUpdate(all);

				// 约定：第一个参数：碰撞的主动方；第二个参数：被碰撞的一方
				elementsCollide(GardenElement.PLAYER, GardenElement.MAPS); // Player和障碍物
				elementsCollide(GardenElement.EXPLODE, GardenElement.PLAYER); // 泡泡爆炸和Player
				elementsCollide(GardenElement.EXPLODE, GardenElement.MAPS); // 泡泡爆炸和地图
				elementsCollide(GardenElement.EXPLODE, GardenElement.ENEMY);
				elementsCollide(GardenElement.PLAYER, GardenElement.TOOL); // Player和道具
				elementsCollide(GardenElement.ENEMY, GardenElement.TOOL);
				elementsCollide(GardenElement.EXPLODE, GardenElement.PAOPAO); // 泡泡爆炸和泡泡
				elementsCollide(GardenElement.PLAYER, GardenElement.PAOPAO); // player和泡泡

				List<GardenObj> enemyList = em.getElementsByKey(GardenElement.ENEMY);
				for (GardenObj obj : enemyList) {
					if (obj instanceof DreamEnemy) {
						DreamEnemy e = (DreamEnemy) obj;
						e.automate();
					}
				}

				gameTime++;
				
				checkEnd(GardenElement.PLAYER);
				checkEnd(GardenElement.ENEMY);
				
			}
			try {
				sleep(33);//一秒刷新30次
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkEnd(GardenElement ge) {
		List<GardenObj> playerList = SceneElementManager.getManager().getElementsByKey(ge);
		for (GardenObj player : playerList) {
			Player player1 = (Player) player;
			if (!player1.isLive()) {
				vectory--;
				if (vectory == 0) {
					String msg = "请确定按结束游戏";
					if (player1.getPlayerNum() == 1) {
						if (mode == 2) {
							msg = "魔法少女2号胜利~" + msg;
						} else {
							msg = "很可惜~你输了~" + msg;
						}
					} else if (player1.getPlayerNum() == 2) {
						if (mode == 2) {
							msg = "魔法少女1号胜利~" + msg;
						} else {
							msg = "太棒了~你赢了~" + msg;
						}
					}
					if (player1.getPlayerNum() == 1) {
						Object[] options = { "确定" };
						JOptionPane.showMessageDialog(null,
								msg, "提示", JOptionPane.INFORMATION_MESSAGE);
						isOver = true;
					}
					if (player1.getPlayerNum() == 2) {
						Object[] options = { "确定" };
						JOptionPane.showMessageDialog(null,
								msg, "提示", JOptionPane.INFORMATION_MESSAGE);
						isOver = true;
					}
					vectory = 1;
				}
			}
		}
	}

	/**
	 * 注意，做一种约定
	 * 
	 * @param eleA 碰撞的主动方
	 * @param eleB 被撞的一方
	 */
	public void elementsCollide(GardenElement eleA, GardenElement eleB) {
		List<GardenObj> listA = em.getElementsByKey(eleA);
		List<GardenObj> listB = em.getElementsByKey(eleB);
		// 人物和道具之间的碰撞设置
		if (eleB == GardenElement.TOOL) {
			for (GardenObj g1 : listA) {
				for (GardenObj g2 : listB) {
					if (g2.collide(g1)) {
						return;
					}
				}
			}
		}
		// 泡泡爆炸和地图之间的碰撞设置
		if (eleA == GardenElement.EXPLODE && eleB == GardenElement.MAPS) {
			for (GardenObj g1 : listA) {
				for (GardenObj g2 : listB) {
					if (g1.collide(g2)) {
						g2.setLive(false);
						return;
					}
				}
			}
		}

		// 泡泡爆炸和泡泡之间的碰撞设置
		if (eleA == GardenElement.EXPLODE && eleB == GardenElement.PAOPAO) {
			for (GardenObj g1 : listA) {
				for (GardenObj g2 : listB) {
					if (g1.collide(g2)) {
						g2.setLive(false);
						return;
					}
				}
			}
		}

		// player和泡泡之间的碰撞设置
		if (eleA == GardenElement.PLAYER && eleB == GardenElement.PAOPAO) {
			for (GardenObj g1 : listA) {
				for (GardenObj g2 : listB) {
					if (g1.collide(g2)) {
					}
				}
			}
		}
		for (GardenObj a : listA) {
			for (GardenObj b : listB) {
				if (a.collide(b)) {

					// 爆炸时碰撞，未爆炸时的不碰撞
					if (eleA.equals(GardenElement.EXPLODE)
							&& (eleB.equals(GardenElement.PLAYER) ||
									eleB.equals(GardenElement.ENEMY))) {
						b.die(gameTime);
						// System.out.println(b);
						if (b.isLive()) {
							Player player = (Player) b;
							player.setBoom(true);
						}
					}
				}
			}
		}

	}

	public void moveAndUpdate(Map<GardenElement, List<GardenObj>> all) {
		// GameElement.values()是隐藏方法，无法点进去
		// 返回的数组的顺序时是枚举变量声明时的顺序

		for (GardenElement ge : GardenElement.values()) {
			List<GardenObj> list = all.get(ge);
			// 操作集合不要使用迭代器foreach，修改数据会抛出异常
			// for (int i = 0; i < list.size(); i++) {
			for (int i = list.size() - 1; i >= 0; i--) {
				GardenObj obj = list.get(i);

				// 如果元素处于消亡状态，将它从元素管理器中移除
				if (!obj.isLive()) {

					obj.die(gameTime); // 调用死亡方法
					em.removeElement(i, ge);
					continue;
				}
				obj.count(gameTime);
				obj.model(gameTime);
			}
		}
	}

	/**
	 * 游戏切换关卡
	 */
	private void gameOver() {
		// 情况元素管理器里面的所有对象
		SceneElementManager.getManager().clearAll();
		DreamGardenFrame.setJPanel("OverJPanel");
	}

	/**
	 * 游戏暂停状态设置
	 * 
	 * @return
	 */
	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean pause) {
		isPause = pause;
	}

	/*
	 * protected void load() {
	 * ImageIcon icon = new ImageIcon("image/tank/play1/player1_up.png");
	 * ElementObj obj = new Play(100, 100, 50, 50, icon);
	 * em.addElement(GameElement.PLAY, obj);
	 * 
	 * // 添加敌人
	 * for (int i = 0; i < 10; i++) {
	 * em.addElement(GameElement.ENEMY, new Enemy().createElement(""));
	 * }
	 * 
	 * // 注意：只讲子弹的发射和死亡。思考：道具的掉落是否与子弹的发射相近？
	 * 
	 * // 添加一个敌人类，仿照玩家类编写，注意不需要实现键盘监听
	 * // 实现敌人的显示，同时实现最简单的移动。
	 * // 例如：在(100, 100)和(500, 100)之间来回移动
	 * 
	 * // obj = new Play(0, 0, 300, 300, icon);
	 * // em.addElement(GameElement.MAPS, obj);
	 * //
	 * // obj = new Play(200, 200, 50, 50, icon);
	 * // em.addElement(GameElement.ENEMY, obj);
	 * 
	 * }
	 */
	/**
	 * 玩家被炸后闪烁
	 */
	private int fclickedTime = 0; // 闪烁次数

	private void fclicked() {
		List<GardenObj> playerList = SceneElementManager.getManager().getElementsByKey(GardenElement.PLAYER);
		for (GardenObj player : playerList) {
			Player player1 = (Player) player;
			if (player1.isBoom()) {
				if (fclickedTime < 48) {
					if (player1.getFclickedY() == 48) {
						player1.setFclickedY(0);
					} else {
						player1.setFclickedY(48);
					}
					fclickedTime++;
				} else {
					fclickedTime = 0;
					player1.setBoom(false);
				}
				player1.model(gameTime);
			}
		}
	}
	

	
	public GardenBGMPlayer getBgm() {
		return bgm;
	}

	public boolean getIsOver() {
		return isOver;
	}

	public void setIsOver(boolean isOver) {
		this.isOver = isOver;
	}


	
}
