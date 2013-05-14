package context.apps.demos.helloroom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ComboBoxEditor;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import context.arch.discoverer.Discoverer;
import context.arch.enactor.Enactor;
import context.arch.enactor.EnactorXmlParser;
import context.arch.widget.Widget;
import context.arch.widget.WidgetXmlParser;

/**
 * Main class for Hello World context-aware application of a living room smart lamp.
 * It demonstrates four steps to create a simple context-aware application:
 * <ol>
 * 	<li>Modeling context with Widgets</li>
 * 	<li>Modeling reasoning with Enactors</li>
 * 	<li>Modeling behavior with Services</li>
 * 	<li>Combining models</li>
 * </ol>
 * Running this program would launch a GUI simulation of a room sensor suite
 * with brightness and presence sensors, and lamp light level.
 * 
 * @author Brian Y. Lim
 *
 */
public class HelloRoom {

	protected Widget roomWidget;
	protected Widget lightWidget;
	protected Widget secretariaWidget;
	protected Widget escutadorWidget;

	protected Enactor enactor;
	protected Enactor enactorMsg;

	protected LightService lightService;
	protected AirService airService;
	protected MensagemFabioService mensagemFabioService;
	protected MensagemSilasService mensagemSilasService;
	protected EscutadorService escutador;


	protected HelloRoomUI ui;

	public String resultadoMensagemFabio;
	public String resultadoMensagemSilas;

	public HelloRoom() {
		super();

		/*
		 * Room sensor Widget
		 */
		roomWidget = WidgetXmlParser.createWidget("demos/helloroom/room-widget.xml");
		secretariaWidget = WidgetXmlParser.createWidget("demos/helloroom/secretaria-widget.xml");


		/*
		 * Light actuator Widget and Service
		 */
		lightWidget = WidgetXmlParser.createWidget("demos/helloroom/light-widget.xml");
		lightService = new LightService(lightWidget);
		lightWidget.addService(lightService);
		airService = new AirService(lightWidget);
		lightWidget.addService(airService);
		mensagemFabioService = new MensagemFabioService(lightWidget);
		lightWidget.addService(mensagemFabioService);
		mensagemSilasService = new MensagemSilasService(lightWidget);
		lightWidget.addService(mensagemSilasService);


		escutadorWidget = WidgetXmlParser.createWidget("demos/helloroom/escutador-widget.xml");
		escutador = new EscutadorService(secretariaWidget);
		secretariaWidget.addService(escutador);

		/*
		 * Enactor to use rules about RoomWidget to update LightWidget
		 */
		enactor = EnactorXmlParser.createEnactor("demos/helloroom/room-enactor.xml");
		enactorMsg = EnactorXmlParser.createEnactor("demos/helloroom/secretaria-enactor.xml");

		// setup UI component
		ui = new HelloRoomUI(lightService.lightLabel, airService.airLabel, mensagemFabioService.mensagemFabioLabel, mensagemSilasService.mensagemSilasLabel, escutador.escutadorLabel); // need to attach lightService before starting
	}


	/**
	 * Class to represent the UI representation for the application.
	 * @author Brian Y. Lim
	 */
	@SuppressWarnings("serial")
	public class HelloRoomUI extends JPanel {

		private JSlider brightnessSlider;
		private JSpinner presenceSpinner;
		private JSpinner temperatureSpinner;
		private JSpinner mensagemSpinner;
		private JComboBox jCombo;
		private JComboBox jComboMorador;
		private JComboBox statusMsg;
		private ArrayList<String> fabio;
		private ArrayList<String> silas;
		private JTextField jText;
		private JButton button = new JButton("Enviar Mensagem");

		String[] person = { "Vazia", "Fabio", "Silas" };
		String[] personMorador = { "Ninguem", "F", "S" };
		String[] status = { "Sim", "Nao" };


		private float fontSize = 20f;

		public void mostrarMsg() throws IOException {

			File arquivo = new File("C:/Users/Victor/workspace/smarthome.txt");

				if (!arquivo.exists()) {
					//cria um arquivo (vazio)
					arquivo.createNewFile();
				}
				
				FileWriter fw = new FileWriter(arquivo, true);
				 
				BufferedWriter bw = new BufferedWriter(fw);
				
				

				if((jCombo.getSelectedItem() == "Fabio") && (!fabio.isEmpty())){
					String result = "";

					for(int i = 0; i < fabio.size(); i++){
						result = result + fabio.get(i) + ";";

					}
					resultadoMensagemFabio = result;
					System.out.println(resultadoMensagemFabio);
					bw.write("Mensagem para Fabio: " + resultadoMensagemFabio);
					bw.newLine();
					bw.close();
					fw.close();
				} else if((jCombo.getSelectedItem() == "Silas") && (!silas.isEmpty())){
					String result = "";

					for(int i = 0; i < silas.size(); i++){
						result = result + silas.get(i) + ";";

					}
					resultadoMensagemSilas = result;
					System.out.println(resultadoMensagemSilas);
					bw.write("Mensagem para Silas: " + resultadoMensagemSilas);
					bw.newLine();
					bw.close();
					fw.close();
				}
		}
		

