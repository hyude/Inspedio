package com.inspedio.basic.ui;

import com.inspedio.basic.shape.InsRect;
import com.inspedio.helper.primitive.InsCallback;
import com.inspedio.helper.primitive.InsPoint;

public class InsSwipe extends InsRect{

	protected InsPoint startPoint;
	protected InsPoint endPoint;
	
	/**
	 * Callback event when UI Swiped to Left
	 */
	protected InsCallback onSwipeLeft;
	/**
	 * Callback event when UI Swiped to Right
	 */
	protected InsCallback onSwipeRight;
	/**
	 * Set this to FALSE to let object behind it to get Event. Defaulted to false
	 */
	public boolean onSwipedReturn = false;
	
	public InsSwipe(){
		this(0, 0, 0, 0);
	}
	
	public InsSwipe(int X, int Y, int Width, int Height){
		super(X, Y, Width, Height);
		this.startPoint = new InsPoint(X, Y);
		this.endPoint = new InsPoint(X, Y);
	}
	
	/**
	 * Set Callback to handle Swipe Left Event
	 */
	public void setSwipedLeftCallback(InsCallback c){
		this.onSwipeLeft = c;
	}
	
	/**
	 * Set Callback to handle Swipe Right Event
	 */
	public void setSwipedRightCallback(InsCallback c){
		this.onSwipeRight = c;
	}
	
	/**
	 * Do not override this unless you want to specifically access coordinate touched
	 */
	public boolean onPointerPressed(int X, int Y) {
		X -= this.x;
		Y -= this.y;
		if((X >= 0 && X <= this.width) && (Y >= 0 && Y <= this.height)){
			this.startPoint.x = X;
			this.startPoint.y = Y;
		}
		return false;
	}

	public boolean onPointerReleased(int X, int Y) {
		X -= this.x;
		Y -= this.y;
		if((X >= 0 && X <= this.width) && (Y >= 0 && Y <= this.height)){
			this.endPoint.x = X;
			this.endPoint.y = Y;
			return evaluate();
		}
		return false;
	}
	
	public boolean evaluate(){
		int dX = endPoint.x - startPoint.x;
		if(dX > 0){
			this.onSwipeRight.call();
			return onSwipedReturn;
		} else if(dX < 0){
			this.onSwipeLeft.call();
			return onSwipedReturn;
		}
		
		return false;
	}
}
