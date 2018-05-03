package com.tank;

import java.util.Vector;

//我的坦克
class MyTank extends Tank{
	public MyTank(int x, int y) {
		super(x, y);
		this.name="我的坦克";
		//将我的坦克初始化为颜色方案0，方向向上，速度为1
		this.setColor(-1);
		this.setDirection(0);
		this.setSpeed(5);
		this.setBulletSpeed(5);
		this.setClipNum(5);
	}
	//发射一颗子弹
	public void shoot(){
		if(clip.size()<clipNum){
			//新建一个子弹对象
			Bullet bu=new Bullet(x,y,direction,this.getBulletSpeed());
			//为子弹建立线程
			Thread t=new Thread(bu);
			t.start();
			clip.add(bu);
			PlayMusic p=new PlayMusic("music\\shoot.wav");
			p.start();
		}
	}
}
//敌方坦克
class EnemyTank extends Tank implements Runnable{
	//敌方坦克等级,默认为0
	private int level=0;
	private int changeDirect=10;//走多少步换方向
	private int shootTime=10;//走多少步发射
	private int score=1;//击中后的得分
	public EnemyTank(int x, int y,int level) {
		super(x,y);
		this.setDirection(2);//敌方坦克方向默认向下
		System.out.println("一辆敌方坦克被创建在("+x+","+y+")");
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
//		System.out.println("一辆敌方坦克被创建在("+x+","+y+")");
//		this.name=Info.enemyTanks.size()+1+"";
//		this.level=level;//设置等级
//		this.setClipNum(3);//设置弹夹子弹数量
//		this.setDirection(2);//敌方坦克方向默认向下
//		//根据等级选择配色方案和速度
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
		//先得到目标坦克的坐标区域
		int x1=0,y1=0,x2=0,y2=0;//左上角及右下角的坐标
		switch(Info.myTank.getDirection()){
		case 0://上
		case 2://下
			x1=Info.myTank.getX()-10;x2=Info.myTank.getX()+10+1;//+1是因为画坦克时炮管长度为1，所以坦克大小应为21*30，下同
			y1=Info.myTank.getY()-15;y2=Info.myTank.getY()+15;
			break;
		case 1://右
		case 3://左
			x1=Info.myTank.getX()-15;x2=Info.myTank.getX()+15;
			y1=Info.myTank.getY()-10;y2=Info.myTank.getY()+10+1;
			break;
		}
		//测试，可控制回避距离，就是距该坦克多远就得回避
		//x1-=1;y1-=1;x2+=1;y2+=1;
		//然后判断此坦克进行一次移动后是否进入目标坦克区域内
		int xl=0,yl=0,xr=0,yr=0;//一次移动后此坦克前段两点坐标
		switch(this.direction){
		case 0://上y-su,(x-10,y-15)(x+10,y-15)
			xl=x-10;yl=y-15-speed;
			xr=x+10+1;yr=y-15-speed;
			break;
		case 2://下y+su
			xl=x+10;yl=y+15+speed;
			xr=x-10+1;yr=y+15+speed;
			break;
		case 1://右x+su
			xl=x+15+speed;yl=y-10;
			xr=x+15+speed;yr=y+10+1;
			break;
		case 3://左x-su
			xl=x-15-speed;yl=y+10;
			xr=x-15-speed;yr=y-10+1;
			break;
		}
		//判断（xl,yl）（xr,yr）两点是否在(x1,y1)(x2,y2)区域内部（包括边界）
		if((xl>=x1&&xl<=x2 && yl>=y1&&yl<=y2)||(xr>=x1&&xr<=x2 && yr>=y1&&yr<=y2))
			isTouch=true;
		return isTouch;
	}
	public void shoot(){
		if(clip.size()<clipNum){
			//新建一个子弹对象
			Bullet bu=new Bullet(x,y,direction,this.bulletSpeed);
			//为子弹建立线程
			Thread t=new Thread(bu);
			t.start();
			clip.add(bu);
		}
	}
	public void run() {
		int count=0;//计数，计算坦克累计走了多少步
		while(isLive){
			//System.out.println("一个敌方坦克进程在运行");
			try {
				do{
					Thread.sleep(100);
				}while(isPause||Info.isPause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!(this.isTouchEnemy()||this.isTouchMyTank()))//如果不会碰撞才往前走一步
			{
				switch(this.direction){//坦克根据方向向前走一步
				case 0://上
					if(y>=speed+15)y-=speed;break;
				case 1://右
					if(x<=MyTankGame.width-speed-15)x+=speed;break;
				case 2://下
					if(y<=MyTankGame.height-speed-15)y+=speed;break;
				case 3://左
					if(x>=speed+15)x-=speed;break;
				}
			}
			
			//随机改变方向
			count++;
			if(count%this.changeDirect==0){//每隔changeDirect步才改变一次方向
				switch((int)(Math.random()*4)){
				case 0://上
					this.setDirection(0);break;
				case 1://右
					this.setDirection(1);break;
				case 2://下
					this.setDirection(2);break;
				case 3://左
					this.setDirection(3);break;
				default:System.out.println("不可能发生情况发生了！！！！！！！！！！！！");
				}
			}
			//发射子弹
			if(count%this.shootTime==0){
				shoot();
			}
		}
		System.out.println("一辆敌方坦克被摧毁");
		
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
//坦克类父类
abstract class Tank{
	//坐标
	int x=0;
	int y=0;
	int direction=0;//方向//0123=上右下左
	int speed=1;//速度，默认为1
	int bulletSpeed=1;//子弹速度，默认为1
	int color=-1;//颜色，默认为方案0
	boolean isLive=true;//是否存活
	boolean isPause=false;//是否暂停行动
	Vector<Bullet>clip=new Vector<Bullet>();//弹夹,即子弹数组（包括已发出但未死亡的子弹）
	int clipNum=5;//弹夹中可容纳的子弹数量
	String name="未命名";
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}
	public boolean isTouchEnemy(){
		boolean isTouch=false;
		for(int i=0;i<Info.enemyTanks.size();i++){
			EnemyTank en=Info.enemyTanks.get(i);//取出一辆坦克
			if(en.equals(this)){//如果取出的坦克是自己，则跳过此次判断
				continue;
			}
			//先得到目标坦克的坐标区域
			int x1=0,y1=0,x2=0,y2=0;//左上角及右下角的坐标
			switch(en.getDirection()){
			case 0://上
			case 2://下
				x1=en.getX()-10;x2=en.getX()+10+1;//+1是因为画坦克时炮管长度为1，所以坦克大小应为21*30，下同
				y1=en.getY()-15;y2=en.getY()+15;
				break;
			case 1://右
			case 3://左
				x1=en.getX()-15;x2=en.getX()+15;
				y1=en.getY()-10;y2=en.getY()+10+1;
				break;
			}
			//测试，可控制回避距离，就是距该坦克多远就得回避
			//x1-=1;y1-=1;x2+=1;y2+=1;
			//然后判断此坦克进行一次移动后是否进入目标坦克区域内
			int xl=0,yl=0,xr=0,yr=0;//一次移动后此坦克前段两点坐标
			switch(this.direction){
			case 0://上y-su,(x-10,y-15)(x+10,y-15)
				xl=x-10;yl=y-15-speed;
				xr=x+10+1;yr=y-15-speed;
				break;
			case 2://下y+su
				xl=x+10;yl=y+15+speed;
				xr=x-10+1;yr=y+15+speed;
				break;
			case 1://右x+su
				xl=x+15+speed;yl=y-10;
				xr=x+15+speed;yr=y+10+1;
				break;
			case 3://左x-su
				xl=x-15-speed;yl=y+10;
				xr=x-15-speed;yr=y-10+1;
				break;
			}
			//判断（xl,yl）（xr,yr）两点是否在(x1,y1)(x2,y2)区域内部（包括边界）
			if((xl>=x1&&xl<=x2 && yl>=y1&&yl<=y2)||(xr>=x1&&xr<=x2 && yr>=y1&&yr<=y2))
				isTouch=true;
		}
		
		return isTouch;
	}
	//以下为自动生成的get,set方法
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