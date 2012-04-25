package com.cohesiva.rpg.game.core;

public class Tile {
	private int width;
	private int height;
	private int assetVerticalOffset;
	private int assetHorizontalOffset;
	private int renderOffsetX;
	private int renderOffsetY;
	
	private boolean blocking;
	private int imageIndex;

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setAssetHorizontalOffset(int assetHorizontalOffset) {
		this.assetHorizontalOffset = assetHorizontalOffset;
	}

	public void setAssetVerticalOffset(int assetVerticalOffset) {
		this.assetVerticalOffset = assetVerticalOffset;
	}

	public int getAssetHorizontalOffset() {
		return assetHorizontalOffset;
	}

	public int getAssetVerticalOffset() {
		return assetVerticalOffset;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getRenderOffsetX() {
		return renderOffsetX;
	}

	public void setRenderOffsetX(int renderOffsetX) {
		this.renderOffsetX = renderOffsetX;
	}

	public int getRenderOffsetY() {
		return renderOffsetY;
	}

	public void setRenderOffsetY(int renderOffsetY) {
		this.renderOffsetY = renderOffsetY;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	public boolean isBlocking() {
		return blocking;
	}
}
