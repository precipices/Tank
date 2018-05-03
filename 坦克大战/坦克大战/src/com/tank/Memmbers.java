package com.tank;


//子弹类
class Bullet implements Runnable{
	private int x;
	private int y;
	private int direction;
	private boolean isLive=true;
	private int speed=3;
	private boolean isPause=false;
	public Bullet(int x,int y,int direction,int speed) {//形参x,y是坦克中心的坐标
		this.direction=direction;
		this.speed=speed;
		switch(direction){
		case 0:y-=15;break;//向上
		case 1:x+=15;break;//向左
		case 2:y+=15;break;//向下
		case 3:x-=15;break;//向右
		}
		this.x=x;
		this.y=y;
		
	}
	public void run() {
		while(isLive){//子弹死亡则进程停止
			//System.out.println("一个子弹进程正在运行");
			try {
				do{
					Thread.sleep(25);
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			//根据子弹方向及速度改变子弹坐标
			switch(direction){
			case 0:y-=speed;break;//向上
			case 1:x+=speed;break;//向左
			case 2:y+=speed;break;//向下
			case 3:x-=speed;break;//向右
			}
			//子弹超出边框则死亡
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
//爆炸类，用于描述爆炸的过程
class Bomb{
	private boolean isLive=true;
	//爆炸的生存时间
	private int life=9;
	//坐标为爆炸左上角，便于绘图
	private int x;
	private int y;
	public Bomb(int x,int y) {//形参是坦克中心
		//根据坦克中心得到爆炸左上角
		this.x=x-15;
		this.y=y-15;
		System.out.println("产生一个爆炸");
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

