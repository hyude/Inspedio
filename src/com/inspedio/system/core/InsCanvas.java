
package com.inspedio.system.core;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

import com.inspedio.enums.ScreenOrientation;
import com.inspedio.system.helper.extension.InsPointerEvent;

/**
 * <code>InsCanvas</code> is how engine render its graphic.<br>
 * It also function as an input detector.<br>
 * It is automatically created when you initialize <code>InsGame</code> instance<br>.
 * Please note that <code>InsCanvas</code> is a singleton class, which means it can have only one object<br>
 * 
 * @author Hyude
 * @version 1.0
 */
public class InsCanvas extends GameCanvas{

	private static InsCanvas instance = null;
	
	public static InsCanvas getInstance(){
		try
		{
			if(instance == null){
				throw new Exception("InsCanvas instance is not initialized");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
	public static InsCanvas init(InsGame Game, ScreenOrientation Mode){
		try
		{
			if(instance == null){
				instance = new InsCanvas(Game, Mode);;
			} else {
				throw new Exception("InsCanvas instance already initialized");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
	
	private InsCanvas(InsGame Game, ScreenOrientation Mode) {
		super(false);
		this.setFullScreenMode(true);
		this.game = Game;
		this.displayMode = Mode;
		InsGlobal.hasTouchScreen = this.hasPointerEvents();
		this.setScreenSize(getWidth(), getHeight());
	}
	
	protected void sizeChanged(int width, int height)
	{
		this.setScreenSize(width, height);
	}
	
	protected void setScreenSize(int width, int height)
	{
		if(!fixedOrientation){
			
			if(width > height){
				if(this.displayMode == ScreenOrientation.PORTRAIT){
					this.rotateCanvas = true;
				}
			}
			else if(width < height){
				if(this.displayMode == ScreenOrientation.LANDSCAPE){
					this.rotateCanvas = true;
				}
			}
			
			if(rotateCanvas){
				InsGlobal.screenWidth = this.deviceWidth = height;
				InsGlobal.screenHeight = this.deviceHeight = width;
			} else {
				InsGlobal.screenWidth = this.deviceWidth = width;
				InsGlobal.screenHeight = this.deviceHeight = height;
			}
			
			InsGlobal.screenOrientation = this.deviceOrientation = this.displayMode;
			
			this.bufferImage = Image.createImage(this.deviceWidth, this.deviceHeight);
			this.graphic = this.bufferImage.getGraphics();
			this.bufferGraphics = getGraphics();
			//this.graphic = getGraphics();
			
			fixedOrientation = true;
			
			System.out.println("Screen Width : " + InsGlobal.screenWidth);
			System.out.println("Screen Height : " + InsGlobal.screenHeight);
		}
	}
	
	/**
	 * Transform Coordinate touched depend on Orientation
	 */
	protected InsPointerEvent transformCoordinate(InsPointerEvent e){
		if(rotateCanvas){
			int tmpX = e.x;
			int tmpY = e.y;
			
			e.x = tmpY;
			e.y = this.deviceHeight - tmpX;
		}
		//System.out.println("Event at (" + e.x + "," + e.y + ")");
		
		return e;
	}
	
	protected void pointerPressed(int X, int Y){
		InsGlobal.pointer.addEvent(transformCoordinate(new InsPointerEvent(X, Y, InsPointerEvent.PRESSED)));
	}
	
	protected void pointerReleased(int X, int Y){
		InsGlobal.pointer.addEvent(transformCoordinate(new InsPointerEvent(X, Y, InsPointerEvent.RELEASED)));
	}
	
	protected void pointerDragged(int X, int Y){
		InsGlobal.pointer.addEvent(transformCoordinate(new InsPointerEvent(X, Y, InsPointerEvent.DRAGGED)));
	}
	
	/**
	 * Clear Screen. Self explanatory
	 */
	public void clearScreen()
	{
		this.graphic.setColor(COLOR_BLACK);
		this.graphic.fillRect(0, 0, this.deviceWidth, this.deviceHeight);
	}
	
	/**
	 * Draw current FPS into screen. Used only for debugging 
	 */
	public void drawFPS()
	{
		int offset = 15;
		this.graphic.setColor(FPS_COLOR);
		this.graphic.setFont(InsCanvas.defaultFont);
		this.graphic.drawString("FPS : " + InsGlobal.stats.currentUPS + " / " + InsGlobal.stats.currentFPS + " / " + InsGlobal.stats.currentFrameSkip, 0, deviceHeight - (4 * offset), (Graphics.TOP|Graphics.LEFT));
		this.graphic.drawString("Process : " + InsGlobal.stats.currentUpdateTime + " / " + InsGlobal.stats.currentRenderTime, 0, deviceHeight - (3 * offset), (Graphics.TOP|Graphics.LEFT));
		this.graphic.drawString("Sleep : " + InsGlobal.stats.currentSleepTime, 0, deviceHeight - (2 * offset), (Graphics.TOP|Graphics.LEFT));
		this.graphic.drawString("Time : " + InsGlobal.stats.currentGameTime, 0, deviceHeight - offset, (Graphics.TOP|Graphics.LEFT));
	}
	
	protected void hideNotify()
	{
		InsGlobal.hideGame();
	}
	
	protected void showNotify()
	{
		InsGlobal.showGame();
	}
	
	protected void keyPressed(int keyCode)
	{
		super.keyPressed(keyCode);
		switch (keyCode)
		{
			case -6:
				InsGlobal.onLeftSoftKey();
				break;
			case -7:
				InsGlobal.onRightSoftKey();
				break;
		}
	}
	
	public void flushGraphics(){
		if(rotateCanvas){
			this.bufferGraphics.drawRegion(this.bufferImage, 0, 0, deviceWidth, deviceHeight, Sprite.TRANS_ROT90, 0, 0, Graphics.LEFT | Graphics.TOP);
		} else{
			this.bufferGraphics.drawRegion(this.bufferImage, 0, 0, deviceWidth, deviceHeight, Sprite.TRANS_NONE, 0, 0, Graphics.LEFT | Graphics.TOP);
			//this.bufferGraphics.drawImage(this.bufferImage, 0, 0, Graphics.LEFT | Graphics.TOP);
		}
		super.flushGraphics();
	}
	
	public static final int COLOR_BLACK = 0x000000;
	public static final int COLOR_RED = 0xFF0000;
	public static final int COLOR_WHITE = 0xFFFFFF;
	public static final int COLOR_YELLOW = 0xCCCC33;
	
	public static int FPS_COLOR = COLOR_WHITE;
	
	private Image bufferImage;
	private Graphics bufferGraphics;
	
	private ScreenOrientation displayMode;
	private boolean rotateCanvas;
	private boolean fixedOrientation = false;
	
	public static final Font defaultFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	
	public InsGame game;
	public Graphics graphic;
	public int deviceWidth;
	public int deviceHeight;
	public ScreenOrientation deviceOrientation;
	

}