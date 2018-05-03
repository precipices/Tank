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
		this.add("战斗", jp1);
//		Thread t=new Thread(jp1);
//		t.start();
		this.add("基地", jp2);
		this.add("仓库", jp3);
		this.add("监视器", jp4);
		this.addKeyListener(Info.k);
		/*不知道为什么，原本加在窗体上的监听器在加上这个选项卡面板后就没用了，
		 * 所以把原来设在游戏面板的监听器另写了一个类，并添加到了这里，发现就有用了（虽然不知道为什么，但以后应该会知道的吧）*/
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
		//出站框
		//画边框
		g.draw3DRect(0, 33, this.getWidth()-2, 110, false);//33-133
		g.setColor(Color.blue);
		g.setFont(new Font("宋体",Font.BOLD,20));
		g.drawString("出战：", 0, 30);//10-30
		if(Info.myTank!=null){
			GamePanel.paintTank(20, 50, g, 0, -1);//35-65
			g.drawString("移速:"+Info.myTank.getSpeed(), 40, 35+20);//35-55
			g.drawString("弹药:"+(Info.myTank.getClipNum()-Info.myTank.getClip().size()), 40, 55+20);//55-75
			g.drawString("弹速:"+Info.myTank.getBulletSpeed(), 40, 75+20);//75-95
			g.drawString("生命:"+Info.MyLife,40,95+20);//95-115
			g.drawString("点数:"+Info.score, 40, 115+20);//115-135
		}
//		//敌方框
//		g.draw3DRect(0, 168, this.getWidth()-2, 100, false);//168-268
//		g.setColor(Color.blue);
//		g.drawString("敌方监视器：", 0, 135+30);//135-165
//		for(int i=0;i<Info.enemyTanks.size();i++){
//			EnemyTank en=Info.enemyTanks.get(i);
//		}
		//时间
		String str=new DecimalFormat("00").format(0);
		DecimalFormat df=new DecimalFormat("00");
		g.drawString("时间:"+df.format(Info.worldTime.getHour())+":"
				+df.format(Info.worldTime.getMinutes())+":"+df.format(Info.worldTime.getSecond()), 0, 400);
	}
}
class EnemyPanel extends JPanel{
	int width=ControlPanel.width;//此面板宽度,这里的到的宽度会是0，所以下面得重新得到
	int tankWidth=35;//每个坦克块宽度
	int lineNum=width/tankWidth;//每行坦克块个数
	int col;//新画的坦克在col列
	int row;//新画的坦克在row行
	int x;//坐标
	int y;//坐标
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
			col=(i+1)%lineNum;//新画的坦克在col列
			row=(i+1)/lineNum+1;//新画的坦克在row行
			if(col==0) {//如果没写这个，最后一列没画出来，画出来位置也不对
				col=lineNum;
				row=row-1;
			}
			x=col*tankWidth-tankWidth/2;//坐标
			y=row*tankWidth-tankWidth/2;//坐标
			EnemyTank en=Info.enemyTanks.get(i);//取出坦克
			GamePanel.paintTank(x, y, g, 0, en.getColor());//画出坦克
			
		}
		
	}
	//画坦克
//	public void paintTank(int x,int y,Graphics g,int direction,int color){
//		//初始化配色方案，col1为车身颜色，col2为炮管颜色
//		Color col1=Color.green ,col2=Color.black;
//		//设置坦克配色方案
//		if(color>-1)
//			col1=col2=EnemyTankLevel.lvColor[color];
//		
//		//根据方向画出坦克
//		g.setColor(col1);
//		switch(direction){
//		case 0://向上
//			g.fill3DRect(x-10, y-15, 5, 30,false);//左轮
//			g.fill3DRect(x-5, y-10, 11, 20,false);//中
//			g.fill3DRect(x+6, y-15, 5, 30,false);//右轮
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);//圆
//			g.drawLine(x, y-15, x, y);//炮管
//			break;
//		case 1://向右
//			g.fill3DRect(x-15, y-10, 30, 5,false);//左轮
//			g.fill3DRect(x-10, y-5, 20, 11,false);
//			g.fill3DRect(x-15, y+6, 30, 5,false);//右轮
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y, x+15, y);
//			break;
//		case 2://向下
//			g.fill3DRect(x-10, y-15, 5, 30,false);//右轮
//			g.fill3DRect(x-5, y-10, 11, 20,false);
//			g.fill3DRect(x+6, y-15, 5, 30,false);//左轮
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y+15, x, y);
//			break;
//		case 3://向左
//			g.fill3DRect(x-15, y-10, 30, 5,false);//右轮
//			g.fill3DRect(x-10, y-5, 20, 11,false);
//			g.fill3DRect(x-15, y+6, 30, 5,false);//左轮
//			g.setColor(col2);
//			g.fillOval(x-5, y-5, 9, 9);
//			g.drawLine(x, y, x-15, y);
//			break;
//		}
//	}
}