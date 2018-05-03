package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GamePanel extends JPanel implements Runnable{
	boolean isLive=true;//判断线程是否存活
	boolean isPause=false;//是否暂停本线程
	public GamePanel(){
		
		this.setBackground(Color.gray);
	}
//	public void newGame(){
//		Info.Init();
//		Info.myTank=new MyTank(Info.bornX,Info.bornY);//新建我的坦克
//		//新建Info.enemyNum辆敌方坦克
//		EnemyTank en=null;
//		Info.enemyTanks=new Vector<EnemyTank>();
//		for(int i=0;i<Info.enemyNum;i++){
//			en=new EnemyTank(20+i*50,20,0);
//			Info.enemyTanks.add(en);
//			Thread t=new Thread(en);
//			t.start();
//		}
//	}
	public void oldGame(){
		
	}
	public void paint(Graphics g){
		super.paint(g);
		//画出基地
		g.drawImage(Info.hq, Info.bornX-30, Info.bornY+15, 60, 40, this);
		//画出我的坦克
		if(Info.myTank.isLive())
		paintTank(Info.myTank.getX(),Info.myTank.getY(),g,Info.myTank.getDirection(),Info.myTank.getColor());
		//画出所有敌方坦克
		EnemyTank en=null;
		for(int i=0;i<Info.enemyTanks.size();i++){
			en=Info.enemyTanks.get(i);
			paintTank(en.getX(),en.getY(),g,en.getDirection(),en.getColor());
		}
		//画出我方的所有子弹
		paintAllMyBullet(g);
		//画出敌方的所有子弹
		paintAllEnemyBullet(g);
		//画出所有爆炸
		for(int i=0;i<Info.bomb.size();i++){
			//取出一个爆炸
			Bomb bo=Info.bomb.get(i);
			if(bo.isLive()){//如果爆炸存活，根据生存时间画出
				if(bo.getLife()>6)
					g.drawImage(Info.bomb1, bo.getX(), bo.getY(), 30, 30,this);
				else if(bo.getLife()>3)
					g.drawImage(Info.bomb2, bo.getX(), bo.getY(), 30, 30,this);
				else
					g.drawImage(Info.bomb3, bo.getX(), bo.getY(), 30, 30,this);
			}
		}
	}
	public void run() {
		int count=0;//计时器
		while(isLive){
			count++;//计时器加1
			//System.out.println("一个游戏面板线程正在运行");
			try {
				do{
					Thread.sleep(1000/Info.fps);//每隔1/fps秒对GamePanel面板进行重绘
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//减少爆炸的生存时间
			for(int i=0;i<Info.bomb.size();i++){
				//取出一个爆炸
				Bomb bo=Info.bomb.get(i);
				//如果爆炸存活，生存时间减少
				if(bo.isLive()){
					bo.setLife(bo.getLife()-1);
				}
				//如果爆炸生存时间为0，爆炸死亡，并将其移出数组
				if(bo.getLife()==0){
					bo.setLive(false);
					Info.bomb.remove(bo);
				}
			}
			
			
			this.repaint();
		}
		
	}
	public void closePanel(){
		//关闭所有坦克进程并将坦克从世界中移除
		for(int i=0;i<Info.enemyTanks.size();i++){
			EnemyTank en=Info.enemyTanks.get(i);
			//关闭该坦克所有子弹进程
			for(int j=0;j<en.getClip().size();j++){
				en.getClip().get(j).setLive(false);
			}
			en.setLive(false);
			Info.enemyTanks.remove(en);
		}
		//关闭我的坦克所有子弹进程
		for(int i=0;i<Info.myTank.getClip().size();i++){
			Info.myTank.getClip().get(i).setLive(false);
		}
		//设置我的坦克死亡
		Info.myTank.setLive(false);
		//设置本游戏面板线程死亡
		this.setLive(false);
	}
	//画坦克
	public static void paintTank(int x,int y,Graphics g,int direction,int color){
		//初始化配色方案，col1为车身颜色，col2为炮管颜色
		Color col1=Color.green ,col2=Color.black;
		//设置坦克配色方案
		if(color>-1)
			col1=col2=EnemyTankLevel.lvColor[color];
		
		//根据方向画出坦克
		g.setColor(col1);
		switch(direction){
		case 0://向上
			g.fill3DRect(x-10, y-15, 5, 30,false);//左轮
			g.fill3DRect(x-5, y-10, 11, 20,false);//中
			g.fill3DRect(x+6, y-15, 5, 30,false);//右轮
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);//圆
			g.drawLine(x, y-15, x, y);//炮管
			break;
		case 1://向右
			g.fill3DRect(x-15, y-10, 30, 5,false);//左轮
			g.fill3DRect(x-10, y-5, 20, 11,false);
			g.fill3DRect(x-15, y+6, 30, 5,false);//右轮
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y, x+15, y);
			break;
		case 2://向下
			g.fill3DRect(x-10, y-15, 5, 30,false);//右轮
			g.fill3DRect(x-5, y-10, 11, 20,false);
			g.fill3DRect(x+6, y-15, 5, 30,false);//左轮
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y+15, x, y);
			break;
		case 3://向左
			g.fill3DRect(x-15, y-10, 30, 5,false);//右轮
			g.fill3DRect(x-10, y-5, 20, 11,false);
			g.fill3DRect(x-15, y+6, 30, 5,false);//左轮
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y, x-15, y);
			break;
		}
	}
	
	//画子弹
	private void paintBullet(Bullet bullet,Graphics g){
		int x=bullet.getX();
		int y=bullet.getY();
		g.setColor(Color.black);
		g.fill3DRect(x-1, y-1, 3, 3, false);
	}
	//画出所有我方子弹，并进行相应处理
	private void paintAllMyBullet(Graphics g){
		for(int i=0;i<Info.myTank.getClip().size();i++){
			//从弹夹中取出子弹
			Bullet bu=Info.myTank.getClip().get(i);
			if(bu.isLive()){
				//如果子弹存活则画出子弹
				paintBullet(bu,g);
				//如果子弹与任何敌方坦克碰撞，则坦克与子弹皆死亡
				EnemyTank en=null;
				for(int j=0;j<Info.enemyTanks.size();j++){
					//取出坦克
					en=Info.enemyTanks.get(j);
					//判断是否击中
					boolean hit=false;
					//取出子弹和坦克的坐标
					int x1=bu.getX(),x2=en.getX(),y1=bu.getY(),y2=en.getY();
					//根据方向判断是否击中
					switch(en.getDirection()){
					case 0://向上
					case 2://向下
						if(x1>=x2-10&&x1<=x2+10 && y1>=y2-15&&y1<=y2+15)
							hit=true;
						break;
					case 1://向右
					case 3://向左
						if(x1>=x2-15&&x1<=x2+15 && y1>=y2-10&&y1<=y2+10)
							hit=true;
						break;
					}
					//如果击中，子弹和坦克均死亡,并将其从数组中移除,产生一个爆炸
					if(hit==true){
						System.out.println("击中敌方坦克------------------------");
						//增加分数
						Info.addScore(en.getScore());
						//坦克和子弹死亡
						en.setLive(false);
						bu.setLive(false);
						//根据坦克中心坐标新建一个爆炸，并加入数组
						Bomb bo=new Bomb(en.getX(),en.getY());
						Info.bomb.add(bo);
						//移除坦克和子弹
						Info.enemyTanks.remove(en);
						Info.myTank.getClip().remove(bu);
					}
				}
			}else{
				//如果子弹死亡则将子弹从弹夹中移除
				Info.myTank.getClip().remove(bu);
			}
		}
	}
	//画出所有敌方子弹，并进行处理
	private void paintAllEnemyBullet(Graphics g){
		for(int i=0;i<Info.enemyTanks.size();i++){
			//取出一辆坦克
			EnemyTank en=Info.enemyTanks.get(i);
			for(int j=0;j<en.getClip().size();j++){
				//从弹夹中取出子弹
				Bullet bu=en.getClip().get(j);
				if(bu.isLive()){
					//如果子弹存活则画出子弹
					paintBullet(bu,g);
					//如果我的坦克已死亡，不用进行是否击中的判断
					if(Info.myTank.isLive()==false)
						continue;
					//判断是否击中
					boolean hit=false;
					//取出子弹和坦克的坐标
					int x1=bu.getX(),x2=Info.myTank.getX(),y1=bu.getY(),y2=Info.myTank.getY();
					//根据方向判断是否击中
					switch(Info.myTank.getDirection()){
					case 0://向上
					case 2://向下
						if(x1>=x2-10&&x1<=x2+10 && y1>=y2-15&&y1<=y2+15)
							hit=true;
						break;
					case 1://向右
					case 3://向左
						if(x1>=x2-15&&x1<=x2+15 && y1>=y2-10&&y1<=y2+10)
							hit=true;
						break;
					}
					//如果击中，子弹和坦克均死亡,并将其从数组中移除,产生一个爆炸
					if(hit==true){
						System.out.println("我方被击中-----------------------");
						
						bu.setLive(false);//子弹死亡
						//根据坦克中心坐标新建一个爆炸，并加入数组
						Bomb bo=new Bomb(Info.myTank.getX(),Info.myTank.getY());
						Info.bomb.add(bo);
						//移除子弹,我方坦克不是线程，不用移除
						en.getClip().remove(bu);
						
						//坦克死亡判断
						Info.myTank.setLive(false);
						Info.MyLife--;
						if(Info.MyLife<=0){
							JOptionPane.showMessageDialog(null, "gameover!!");
						}else{
							Info.myTank=new MyTank(Info.bornX,Info.bornY);
						}
					}
				
				}else{
					//如果子弹死亡则将子弹从弹夹中移除
					en.getClip().remove(bu);
				}
			}
			
		}
	}


	
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
}