package org.cresse.rpg.client.map;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConnectMapDialog extends JDialog {
	
	private static final String SHARE = "Connect";
	private static final String UNSHARE = "Disconnect";

	private static final long serialVersionUID = 1L;
	
	private JTextField hostField;
	private JFormattedTextField portField;
	private JButton shareButton;
	private ShareAction shareAction = ShareAction.NO_ACTION;
	
	public ConnectMapDialog(JFrame parent) {
		super(parent, "Connect to map...", true);
		Container panel = this.getContentPane();
		panel.setLayout(new GridLayout(0,2));
		
		panel.add(new JLabel("Host:"));
		hostField = new JTextField("127.0.0.1");
		panel.add(hostField);
		
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
				ConnectMapDialog.this.setVisible(false);
			}

		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				ConnectMapDialog.this.shareAction = ShareAction.NO_ACTION;
				ConnectMapDialog.this.setVisible(false);
			}
		});
		panel.add(shareButton);
		panel.add(cancelButton);
				
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	private void startSharing() {
		this.shareAction = ShareAction.CONNECT;
		this.hostField.setEnabled(false);
		this.portField.setEnabled(false);
		this.shareButton.setText(UNSHARE);
	}

	private void stopSharing() {
		this.shareAction = ShareAction.DISCONNECT;
		this.hostField.setEnabled(true);
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

	public String getHost() {
		return hostField.getText().trim();
	}

}
