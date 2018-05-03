package com.tank;

import java.util.Vector;

//�ҵ�̹��
class MyTank extends Tank{
	public MyTank(int x, int y) {
		super(x, y);
		this.name="�ҵ�̹��";
		//���ҵ�̹�˳�ʼ��Ϊ��ɫ����0���������ϣ��ٶ�Ϊ1
		this.setColor(-1);
		this.setDirection(0);
		this.setSpeed(5);
		this.setBulletSpeed(5);
		this.setClipNum(5);
	}
	//����һ���ӵ�
	public void shoot(){
		if(clip.size()<clipNum){
			//�½�һ���ӵ�����
			Bullet bu=new Bullet(x,y,direction,this.getBulletSpeed());
			//Ϊ�ӵ������߳�
			Thread t=new Thread(bu);
			t.start();
			clip.add(bu);
			PlayMusic p=new PlayMusic("music\\shoot.wav");
			p.start();
		}
	}
}
//�з�̹��
class EnemyTank extends Tank implements Runnable{
	//�з�̹�˵ȼ�,Ĭ��Ϊ0
	private int level=0;
	private int changeDirect=10;//�߶��ٲ�������
	private int shootTime=10;//�߶��ٲ�����
	private int score=1;//���к�ĵ÷�
	public EnemyTank(int x, int y,int level) {
		super(x,y);
		this.setDirection(2);//�з�̹�˷���Ĭ������
		System.out.println("һ���з�̹�˱�������("+x+","+y+")");
		EnemyTankLevel l=EnemyTankLevel.lv[level];
		this.level=l.level;
		this.color=l.color;
		this.speed=l.speed;
		this.bulletSpeed=l.bulletSpeed;
		this.clipNum=l.clipNum;
		this.score=l.score;
		this.changeDirect=l.changeDirect;
		this.shootTime=l.shootTime;
		this.name=l.name;
	}
//	public EnemyTank(int x, int y,int level) {
//		super(x, y);
//		System.out.println("һ���з�̹�˱�������("+x+","+y+")");
//		this.name=Info.enemyTanks.size()+1+"";
//		this.level=level;//���õȼ�
//		this.setClipNum(3);//���õ����ӵ�����
//		this.setDirection(2);//�з�̹�˷���Ĭ������
//		//���ݵȼ�ѡ����ɫ�������ٶ�
//		switch(level){
//		case 0:
//			this.setColor(1);
//			this.setSpeed(1);
//			this.setBulletSpeed(1);
//			break;
//		case 1:
//			this.setColor(2);
//			this.setSpeed(2);
//			this.setBulletSpeed(2);
//			break;
//		case 2:
//			this.setColor(3);
//			this.setSpeed(3);
//			this.setBulletSpeed(3);
//			break;
//		}
//	}
	public boolean isTouchMyTank(){
		boolean isTouch=false;
		//�ȵõ�Ŀ��̹�˵���������
		int x1=0,y1=0,x2=0,y2=0;//���ϽǼ����½ǵ�����
		switch(Info.myTank.getDirection()){
		case 0://��
		case 2://��
			x1=Info.myTank.getX()-10;x2=Info.myTank.getX()+10+1;//+1����Ϊ��̹��ʱ�ڹܳ���Ϊ1������̹�˴�СӦΪ21*30����ͬ
			y1=Info.myTank.getY()-15;y2=Info.myTank.getY()+15;
			break;
		case 1://��
		case 3://��
			x1=Info.myTank.getX()-15;x2=Info.myTank.getX()+15;
			y1=Info.myTank.getY()-10;y2=Info.myTank.getY()+10+1;
			break;
		}
		//���ԣ��ɿ��ƻرܾ��룬���Ǿ��̹�˶�Զ�͵ûر�
		//x1-=1;y1-=1;x2+=1;y2+=1;
		//Ȼ���жϴ�̹�˽���һ���ƶ����Ƿ����Ŀ��̹��������
		int xl=0,yl=0,xr=0,yr=0;//һ���ƶ����̹��ǰ����������
		switch(this.direction){
		case 0://��y-su,(x-10,y-15)(x+10,y-15)
			xl=x-10;yl=y-15-speed;
			xr=x+10+1;yr=y-15-speed;
			break;
		case 2://��y+su
			xl=x+10;yl=y+15+speed;
			xr=x-10+1;yr=y+15+speed;
			break;
		case 1://��x+su
			xl=x+15+speed;yl=y-10;
			xr=x+15+speed;yr=y+10+1;
			break;
		case 3://��x-su
			xl=x-15-speed;yl=y+10;
			xr=x-15-speed;yr=y-10+1;
			break;
		}
		//�жϣ�xl,yl����xr,yr�������Ƿ���(x1,y1)(x2,y2)�����ڲ��������߽磩
		if((xl>=x1&&xl<=x2 && yl>=y1&&yl<=y2)||(xr>=x1&&xr<=x2 && yr>=y1&&yr<=y2))
			isTouch=true;
		return isTouch;
	}
	public void shoot(){
		if(clip.size()<clipNum){
			//�½�һ���ӵ�����
			Bullet bu=new Bullet(x,y,direction,this.bulletSpeed);
			//Ϊ�ӵ������߳�
			Thread t=new Thread(bu);
			t.start();
			clip.add(bu);
		}
	}
	public void run() {
		int count=0;//����������̹���ۼ����˶��ٲ�
		while(isLive){
			//System.out.println("һ���з�̹�˽���������");
			try {
				do{
					Thread.sleep(100);
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!(this.isTouchEnemy()||this.isTouchMyTank()))//���������ײ����ǰ��һ��
			{
				switch(this.direction){//̹�˸��ݷ�����ǰ��һ��
				case 0://��
					if(y>=speed+15)y-=speed;break;
				case 1://��
					if(x<=MyTankGame.width-speed-15)x+=speed;break;
				case 2://��
					if(y<=MyTankGame.height-speed-15)y+=speed;break;
				case 3://��
					if(x>=speed+15)x-=speed;break;
				}
			}
			
			//����ı䷽��
			count++;
			if(count%this.changeDirect==0){//ÿ��changeDirect���Ÿı�һ�η���
				switch((int)(Math.random()*4)){
				case 0://��
					this.setDirection(0);break;
				case 1://��
					this.setDirection(1);break;
				case 2://��
					this.setDirection(2);break;
				case 3://��
					this.setDirection(3);break;
				default:System.out.println("�����ܷ�����������ˣ�����������������������");
				}
			}
			//�����ӵ�
			if(count%this.shootTime==0){
				shoot();
			}
		}
		System.out.println("һ���з�̹�˱��ݻ�");
		
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
}
//̹���ุ��
abstract class Tank{
	//����
	int x=0;
	int y=0;
	int direction=0;//����//0123=��������
	int speed=1;//�ٶȣ�Ĭ��Ϊ1
	int bulletSpeed=1;//�ӵ��ٶȣ�Ĭ��Ϊ1
	int color=-1;//��ɫ��Ĭ��Ϊ����0
	boolean isLive=true;//�Ƿ���
	boolean isPause=false;//�Ƿ���ͣ�ж�
	Vector<Bullet>clip=new Vector<Bullet>();//����,���ӵ����飨�����ѷ�����δ�������ӵ���
	int clipNum=5;//�����п����ɵ��ӵ�����
	String name="δ����";
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}
	public boolean isTouchEnemy(){
		boolean isTouch=false;
		for(int i=0;i<Info.enemyTanks.size();i++){
			EnemyTank en=Info.enemyTanks.get(i);//ȡ��һ��̹��
			if(en.equals(this)){//���ȡ����̹�����Լ����������˴��ж�
				continue;
			}
			//�ȵõ�Ŀ��̹�˵���������
			int x1=0,y1=0,x2=0,y2=0;//���ϽǼ����½ǵ�����
			switch(en.getDirection()){
			case 0://��
			case 2://��
				x1=en.getX()-10;x2=en.getX()+10+1;//+1����Ϊ��̹��ʱ�ڹܳ���Ϊ1������̹�˴�СӦΪ21*30����ͬ
				y1=en.getY()-15;y2=en.getY()+15;
				break;
			case 1://��
			case 3://��
				x1=en.getX()-15;x2=en.getX()+15;
				y1=en.getY()-10;y2=en.getY()+10+1;
				break;
			}
			//���ԣ��ɿ��ƻرܾ��룬���Ǿ��̹�˶�Զ�͵ûر�
			//x1-=1;y1-=1;x2+=1;y2+=1;
			//Ȼ���жϴ�̹�˽���һ���ƶ����Ƿ����Ŀ��̹��������
			int xl=0,yl=0,xr=0,yr=0;//һ���ƶ����̹��ǰ����������
			switch(this.direction){
			case 0://��y-su,(x-10,y-15)(x+10,y-15)
				xl=x-10;yl=y-15-speed;
				xr=x+10+1;yr=y-15-speed;
				break;
			case 2://��y+su
				xl=x+10;yl=y+15+speed;
				xr=x-10+1;yr=y+15+speed;
				break;
			case 1://��x+su
				xl=x+15+speed;yl=y-10;
				xr=x+15+speed;yr=y+10+1;
				break;
			case 3://��x-su
				xl=x-15-speed;yl=y+10;
				xr=x-15-speed;yr=y-10+1;
				break;
			}
			//�жϣ�xl,yl����xr,yr�������Ƿ���(x1,y1)(x2,y2)�����ڲ��������߽磩
			if((xl>=x1&&xl<=x2 && yl>=y1&&yl<=y2)||(xr>=x1&&xr<=x2 && yr>=y1&&yr<=y2))
				isTouch=true;
		}
		
		return isTouch;
	}
	//����Ϊ�Զ����ɵ�get,set����
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public Vector<Bullet> getClip() {
		return clip;
	}

	public void setClip(Vector<Bullet> clip) {
		this.clip = clip;
	}

	public int getClipNum() {
		return clipNum;
	}

	public void setClipNum(int clipNum) {
		this.clipNum = clipNum;
	}
	public boolean isPause() {
		return isPause;
	}
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}