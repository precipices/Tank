package com.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

class GamePanel extends JPanel implements Runnable{
	boolean isLive=true;//�ж��߳��Ƿ���
	boolean isPause=false;//�Ƿ���ͣ���߳�
	public GamePanel(){
		
		this.setBackground(Color.gray);
	}
//	public void newGame(){
//		Info.Init();
//		Info.myTank=new MyTank(Info.bornX,Info.bornY);//�½��ҵ�̹��
//		//�½�Info.enemyNum���з�̹��
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
		//��������
		g.drawImage(Info.hq, Info.bornX-30, Info.bornY+15, 60, 40, this);
		//�����ҵ�̹��
		if(Info.myTank.isLive())
		paintTank(Info.myTank.getX(),Info.myTank.getY(),g,Info.myTank.getDirection(),Info.myTank.getColor());
		//�������ез�̹��
		EnemyTank en=null;
		for(int i=0;i<Info.enemyTanks.size();i++){
			en=Info.enemyTanks.get(i);
			paintTank(en.getX(),en.getY(),g,en.getDirection(),en.getColor());
		}
		//�����ҷ��������ӵ�
		paintAllMyBullet(g);
		//�����з��������ӵ�
		paintAllEnemyBullet(g);
		//�������б�ը
		for(int i=0;i<Info.bomb.size();i++){
			//ȡ��һ����ը
			Bomb bo=Info.bomb.get(i);
			if(bo.isLive()){//�����ը����������ʱ�仭��
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
		int count=0;//��ʱ��
		while(isLive){
			count++;//��ʱ����1
			//System.out.println("һ����Ϸ����߳���������");
			try {
				do{
					Thread.sleep(1000/Info.fps);//ÿ��1/fps���GamePanel�������ػ�
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			//���ٱ�ը������ʱ��
			for(int i=0;i<Info.bomb.size();i++){
				//ȡ��һ����ը
				Bomb bo=Info.bomb.get(i);
				//�����ը������ʱ�����
				if(bo.isLive()){
					bo.setLife(bo.getLife()-1);
				}
				//�����ը����ʱ��Ϊ0����ը�������������Ƴ�����
				if(bo.getLife()==0){
					bo.setLive(false);
					Info.bomb.remove(bo);
				}
			}
			
			
			this.repaint();
		}
		
	}
	public void closePanel(){
		//�ر�����̹�˽��̲���̹�˴��������Ƴ�
		for(int i=0;i<Info.enemyTanks.size();i++){
			EnemyTank en=Info.enemyTanks.get(i);
			//�رո�̹�������ӵ�����
			for(int j=0;j<en.getClip().size();j++){
				en.getClip().get(j).setLive(false);
			}
			en.setLive(false);
			Info.enemyTanks.remove(en);
		}
		//�ر��ҵ�̹�������ӵ�����
		for(int i=0;i<Info.myTank.getClip().size();i++){
			Info.myTank.getClip().get(i).setLive(false);
		}
		//�����ҵ�̹������
		Info.myTank.setLive(false);
		//���ñ���Ϸ����߳�����
		this.setLive(false);
	}
	//��̹��
	public static void paintTank(int x,int y,Graphics g,int direction,int color){
		//��ʼ����ɫ������col1Ϊ������ɫ��col2Ϊ�ڹ���ɫ
		Color col1=Color.green ,col2=Color.black;
		//����̹����ɫ����
		if(color>-1)
			col1=col2=EnemyTankLevel.lvColor[color];
		
		//���ݷ��򻭳�̹��
		g.setColor(col1);
		switch(direction){
		case 0://����
			g.fill3DRect(x-10, y-15, 5, 30,false);//����
			g.fill3DRect(x-5, y-10, 11, 20,false);//��
			g.fill3DRect(x+6, y-15, 5, 30,false);//����
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);//Բ
			g.drawLine(x, y-15, x, y);//�ڹ�
			break;
		case 1://����
			g.fill3DRect(x-15, y-10, 30, 5,false);//����
			g.fill3DRect(x-10, y-5, 20, 11,false);
			g.fill3DRect(x-15, y+6, 30, 5,false);//����
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y, x+15, y);
			break;
		case 2://����
			g.fill3DRect(x-10, y-15, 5, 30,false);//����
			g.fill3DRect(x-5, y-10, 11, 20,false);
			g.fill3DRect(x+6, y-15, 5, 30,false);//����
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y+15, x, y);
			break;
		case 3://����
			g.fill3DRect(x-15, y-10, 30, 5,false);//����
			g.fill3DRect(x-10, y-5, 20, 11,false);
			g.fill3DRect(x-15, y+6, 30, 5,false);//����
			g.setColor(col2);
			g.fillOval(x-5, y-5, 9, 9);
			g.drawLine(x, y, x-15, y);
			break;
		}
	}
	
