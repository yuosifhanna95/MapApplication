package application;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ImagePlace {
	private int Id;
	private ImageView imageview;
	private Label label;
	private Place place;
	private int X, Y;

	public ImagePlace() {
		this.X = 0;
		this.Y = 0;
		this.Id = -1;
		this.imageview = null;
		this.label = null;
		this.place = null;
	}

	public ImagePlace(int Id, ImageView imageview, Label label, Place place, int X, int Y) {
		this.Id = Id;
		this.imageview = imageview;
		this.label = label;
		this.place = place;
		this.X = X;
		this.Y = Y;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getId() {
		return Id;
	}

	public void setImageview(ImageView imageview) {
		this.imageview = imageview;
	}

	public ImageView getImageview() {
		return imageview;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Label getLabel() {
		return label;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}

	public void setX(int x) {
		X = x;
	}

	public int getX() {
		return X;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getY() {
		return Y;
	}

}