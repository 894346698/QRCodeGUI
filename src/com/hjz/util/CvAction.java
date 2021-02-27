package com.hjz.util;

import java.util.ArrayList;


import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import org.opencv.imgproc.Imgproc;


public class CvAction {

	
	public static Mat Canny (Mat inImg) {
		
		//绘制边框
		Mat imgGray = new Mat();
		Imgproc.cvtColor(inImg, imgGray, Imgproc.COLOR_BGR2GRAY);
		Mat edges = new Mat();
		Imgproc.Canny(imgGray, edges, 80, 200);
		return edges;	
	}
	
	public static Mat Contours (Mat inImg) {
	
		Mat edges = inImg.clone(); 
		Mat edges1 = inImg.clone(); 
		Imgproc.cvtColor(inImg, edges, Imgproc.COLOR_BGR2GRAY);
		
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(edges, contours, hierarchy,
		                     Imgproc.RETR_LIST,
		                     Imgproc.CHAIN_APPROX_SIMPLE);
		Imgproc.drawContours(edges1, contours, -1, CvUtils.COLOR_WHITE);
		
	
		return edges1;

		
	}
	
	
	
	
	
	
	
	
	
}