	//���ӵ�
	private void paintBullet(Bullet bullet,Graphics g){
		int x=bullet.getX();
		int y=bullet.getY();
		g.setColor(Color.black);
		g.fill3DRect(x-1, y-1, 3, 3, false);
	}
	//���������ҷ��ӵ�����������Ӧ����
	private void paintAllMyBullet(Graphics g){
		for(int i=0;i<Info.myTank.getClip().size();i++){
			//�ӵ�����ȡ���ӵ�
			Bullet bu=Info.myTank.getClip().get(i);
			if(bu.isLive()){
				//����ӵ�����򻭳��ӵ�
				paintBullet(bu,g);
				//����ӵ����κεз�̹����ײ����̹�����ӵ�������
				EnemyTank en=null;
				for(int j=0;j<Info.enemyTanks.size();j++){
					//ȡ��̹��
					en=Info.enemyTanks.get(j);
					//�ж��Ƿ����
					boolean hit=false;
					//ȡ���ӵ���̹�˵�����
					int x1=bu.getX(),x2=en.getX(),y1=bu.getY(),y2=en.getY();
					//���ݷ����ж��Ƿ����
					switch(en.getDirection()){
					case 0://����
					case 2://����
						if(x1>=x2-10&&x1<=x2+10 && y1>=y2-15&&y1<=y2+15)
							hit=true;
						break;
					case 1://����
					case 3://����
						if(x1>=x2-15&&x1<=x2+15 && y1>=y2-10&&y1<=y2+10)
							hit=true;
						break;
					}
					//������У��ӵ���̹�˾�����,��������������Ƴ�,����һ����ը
					if(hit==true){
						System.out.println("���ез�̹��------------------------");
						//���ӷ���
						Info.addScore(en.getScore());
						//̹�˺��ӵ�����
						en.setLive(false);
						bu.setLive(false);
						//����̹�����������½�һ����ը������������
						Bomb bo=new Bomb(en.getX(),en.getY());
						Info.bomb.add(bo);
						//�Ƴ�̹�˺��ӵ�
						Info.enemyTanks.remove(en);
						Info.myTank.getClip().remove(bu);
					}
				}
			}else{
				//����ӵ��������ӵ��ӵ������Ƴ�
				Info.myTank.getClip().remove(bu);
			}
		}
	}
	//�������ез��ӵ��������д���
	private void paintAllEnemyBullet(Graphics g){
		for(int i=0;i<Info.enemyTanks.size();i++){
			//ȡ��һ��̹��
			EnemyTank en=Info.enemyTanks.get(i);
			for(int j=0;j<en.getClip().size();j++){
				//�ӵ�����ȡ���ӵ�
				Bullet bu=en.getClip().get(j);
				if(bu.isLive()){
					//����ӵ�����򻭳��ӵ�
					paintBullet(bu,g);
					//����ҵ�̹�������������ý����Ƿ���е��ж�
					if(Info.myTank.isLive()==false)
						continue;
					//�ж��Ƿ����
					boolean hit=false;
					//ȡ���ӵ���̹�˵�����
					int x1=bu.getX(),x2=Info.myTank.getX(),y1=bu.getY(),y2=Info.myTank.getY();
					//���ݷ����ж��Ƿ����
					switch(Info.myTank.getDirection()){
					case 0://����
					case 2://����
						if(x1>=x2-10&&x1<=x2+10 && y1>=y2-15&&y1<=y2+15)
							hit=true;
						break;
					case 1://����
					case 3://����
						if(x1>=x2-15&&x1<=x2+15 && y1>=y2-10&&y1<=y2+10)
							hit=true;
						break;
					}
					//������У��ӵ���̹�˾�����,��������������Ƴ�,����һ����ը
					if(hit==true){
						System.out.println("�ҷ�������-----------------------");
						
						bu.setLive(false);//�ӵ�����
						//����̹�����������½�һ����ը������������
						Bomb bo=new Bomb(Info.myTank.getX(),Info.myTank.getY());
						Info.bomb.add(bo);
						//�Ƴ��ӵ�,�ҷ�̹�˲����̣߳������Ƴ�
						en.getClip().remove(bu);
						
						//̹�������ж�
						Info.myTank.setLive(false);
						Info.MyLife--;
						if(Info.MyLife<=0){
							JOptionPane.showMessageDialog(null, "gameover!!");
						}else{
							Info.myTank=new MyTank(Info.bornX,Info.bornY);
						}
					}
				
				}else{
					//����ӵ��������ӵ��ӵ������Ƴ�
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