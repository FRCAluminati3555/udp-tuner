/**
 * Copyright (c) 2019 Team 3555
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.aluminati3555.aluminativisionutil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Configuration interface
 * 
 * @author Caleb Heydon
 */
public class TunerPane extends VBox {
	private static final int PACKET_SIZE = 24;

	/**
	 * Sends tuning data to the robot
	 * 
	 * @param address
	 * @param port
	 * @param data
	 * @throws IOException
	 */
	private static void sendTuningData(String address, int port, TuningData data) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		DataOutputStream output = new DataOutputStream(byteOutput);

		output.writeDouble(data.kP);
		output.writeDouble(data.kI);
		output.writeDouble(data.kD);
		output.close();

		DatagramPacket packet = new DatagramPacket(byteOutput.toByteArray(), PACKET_SIZE);
		packet.setAddress(InetAddress.getByName(address));
		packet.setPort(port);
		socket.send(packet);

		socket.close();
	}

	public TunerPane() {
		super(1);
		this.setPadding(new Insets(10, 10, 10, 10));

		this.getChildren().add(new Label("IP Address"));
		TextField ipField = new TextField();
		this.getChildren().add(ipField);

		this.getChildren().add(new Label("Port"));
		TextField portField = new TextField();
		this.getChildren().add(portField);

		this.getChildren().add(new Label("kP"));
		TextField pField = new TextField();
		this.getChildren().add(pField);

		this.getChildren().add(new Label("kI"));
		TextField iField = new TextField();
		this.getChildren().add(iField);

		this.getChildren().add(new Label("kD"));
		TextField dField = new TextField();
		this.getChildren().add(dField);

		Button sendButton = new Button("Send Values");
		sendButton.setOnMouseClicked((e) -> {
			try {
				sendTuningData(ipField.getText(), Integer.parseInt(portField.getText()),
						new TuningData(Double.parseDouble(pField.getText()), Double.parseDouble(iField.getText()),
								Double.parseDouble(dField.getText())));
			} catch (NumberFormatException | IOException e1) {
				e1.printStackTrace();
			}
		});
		this.getChildren().add(sendButton);
	}

	public class TuningData {
		public double kP;
		public double kI;
		public double kD;

		public TuningData(double kP, double kI, double kD) {
			this.kP = kP;
			this.kI = kI;
			this.kD = kD;
		}
	}
}
