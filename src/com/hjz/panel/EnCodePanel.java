package com.hjz.panel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.JobAttributes;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import com.hjz.filter.MyFilter;
import com.hjz.main.CodeFrame;

import com.hjz.util.CreatAndReadExcel;
import com.hjz.util.TwoDimensionCode;
import com.hjz.util.verify;

public class EnCodePanel extends JPanel {
	//设置panel背景
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("./images/4.jpg").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
				null);
	}
	
	private TwoDimensionCode codeUtil = new TwoDimensionCode();
	private String execlPath = null;
	private String execlname = null;
	public EnCodePanel() {
		setForeground(Color.BLACK);
		setLayout(null);
		
		// 根据用户选择的语言，确定界面语言
		JLabel label0 = null;
		JLabel label1 = null;
		JButton codeBtn = null;
		JLabel choosePathLabel = null;
		JButton chooseExcelBtn = null;
		
		
		//初始化文本框
		JLabel[] label = new JLabel[12];   //二维码信息标签
		JTextArea[] n = new JTextArea[12]; //二维码内容
		
		if (CodeFrame.language == "Chinese") {
			label0 = new JLabel("请输入内容");
			chooseExcelBtn = new JButton("选择文件");
			label1 = new JLabel("文件名");
			codeBtn = new JButton("生成二维码");
			choosePathLabel = new JLabel("存储路径");
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
			chooseExcelBtn = new JButton("Select file");
			label0 = new JLabel("Enter content");
			label1 = new JLabel("File name:");
			codeBtn = new JButton("Generate QR");
			choosePathLabel = new JLabel("Storage path:");
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
			chooseExcelBtn = new JButton("حدد الملف");
			label0 = new JLabel("الرجاء إدخال المحتوى");
			label1 = new JLabel("اسم الملف:");
			codeBtn = new JButton("توليد QR");
			choosePathLabel = new JLabel("مسار التخزين:");
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
		
		//定义文本框
		
		
		//请输入内容标签
		label0.setFont(new Font("lucida", Font.BOLD, 15));
		label0.setForeground(Color.white);
		label0.setBounds(26, 20, 170, 29);//请输入内容位置
		add(label0);
		//文件名标签
		label1.setFont(new Font("lucida", Font.BOLD, 15));
		label1.setForeground(Color.white);
		label1.setBounds(150, 20, 93, 29);
		add(label1);
		//文件名内容文本框
		JTextArea namecontent = new JTextArea();
		namecontent.setFont(new Font("lucida", Font.PLAIN, 15));
		namecontent.setBounds(225, 20, 145, 26);//文件名内容输入位置
		add(namecontent);
		
		for(int i = 0;i < 12; i++) {
			label[i].setFont(new Font("lucida", Font.BOLD, 15));
			label[i].setForeground(Color.white);
			n[i] = new JTextArea();
			n[i].setFont(new Font("lucida", Font.PLAIN, 15));
		}
						
		//二维码文本框第一行
		//任务代号
		label[0].setBounds(26, 60, 80, 29);//显示距离左上角位置以及长度宽度
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
		
		
		//生成二维码成功后信息显示区域
		final JTextArea saveArea = new JTextArea();
		saveArea.setLineWrap(true);
		saveArea.setFont(new Font("lucida", Font.PLAIN, 18));
		saveArea.setForeground(Color.RED);
		saveArea.setEditable(false);
		saveArea.setBounds(26, 270, 700, 100);
		add(saveArea);

		//当前路径显示标签
		final JTextField choosePathField = new JTextField();
		String jarFilePath = "./binaryCode";
		try {
			jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		choosePathField.setText(jarFilePath);
		choosePathField
				.setToolTipText("默认路径为当前应用所在路径");
		choosePathField.setEditable(false);
		choosePathField.setBounds(150, 240, 209, 21);
		add(choosePathField);
		choosePathField.setColumns(10);





		
		
		
		
		/**
		 * 生成二维码(QRCode)按钮功能实现
		 * 
		 */
		codeBtn.setFont(new Font("lucida", Font.BOLD, 15));// 15
		codeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//判断信息是否为空
				if (!isContentSuit(n[0].getText()) ||!isContentSuit(n[1].getText())||!isContentSuit(n[2].getText())||!isContentSuit(n[3].getText())||!isContentSuit(n[4].getText())
						||!isContentSuit(n[5].getText())||!isContentSuit(n[6].getText())||!isContentSuit(n[7].getText())||!isContentSuit(n[8].getText())||!isContentSuit(n[9].getText())
						||!isContentSuit(n[10].getText())||!isContentSuit(n[11].getText()))
					return;
				//判断具体信息格式
				//判断编码地域/装备类型/证明书
				for(int i = 1 ;i<=11;i++) {
					if(!(verify.isInteger(n[i].getText()) && Integer.valueOf(n[i].getText()) <= 1 && Integer.valueOf(n[i].getText()) >= 0)) {
						if (CodeFrame.language == "Chinese") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"格式不正确，应该为0-1", "提示", JOptionPane.ERROR_MESSAGE);
						} else if (CodeFrame.language == "English") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"The format is incorrect, it should be 0-1", "Please note", JOptionPane.ERROR_MESSAGE);
						} else if (CodeFrame.language == "Arabic") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"التنسيق غير صحيح ، يجب أن يكون 0-1", "مستعجل", JOptionPane.ERROR_MESSAGE);
						}
						return;
					}
					if(i==2) i=10;
				}
				
				//判断所属系统
				if(!(verify.isInteger(n[3].getText()) && Integer.valueOf(n[3].getText()) <= 4 && Integer.valueOf(n[3].getText()) >= 1)) {
					if (CodeFrame.language == "Chinese") {
						JOptionPane.showMessageDialog(null, label[3].getText()+"格式不正确，应该为1-4", "提示", JOptionPane.ERROR_MESSAGE);
					} else if (CodeFrame.language == "English") {
						JOptionPane.showMessageDialog(null, label[3].getText()+"The format is incorrect, it should be 1-4", "Please note", JOptionPane.ERROR_MESSAGE);
					} else if (CodeFrame.language == "Arabic") {
						JOptionPane.showMessageDialog(null, label[3].getText()+"التنسيق غير صحيح ، يجب أن يكون 1-4", "مستعجل", JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
				
				//判断子系统
				for(int i = 4 ;i<=6;i++) {
					if(!(verify.isInteger(n[i].getText()) && Integer.valueOf(n[i].getText()) <= 99 && Integer.valueOf(n[i].getText()) >= 0)) {
						if (CodeFrame.language == "Chinese") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"格式不正确，应该为0-99", "提示", JOptionPane.ERROR_MESSAGE);
						} else if (CodeFrame.language == "English") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"The format is incorrect, it should be 0-99", "Please note", JOptionPane.ERROR_MESSAGE);
						} else if (CodeFrame.language == "Arabic") {
							JOptionPane.showMessageDialog(null, label[i].getText()+"التنسيق غير صحيح ، يجب أن يكون 0-99", "مستعجل", JOptionPane.ERROR_MESSAGE);
						}
						return;
					}

				}
				
				//判断时间
				if(!(verify.isRqFormat(n[10].getText()) )) {
					if (CodeFrame.language == "Chinese") {
						JOptionPane.showMessageDialog(null, label[10].getText()+"格式不正确，应该为yyyyMMdd格式", "提示", JOptionPane.ERROR_MESSAGE);
					} else if (CodeFrame.language == "English") {
						JOptionPane.showMessageDialog(null, label[10].getText()+"The format is incorrect, it should be in yyyyMMdd format", "Please note", JOptionPane.ERROR_MESSAGE);
					} else if (CodeFrame.language == "Arabic") {
						JOptionPane.showMessageDialog(null, label[10].getText()+"التنسيق غير صحيح ، يجب أن يكون بتنسيق yyyyMMdd", "مستعجل", JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
				
				
				
				//按照规范格式存储信息
				String con = null;
				con = n[0].getText().toString()+"**"+n[1].getText().toString()+"**"+n[2].getText().toString()+"**"+n[3].getText().toString()+"**"+n[4].getText().toString()+"**"+
						n[5].getText().toString()+"**"+n[6].getText().toString()+"**"+n[7].getText().toString()+"**"+n[8].getText().toString()+"**"+n[9].getText().toString()+"**"+
						n[10].getText().toString()+"**"+n[11].getText().toString();

				// 若文件名命名否则以产品信息命名二维码图片
				String imgName = null;
				if (namecontent.getText().getBytes().length > 0) {
					imgName = namecontent.getText() + ".png";
				} else {
					
					String st2 = n[7].getText().toString();
					imgName = st2 + ".png";
					System.out.println(imgName);
				}

				//文件保存路径
				String savePath = choosePathField.getText() + "/" + imgName;
				codeUtil.encoderQRCode(con, savePath, "png", null);
				File imgFile = new File(savePath);
				String str1 = null;
				if (CodeFrame.language == "Chinese") {
					str1 = "图片已经保存到";
				} else if (CodeFrame.language == "English") {
					str1 = "The picture has been saved to";
				} else if (CodeFrame.language == "Arabic") {
				
					str1 = "تم حفظ الصورة في";
				}	
				if (imgFile.exists()) {
					saveArea.setText(str1 + imgFile.getAbsolutePath());
					try {
						Image image = ImageIO.read(imgFile);
				
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});	
		codeBtn.setBounds(26, 410, 130, 29);//生成二维码按钮		
		add(codeBtn);
		
		/**
		 * 选择execl文件读取信息(QRCode)按钮功能实现
		 * 
		 */
		chooseExcelBtn.setFont(new Font("lucida", Font.BOLD, 15));
		chooseExcelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// 设置文件的过滤器
				String[] filterString = {".xls"};
				MyFilter filter = new MyFilter(filterString);
				// 获取jar包位置，设置JFileChooser当前路径
				String jarFilePath = "./excel";
				jfc.setCurrentDirectory(new File(jarFilePath));
				jfc.setFileFilter(filter);
				jfc.showOpenDialog(null);
				File fl = jfc.getSelectedFile();
				execlPath = fl.toString();
				execlname = fl.getName();
				System.out.println(fl.getName());
				if (fl.exists()) {
					String excel2003Path = fl.toString();
					try {
						
						List<List<Object>> List = CreatAndReadExcel.readExcel(excel2003Path);		
							n[0].setText(List.get(1).get(4).toString());
							n[1].setText(List.get(2).get(4).toString());
							n[2].setText(List.get(3).get(4).toString());
							n[3].setText(List.get(4).get(4).toString());						
							n[4].setText(List.get(5).get(4).toString());
							n[5].setText(List.get(6).get(4).toString());
							n[6].setText(List.get(7).get(4).toString());
							n[7].setText(List.get(8).get(4).toString());
							n[8].setText(List.get(9).get(4).toString());
							n[9].setText(List.get(10).get(4).toString());
							n[10].setText(List.get(11).get(4).toString());
							n[11].setText(List.get(12).get(4).toString());	
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
			
				}
			}
		});
		chooseExcelBtn.setBounds(380, 20, 93, 29);
		add(chooseExcelBtn);
		
		/**
		 * 更改路径
		 * 
		 */
		choosePathLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		choosePathLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// 获取jar包位置，设置JFileChooser当前路径
				String jarFilePath = "./binaryCode";
				try {
					jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
				} catch (UnsupportedEncodingException ex) {
					ex.printStackTrace();
				}
				jfc.setCurrentDirectory(new File(jarFilePath));
				jfc.showOpenDialog(null);
				File fl = jfc.getSelectedFile();
				if (fl != null)
					choosePathField.setText(fl.getAbsolutePath());
			}
		});
		choosePathLabel.setForeground(Color.RED);
		choosePathLabel.setBackground(Color.BLACK);
		choosePathLabel.setFont(new Font("lucida", Font.BOLD, 16));
		choosePathLabel.setBounds(26, 240, 110, 21);//选择文件位置
		add(choosePathLabel);
	}

	
	/**
	 * 判断信息是否为空
	 * 
	 * @param content 内容
	 */
	private boolean isContentSuit(String content) {

		if (content.getBytes().length <= 0 ) {
			if (CodeFrame.language == "Chinese") {
				JOptionPane.showMessageDialog(null, "内容缺失", "提示", JOptionPane.ERROR_MESSAGE);
			} else if (CodeFrame.language == "English") {
				JOptionPane.showMessageDialog(null, "Some content is missing", "Please note", JOptionPane.ERROR_MESSAGE);
			} else if (CodeFrame.language == "Arabic") {
				JOptionPane.showMessageDialog(null, "المحتوى مفقود", "مستعجل", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
		return true;
	}
}
