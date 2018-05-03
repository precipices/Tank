/**
 * 3.读档和存档功能随时修改
 * 坦克大战4.0
 * 8.信息提示框（击中坦克之类）
 * 5.记录分数，当达到一定程度我的坦克升级
 * 6.敌人死亡后出现残骸，拾取残骸后才能的到分数，残骸一段时间后自动消失
 * 6.游戏面板上显示基地，基地不断升级可以造出更厉害的坦克
 * 7.我的坦克进入基地后消失，可以在基地替换其它坦克出场
 * 8.必须消耗一定分数才能制造坦克和子弹，每种坦克的花销不同
 * 9.基地有生命值，基地或所有坦克被摧毁则游戏失败
 * 10.长时间没被打中的坦克会升级
 * 11.得到分数改为零部件
 * 12.开启作弊面板
 */
package com.tank;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.ResourceBundle.Control;

public class MyTankGame extends JFrame implements ActionListener{
	public static void main(String[] args) {
		new MyTankGame();
	}
	//游戏界面的宽高
	static int width=800;
	static int height=600;

	ControlPanel cp=null;//控制界面
	GamePanel mp=null;//游戏界面
	JMenuBar jmb=null;
	JMenu jm1,jm2,jm3;
	JMenuItem jmi1,jmi2,jmi3,jmi4,jmi5,jmi6;
	public MyTankGame(){
		this.setLayout(null);//将界面管理设为空
		//开启时钟
		Info.worldTime.start();
		//总菜单
		jmb=new JMenuBar();
		this.setJMenuBar(jmb);
		//游戏菜单
		jm1=new JMenu("游戏(G)");
		jm1.setMnemonic('G');
		jmb.add(jm1);
			//新游戏
		jmi1=new JMenuItem("新游戏(B)");//新建
		jmi1.setActionCommand("begin");//设置命令
		jmi1.setMnemonic('B');//设置快捷键
		jmi1.addActionListener(this);//注册监听器
		jm1.add(jmi1);//加入到菜单中
			//读取存档
		jmi4=new JMenuItem("读取存档");
		jmi4.setActionCommand("read");
		jmi4.addActionListener(this);
		jm1.add(jmi4);
			//加入分割线
		jm1.addSeparator();
			//保存
		jmi3=new JMenuItem("保存");
		jmi3.setActionCommand("save");
		jmi3.addActionListener(this);
		jm1.add(jmi3);
			//保存并退出
		jmi5=new JMenuItem("保存并退出");
		jmi5.setActionCommand("exit");
		jmi5.addActionListener(this);
		jm1.add(jmi5);
		//控制菜单
		jm2=new JMenu("控制(C)");
		jm2.setMnemonic('C');
		jmb.add(jm2);
			//暂停/继续
		jmi2=new JMenuItem("暂停/继续(空格)");
		jmi2.setActionCommand("pause");
		jmi2.addActionListener(this);
		jm2.add(jmi2);
		//作弊菜单
		jm3=new JMenu("作弊(Z)");
		jm3.setMnemonic('Z');
		jmb.add(jm3);
			//暂停所有敌人
		jmi6=new JMenuItem("作弊器");
		jmi6.setActionCommand("cheat");
		jmi6.addActionListener(this);
		jm3.add(jmi6);
		

		cp=new ControlPanel();//控制界面
		cp.setBounds(0, 0, ControlPanel.width, height);
		this.add(cp);
		Thread t=new Thread(cp);
		t.start();
		
		Info.FileInit();
		this.setTitle("坦克大战");
		this.setIconImage(new ImageIcon("imgs\\title.jpg").getImage());
		this.setSize(width+16+ControlPanel.width, height+39+23);
		this.setLocation(100,30);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void setWidth(int width) {
		width = width;
	}

	public static void setHeight(int height) {
		height = height;
	}
	private void begin(){
		//游戏面板
		if(mp!=null){
			mp.closePanel();
			//this.removeKeyListener(mp);
		}
		//游戏面板
		mp=new GamePanel();
		//mp.setFocusable(true);
		mp.requestFocus();
		mp.setLocation(ControlPanel.width, 0);
		mp.setSize(width, height);
		this.add(mp);//将游戏面板加入窗口
		
		//this.addKeyListener(mp);//添加键盘监听器
		//mp.grabFocus();
		//mp.requestFocus();
		
		Thread t=new Thread(mp);//为游戏面板建立线程并启动
		t.start();
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("begin")){
			PlayMusic m=new PlayMusic("music\\newgame.wav");
			m.start();
			begin();
			Info.DataInit();
			Info.myTank=new MyTank(Info.bornX,Info.bornY);//新建我的坦克
			//新建Info.enemyNum辆敌方坦克
			EnemyTank en=null;
			Info.enemyTanks=new Vector<EnemyTank>();
			for(int i=0;i<Info.enemyNum;i++){
				en=new EnemyTank(20+i*50,20,0);
				Info.enemyTanks.add(en);
				Thread t=new Thread(en);
				t.start();
			}
			System.out.println("测试");
			Info.worldTime.clear();
			Info.worldTime.setTimeRun(true);//世界时间开始运转
			Info.status="begin";//游戏状态变为开始
			//mp.newGame();
		}else if(e.getActionCommand().equals("pause")){
			if(mp!=null)
			//mp.pauseOrContinue();
				Info.isPause=!Info.isPause;
		}else if(e.getActionCommand().equals("exit")){
			if(Info.status.equals("begin"))
				Info.save();
			System.exit(0);
		}else if(e.getActionCommand().equals("save")){
			if(Info.status.equals("begin"))
				Info.save();
			else
				JOptionPane.showMessageDialog(null, "尚未开始游戏！");
		}else if(e.getActionCommand().equals("read")){
			System.out.println("读档中。。。");
			if(Info.checkSave()){
				begin();
				Info.read();
			}
			Info.worldTime.setTimeRun(true);//世界时间开始运转
			Info.status="begin";//游戏状态变为开始
		}else if(e.getActionCommand().equals("cheat")){
			System.out.println("开启作弊器");
			new Cheat();
		}
		
	}

}
