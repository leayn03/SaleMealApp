package com.meal.util;

/**
 * @author xiamingxing
 * 
 */
public interface GestureListenerImpl {

	public void DownAction(double distance, double viewWidth, double viewHeight);

	public void DownLeftAction(double distance, double viewWidth,
			double viewHeight);

	public void DownRightAction(double distance, double viewWidth,
			double viewHeight);

	public void LeftAction(double distance, double viewWidth, double viewHeight);

	public void RightAction(double distance, double viewWidth, double viewHeight);

	public void UpAction(double distance, double viewWidth, double viewHeight);

	public void UpLeftAction(double distance, double viewWidth,
			double viewHeight);

	public void UpRightAction(double distance, double viewWidth,
			double viewHeight);

}