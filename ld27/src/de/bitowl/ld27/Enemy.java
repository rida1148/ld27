package de.bitowl.ld27;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.bitowl.ld27.astar.AStar;
import de.bitowl.ld27.astar.AreaMap;
import de.bitowl.ld27.astar.ClosestHeuristic;
import de.bitowl.ld27.astar.Node;
import de.bitowl.ld27.astar.Path;

public class Enemy extends Entity{

	Path path;
	int pathIndex;
	
	float newPathTime;
	
	float time;
	
	Animation animation;
	float animTime;
	
	public Enemy(float pX,float pY){
		z=3;
		
		offsetX=2;
		offsetY=6;
		x=pX*level.tileWidth+offsetX;
		y=pY*level.tileHeight+offsetY;
		width=28;
		height=20;
		damageOnPlayer=1;
		collidable=true;
		blocking=true;
		
		
		findPlayer();
		animation=new Animation(0.2f, TestScreen.atlas.findRegions("enemy"));
	}
	@Override
	public void update(float pDelta) {
		
		if(newPathTime>0){
			newPathTime-=pDelta;
		}
		
		time+=pDelta;
		if(path!=null && pathIndex<path.getLength()){
			if(time>0.5f){
				//time-=0.5f;
				time=0;
				x=path.getX(pathIndex)*level.tileWidth + offsetX;
				y=path.getY(pathIndex)*level.tileHeight + offsetY;
				pathIndex++;
			}else{
				
				// interpolate
				x=interpol(path.getX(pathIndex-1),path.getX(pathIndex),time*2)*level.tileWidth + offsetX;
				y=interpol(path.getY(pathIndex-1),path.getY(pathIndex),time*2)*level.tileHeight + offsetY;
			}
		}else{
			if(newPathTime<=0){
				findPlayer();
			}
		}

		animTime+=pDelta;
		if(animTime>animation.animationDuration){animTime-=animation.animationDuration;}
	}
	@Override
	public void draw(SpriteBatch batch) {
		batch.draw(animation.getKeyFrame(animTime),x-offsetX,y-offsetY);
	}
	
	public float interpol(float v0,float v1,float delta){ // catch dat bad guy!
		return v0+(v1-v0)*delta;
	}
	
	 @Override
	public void hitByPlayer(boolean pX) {
		level.restart();
	}
	@Override
	public void hitByBullet() {
		kill();
	}
	public void kill(){
		level.entities.removeValue(this, true);
	}
	
	public void findPlayer(){
		// search the way to the player
		// http://code.google.com/p/a-star-java/source/browse/AStar/src/aStar/TestAStar.java?r=7
		AreaMap map=new AreaMap(level.mapWidth,level.mapHeight,level.obstacleMap);
		
		ClosestHeuristic heuristic = new ClosestHeuristic();
		
		AStar pathFinder = new AStar(map,heuristic);
		
		path=pathFinder.calcShortestPath((int)((x+width/2)/level.tileWidth),(int)((y+height/2)/level.tileHeight), (int)((level.player.x+level.player.width/2)/level.tileWidth),(int)((level.player.y+level.player.height/2)/level.tileHeight));
		

		pathFinder.printPath();
		
		pathIndex=1;
		time=0;
		if(path==null){ // could not find a path
			newPathTime=2;
		}else{
			path.prependWayPoint(new Node((int)(x/level.tileWidth),(int)(y/level.tileHeight)));
		}
	}
}
