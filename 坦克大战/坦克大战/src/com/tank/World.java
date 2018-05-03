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
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			//System.out.println("����ʱ��������");
			//����ʱ�����̹��
			int n=this.addTime();
			if(n>0&&Info.isNewEnemy&&!Info.isPause){
				switch(n){
				case 1://������
					if(second%10==0&&second!=0)newEnemy(0);
					if(second%20==0&&second!=0)newEnemy(1);
					if(second%30==0&&second!=0)newEnemy(2);
					break;
				case 2://������
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
//������Ϣ��������Ϸ��������ɷ��ʼ�����
class Info{
	public static String status="prepare";//prepare,begin,newgame,oldgame
	public static int score=0;
	public static void addScore(int tankScore){
		score+=tankScore;
	}
	public static Key k=new Key();//���̼�����
	public static GameTime worldTime=new GameTime();
	public static int fps=25;//���������ȣ�ÿ�����֡
	public static int bornX=MyTankGame.width/2,bornY=MyTankGame.height-55;
	//public static int enemyBornX=200,enemyBornY=15;
	//public static int addTankTime=100;//̹�����ӵ�ʱ��
	public static boolean isPause=false;//�Ƿ���ͣ��Ϸ
	public static boolean isNewEnemy=true;//�Ƿ�����µ���
	public static int MyLife=5;//��ʣ���������
	public static int enemyNum=16;//��ʼ�з�̹������
	public static Vector<EnemyTank> enemyTanks=new Vector<EnemyTank>();
	public static MyTank myTank=null;//�ҵ�̹��
	public static Vector<Bomb> bomb=new Vector<Bomb>();//��ը������,������������еı�ը
	//���뱬ըЧ��ͼ
	public static Image bomb1=new ImageIcon("imgs/bomb_1.gif").getImage();
	public static Image bomb2=new ImageIcon("imgs/bomb_2.gif").getImage();
	public static Image bomb3=new ImageIcon("imgs/bomb_3.gif").getImage();
	//�������ͼ
	public static Image hq=new ImageIcon("imgs/headquarters.jpg").getImage();
	
	public static void FileInit(){
		//����ļ����Ƿ���ڣ��粻�����򴴽�
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
		JOptionPane.showMessageDialog(null, "û�д浵��");
		return false;
	}
	public static void save(){
		FileInit();
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			fw=new FileWriter("save\\save.txt");//FileWriter�������ļ�������ʱ���Զ�����
			bw=new BufferedWriter(fw);
			//д���������
			bw.write(worldTime.getHour()+" "+worldTime.getMinutes()
				+" "+worldTime.getSecond()+" "+worldTime.getBs()+"\r\n");
			bw.write(MyLife+"\r\n");
			bw.write(score+"\r\n");
			bw.write(enemyTanks.size()+"\r\n");
			//д���ҵ�̹�˵�����
			bw.write(myTank.getX()+" "+myTank.getY()+" "+myTank.getDirection()+" "+myTank.getClip().size()+" "+myTank.isLive()+"\r\n");
			//д���ҵ�̹�˵������ӵ�������
			for(int j=0;j<myTank.getClip().size();j++){
				Bullet bu=myTank.getClip().get(j);
				bw.write(bu.getX()+" "+bu.getY()+" "+bu.getDirection()+"\r\n");
			}
			//д�����ез�̹�˼����ӵ�����
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
			int n=0;//������ʱ����
			Vector<Bullet>clip=null;
 			//��ȡ�������
			//bw.write(worldTime.getHour()+" "+worldTime.getMinutes()
			//+" "+worldTime.getSecond()+" "+worldTime.getBs()+"\r\n");
			sList=br.readLine().split(" ");
			worldTime.setTime(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),Integer.parseInt(sList[3]));
			MyLife=Integer.parseInt(br.readLine());
			score=Integer.parseInt(br.readLine());
			int enemyNum=Integer.parseInt(br.readLine());
			//��ȡ�ҵ�̹�˵�����
			sList=br.readLine().split(" ");
			myTank=new MyTank(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]));
			//System.out.println(sList.length);
			myTank.setDirection(Integer.parseInt(sList[2]));
			n=Integer.parseInt(sList[3]);//�ӵ���
			myTank.setLive(Boolean.parseBoolean(sList[4]));
			//��ȡ�ҵ�̹�˵������ӵ�������
			clip=myTank.getClip();
			for(int j=0;j<n;j++){
				sList=br.readLine().split(" ");
				Bullet bu=new Bullet(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),myTank.getBulletSpeed());
				clip.add(bu);
				Thread t3=new Thread(bu);
				t3.start();//�����ӵ��߳�
			}
			
			//��ȡ���ез�̹�˼����ӵ�����
			enemyTanks=new Vector<EnemyTank>();
			for(int i=0;i<enemyNum;i++){//enemyNum��ǰ��ն�ȡ��
				//��ȡ�з�̹������
				sList=br.readLine().split(" ");
				EnemyTank en=new EnemyTank(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]));
				en.setDirection(Integer.parseInt(sList[3]));
				Thread t=new Thread(en);
				t.start();//����̹���߳�
				enemyTanks.add(en);//���з�̹����ӵ�������
				n=Integer.parseInt(sList[4]);//�ӵ�����
				clip=en.getClip();//�õ���̹�˵���
				//��ȡ��̹���ӵ�����
				for(int j=0;j<n;j++){
					sList=br.readLine().split(" ");
					Bullet bu=new Bullet(Integer.parseInt(sList[0]),Integer.parseInt(sList[1]),Integer.parseInt(sList[2]),en.getBulletSpeed());
					clip.add(bu);//���ӵ���ӵ�����
					Thread t2=new Thread(bu);
					t2.start();//�����ӵ��߳�
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
	int color=0;//��ɫ��Ĭ��Ϊ����0
	int speed=1;//�ٶȣ�Ĭ��Ϊ1
	int bulletSpeed=1;//�ӵ��ٶȣ�Ĭ��Ϊ1
	int clipNum=5;//�����п����ɵ��ӵ�����
	int score=1;//���к�ĵ÷�
	int changeDirect=10;//�߶��ٲ�������
	int shootTime=10;//�߶��ٲ������ӵ�
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
	//�Ұ������ϳȺ��
	public static Color[] lvColor={Color.gray,Color.white,Color.green,
			Color.blue,new Color(0x7f00ff),Color.orange,Color.red,Color.pink};//���ݵȼ�����ɫ
	//public final int levelNum=6;
	public static EnemyTankLevel[] lv={
			//level,color,speed,bulletSpeed,clipNum,score,changeDirect,shootTime,name
			new EnemyTankLevel(0, 0, 1, 1, 2, 1, 10, 10,"�͵��ӱ�"),
			new EnemyTankLevel(1, 1, 2, 2, 3, 2, 10, 10,"�е��ӱ�"),
			new EnemyTankLevel(2, 2, 3, 3, 4, 3, 10, 10,"�е��ӱ�"),
			new EnemyTankLevel(3, 3, 4, 4, 5, 4, 10, 10,"�е��ӱ�"),
			new EnemyTankLevel(4, 4, 5, 5, 6, 5, 10, 10,"�е��ӱ�"),
			new EnemyTankLevel(5, 5, 6, 6, 7, 6, 10, 10,"�е��ӱ�")};
}
//������������
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
	//������
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==32){
			Info.isPause=!Info.isPause;
		}
		//�ҵ�̹�˴��ʱ���ܶ�����в���
		if(Info.myTank.isLive()&&!Info.isPause){
			//���ݰ������ҵ�̹�˽��з����������޸�
			switch(e.getKeyChar()){
			case 'w':
				Info.myTank.setDirection(0);
				if(Info.myTank.y>=Info.myTank.getSpeed()+15&&!Info.myTank.isTouchEnemy())//���û��ײ�Ҳ������߽�����˶�
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
	//���ͷ�
	public void keyReleased(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}
	//��ֵ�ļ�
	public void keyTyped(KeyEvent e) {
		// TODO �Զ����ɵķ������
		
	}

}