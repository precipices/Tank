package com.tank;


//�ӵ���
class Bullet implements Runnable{
	private int x;
	private int y;
	private int direction;
	private boolean isLive=true;
	private int speed=3;
	private boolean isPause=false;
	public Bullet(int x,int y,int direction,int speed) {//�β�x,y��̹�����ĵ�����
		this.direction=direction;
		this.speed=speed;
		switch(direction){
		case 0:y-=15;break;//����
		case 1:x+=15;break;//����
		case 2:y+=15;break;//����
		case 3:x-=15;break;//����
		}
		this.x=x;
		this.y=y;
		
	}
	public void run() {
		while(isLive){//�ӵ����������ֹͣ
			//System.out.println("һ���ӵ�������������");
			try {
				do{
					Thread.sleep(25);
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			//�����ӵ������ٶȸı��ӵ�����
			switch(direction){
			case 0:y-=speed;break;//����
			case 1:x+=speed;break;//����
			case 2:y+=speed;break;//����
			case 3:x-=speed;break;//����
			}
			//�ӵ������߿�������
			if(x<0||y<0||x>MyTankGame.width||y>MyTankGame.height){
				this.isLive=false;
			}
		}
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
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public boolean isPause() {
		return isPause;
	}
	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}
}
//��ը�࣬����������ը�Ĺ���
class Bomb{
	private boolean isLive=true;
	//��ը������ʱ��
	private int life=9;
	//����Ϊ��ը���Ͻǣ����ڻ�ͼ
	private int x;
	private int y;
	public Bomb(int x,int y) {//�β���̹������
		//����̹�����ĵõ���ը���Ͻ�
		this.x=x-15;
		this.y=y-15;
		System.out.println("����һ����ը");
		PlayMusic p=new PlayMusic("music\\bomb.wav");
		p.start();
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
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
}

