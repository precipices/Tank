package com.tank;

import java.awt.*;
import java.text.DecimalFormat;

import javax.swing.*;

public class ControlPanel extends JTabbedPane implements Runnable{
	public static int width=250;
	BattlePanel jp1;
	EnemyPanel jp4;
	JPanel jp2,jp3;
	public ControlPanel() {
		jp1=new BattlePanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new EnemyPanel();
		this.add("ս��", jp1);
//		Thread t=new Thread(jp1);
//		t.start();
		this.add("����", jp2);
		this.add("�ֿ�", jp3);
		this.add("������", jp4);
		this.addKeyListener(Info.k);
		/*��֪��Ϊʲô��ԭ�����ڴ����ϵļ������ڼ������ѡ������û���ˣ�
		 * ���԰�ԭ��������Ϸ���ļ�������д��һ���࣬����ӵ���������־������ˣ���Ȼ��֪��Ϊʲô�����Ժ�Ӧ�û�֪���İɣ�*/
		this.setBackground(Color.cyan);
	}
	public void paint(Graphics g){
		super.paint(g);
	}
	public void run() {

		while(true){

			try {
				Thread.sleep(100);
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.repaint();
		}
	}
}
class BattlePanel extends JPanel{
//	EnemyPanel ep=null;
	public BattlePanel() {
//		this.setLayout(null);
//		ep=new EnemyPanel();
//		this.add(ep);
//		ep.setBounds(0, 168, ControlPanel.width, 100);
	}
	public void paint(Graphics g){
		super.paint(g);
		//��վ��
		//���߿�
		g.draw3DRect(0, 33, this.getWidth()-2, 110, false);//33-133
		g.setColor(Color.blue);
		g.setFont(new Font("����",Font.BOLD,20));
		g.drawString("��ս��", 0, 30);//10-30
		if(Info.myTank!=null){
			GamePanel.paintTank(20, 50, g, 0, -1);//35-65
			g.drawString("����:"+Info.myTank.getSpeed(), 40, 35+20);//35-55
			g.drawString("��ҩ:"+(Info.myTank.getClipNum()-Info.myTank.getClip().size()), 40, 55+20);//55-75
			g.drawString("����:"+Info.myTank.getBulletSpeed(), 40, 75+20);//75-95
			g.drawString("����:"+Info.MyLife,40,95+20);//95-115
			g.drawString("����:"+Info.score, 40, 115+20);//115-135
		}
//		//�з���
//		g.draw3DRect(0, 168, this.getWidth()-2, 100, false);//168-268
//		g.setColor(Color.blue);
//		g.drawString("�з���������", 0, 135+30);//135-165
//		for(int i=0;i<Info.enemyTanks.size();i++){
//			EnemyTank en=Info.enemyTanks.get(i);
//		}
		//ʱ��
		String str=new DecimalFormat("00").format(0);
		DecimalFormat df=new DecimalFormat("00");
		g.drawString("ʱ��:"+df.format(Info.worldTime.getHour())+":"
				+df.format(Info.worldTime.getMinutes())+":"+df.format(Info.worldTime.getSecond()), 0, 400);
	}
}
class EnemyPanel extends JPanel{
	int width=ControlPanel.width;//�������,����ĵ��Ŀ�Ȼ���0��������������µõ�
	int tankWidth=35;//ÿ��̹�˿���
	int lineNum=width/tankWidth;//ÿ��̹�˿����
	int col;//�»���̹����col��
	int row;//�»���̹����row��
	int x;//����
	int y;//����
	public EnemyPanel() {
		this.setBackground(Color.BLACK);
	}
	public void paint(Graphics g){
		super.paint(g);
		observer(g);
	}
	public void observer(Graphics g){
//		width=this.getWidth();
//		lineNum=width/tankWidth;
		for(int i=0;i<Info.enemyTanks.size();i++){
			col=(i+1)%lineNum;//�»���̹����col��
			row=(i+1)/lineNum+1;//�»���̹����row��
			if(col==0) {//���ûд��������һ��û��������������λ��Ҳ����
				col=lineNum;
				row=row-1;
			}
			x=col*tankWidth-tankWidth/2;//����
			y=row*tankWidth-tankWidth/2;//����
			EnemyTank en=Info.enemyTanks.get(i);//ȡ��̹��
			GamePanel.paintTank(x, y, g, 0, en.getColor());//����̹��
			
		}
		
	}
	//��̹��
//	public void paintTank(int x,int y,Graphics g,int direction,int color){
//		//��ʼ����ɫ������col1Ϊ������ɫ��col2Ϊ�ڹ���ɫ
//		Color col1=Color.green ,col2=Color.black;
//		//����̹����ɫ����
//		if(color>-1)
//			col1=col2=EnemyTankLevel.lvColor[color];
//		
//		//���ݷ��򻭳�̹��
//		g.setColor(col1);
//		switch(direction){
//		case 0://����
//			g.fill3DRect(x-10, y-15, 5, 30,false);//����
//			g.fill3DRect(x-5, y-10, 11, 20,false);//��
//			g.fill3DRect(x+6, y-15, 5, 30,false);//����
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);//Բ
//			g.drawLine(x, y-15, x, y);//�ڹ�
//			break;
//		case 1://����
//			g.fill3DRect(x-15, y-10, 30, 5,false);//����
//			g.fill3DRect(x-10, y-5, 20, 11,false);
//			g.fill3DRect(x-15, y+6, 30, 5,false);//����
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y, x+15, y);
//			break;
//		case 2://����
//			g.fill3DRect(x-10, y-15, 5, 30,false);//����
//			g.fill3DRect(x-5, y-10, 11, 20,false);
//			g.fill3DRect(x+6, y-15, 5, 30,false);//����
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y+15, x, y);
//			break;
//		case 3://����
//			g.fill3DRect(x-15, y-10, 30, 5,false);//����
//			g.fill3DRect(x-10, y-5, 20, 11,false);
//			g.fill3DRect(x-15, y+6, 30, 5,false);//����
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y, x-15, y);
//			break;
//		}
//	}
}