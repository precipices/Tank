package com.tank;

import java.util.*;

import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
class GameTime extends Thread{
	private boolean timeRun=false;
	private int hour=0;
	private int minutes=0;
	private int second=0;
	private int bs=0;
	public GameTime() {
		this.hour=0;
		this.minutes=0;
		this.second=0;
		this.bs=0;
	}
	public void clear(){
		this.hour=0;
		this.minutes=0;
		this.second=0;
		this.bs=0;
	}
	public GameTime(int hour,int minutes,int second,int ms) {
		this.hour=hour;
		this.minutes=minutes;
		this.second=second;
		this.bs=bs;
	}
	public void setTime(int hour,int minutes,int second,int ms) {
		this.hour=hour;
		this.minutes=minutes;
		this.second=second;
		this.bs=bs;
	}
	private int addTime(){
		if(bs<=98) {
			bs++;
			return 0;
		}
		else if(second<=58) {
			second++;bs=0;
			return 1;
		}
		else if(minutes<=58) {
			minutes++;second=0;
			return 2;
		}
		else {
			hour++;minutes=0;
			return 3;
		}
	}
	public void run(){
		while(true){
			try {
				do{
					Thread.sleep(10);
				}while(!timeRun);
				
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//System.out.println("世界时间运行中");
			//根据时间产生坦克
			int n=this.addTime();
			if(n>0&&Info.isNewEnemy&&!Info.isPause){
				switch(n){
				case 1://秒增加
					if(second%10==0&&second!=0)newEnemy(0);
					if(second%20==0&&second!=0)newEnemy(1);
					if(second%30==0&&second!=0)newEnemy(2);
					break;
				case 2://分增加
					if(minutes%1==0&&second!=0)newEnemy(3);
					if(minutes%2==0&&second!=0)newEnemy(4);
					if(minutes%3==0&&second!=0)newEnemy(5);
					break;
					
				}
			}
			
		}
	}

	public void newEnemy(int level){
		EnemyTank en=new EnemyTank((int)(Math.random()*(MyTankGame.width-11-11))+11,
				(int)(Math.random()*(MyTankGame.height-16-16))+16,level);
		Info.enemyTanks.add(en);
		Thread t=new Thread(en);
		t.start();
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getBs() {
		return bs;
	}
	public void setBs(int bs) {
		this.bs = bs;
	}
	public boolean isTimeRun() {
		return timeRun;
	}
	public void setTimeRun(boolean timeRun) {
		this.timeRun = timeRun;
	}
}
//世界信息，整个游戏所有类均可访问及更改
class Info{
	public static String status="prepare";//prepare,begin,newgame,oldgame
	public static int score=0;
	public static void addScore(int tankScore){
		score+=tankScore;
	}
	public static Key k=new Key();//键盘监听器
	public static GameTime worldTime=new GameTime();
	public static int fps=25;//画面流畅度，每秒多少帧
	public static int bornX=MyTankGame.width/2,bornY=MyTankGame.height-55;
	//public static int enemyBornX=200,enemyBornY=15;
	//public static int addTankTime=100;//坦克增加的时间
	public static boolean isPause=false;//是否暂停游戏
	public static boolean isNewEnemy=true;//是否产生新敌人
	public static int MyLife=5;//我剩余的生命数
	public static int enemyNum=16;//初始敌方坦克数量
	public static Vector<EnemyTank> enemyTanks=new Vector<EnemyTank>();
	public static MyTank myTank=null;//我的坦克
	public static Vector<Bomb> bomb=new Vector<Bomb>();//爆炸类数组,储存面板上所有的爆炸
	//导入爆炸效果图
	public static Image bomb1=new ImageIcon("imgs/bomb_1.gif").getImage();
	public static Image bomb2=new ImageIcon("imgs/bomb_2.gif").getImage();
	public static Image bomb3=new ImageIcon("imgs/bomb_3.gif").getImage();
	//导入基地图
	public static Image hq=new ImageIcon("imgs/headquarters.jpg").getImage();
	
	public static void FileInit(){
		//检查文件夹是否存在，如不存在则创建
		File f=new File("save");
		if(!f.isDirectory())
			f.mkdir();
	}
	public static void DataInit(){
		MyLife=5;
		score=0;
		//worldTime=new GameTime();
		isPause=false;
		isNewEnemy=true;
		enemyTanks=new Vector<EnemyTank>();
	}
	public static boolean checkSave(){
		File f=new File("save\\save.txt");
		if(f.exists())
			return true;
		JOptionPane.showMessageDialog(null, "没有存档！");
		return false;
	}
	public static void save(){
		FileInit();
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			fw=new FileWriter("save\\save.txt");//FileWriter函数当文件不存在时会自动创建
			bw=new BufferedWriter(fw);
			//写下相关数据
			bw.write(worldTime.getHour()+" "+worldTime.getMinutes()
				+" "+worldTime.getSecond()+" "+worldTime.getBs()+"\r\n");
			bw.write(MyLife+"\r\n");
			bw.write(score+"\r\n");
			bw.write(enemyTanks.size()+"\r\n");
			//写下我的坦克的数据
			bw.write(myTank.getX()+" "+myTank.getY()+" "+myTank.getDirection()+" "+myTank.getClip().size()+" "+myTank.isLive()+"\r\n");
			//写下我的坦克的所有子弹的数据
			for(int j=0;j<myTank.getClip().size();j++){
				Bullet bu=myTank.getClip().get(j);
				bw.write(bu.getX()+" "+bu.getY()+" "+bu.getDirection()+"\r\n");
			}
			//写下所有敌方坦克及其子弹数据
			for(int i=0;i<enemyTanks.size();i++){
				EnemyTank en=enemyTanks.get(i);
				bw.write(en.getX()+" "+en.getY()+" "+en.getLevel()+" "+en.getDirection()+" "+en.getClip().size()+"\r\n");
				for(int j=0;j<en.getClip().size();j++){
					Bullet bu=en.getClip().get(j);
					bw.write(bu.getX()+" "+bu.getY()+" "+bu.getDirection()+"\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void read(){
		FileInit();
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fr=new FileReader("save\\save.txt");
			br=new BufferedReader(fr);
			String[] sList=null;
			int n=0;//用于临时计数
			Vector<Bullet>clip=null;
 			//读取相关数据
			//bw.write(worldTime.getHour()+" "+worldTime.getMinutes()
			//+" "+worldTime.getSecond()+" "+worldTime.getBs()+"\r\n");
			sList=br.readLine().split(" ");
			worldTime.setTime(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),Integer.parseInt(sList[3]));
			MyLife=Integer.parseInt(br.readLine());
			score=Integer.parseInt(br.readLine());
			int enemyNum=Integer.parseInt(br.readLine());
			//读取我的坦克的数据
			sList=br.readLine().split(" ");
			myTank=new MyTank(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]));
			//System.out.println(sList.length);
			myTank.setDirection(Integer.parseInt(sList[2]));
			n=Integer.parseInt(sList[3]);//子弹数
			myTank.setLive(Boolean.parseBoolean(sList[4]));
			//读取我的坦克的所有子弹的数据
			clip=myTank.getClip();
			for(int j=0;j<n;j++){
				sList=br.readLine().split(" ");
				Bullet bu=new Bullet(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),myTank.getBulletSpeed());
				clip.add(bu);
				Thread t3=new Thread(bu);
				t3.start();//启动子弹线程
			}
			
			//读取所有敌方坦克及其子弹数据
			enemyTanks=new Vector<EnemyTank>();
			for(int i=0;i<enemyNum;i++){//enemyNum是前面刚读取的
				//读取敌方坦克数据
				sList=br.readLine().split(" ");
				EnemyTank en=new EnemyTank(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]));
				en.setDirection(Integer.parseInt(sList[3]));
				Thread t=new Thread(en);
				t.start();//启动坦克线程
				enemyTanks.add(en);//将敌方坦克添加到数组中
				n=Integer.parseInt(sList[4]);//子弹数量
				clip=en.getClip();//得到该坦克弹夹
				//读取该坦克子弹数据
				for(int j=0;j<n;j++){
					sList=br.readLine().split(" ");
					Bullet bu=new Bullet(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),en.getBulletSpeed());
					clip.add(bu);//将子弹添加到弹夹
					Thread t2=new Thread(bu);
					t2.start();//启动子弹线程
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class EnemyTankLevel{
	int level=0;
	int color=0;//颜色，默认为方案0
	int speed=1;//速度，默认为1
	int bulletSpeed=1;//子弹速度，默认为1
	int clipNum=5;//弹夹中可容纳的子弹数量
	int score=1;//击中后的得分
	int changeDirect=10;//走多少步换方向
	int shootTime=10;//走多少步发射子弹
	String name=null;
	public EnemyTankLevel(int level,int color,int speed,int bulletSpeed,
			int clipNum,int score,int changeDirect,int shootTime,String name) {
		this.level=level;
		this.color=color;
		this.speed=speed;
		this.bulletSpeed=bulletSpeed;
		this.clipNum=clipNum;
		this.score=score;
		this.changeDirect=changeDirect;
		this.shootTime=shootTime;
		this.name=name;
	}
	//灰白绿蓝紫橙红粉
	public static Color[] lvColor={Color.gray,Color.white,Color.green,
			Color.blue,new Color(0x7f00ff),Color.orange,Color.red,Color.pink};//根据等级分颜色
	//public final int levelNum=6;
	public static EnemyTankLevel[] lv={
			//level,color,speed,bulletSpeed,clipNum,score,changeDirect,shootTime,name
			new EnemyTankLevel(0, 0, 1, 1, 2, 1, 10, 10,"低等杂兵"),
			new EnemyTankLevel(1, 1, 2, 2, 3, 2, 10, 10,"中等杂兵"),
			new EnemyTankLevel(2, 2, 3, 3, 4, 3, 10, 10,"中等杂兵"),
			new EnemyTankLevel(3, 3, 4, 4, 5, 4, 10, 10,"中等杂兵"),
			new EnemyTankLevel(4, 4, 5, 5, 6, 5, 10, 10,"中等杂兵"),
			new EnemyTankLevel(5, 5, 6, 6, 7, 6, 10, 10,"中等杂兵")};
}
//播放声音的类
class PlayMusic extends Thread{
	private String filename;
	public PlayMusic(String file){
		this.filename=file;
	}
	public void run() {

		File soundFile = new File(filename);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		AudioFormat format = audioInputStream.getFormat();
		SourceDataLine auline = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[512];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0)
					auline.write(abData, 0, nBytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			auline.drain();
			auline.close();
		}

	}

}
class Key implements KeyListener{
	//键按下
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==32){
			Info.isPause=!Info.isPause;
		}
		//我的坦克存活时才能对其进行操作
		if(Info.myTank.isLive()&&!Info.isPause){
			//根据按键对我的坦克进行方向和坐标的修改
			switch(e.getKeyChar()){
			case 'w':
				Info.myTank.setDirection(0);
				if(Info.myTank.y>=Info.myTank.getSpeed()+15&&!Info.myTank.isTouchEnemy())//如果没碰撞且不超过边界才能运动
				Info.myTank.setY(Info.myTank.getY()-Info.myTank.getSpeed());
				break;
			case 'd':
				Info.myTank.setDirection(1);
				if(Info.myTank.x<=MyTankGame.width-Info.myTank.getSpeed()-15&&!Info.myTank.isTouchEnemy())
				Info.myTank.setX(Info.myTank.getX()+Info.myTank.getSpeed());
				break;
			case 's':
				Info.myTank.setDirection(2);
				if(Info.myTank.y<=MyTankGame.height-Info.myTank.getSpeed()-15&&!Info.myTank.isTouchEnemy())
				Info.myTank.setY(Info.myTank.getY()+Info.myTank.getSpeed());
				break;
			case 'a':
				Info.myTank.setDirection(3);
				if(Info.myTank.x>=Info.myTank.getSpeed()+15&&!Info.myTank.isTouchEnemy())
				Info.myTank.setX(Info.myTank.getX()-Info.myTank.getSpeed());
				break;
			}
			
			if(e.getKeyChar()=='j'){
				Info.myTank.shoot();
			}
//			if(e.getKeyChar()=='p'){
//				Info.pauseOrContinue();
//			}	
		}
		
		
	}
	//键释放
	public void keyReleased(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}
	//有值的键
	public void keyTyped(KeyEvent e) {
		// TODO 自动生成的方法存根
		
	}

}