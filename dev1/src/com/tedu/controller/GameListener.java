package com.tedu.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tedu.element.GardenObj;
import com.tedu.manager.SceneElementManager;
import com.tedu.manager.GardenElement;
import com.tedu.show.DreamGardenFrame;

/**
 * 监听的实现类，用于监听用户的操作
 * 
 * @author cjc
 * @create 2023年7月10日
 */
public class GameListener implements KeyListener {
	private SceneElementManager em = SceneElementManager.getManager();
	
	// 记录按下的方向键
	private Set<Integer> set = new HashSet<>();
	//记录当前暂停状态
	private boolean isPause=false;
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 左：37
	 * 上：38
	 * 右：39
	 * 下：40
	 */
	@Override
	public void keyPressed(KeyEvent e) {
//		System.out.println("keyPressed: " + e.getKeyCode());
		int key = e.getKeyCode();
		if (set.contains(key)) {
			// 防止按住一个方向键不放，连续多次修改状态
			// 之所以用set集合，是因为可能不只有4个方向键，可能还有其他键
			return;
		}
		set.add(key);
		
		// 拿到玩家集合
		List<GardenObj> playList = em.getElementsByKey(GardenElement.PLAYER);
		for (GardenObj obj : playList) {
			obj.keyClick(true, key);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//System.out.println("keyReleased: " + e.getKeyCode());
		
		int key = e.getKeyCode();
		if (!set.contains(key)) { // 不  存在
			return;
		}
		set.remove(key);
		
		List<GardenObj> playList = em.getElementsByKey(GardenElement.PLAYER);
		for (GardenObj obj : playList) {
			obj.keyClick(false, key);
		}
		if(key==80)
		{
			isPause=!isPause;
			((GameThread) DreamGardenFrame.gj.getThread()).setPause(isPause);
//			GameThread.setPause(isPause);
		}
	}

}
