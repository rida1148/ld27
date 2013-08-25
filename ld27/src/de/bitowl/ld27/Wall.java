package de.bitowl.ld27;

public class Wall extends Entity{
	public Wall(int pX,int pY) {
		tileX=pX;tileY=pY;
		offsetX=0;
		offsetY=3;
		x=pX*level.tileWidth;y=pY*level.tileHeight+offsetY;
		width=32;
		height=22;
		texture=TestScreen.wallT;
		collidable=true;
		blocking=true;
	}
	public void down(){
		texture=TestScreen.wallOpenT;
		blocking=false;
		collidable=false;
	}
	public void up(){
		texture=TestScreen.wallT;
		blocking=true;
		collidable=true;
	}
	
	@Override
	public void powerByConnection(boolean pOn) {
		if(pOn){
			down();
		}else{
			up();
		}
	}
}
