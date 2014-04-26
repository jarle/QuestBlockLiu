package Entities;

import Tiles.TileMap;

import java.awt.*;

public class Player extends Movable {

	public Player(TileMap tm, int size) {
		tileMap = tm;
		playerColor = new Color(255,0,0,90);

		this.width = size;
		this.height = size;

		moveSpeed = 0.5;
		maxSpeed = 5.1;
		maxFallingSpeed = 6;
		stopSpeed = 0.40;
		jumpStart = -8.0;
		gravity = 0.30;

	}

	public double getX(){
		return x;
	}

	public double getY(){
		return y;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setLeft(boolean b){
		left = b;
	}

	public void setRight(boolean b){
		right = b;
	}

	public void setJumping(boolean b){
		double nextX;
		if(dx > 0){
			nextX = x + 5;
		}
		else{
			nextX = x - 5;
		}
		calculateCorners(nextX, y);
		if(!falling || sliding){ //player can jump from walls
			jumping = b;
		}
	}

	public void setSprint(boolean b){
		sprinting = b;
	}

	public void calculateCorners(double x, double y){ //rectangular hit-detection
		int leftTile = tileMap.getColTile((int) x - width / 2);
		int rightTile = tileMap.getColTile(((int) (x + width / 2)) - 1);
		int topTile = tileMap.getRowTile((int)(y - height / 2));
		int bottomTile = tileMap.getRowTile((int)(y + height / 2) - 1);
		int[] blocked = {0,2}; //blocks that will block movement
		topLeft = (tileMap.getTile(topTile, leftTile) == blocked[0]) ||
				(tileMap.getTile(topTile, leftTile) == blocked[1]);

		topRight = tileMap.getTile(topTile,rightTile) == blocked[0] ||
				tileMap.getTile(topTile,rightTile) == blocked[1];

		bottomLeft = tileMap.getTile(bottomTile, leftTile) == blocked[0] ||
				tileMap.getTile(bottomTile, leftTile) == blocked[1];

		bottomRight = tileMap.getTile(bottomTile, rightTile) == blocked[0] ||
				tileMap.getTile(bottomTile, rightTile) == blocked[1];

	}

	public void update(){
		if(sliding){
			maxFallingSpeed = 3;
		}
		else{
			maxFallingSpeed = 6;
		}
		//calculate next position
		if(left){//accelerates/moves left
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		} //accelerates/moves right
		else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		else { //slows down to a stop
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			}
			else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
		}

		if(jumping){ //start of jump
			dy = jumpStart;
			falling = true;
			jumping = false;
		}

		if(falling){ //mid jump or if falling down
			dy += gravity;
			if(dy > maxFallingSpeed){
				dy = maxFallingSpeed;
			}
		}
		else{ //standing still
			dy = 0;
		}

		//slide detection
		calculateCorners(x+5, y);
		if(topRight || topLeft){
			sliding = true;
		}
		else{
			calculateCorners(x-5,y);
			if(topLeft || topRight){
				sliding = true;
			}
			else{
				sliding = false;
			}
		}

		//collision detection

		int currentColumn = tileMap.getColTile((int)x);
		int currentRow = tileMap.getRowTile((int)y);

		double nextX = x + dx;
		double nextY = y + dy;

		double tempX = x;
		double tempY = y;

		calculateCorners(x, nextY); //calculate for next y
		if(dy < 0){ //accelerating upwards
			if(topLeft || topRight){
				dy = 0;
				tempY = currentRow * tileMap.getTileSize() + height / 2;

			}
			else{ //no collision, can continue
				tempY += dy;
			}
		}
		if(dy > 0){ //accelerating towards ground
			if(bottomLeft || bottomRight){
				dy = 0;
				falling = false;
				tempY = (currentRow + 1) * tileMap.getTileSize() - height / 2;
			}
			else{ //no collision
				tempY += dy;
			}
		}

		calculateCorners(nextX, y); //calculate for x
		if(dx < 0){ //to the left
			if(topLeft || bottomLeft){
				dx = 0;
				tempX = currentColumn * tileMap.getTileSize() + width / 2;

			}
			else{ //no collision
				tempX += dx;
			}
		}
		if(dx > 0){ //to the right
			if(topRight || bottomRight){
				sliding = true;
				dx = 0;
				tempX = (currentColumn + 1) * tileMap.getTileSize() - width / 2;
			}
			else{ //no collision
				tempX += dx;
			}
		}

		if(!falling){
			calculateCorners(x, y + 1);
			if(!bottomLeft && !bottomRight){
				falling = true;
			}
		}
		x = tempX;
		y = tempY;

		//moves the camera


	}

	public void draw(Graphics2D g){

		int tx = tileMap.getX();
		int ty = tileMap.getY();

		g.setColor(playerColor);
		g.fillRect((int) (tx + x - width / 2), (int) (ty + y - height / 2), width, height);
		g.setColor(Color.WHITE);
		g.drawRect((int) (tx + x - width / 2), (int) (ty + y - height / 2), width, height);
	}

}