			public HelloRoomUI(JLabel lightLabel, JLabel airLabel, JLabel mensagemFabioLabel, JLabel mensagemSilasLabel, JLabel escutadorLabel) {			
				setLayout(new GridLayout(12, 2)); // 3 rows, 2 columns
				fabio = new ArrayList<String>();
				silas = new ArrayList<String>();
				/*
				 * UI for brightness
				 */
				add(new JLabel("brightness") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(brightnessSlider = new JSlider(new DefaultBoundedRangeModel(255, 0, 0, 255)) {{
					addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent evt) {
							// brightness is from 0 to 255
							short brightness = (short)brightnessSlider.getValue();
							roomWidget.updateData("brightness", brightness);

							// set color to represent brightness level
							setBackground(new Color(brightness, brightness, brightness));
						}
					});
					setOpaque(true); // to allow background color to show
					setMajorTickSpacing(50);
					setPaintTicks(true);
					setPaintLabels(true);
				}});

				/*
				 * UI for Temperature
				 */

				add(new JLabel("Temperature") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(temperatureSpinner = new JSpinner(new SpinnerNumberModel(10, 10, 45, 1)) {{ // # people from 0 to 5
					// get text field to color text
					final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) getEditor();
					final JFormattedTextField tf = editor.getTextField();
					tf.setForeground(Color.red);
					setFont(getFont().deriveFont(fontSize));

					addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent evt) {
							int temp = (Integer) temperatureSpinner.getValue();
							roomWidget.updateData("temperatura", temp);

							// color text red for when presence is red
							if (9 < temp && temp < 25) { tf.setForeground(Color.red); }
							else { tf.setForeground(Color.black); }
						}
					});
				}});


				/*
				 * UI for presence
				 */
				add(new JLabel("presence") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(presenceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1)) {{ // # people from 0 to 5
					// get text field to color text
					final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) getEditor();
					final JFormattedTextField tf = editor.getTextField();
					tf.setForeground(Color.red);
					setFont(getFont().deriveFont(fontSize));

					addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent evt) {
							int presence = (Integer) presenceSpinner.getValue();
							roomWidget.updateData("presence", presence);

							// color text red for when presence is red
							if (presence == 0) { tf.setForeground(Color.red); }
							else { tf.setForeground(Color.black); }
						}
					});
				}});

				add(new JLabel("presencaControlada") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(jCombo = new JComboBox(person) {{
					addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String presencaControlada = (String) jCombo.getSelectedItem();
							roomWidget.updateData("presencaControlada", presencaControlada);
							System.out.println(presencaControlada);
							try {
								mostrarMsg();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					});
				}});

				/*
				 * UI for Mensagens
				 */
				add(new JLabel("Mensagem Enviada") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(mensagemSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, 1)) {{ // # people from 0 to 5
					// get text field to color text
					final JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) getEditor();
					final JFormattedTextField tf = editor.getTextField();
					tf.setForeground(Color.red);
					setFont(getFont().deriveFont(fontSize));

					addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent evt) {
							int mensagem = (Integer) mensagemSpinner.getValue();
							roomWidget.updateData("mensagem", mensagem);
							System.out.println(mensagem);
						}
					});
				}});

				add(new JLabel("morador") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(jComboMorador = new JComboBox(personMorador) {{
					addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String presencaControlada = (String) jComboMorador.getSelectedItem();
							roomWidget.updateData("morador", presencaControlada);
							System.out.println(presencaControlada);

						}
					});
				}});

				add(new JLabel("StatusMsg") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(statusMsg = new JComboBox(status) {{
					addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							String presencaControlada = (String) statusMsg.getSelectedItem();
							roomWidget.updateData("msgStatus", presencaControlada);
							System.out.println(presencaControlada);

						}
					});
				}});

				add(new JLabel("Mensagens") {{ setFont(getFont().deriveFont(fontSize)); }});

				add(jText = new JTextField() {{
					addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {

							if((jComboMorador.getSelectedItem() == "F") && (statusMsg.getSelectedItem() == "Sim")){
								fabio.add(jText.getText());

							}

							if((jComboMorador.getSelectedItem() == "S") && (statusMsg.getSelectedItem() == "Sim")){
								silas.add(jText.getText());

							}

						}
					});
				}});






				// UI for light level
				add(new JLabel("light") {{ setFont(getFont().deriveFont(fontSize)); }});
				add(lightLabel);

				// UI for Ar status
				add(new JLabel("air") {{ setFont(getFont().deriveFont(fontSize)); }});
				add(airLabel);

				// UI for Mensagens Fabio
				add(new JLabel("qtdMsgFabio") {{ setFont(getFont().deriveFont(fontSize)); }});
				add(mensagemFabioLabel);

				// UI for Mensagens Silas
				add(new JLabel("qtdMsgSilas") {{ setFont(getFont().deriveFont(fontSize)); }});
				add(mensagemSilasLabel);

				/*
				 * Init state of widgets
				 */
				short brightness = (short)brightnessSlider.getValue();
				int presence = (Integer) presenceSpinner.getValue();
				int temperature = (Integer) temperatureSpinner.getValue();
				int mensagem = (Integer) mensagemSpinner.getValue();
				String controlada = (String) jCombo.getSelectedItem();
				roomWidget.updateData("brightness", brightness);
				roomWidget.updateData("presence", presence);
				roomWidget.updateData("temperatura", temperature);
				roomWidget.updateData("mensagem", mensagem);
				roomWidget.updateData("presencaControlada", controlada);
			}

		}

		public static void main(String[] args) {
			Discoverer.start();

			HelloRoom app = new HelloRoom();

			/*
			 * GUI frame
			 */
			JFrame frame = new JFrame("Hello Room");
			frame.add(app.ui);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(new Dimension(800, 600));
			frame.setLocationRelativeTo(null); // center of screen
			frame.setVisible(true);
		}

	}