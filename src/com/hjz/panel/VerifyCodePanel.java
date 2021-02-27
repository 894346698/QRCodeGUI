package com.hjz.panel;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Label;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;
import java.io.ByteArrayOutputStream;
import com.hjz.filter.MyFilter;
import com.hjz.main.CodeFrame;
import com.hjz.util.CreatAndReadExcel;
import com.hjz.util.TwoDimensionCode;

import com.hjz.util.VerifyXml;
import com.hjz.util.verify;
public class VerifyCodePanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image image = new ImageIcon("./images/4.jpg").getImage();
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), 0, 0, image.getWidth(null), image.getHeight(null),
				null);
	}

	private String imgPath = null;
	private String execlPath = null;
	private String execlname = null;
	private TwoDimensionCode codeUtil = new TwoDimensionCode();
	private VerifyXml verifyXml = new VerifyXml();//验证类
	public VerifyCodePanel() {
		setLayout(null);
		// 根据用户选择的语言，确定界面语言
		JLabel nameLabel = null;
		JButton chooseExcelBtn = null;
		JButton verifyBtn = null;

		JLabel contentLabel = null;
		System.out.println(CodeFrame.language);
		if (CodeFrame.language == "Chinese") {
		
			nameLabel = new JLabel("验证信息");
			chooseExcelBtn = new JButton("选择文件");
			verifyBtn = new JButton("验证");
			contentLabel = new JLabel("\u5185\u5BB9\u5982\u4E0B");

		} else if (CodeFrame.language == "English") {
			
			nameLabel = new JLabel("Verify message");
			chooseExcelBtn = new JButton("Select file");
			verifyBtn = new JButton("Verification");
			contentLabel = new JLabel("Show content");

		} else if (CodeFrame.language == "Arabic") {
			
			nameLabel = new JLabel("تحقق من الرسالة");
			chooseExcelBtn = new JButton("حدد الملف");
			verifyBtn = new JButton("التحقق");
	        contentLabel = new JLabel("عرض المحتوى");
		}

//		JLabel nameLabel = new JLabel("\u4E8C\u7EF4\u7801\u56FE\u7247");
		nameLabel.setForeground(Color.white);
		nameLabel.setFont(new Font("lucida", Font.BOLD, 16));
		nameLabel.setBounds(30, 25, 92, 24);
		add(nameLabel);
//放文件内容的文本显示框
		
		final JTextArea resultArea = new JTextArea();
		resultArea.setFont(new Font("lucida", Font.PLAIN, 20));
		JScrollPane resultArea1 = new JScrollPane(resultArea);
		resultArea1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultArea1.setBounds(30, 125, 700, 220);// 最后一个116
		add(resultArea1);
		

		
//		JButton chooseExcelBtn = new JButton("\u9009\u62E9\u56FE\u7247");
		chooseExcelBtn.setFont(new Font("lucida", Font.BOLD, 16));

		chooseExcelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				// 设置文件的过滤器
				String[] filterString = {".xls", ".xlsx"};
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
						
						List<List<Object>> excel2003List = CreatAndReadExcel.readExcel(excel2003Path);				
						resultArea.setText(fl.getName()+'\n'+excel2003List.get(0).toString()+'\n'+excel2003List.get(1).toString()+'\n'+excel2003List.get(2).toString()+'\n'+excel2003List.get(3).toString());
						
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
			
				}
			}
		});
		
		chooseExcelBtn.setBounds(154, 26, 160, 23);
		add(chooseExcelBtn);


		verifyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = "";
				try {
					List<List<Object>> excelList = CreatAndReadExcel.readExcel(execlPath);
					List<String> list = new ArrayList<String>();
					verify.excelistToList(excelList,list);
					result = verify.verify_name(execlname,excelList,CodeFrame.language)+"     ";
					//判断调用组合件信息验证还是整机或者零件级信息验证
					//组合件二维码所属组合件为0也就是list[4]
					System.out.println(list);
					if(list.get(4).equals("0")&&list.get(5).equals("0")&&list.get(6).equals("0")){
						result += verifyXml.verifyComproduct(list,CodeFrame.language);
					}
					//整机二维码二维码所属整机为0也就是list[6]
					if(!list.get(4).equals("0")&&!list.get(5).equals("0")&&list.get(6).equals("0")){
						result +=verifyXml.verifyMproduct(list,CodeFrame.language);
					}else if(!list.get(6).equals("0")){
						result +=verifyXml.verifyCproduct(list,CodeFrame.language);
					}
					JOptionPane.showMessageDialog(null, result, "", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}



			}
		});
		verifyBtn.setFont(new Font("lucida", Font.BOLD, 16));
		verifyBtn.setBounds(330, 27, 140, 23);
		add(verifyBtn);

//		JLabel contentLabel = new JLabel("\u5185\u5BB9\u5982\u4E0B");
		contentLabel.setForeground(Color.white);
		contentLabel.setFont(new Font("lucida", Font.BOLD, 16));
		contentLabel.setBounds(30, 91, 140, 24);
		add(contentLabel);
		

	}
}
