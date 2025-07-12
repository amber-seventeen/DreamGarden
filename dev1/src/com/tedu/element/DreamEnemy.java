package com.tedu.element;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import com.tedu.manager.SceneElementManager;
import com.tedu.manager.GardenElement;

/** @说明 
 * 1.寻找玩家并杀死玩家 
 * 2.躲避玩家的攻击 
 * 3.其他如拾取导入 
 */
public class DreamEnemy extends Player {
    private final static int maxW = 15;
    private final static int maxH = 14;
    private final static int eW = 48;
    private final static int eH = 48;
    private GardenElement[][] prmaps = new GardenElement[maxW][maxH];
    private GardenElement[][] maps = new GardenElement[maxW][maxH];
    private SceneElementManager em = SceneElementManager.getManager();

    private int[] dx = { -1, 0, 1, 0, 0 };
    private int[] dy = { 0, -1, 0, 1, 0 };
    
    private int imgX = 0;
	private int imgY = 0; // 0下 1左 2右 3上
	

    private boolean outBoundary(int x, int y) {
        return x < 0 || y < 0 || x >= maxW || y >= maxH;
    }

    private void getBubbleThreads(int x, int y, int r, GardenElement ge) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 1; j <= r; ++j) {// j=1;
                int ax = x + dx[i] * j;
                int ay = y + dy[i] * j;
                if (outBoundary(ax, ay)) {
                    continue;
                }
                if (maps[ax][ay].equals(GardenElement.FLOOR) ||
                        maps[ax][ay].equals(GardenElement.TOOL) ||
                        maps[ax][ay].equals(GardenElement.PLAYER)) {
                    maps[ax][ay] = ge;
                }
            }
        }
    }

    private void getMapElement(GardenElement ge) {
        List<GardenObj> list = em.getElementsByKey(ge);
        for (GardenObj obj : list) {
            int mx = obj.getX() / eW;
            int my = obj.getY() / eH;
            GardenElement type = null;
            if (ge.equals(GardenElement.MAPS)) {
                GardenMap gardenMap = (GardenMap) obj;
                if (gardenMap.getType() == 1) {
                    type = GardenElement.WEAKMAPS;
                } else if (gardenMap.getType() == 0) {
                    type = GardenElement.FLOOR;
                } else {
                    type = ge;
                }
            } else if (ge.equals(GardenElement.PAOPAO)) {
                type = GardenElement.PAOPAO;
                MagicCircle bubble = (MagicCircle) obj;
                getBubbleThreads(mx, my, bubble.getPower(), GardenElement.EXPLODE);
            } else if (ge.equals(GardenElement.EXPLODE)) {
                type = GardenElement.MAPS;
                CircleBurst bubble = (CircleBurst) obj;
                getBubbleThreads(mx, my, bubble.getPower(), GardenElement.MAPS);
            } else {
                type = ge;
            }
            maps[mx][my] = type;
        }
    }

    /** 调试用 */
    public void printMaps() {
        for (int j = 0; j < maxH; ++j) {
            for (int i = 0; i < maxW; ++i) {
                if (maps[i][j] != null) {
                    System.out.print(String.format("%8s", maps[i][j]));
                } else {
                    System.out.print(String.format("%8s", "NULL"));
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int[][] dist = new int[maxW][maxH]; // 最短步数
    private int[][] prev = new int[maxW][maxH]; // 当前这一步可以从上一步怎么走来
    private final static int infinity = maxW * maxH;

    private class node {
        public int x = 0;
        public int y = 0;
        public int d = 0;

        public node(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }

    /** 小根堆比较谓词 */
    private static Comparator<node> cmp = new Comparator<DreamEnemy.node>() {
        public int compare(node lhs, node rhs) {
            if (lhs.d == rhs.d) {
                return Math.random() < 0.5 ? 1 : -1;
            }
            return lhs.d - rhs.d;
        }
    };

    Queue<node> attackTarget = new PriorityQueue<>(cmp);

    private void BFS() {
        int ax = getX() / eW;
        int ay = getY() / eH;
        for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                dist[i][j] = infinity;
                prev[i][j] = -1;// 不可达
            }
        }
        Queue<node> q = new PriorityQueue<>(cmp);
        q.add(new node(ax, ay, 0));
        dist[ax][ay] = 0;
        while (!q.isEmpty()) {
            node p = q.peek();
            q.poll();
            int bx = p.x;
            int by = p.y;
            for (int i = 0; i < 4; ++i) {
                int cx = bx + dx[i];
                int cy = by + dy[i];
                if (outBoundary(cx, cy) || dist[cx][cy] != infinity) {
                    continue;
                }
                prev[cx][cy] = i;
                if (maps[cx][cy].equals(GardenElement.WEAKMAPS)) {
                    attackTarget.add(new node(cx, cy, 2 * infinity + p.d + 1));
                } else if (maps[cx][cy].equals(GardenElement.TOOL)) {
                    attackTarget.add(new node(cx, cy, 1 * infinity + p.d + 1));
                } else if (maps[cx][cy].equals(GardenElement.PLAYER)) {
                    attackTarget.add(new node(cx, cy, 0 * infinity + p.d + 1));
                }
                if (maps[cx][cy].equals(GardenElement.MAPS)
                        || maps[cx][cy].equals(GardenElement.WEAKMAPS)) {
                    dist[cx][cy] = infinity - 1;
                    continue;
                }
                if (maps[cx][cy].equals(GardenElement.PAOPAO) && !(cx == ax && cy == ay)) {
                    dist[cx][cy] = infinity - 1;
                    continue;
                }
                dist[cx][cy] = p.d + 1;
                q.add(new node(cx, cy, dist[cx][cy]));
            }
        }
    }

    private void fillInTheBlank() {
        for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                if (maps[i][j] == null) {
                    maps[i][j] = GardenElement.FLOOR;
                }
            }
        }
    }

    private int stopped = 0;

    private void learnFromPrev() {
        boolean warn = false;
        tag: for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                if (prmaps[i][j] == null) {
                    continue;
                }
                if (prmaps[i][j].equals(GardenElement.PAOPAO) &&
                        !maps[i][j].equals(GardenElement.PAOPAO)) {
                    warn = true;
                    break tag;
                }
            }
        }
        if (warn) {
            stopped = 15;
            System.out.println("fflush");
        }
        if (stopped >= 0 &&
                !maps[getX() / eW][getY() / eH].equals(GardenElement.PAOPAO)) {
            for (int i = 0; i < maxW; ++i) {
                for (int j = 0; j < maxH; ++j) {
                    maps[i][j] = GardenElement.MAPS;
                }
            }
        }
        for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                prmaps[i][j] = maps[i][j];
            }
        }
        --stopped;
    }

    private void learnCurrentMap() {
        GardenElement[] geReq = { GardenElement.MAPS, GardenElement.TOOL,
                GardenElement.PLAYER, GardenElement.PAOPAO, GardenElement.EXPLODE };
        for (GardenElement ge : geReq) {
            getMapElement(ge);
        }
        fillInTheBlank();
        learnFromPrev();
        BFS();
    }

    /** 调试用，一次性输出并清空队列 */
    public void printAttacks() {
        Queue<node> q = attackTarget;
        System.out.println("Attacks:");
        while (!q.isEmpty()) {
            node p = q.peek();
            q.poll();
            System.out.println("" + p.x + "," + p.y + "," + p.d + "," + maps[p.x][p.y]);
        }
    }

    private int moveLeft = 0;
    private int direction = 0;
    
    private int lockX = 0;
    private int lockY = 0;
    
    private int remainX = 0;
    private int remainY = 0;

    private void walk() {
        setX(lockX);
        setY(lockY);
        int speed = getMoveSpeed();
        
        int newx = getX() + dx[direction] * speed;
        int newy = getY() + dy[direction] * speed;
        if (moveLeft == 1) {
            newx = remainX;
            newy = remainY;
        }
        lockX = newx;
        lockY = newy;
        setX(newx);
        setY(newy);
        --moveLeft;
    }

    private boolean checkSafe(int d) {
        int cx = getX() / eW;
        int cy = getY() / eH;
        int nx = cx + dx[d];
        int ny = cy + dy[d];
        if (maps[nx][ny].equals(GardenElement.EXPLODE)) {
            return false;
        }
        return true;
    }

    @Override
    protected void move() {
    }

    private void moveout(int d, boolean safely) {
        if (d >= 0 && d < 4) {
            if (safely && !checkSafe(d)) {
                return;
            }
            int cx = getX() / eW;
            int cy = getY() / eH;
            int nx = cx + dx[d];
            int ny = cy + dy[d];
            if (maps[nx][ny].equals(GardenElement.MAPS) ||
                    maps[nx][ny].equals(GardenElement.WEAKMAPS) ||
                    maps[nx][ny].equals(GardenElement.PAOPAO)) {
                return;
            }
            int speed = getMoveSpeed();
            moveLeft = (eW + speed - 1) / speed;// 上取整
            remainX = getX() + dx[d] * eW;
            remainY = getY() + dy[d] * eH;
            direction = d;
            if(direction==0) this.imgY = 3;
            else if(direction==3) this.imgY=0;
            else this.imgY=direction;
            lockX = getX();
            lockY = getY();
            walk();
        }
    }

    private void moveout(int d) {
        moveout(d, true);
    }

    private int lastNum = getBubbleNum();// 上一回合炸弹数

    private boolean checkSuicide() {
        GardenElement[][] news = new GardenElement[maxW][maxH];
        int safe = 0;
        for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                if (dist[i][j] >= infinity - 1) {
                    news[i][j] = GardenElement.MAPS;
                } else {
                    news[i][j] = maps[i][j];
                }
                if (news[i][j].equals(GardenElement.TOOL) ||
                        news[i][j].equals(GardenElement.PLAYER) ||
                        news[i][j].equals(GardenElement.FLOOR)) {
                    ++safe;
                }
            }
        }
        return safe == 0;
    }

    private void bomb() {
        if (lastNum == 0) {
            lastNum = getBubbleNum();
            return;
        }
        if (checkSuicide()) {
            lastNum = getBubbleNum();
            return;
        }
        setPkType(true);
        add();
        lastNum = getBubbleNum();
    }

    private int neighbor(int ax, int ay, int bx, int by) {
        for (int i = 0; i < 4; ++i) {
            if (bx + dx[i] == ax && by + dy[i] == ay) {
                return i;
            }
        }
        return (ax == ay && bx == by) ? 4 : -1;
    }

    private boolean bombable(int ax, int ay, int bx, int by) {
        for (int i = 0; i < 4; ++i) {
            for (int j = 1; j <= getPower(); ++j) {
                int cx = bx + dx[i] * j;
                int cy = by + dy[i] * j;
                if (outBoundary(cx, cy)) {
                    continue;
                }
                if (cx == ax && cy == ay && (maps[cx][cy].equals(GardenElement.WEAKMAPS)
                        || maps[cx][cy].equals(GardenElement.PLAYER))) {
                    return true;
                }
                if (maps[cx][cy].equals(GardenElement.MAPS) ||
                        maps[cx][cy].equals(GardenElement.WEAKMAPS)) {
                    break;
                }
            }
        }
        return false;
    }

    /** 消极策略：回避炸弹 */
    private void negative() {
        int minDist = infinity;
        int tx = 0;
        int ty = 0;
        int cx = getX() / eW;
        int cy = getY() / eH;
        for (int i = 0; i < maxW; ++i) {
            for (int j = 0; j < maxH; ++j) {
                if (cx == i && cy == j) {
                    continue;
                }
                if (!maps[i][j].equals(GardenElement.EXPLODE) &&
                        !maps[i][j].equals(GardenElement.PAOPAO)
                        && dist[i][j] < minDist) {
                    minDist = dist[i][j];
                    tx = i;
                    ty = j;
                }
            }
        }

        while (true) {
            if (-1 != neighbor(tx, ty, cx, cy)) {
                break;
            }
            int nx = tx - dx[prev[tx][ty]];
            int ny = ty - dy[prev[tx][ty]];
            tx = nx;
            ty = ny;
        }
        moveout(neighbor(tx, ty, cx, cy), false);
    }

    /** 积极策略：攻击玩家->拾取道具->攻击墙 */
    private void positive(node target) {
        int tx = target.x;
        int ty = target.y;
        int ox = tx;
        int oy = ty;
        int cx = getX() / eW;
        int cy = getY() / eH;
        while (true) {
            if (neighbor(tx, ty, cx, cy) != -1) {
                break;
            }
            if (!maps[ox][oy].equals(GardenElement.TOOL) &&
                    bombable(tx, ty, cx, cy)) {
                break;
            }
            if (prev[tx][ty] == -1) {
                break;
            }
            int nx = tx - dx[prev[tx][ty]];
            int ny = ty - dy[prev[tx][ty]];
            tx = nx;
            ty = ny;
        }
        if (maps[ox][oy].equals(GardenElement.TOOL)) {
            moveout(neighbor(tx, ty, cx, cy));
        } else {
            if (bombable(ox, oy, cx, cy)) {
                bomb();
            } else {
                moveout(neighbor(tx, ty, cx, cy));
            }
        }
    }

    private void decide() {
        if (maps[getX() / eW][getY() / eH].equals(GardenElement.EXPLODE) ||
                maps[getX() / eW][getY() / eH].equals(GardenElement.PAOPAO)) {
            negative();
            return;
        }
        if (!attackTarget.isEmpty()) {
            node target = attackTarget.peek();
            attackTarget.poll();
            positive(target);
        }
    }

    public void automate() {
        if (moveLeft != 0) {
            walk();
            return;
        }
        learnCurrentMap();
        decide();
    }
}
