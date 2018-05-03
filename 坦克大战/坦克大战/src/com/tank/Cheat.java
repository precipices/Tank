package com.tank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cheat extends JFrame implements ActionListener{
	JPanel jp=null;
	JButton jb1,jb2,jb3,jb4;
	public static void main(String[] args) {
		//JFrame jf=new JFrame();
		//jf.add(new Cheat());
		new Cheat();
		
//		jf.setBounds(300, 200, 400, 300);
//		jf.setVisible(true);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public Cheat() {
		jp=new JPanel();
		//this.setLayout(new FlowLayout());
		jb1=new JButton("暂停所有敌方坦克及子弹且不产生新坦克");
		jb1.addActionListener(this);
		jp.add(jb1);
		
		jb2=new JButton("暂停所有敌方坦克及子弹");
		jb2.addActionListener(this);
		jp.add(jb2);

		jb3=new JButton("不产生新坦克");
		jb3.addActionListener(this);
		jp.add(jb3);
		
		jb4=new JButton("产生一辆坦克");
		jb4.addActionListener(this);
		this.add(jp);
		
		this.setBounds(300, 200, 400, 300);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public void pauseTank(){
		//将所有敌方坦克及其子弹暂停
		for(int i=0;i<Info.enemyTanks.size();i++){
			EnemyTank en=Info.enemyTanks.get(i);
			en.setPause(!en.isPause());
			for(int j=0;j<en.getClip().size();j++){
				Bullet bu=en.getClip().get(j);
				bu.setPause(!bu.isPause());
			}
		}
	}
	public void pauseNewTank(){
		//暂停产生坦克
		Info.isNewEnemy=!Info.isNewEnemy;
	}

	public void actionPerformed(ActionEvent e) {
		if(Info.status.equals("begin")){
			if(e.getSource()==jb1){
				pauseTank();
				pauseNewTank();
			}else if(e.getSource()==jb2){
				pauseTank();
			}else if(e.getSource()==jb3){
				pauseNewTank();
			}else if(e.getSource()==jb4){
				
			}
		}
		
	}
}
