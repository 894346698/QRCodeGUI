package com.hjz.panel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.hjz.util.VerifyXml;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayOutputStream;
import com.hjz.filter.MyFilter;
import com.hjz.main.CodeFrame;

import com.hjz.util.AES;
import com.hjz.util.CvUtils;
import com.hjz.util.TwoDimensionCode;

import jp.sourceforge.qrcode.exception.DecodingFailedException;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class DeCodePanel extends JPanel {
	
	/**
	 * 设置界面背景
	 * 
	 * @param g 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("./images/4.jpg").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
				null);
	}

	static {
		      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		   }
	private String imgPath = null;  //文件路径
	private TwoDimensionCode codeUtil = new TwoDimensionCode(); //解码类
	private VerifyXml verifyXml = new VerifyXml();//验证类
	public boolean flag = true; //控制线程终止条件
	public static boolean isRun = true;
	public static boolean isEnd = false;
	

	public DeCodePanel() {
		setLayout(null);
		// 根据用户选择 确定界面语言
		JLabel nameLabel = null;    
		JButton chooseImgBtn = null; //选择文件按钮
		JButton deCodeBtn = null; //解码按钮
		JButton opencamera = null; //打开摄像头按钮
		JButton verifyCode = null; //验证二维码
		JLabel contentLabel = null; 
		
		//定义文本框
		JLabel[] label = new JLabel[12];   //二维码信息标签
		JTextArea[] n = new JTextArea[12]; //二维码内容
		
		if (CodeFrame.language == "Chinese") {
		
			nameLabel = new JLabel("二维码图片");
			chooseImgBtn = new JButton("选择图片");
			deCodeBtn = new JButton("解码");
			verifyCode = new JButton("验证");
			contentLabel = new JLabel("内容如下");
			opencamera = new JButton("打开摄像头");
			label[0] = new JLabel("任务代号：");
			label[1] = new JLabel("编码地域：");
			label[2] = new JLabel("装备类型：");
			label[3] = new JLabel("所属系统：");
			label[4] = new JLabel("一级系统：");
			label[5] = new JLabel("二级系统：");
			label[6] = new JLabel("三级系统：");
			label[7] = new JLabel("产品名称：");
			label[8] = new JLabel("产品代号：");
			label[9] = new JLabel("产品编号：");
			label[10] = new JLabel("出厂时间：");
			label[11] = new JLabel("证明书：");
		} else if (CodeFrame.language == "English") {
			
			nameLabel = new JLabel("QR picture");
			chooseImgBtn = new JButton("Choose picture");
			deCodeBtn = new JButton("Decode QR");
			verifyCode = new JButton("Verify");
			contentLabel = new JLabel("Show content");
			opencamera = new JButton("open camera");
			label[0] = new JLabel("Task code");
			label[1] = new JLabel("Code area");
			label[2] = new JLabel("Equipment");
			label[3] = new JLabel("Total：");
			label[4] = new JLabel("First：");
			label[5] = new JLabel("Second：");
			label[6] = new JLabel("Thrid：");
			label[7] = new JLabel("P_Nname");
			label[8] = new JLabel("P_Code");
			label[9] = new JLabel("P_Number");
			label[10] = new JLabel("Date：");
			label[11] = new JLabel("Manual：");
		} else if (CodeFrame.language == "Arabic") {
			nameLabel = new JLabel("QRصورة");
			chooseImgBtn = new JButton("اختر صورة");
			deCodeBtn = new JButton("فك");
			verifyCode = new JButton("التحقق");
	        contentLabel = new JLabel("عرض المحتوى");
	        opencamera = new JButton("الة تصوير");
	        label[0] = new JLabel("رمز المهمة");
	        label[1] = new JLabel("منطقة الترميز");
	        label[2] = new JLabel("نوع الآداة");
	        label[3] = new JLabel("نظام الامتلاك");
	        label[4] = new JLabel("النظام 1");
	        label[5] = new JLabel("النظام 2");
	        label[6] = new JLabel("النظام 3");
	        label[7] = new JLabel("اسم المنتج");
	        label[8] = new JLabel("كود المنتج");
	        label[9] = new JLabel("رقم المنتج");
	        label[10] = new JLabel("تاريخ الظهور");
	        label[11] = new JLabel("شهادة");	
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		//定义文本框
		//二维码Label所在位置
		nameLabel.setFont(new Font("lucida", Font.BOLD, 16));	
		nameLabel.setForeground(Color.white);
		nameLabel.setBounds(30, 25, 92, 24);	
		add(nameLabel);
		
		contentLabel.setForeground(Color.white);
		for(int i = 0;i < 12; i++) {
			label[i].setFont(new Font("lucida", Font.BOLD, 15));
			label[i].setForeground(Color.white);
			n[i] = new JTextArea();
			n[i].setFont(new Font("lucida", Font.PLAIN, 15));
		}
				
		//二维码文本框第一行
		//任务代号
		label[0].setBounds(26, 60, 80, 29);
		n[0].setBounds(100, 60, 80, 29);
		//编码地域			
		label[1].setBounds(200, 60, 80, 29);
		n[1].setBounds(280, 60, 80, 29);
		//装备类型
		label[2].setBounds(380, 60, 80, 29);
		n[2].setBounds(460, 60, 80, 29);
		//所属系统
		label[3].setBounds(560, 60, 80, 29);
		n[3].setBounds(640, 60, 80, 29);
				
		//第二行
		//一级系统
		label[4].setBounds(26, 100, 80, 29);
		n[4].setBounds(100, 100, 80, 29);
		//二级系统	
		label[5].setBounds(200, 100, 80, 29);
		n[5].setBounds(280, 100, 80, 29);
		//三级系统	
		label[6].setBounds(380, 100, 80, 29);
		n[6].setBounds(460, 100, 80, 29);
		//证明书
		label[11].setBounds(560, 100, 80, 29);	
		n[11].setBounds(640, 100, 80, 29);
				
				
		//第三行
		//产品名称
		label[7].setBounds(26, 140, 80, 29);
		JScrollPane n7 = new JScrollPane(n[7]);
		n7.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		n7.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		n7.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		n7.setBounds(100, 140, 240, 35);
		add(n7);
		
		//产品代号
		label[8].setBounds(380, 140, 80, 29);
		JScrollPane n8 = new JScrollPane(n[8]);
		n8.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		n8.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		n8.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		n8.setBounds(460, 140, 240, 35);
		add(n8);
				
		//第四行	
		//产品编号
		label[9].setBounds(26, 180, 80, 29);
		JScrollPane n9 = new JScrollPane(n[9]);
		n9.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		n9.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		n9.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		n9.setBounds(100, 180, 240, 35);
		add(n9);
		//出厂时间			
		label[10].setBounds(380, 180, 80, 29);	
		JScrollPane n10 = new JScrollPane(n[10]);
		n10.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		n10.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		n10.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		n10.setBounds(460, 180, 240, 35);
		add(n10);
		        
		//添加文本框到panel
		for(int i = 0;i < 12;i++) {
			add(label[i]);
			if(i < 7 || i > 10) {
				add(n[i]);
			}
		}
		

		
		//选择上传图片按钮
		chooseImgBtn.setFont(new Font("lucida", Font.BOLD, 16));
		chooseImgBtn.setBounds(154, 26, 160, 23);
		add(chooseImgBtn);
		//实现上传图片功能按钮
		chooseImgBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// 设置文件的过滤器
				String[] filterString = { ".png", ".jpg", ".icon" };
				MyFilter filter = new MyFilter(filterString);
				// 获取jar包位置，设置JFileChooser当前路径
				String jarFilePath = "./binaryCode";
				try {
					jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
				jfc.setCurrentDirectory(new File(jarFilePath));
				jfc.setFileFilter(filter);
				jfc.showOpenDialog(null);
				File fl = jfc.getSelectedFile();
				if (fl.exists()) {
					imgPath = fl.getAbsolutePath();
					try {			
						Image image = ImageIO.read(fl);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		

		//显示二维码内容文本+的监听扫描枪
		final JTextArea resultArea = new JTextArea();
		resultArea.setFont(new Font("lucida", Font.PLAIN, 20));
		JScrollPane resultArea1 = new JScrollPane(resultArea);
		resultArea1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultArea1.setBounds(30, 270, 700, 80);// 存放位置地点
		add(resultArea1);
		
		//显示二维码内容+监听扫描枪
		resultArea.addKeyListener(new KeyListener() {
			String content1 = null;//返回文本的内容
			// 一个按下弹起的组合动作
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					String content = resultArea.getText().toString();
					content1 = codeUtil.decoderString(content);	
					
				}			
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode() == 10) {
					resultArea.setText(content1);
					String[] str = new String[13];
					str = jiequ(content1);
					for(int i = 0;i<12;i++) {
						n[i].setText(str[i]);
					}
				}
			}	
			@Override
			public void keyTyped(KeyEvent arg0) {			
				// TODO Auto-generated method stub	
			}
		});	

		
		
		//解码按钮
		deCodeBtn.setFont(new Font("lucida", Font.BOLD, 16));
		deCodeBtn.setBounds(330, 27, 140, 23);
		add(deCodeBtn);
		contentLabel.setFont(new Font("lucida", Font.BOLD, 16));
		contentLabel.setBounds(30, 240, 140, 24);//
		add(contentLabel);
		deCodeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (imgPath == null) {
					if (CodeFrame.language == "Chinese") {
						JOptionPane.showMessageDialog(null, "请插入二维码!", "提示", JOptionPane.INFORMATION_MESSAGE);
					} else if (CodeFrame.language == "English") {
						JOptionPane.showMessageDialog(null, "Please select a QR code image!", "Please note", JOptionPane.INFORMATION_MESSAGE);
					} else if (CodeFrame.language == "Arabic") {
						JOptionPane.showMessageDialog(null, "الرجاء إدخال رمز الاستجابة السريعة!", "مستعجل", JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				}
				String result = codeUtil.decoderQRCode(imgPath);
				String[] str = new String[13]; 
				str = jiequ(result);
				for(int i = 0;i<12;i++) {
					n[i].setText(str[i]);
				}
				resultArea.setText(result);
			}  
		});

		//验证按钮
		verifyCode.setFont(new Font("lucida", Font.BOLD, 16));
		verifyCode.setBounds(30,380,140,23);
		add(verifyCode);
		verifyCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> list = new ArrayList<String>();
				for (int i = 0;i<12;i++){
					list.add(n[i].getText());
				}
				//判断调用组合件二维码还是整机或者零件级二维码
				//组合件二维码所属组合件为0也就是n[4]
				String result = "";//界面返回的值
				if(n[4].getText().equals("0")&&n[5].getText().equals("0")&&n[6].getText().equals("0")){
					result = verifyXml.verifyComproduct(list,CodeFrame.language);
				}
				//整机二维码二维码所属整机为0也就是n[6]
				if(!n[4].getText().equals("0")&&!n[5].getText().equals("0")&&n[6].getText().equals("0")){
					result =verifyXml.verifyMproduct(list,CodeFrame.language);
				}else if(!n[6].getText().equals("0")){
					result =verifyXml.verifyCproduct(list,CodeFrame.language);
				}
				JOptionPane.showMessageDialog(null, result, "", JOptionPane.INFORMATION_MESSAGE);


			}
		});


		//打开摄像头按钮
		opencamera.setFont(new Font("lucida", Font.BOLD, 16));
		opencamera.setBounds(500, 27, 140, 23);
		add(opencamera);
		opencamera.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				JFrame window = new JFrame("Camera");
			    window.setSize(640, 480);
			    window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			    window.setLocationRelativeTo(null);
			      // 单击标题栏中的“关闭”按钮
				window.addWindowListener(new WindowAdapter() {//监听退出事件
					public void windowClosing(WindowEvent e) {	
						flag =false;
						window.dispose();//释放当前窗dao口资源，并且4102设置为不可1653见
						}
				});	
			 // <Esc>按键处理			  
			    JLabel la = new JLabel();
			    window.setContentPane(la);
			    window.setVisible(true);
			    VideoCapture camera = new VideoCapture(0);
			    boolean flag1 = true;
			    new Thread() {
			    public void run() {
			    		  try {
					          // 调整框架尺寸
					          camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
					          camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
					          // 读框架
					          Mat frame = new Mat();
					          BufferedImage img = null;
					          String content = null;
					          String result = null;
					          while ( flag1 ) {
					             if (camera.read(frame)) {
					                // 在这里您可以插入用于处理框架的代码
					                img = CvUtils.MatToBufferedImage(frame);			                
					                content = codeUtil.decoderQRCode(img);
					                if(content != null) {
							            // 解密
					                	try {
					                	content = codeUtil.decoderString(content);
										String[] str = new String[13]; 
										str = jiequ(content);
										for(int i = 0;i<12;i++) {
											n[i].setText(str[i]);
										}
										resultArea.setText(content);				
										window.dispose();//关闭窗口				
										return;//退出线程
					                	}catch (DecodingFailedException dfe) {
								            dfe.printStackTrace();
									        } catch (Exception re) {
									       re.printStackTrace();
									    }		 
					                }
					                if (img != null) {
					                   ImageIcon imageIcon = new ImageIcon(img);
					                   la.setIcon(imageIcon);
					                   la.repaint();
					                   window.pack();
					                }
					                try {
					                   Thread.sleep(10); // 每秒10帧
					                } catch (InterruptedException e1) {}
					             }
					             else {
					                break;
					             }
					          }
					       }
					       finally {
					          camera.release();
					          isRun = false;
					          isEnd = true;
					       }
					       window.setTitle("相机已禁用");
			    	  }
			    	  
			      }.start();
						}  
					});
	}
	

	
	/**
	 * 拆分解码内容
	 * 
	 * @param result 解码信息
	 * @return String[] 拆分后的二维码信息
	 */
	private String[] jiequ(String result) {
		String[] str = new String[13]; 
		for(int i = 0;i<12;i++) {
			if(i==11) {
				str[i] = result;
				break;	
			}
			int index=result.indexOf("*");
			str[i] = result.substring(0,index);
			result = result.substring(index+2,result.length());
		}
		return str;
	}
	
		
		

		
		
	
	


}
