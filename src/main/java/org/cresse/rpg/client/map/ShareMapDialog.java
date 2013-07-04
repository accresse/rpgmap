package org.cresse.rpg.client.map;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ShareMapDialog extends JDialog {
	
	private static final String SHARE = "Share";
	private static final String UNSHARE = "Unshare";

	private static final long serialVersionUID = 1L;
	
	private JFormattedTextField portField;
	private JButton shareButton;
	private ShareAction shareAction = ShareAction.NO_ACTION;
	
	public ShareMapDialog(JFrame parent) {
		super(parent, "Share map...", true);
		Container panel = this.getContentPane();
		panel.setLayout(new GridLayout(0,2));
		
		panel.add(new JLabel("Host:"));
		panel.add(new JLabel(getHostInfo()));
		
		panel.add(new JLabel("Port:"));
		portField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		portField.setValue(24601);
		panel.add(portField);
				
		shareButton = new JButton(SHARE);
		shareButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String buttonText = shareButton.getText();
				if(buttonText.equals(SHARE)) {
					startSharing();
				} else {
					stopSharing();
				}
				ShareMapDialog.this.setVisible(false);
			}

		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				ShareMapDialog.this.shareAction = ShareAction.NO_ACTION;
				ShareMapDialog.this.setVisible(false);
			}
		});
		panel.add(shareButton);
		panel.add(cancelButton);
				
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	private String getHostInfo() {
		String host = "UNKNOWN";
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return host;
	}

	private void startSharing() {
		this.shareAction = ShareAction.CONNECT;
		this.portField.setEnabled(false);
		this.shareButton.setText(UNSHARE);
	}

	private void stopSharing() {
		this.shareAction = ShareAction.DISCONNECT;
		this.portField.setEnabled(true);
		this.shareButton.setText(SHARE);
	}

	private int convertToInt(Object obj) {
		Number number = (Number)obj;
		return number.intValue();
	}

	public int getPort() {
		return convertToInt(portField.getValue());
	}

	public ShareAction getShareAction() {
		return this.shareAction;
	}

}
