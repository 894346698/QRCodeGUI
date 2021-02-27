package com.hjz.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;


import com.hjz.panel.DeCodePanel;
import com.hjz.panel.EnCodePanel;
import com.hjz.panel.VerifyCodePanel;
import com.hjz.util.*;

  
public class CodeFrame extends JFrame {
	private static int FW = 800;// 界面宽度
	private static int FH = 700;// 界面高度
	public static String language = null;//界面语言

	//初始化框架
	private void initFrame() { 
		try {
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
	

		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] possibleValues = { "Chinese", "English", "Arabic" };//语言选择
		Object selectedValue = JOptionPane.showInputDialog(null, "Choose language", "",
				JOptionPane.PLAIN_MESSAGE, null, possibleValues, possibleValues[0]);
		language = (String) selectedValue;
		UIManager.put("JTabbedPane.contentOpaque", false);
		JTabbedPane tabPane = new JTabbedPane() {};
		EnCodePanel enCodePanel = new EnCodePanel(); // 加密panel
		DeCodePanel deCodePanel = new DeCodePanel();//解密panel
		VerifyCodePanel vel = new VerifyCodePanel();//验证panel
		JPanel j1 = new JPanel();
		JCheckBoxMenuItem jcbmi = null;
		tabPane.setFont(new Font("lucida", Font.BOLD, 20));
		
		// 根据用户选择的语言确定界面语言
		if (CodeFrame.language == "Chinese") {
			tabPane.add("编码",enCodePanel);
			tabPane.add("解码", deCodePanel);
			tabPane.add("验证", vel);		
		} else if (CodeFrame.language == "English") {
			tabPane.add("Encoding", enCodePanel);
			tabPane.add("DEcoding", deCodePanel);
			tabPane.add("Verify", vel);
		} else if (CodeFrame.language == "Arabic") {
			tabPane.add("الترميز", enCodePanel);
			tabPane.add("فك", deCodePanel);
			tabPane.add("التحقق", vel);
		}

		this.add(tabPane);
		this.setIconImage(new ImageIcon("./images/icon.png").getImage());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - FW) / 2, (screenSize.height - FH) / 2, FW, FH);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setVisible(true);
		
	}
	
	

	public CodeFrame() throws HeadlessException {
		super();
		initFrame();
	}

	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}

	public CodeFrame(String title) throws HeadlessException {
		super(title);
		
		initFrame();

	}
	public static void main(String[] args) {
		//软件名称
		new CodeFrame("二维码编码及采集系统                                                                                                                                      V1.0");

	}
}
